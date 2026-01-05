<%@ page isErrorPage="true"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.io.StringWriter,java.io.PrintWriter" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="theme-color" content="#2563eb">
    <title>Oops! Something went wrong</title>

    <!-- PWA & Icons -->
    <link rel="manifest" href="../manifest.json">
    <link rel="icon" type="image/svg+xml" href="../assets/images/icon-192.svg">
    <link rel="alternate icon" type="image/png" href="../assets/images/favicon.png">

    <!-- CSS -->
    <link rel="stylesheet" href="../assets/css/error.css">
    <link rel="stylesheet" href="../assets/css/dark-mode.css">
    <link rel="stylesheet" href="../assets/css/animations.css">
    <link rel="stylesheet" href="../assets/css/effects.css">
</head>

<body>

<a class="skip-link" href="#mainContent">Skip to main content</a>

<header class="page-header">
    <div class="header-inner">
        <a class="brand" href="../index.html">Productivity Tracker</a>
        <button id="theme-toggle" class="theme-toggle"
                aria-label="Toggle color theme" title="Toggle theme">🌓</button>
    </div>
</header>

<main id="mainContent">

<div class="error-container" role="alert">

    <div class="error-icon">⚠️</div>

    <h1>Something didn't go as planned</h1>

    <p>
        Don’t worry — this happens sometimes. You can try again or return to the dashboard.
    </p>

    <% String errorMessage = (String) request.getAttribute("errorMessage");
       if (errorMessage != null && !errorMessage.isEmpty()) { %>
        <div class="user-error-message" style="color:#ffb347; font-weight:bold; margin-bottom:12px;">
            <%= errorMessage %>
        </div>
    <% } %>

    <!-- Technical message (DEV ONLY) -->
    <div class="error-details" id="techDetails" style="display:none;">
        <strong>Technical details:</strong>
        <pre id="errorStack"
             style="white-space:pre-wrap; margin-top:8px; color:#e6f0ff;"></pre>
    </div>

    <div class="error-actions">
        <button class="primary" id="retryBtn">Retry</button>
        <a href="index.jsp" class="primary">Back to Dashboard</a>
        <a href="login.jsp" class="secondary">Login Again</a>
    </div>

    <div class="notice" id="autoRedirectNotice" style="display:none;">
        Redirecting in <span id="redirectSeconds">5</span> seconds...
    </div>
</div>

<script>
(function () {
    var devMode = true; // ⚠️ set false in production

    <% if (exception != null) {
           StringWriter sw = new StringWriter();
           exception.printStackTrace(new PrintWriter(sw));
    %>
        var stackTrace = "<%= sw.toString()
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "")
                .replace("\n", "\\n") %>";
    <% } else { %>
        var stackTrace = "";
    <% } %>

    if (devMode && stackTrace) {
        document.getElementById("techDetails").style.display = "block";
        document.getElementById("errorStack").textContent = stackTrace;
    }
})();
</script>

<script>
    // Retry button
    document.getElementById('retryBtn').addEventListener('click', function () {
        this.disabled = true;
        this.textContent = 'Retrying…';
        setTimeout(function () {
            location.reload();
        }, 600);
    });

    // Auto redirect
    (function () {
        var seconds = 10;
        var countdownEl = document.getElementById('redirectSeconds');
        var noticeEl = document.getElementById('autoRedirectNotice');

        noticeEl.style.display = 'block';
        countdownEl.textContent = seconds;

        var timer = setInterval(function () {
            seconds--;
            countdownEl.textContent = seconds;
            if (seconds <= 0) {
                clearInterval(timer);
                window.location.href = 'index.jsp';
            }
        }, 1000);
    })();
</script>

<script src="../assets/js/register-sw.js"></script>
<script src="../assets/js/theme.js"></script>
<script src="../assets/js/accessibility.js"></script>
<script src="../assets/js/effects.js"></script>

</main>
</body>
</html>