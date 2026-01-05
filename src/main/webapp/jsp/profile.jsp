<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Profile | Productivity Tracker</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/profile.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">
</head>

<body>

<header class="header">
    <h1>Your Profile</h1>
    <p>Keep your information accurate and secure</p>
    <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme" title="Toggle theme">🌓</button>
</header>

<main class="content profile-container">

    <div id="successMessage" class="alert alert-success">
        <strong>✓</strong> Profile updated successfully!
    </div>

    <div id="errorMessage" class="alert alert-error">
        <strong>✕</strong> <span id="errorText">Please fill in all required fields correctly.</span>
    </div>

    <section class="card">
        <h2>Profile Information</h2>
        <p class="section-note">
            This information is used to personalize your experience.
        </p>

        <!-- Temporary static data (replace with session/servlet) -->
        <%
            String name = "John Doe";
            String email = "john@example.com";
        %>

        <form id="profileForm" action="../ProfileServlet" method="post">

            <div class="form-group" id="nameGroup">
                <label for="name">Full name</label>
                <input type="text" id="name" name="name"
                       value="<%= name %>" required>
                <div class="form-message">Name must be at least 2 characters</div>
            </div>

            <div class="form-group" id="emailGroup">
                <label for="email">Email address</label>
                <input type="email" id="email" name="email"
                       value="<%= email %>" required>
                <div class="form-message">Please enter a valid email address</div>
            </div>

            <div class="divider"></div>

            <h3>Security</h3>
            <p class="section-note">
                Leave this blank if you don't want to change your password.
            </p>

            <div class="form-group" id="passwordGroup">
                <label for="password">New password</label>
                <input type="password" id="password" name="password"
                       placeholder="••••••••">
                <div class="password-strength-container" id="strengthContainer">
                    <div class="password-strength-bar">
                        <div class="password-strength-fill" id="strengthFill"></div>
                    </div>
                    <div class="password-strength-text" id="strengthText"></div>
                </div>
                <div class="security-hint">
                    Use at least 8 characters with uppercase, lowercase, numbers, and symbols for better security.
                </div>
            </div>

            <button type="submit" class="save-btn" id="saveBtn" disabled>
                Save Changes
            </button>

        </form>
    </section>

</main>

<footer class="footer">
    <a href="index.jsp">← Back to Dashboard</a>
</footer>

<script>
    // Form elements
    const profileForm = document.getElementById('profileForm');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const saveBtn = document.getElementById('saveBtn');
    const successMessage = document.getElementById('successMessage');
    const errorMessage = document.getElementById('errorMessage');
    const errorText = document.getElementById('errorText');
    const strengthContainer = document.getElementById('strengthContainer');
    const strengthFill = document.getElementById('strengthFill');
    const strengthText = document.getElementById('strengthText');

    // Validate name field
    function validateName(name) {
        return name.trim().length >= 2;
    }

    // Validate email field
    function validateEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }

    // Calculate password strength (0-100)
    function calculatePasswordStrength(password) {
        if (!password) return 0;

        let strength = 0;

        // Length checks
        if (password.length >= 8) strength += 20;
        if (password.length >= 12) strength += 10;
        if (password.length >= 16) strength += 10;

        // Character type checks
        if (/[a-z]/.test(password)) strength += 15;
        if (/[A-Z]/.test(password)) strength += 15;
        if (/\d/.test(password)) strength += 15;
        if (/[^a-zA-Z\d]/.test(password)) strength += 15;

        return Math.min(strength, 100);
    }

    // Get password strength label and styling
    function getPasswordStrengthInfo(strength) {
        if (strength === 0) return { label: '', class: '' };
        if (strength < 30) return { label: 'Weak', class: 'strength-weak' };
        if (strength < 60) return { label: 'Fair', class: 'strength-fair' };
        if (strength < 80) return { label: 'Good', class: 'strength-good' };
        return { label: 'Strong', class: 'strength-strong' };
    }

    // Update password strength indicator
    function updatePasswordStrength(password) {
        const strength = calculatePasswordStrength(password);

        if (!password) {
            strengthContainer.style.display = 'none';
            strengthFill.style.width = '0%';
            strengthFill.className = 'password-strength-fill';
            strengthText.textContent = '';
            return;
        }

        strengthContainer.style.display = 'block';
        const info = getPasswordStrengthInfo(strength);

        // Update fill bar
        strengthFill.style.width = strength + '%';
        strengthFill.className = 'password-strength-fill';

        if (strength < 30) {
            strengthFill.classList.add('fill-weak');
        } else if (strength < 60) {
            strengthFill.classList.add('fill-fair');
        } else if (strength < 80) {
            strengthFill.classList.add('fill-good');
        } else {
            strengthFill.classList.add('fill-strong');
        }

        // Update text
        strengthText.textContent = info.label;
        strengthText.className = 'password-strength-text ' + info.class;
    }

    // Validate and update a field
    function validateField(fieldId, isValid) {
        const group = document.getElementById(fieldId + 'Group');
        if (isValid) {
            group.classList.add('valid');
            group.classList.remove('invalid');
        } else {
            group.classList.add('invalid');
            group.classList.remove('valid');
        }
    }

    // Check if all fields are valid
    function validateForm() {
        const isNameValid = validateName(nameInput.value);
        const isEmailValid = validateEmail(emailInput.value);
        
        // Password is optional, but if provided, must be at least 8 chars
        let isPasswordValid = true;
        if (passwordInput.value) {
            isPasswordValid = passwordInput.value.length >= 8;
        }

        validateField('name', isNameValid);
        validateField('email', isEmailValid);
        
        // Update password field styling if it has a value
        if (passwordInput.value) {
            validateField('password', isPasswordValid);
        } else {
            document.getElementById('passwordGroup').classList.remove('valid', 'invalid');
        }

        // Enable/disable save button
        saveBtn.disabled = !(isNameValid && isEmailValid && isPasswordValid);
    }

    // Real-time validation on input
    nameInput.addEventListener('input', () => {
        validateForm();
    });

    emailInput.addEventListener('input', () => {
        validateForm();
    });

    passwordInput.addEventListener('input', (e) => {
        updatePasswordStrength(e.target.value);
        validateForm();
    });

    // Form submission
    profileForm.addEventListener('submit', (e) => {
        e.preventDefault();

        // Show loading state
        saveBtn.classList.add('loading');
        saveBtn.disabled = true;

        // Simulate server request (replace with actual form submission)
        setTimeout(() => {
            // Show success message
            errorMessage.classList.remove('show');
            successMessage.classList.add('show');

            // Hide message after 4 seconds
            setTimeout(() => {
                successMessage.classList.remove('show');
            }, 4000);

            // Reset loading state
            saveBtn.classList.remove('loading');
            validateForm();
        }, 1500);
    });

    // Auto-hide error message
    function hideErrorMessage() {
        setTimeout(() => {
            errorMessage.classList.remove('show');
        }, 5000);
    }

    // Check if there's a success parameter in URL
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('success') === 'true') {
        successMessage.classList.add('show');
        setTimeout(() => {
            successMessage.classList.remove('show');
        }, 4000);
    }

    // Initial validation check
    validateForm();
</script>

    <!-- Register service worker helper and Theme & Accessibility -->
    <script src="../assets/js/register-sw.js"></script>
    <script src="../assets/js/theme.js"></script>
    <script src="../assets/js/accessibility.js"></script>
    <script src="../assets/js/effects.js"></script>

</body>
</html>
