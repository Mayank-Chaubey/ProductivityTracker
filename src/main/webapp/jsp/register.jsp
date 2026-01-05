<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Create Account | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/style.css">
    <link rel="stylesheet" href="../assets/css/register.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">

    <!-- Validation JS -->
    <script src="../assets/js/validation.js" defer></script>

</head>

<body>

<!-- Skip link for keyboard users -->
<a class="skip-link" href="#mainContent">Skip to main content</a>

<header class="page-header">
    <div class="header-inner">
        <a class="brand" href="../index.html">Productivity Tracker</a>
        <button id="theme-toggle" class="theme-toggle" aria-label="Toggle color theme" title="Toggle theme">🌓</button>
    </div>
</header>

<main id="mainContent">

<div class="auth-container">

    <h2>Create your account</h2>
    <p class="auth-subtitle">
        Join thousands building clarity and consistency today
    </p>

    <!-- Error message from servlet -->
    <%
        String error = (String) request.getAttribute("errorMessage");
        if (error != null) {
    %>
        <div class="error" id="errorMsg">
            <%= error %>
        </div>
    <%
        }
    %>

    <!-- Success message -->
    <%
        String success = (String) request.getAttribute("successMessage");
        if (success != null) {
    %>
        <div class="success" id="successMsg">
            <%= success %>
        </div>
    <%
        }
    %>

    <!-- Registration Form -->
    <form action="../RegisterServlet" method="post" novalidate id="registerForm">

        <div class="form-group">
            <label for="name">Full Name</label>
            <input type="text" id="name" name="name"
                   placeholder="Enter your full name" required
                   oninput="validateField(this)">
        </div>

        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email"
                   placeholder="you@example.com" required
                   oninput="validateField(this)">
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password"
                   placeholder="••••••••" required
                   oninput="checkPasswordStrength(this.value)">
            <div class="password-strength">
                <div class="strength-bar" id="strengthBar"></div>
            </div>
            <div class="strength-text" id="strengthText"></div>
            <div class="password-hint">
                Use at least 8 characters with mix of uppercase, lowercase, and numbers
            </div>
        </div>

        <div class="form-group">
            <label for="confirm">Confirm Password</label>
            <input type="password" id="confirm" name="confirmPassword"
                   placeholder="••••••••" required
                   oninput="validateField(this)">
        </div>

        <button type="submit" id="submitBtn">
            Create Account
        </button>

    </form>

    <p class="switch-link">
        Already have an account?
        <a href="login.jsp">Sign in here</a>
    </p>

    <div class="trust-note">
        🔒 We respect your privacy. No spam. No data sharing. Enterprise-grade encryption.
    </div>

</div>

<script>
    // Validate individual field
    function validateField(field) {
        const formGroup = field.closest('.form-group');
        const value = field.value.trim();
        let isValid = false;

        if (field.type === 'email') {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            isValid = emailRegex.test(value) && value.length > 0;
        } else if (field.name === 'name') {
            isValid = value.length >= 2;
        } else if (field.name === 'password') {
            isValid = value.length >= 8;
        } else if (field.name === 'confirmPassword') {
            const password = document.getElementById('password').value;
            isValid = value === password && value.length >= 8;
        }

        // Update visual feedback
        if (value.length > 0) {
            formGroup.classList.remove('invalid');
            if (isValid) {
                formGroup.classList.add('valid');
            } else {
                formGroup.classList.add('invalid');
            }
        } else {
            formGroup.classList.remove('valid', 'invalid');
        }

        validateForm();
    }

    // Check password strength
    function checkPasswordStrength(password) {
        const strengthBar = document.getElementById('strengthBar');
        const strengthText = document.getElementById('strengthText');
        let strength = 0;

        if (password.length >= 8) strength += 25;
        if (password.length >= 12) strength += 25;
        if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength += 25;
        if (/[0-9]/.test(password)) strength += 25;

        strengthBar.style.width = strength + '%';

        if (strength < 50) {
            strengthBar.style.background = '#dc2626';
            strengthText.textContent = '⚠️ Weak password';
        } else if (strength < 75) {
            strengthBar.style.background = '#f59e0b';
            strengthText.textContent = '→ Fair password';
        } else {
            strengthBar.style.background = '#16a34a';
            strengthText.textContent = '✓ Strong password';
        }

        validateField(document.getElementById('password'));
    }

    // Validate entire form
    function validateForm() {
        const name = document.getElementById('name').value.trim();
        const email = document.getElementById('email').value.trim();
        const password = document.getElementById('password').value;
        const confirm = document.getElementById('confirm').value;
        const submitBtn = document.getElementById('submitBtn');

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        const isValid = name.length >= 2 && 
                       emailRegex.test(email) && 
                       password.length >= 8 && 
                       password === confirm;

        submitBtn.disabled = !isValid;
    }

    // Form submission
    document.getElementById('registerForm').addEventListener('submit', function(e) {
        const submitBtn = document.getElementById('submitBtn');
        const originalText = submitBtn.textContent;

        // Add loading state
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<span class="btn-loading"></span>Creating account...';

        // Simulate processing (remove in production)
        setTimeout(() => {
            // Form will submit normally after this
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        }, 1500);
    });

    // Auto-hide messages after 5 seconds
    document.addEventListener('DOMContentLoaded', function() {
        const errorMsg = document.getElementById('errorMsg');
        const successMsg = document.getElementById('successMsg');

        if (errorMsg) {
            setTimeout(() => {
                errorMsg.style.animation = 'slideDown 0.5s ease reverse forwards';
                setTimeout(() => errorMsg.remove(), 500);
            }, 5000);
        }

        if (successMsg) {
            setTimeout(() => {
                successMsg.style.animation = 'slideDown 0.5s ease reverse forwards';
                setTimeout(() => successMsg.remove(), 500);
            }, 4000);
        }

        validateForm();
    });

    // Real-time validation as user types
    document.getElementById('registerForm').addEventListener('input', function() {
        validateForm();
    });
</script>

    <!-- Register service worker helper and Theme & Accessibility -->
    <script src="../assets/js/register-sw.js"></script>
    <script src="../assets/js/theme.js"></script>
    <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/validation.js"></script>
    <script src="../assets/js/effects.js"></script>

</main>
</body>
</html>