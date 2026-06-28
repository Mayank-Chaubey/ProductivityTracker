package com.productivitytracker.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {

    private static final String PBKDF2_PREFIX = "pbkdf2";
    private static final int ITERATIONS = 120_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private PasswordUtil() {
        throw new AssertionError("PasswordUtil should not be instantiated");
    }

    public static String hash(String plainTextPassword) {
        if (plainTextPassword == null) {
            return "";
        }

        try {
            byte[] salt = new byte[SALT_BYTES];
            SECURE_RANDOM.nextBytes(salt);
            byte[] hashed = pbkdf2(plainTextPassword.toCharArray(), salt, ITERATIONS, HASH_BYTES);
            return PBKDF2_PREFIX + "$" + ITERATIONS + "$" +
                    Base64.getEncoder().encodeToString(salt) + "$" +
                    Base64.getEncoder().encodeToString(hashed);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("PBKDF2 hashing failed", ex);
        }
    }

    public static boolean matches(String plainTextPassword, String storedPassword) {
        if (plainTextPassword == null || storedPassword == null) {
            return false;
        }

        if (storedPassword.startsWith(PBKDF2_PREFIX + "$")) {
            return matchesPbkdf2(plainTextPassword, storedPassword);
        }

        String legacySha256 = legacySha256(plainTextPassword);
        if (MessageDigest.isEqual(
                legacySha256.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8))) {
            return true;
        }
        return MessageDigest.isEqual(
                plainTextPassword.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean matchesPbkdf2(String plainTextPassword, String storedPassword) {
        try {
            String[] parts = storedPassword.split("\\$");
            if (parts.length != 4) {
                return false;
            }
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
            byte[] actualHash = pbkdf2(plainTextPassword.toCharArray(), salt, iterations, expectedHash.length);
            return MessageDigest.isEqual(actualHash, expectedHash);
        } catch (Exception ex) {
            return false;
        }
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int hashBytes)
            throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, hashBytes * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
    }

    private static String legacySha256(String plainTextPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }
}
