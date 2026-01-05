<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Login | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/login.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">
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

    <h2>Welcome back</h2>
    <p class="auth-subtitle">
        Log in to continue tracking your progress
    </p>

    <!-- Success alert -->
    <div id="successMessage" class="alert alert-success">
        <strong>✓</strong> Login successful! Redirecting...
    </div>

    <!-- Error alert from servlet -->
    <div id="errorMessage" class="alert alert-error">
        <strong>✕</strong> <span id="errorText"><%
            String error = (String) request.getAttribute("errorMessage");
            if (error != null) {
                out.print(error);
            } else {
                out.print("Invalid email or password");
            }
        %></span>
    </div>

    <!-- Login Form -->
    <form id="loginForm" action="../LoginServlet" method="post" novalidate>

        <div class="form-group" id="emailGroup">
            <label for="email">Email address</label>
            <input type="email" id="email" name="email"
                   placeholder="you@example.com" required>
            <div class="form-message">Please enter a valid email address</div>
        </div>

        <div class="form-group" id="passwordGroup">
            <label for="password">Password</label>
            <input type="password" id="password" name="password"
                   placeholder="••••••••" required>
            <div class="form-message">Password is required</div>
        </div>

        <button type="submit" id="signInBtn" disabled>
            Sign in
        </button>

    </form>

    <p class="switch-link">
        New here?
        <a href="register.jsp">Create an account</a>
    </p>

    <div class="trust-note">
        Your information is private and never shared.
    </div>

</div>

<script type="text/javascript">
(function () {
    'use strict';

    var loginForm = document.getElementById('loginForm');
    var emailInput = document.getElementById('email');
    var passwordInput = document.getElementById('password');
    var signInBtn = document.getElementById('signInBtn');
    var successMessage = document.getElementById('successMessage');
    var errorMessage = document.getElementById('errorMessage');
    var errorText = document.getElementById('errorText');

    function validateEmail(email) {
        var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    function validatePassword(password) {
        return password.replace(/^\s+|\s+$/g, '').length >= 6;
    }

    function validateField(fieldId, isValid) {
        var group = document.getElementById(fieldId + 'Group');
        if (!group) return;

        if (isValid) {
            group.className = group.className.replace('invalid', '') + ' valid';
        } else {
            group.className = group.className.replace('valid', '') + ' invalid';
        }
    }

    function validateForm() {
        var emailValid = validateEmail(emailInput.value);
        var passwordValid = validatePassword(passwordInput.value);

        validateField('email', emailValid);
        validateField('password', passwordValid);

        signInBtn.disabled = !(emailValid && passwordValid);
    }

    emailInput.addEventListener('input', function () {
        validateForm();
    });

    passwordInput.addEventListener('input', function () {
        validateForm();
    });

    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();

        if (!validateEmail(emailInput.value) || !validatePassword(passwordInput.value)) {
            errorText.innerText = 'Please enter a valid email and password';
            errorMessage.className += ' show';
            setTimeout(function () {
                errorMessage.className = errorMessage.className.replace('show', '');
            }, 5000);
            return;
        }

        signInBtn.disabled = true;
        signInBtn.className += ' loading';

        setTimeout(function () {
            loginForm.submit();
        }, 800);
    });

    <% if (request.getAttribute("errorMessage") != null) { %>
        errorMessage.className += ' show';
        setTimeout(function () {
            errorMessage.className = errorMessage.className.replace('show', '');
        }, 5000);
    <% } %>

    validateForm();
    emailInput.focus();

})();
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
