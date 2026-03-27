<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles.css">
</head>
<body class="page">
<header class="navbar">
    <div class="navbar-inner">
        <div class="brand">
            <span class="brand-badge">GH</span>
            GradHire
        </div>
        <div class="nav-actions">
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/auth/login">Sign In</a>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/auth/register">Register</a>
        </div>
    </div>
</header>

<main class="content">
    <section class="hero">
        <span class="badge">Campus & early-career hiring</span>
        <h1>Launch your next hire with confidence.</h1>
        <p>Role-aware dashboards, job postings, application tracking, and activity history in one lightweight JSP app.</p>
        <div class="row">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/auth/login">Go to Login</a>
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/auth/register">Create a student account</a>
        </div>
    </section>

    <section class="grid grid-2">
        <div class="card">
            <div class="section-header">
                <h2>Quick links</h2>
                <span class="pill">Live endpoints</span>
            </div>
            <ul class="list">
                <li><strong>/health</strong> — uptime & DB check</li>
                <li><strong>/auth/login</strong> — multi-role auth</li>
                <li><strong>/auth/register</strong> — student onboarding</li>
                <li><strong>/dashboard</strong> — role-based landing after login</li>
                <li><strong>/jobs/details?jobId=&lt;id&gt;</strong> — job details & apply</li>
            </ul>
        </div>
        <div class="card">
            <div class="section-header">
                <h2>What’s inside</h2>
                <span class="pill">Phase 5</span>
            </div>
            <div class="list">
                <div>
                    <strong>Role-aware UI</strong>
                    <p class="subtle">Student, Recruiter, and Admin dashboards tailored for their workflows.</p>
                </div>
                <div>
                    <strong>Job lifecycle</strong>
                    <p class="subtle">Post, edit, and review applications with reviewer notes and status updates.</p>
                </div>
                <div>
                    <strong>Activity visibility</strong>
                    <p class="subtle">Recent actions, recommendations, and saved jobs surfaced right away.</p>
                </div>
            </div>
        </div>
    </section>
</main>

<footer class="footer">
    GradHire • Made for rapid campus recruiting
    <span class="muted">|</span>
    <a href="${pageContext.request.contextPath}/auth/login">Login</a>
    <span class="muted">•</span>
    <a href="${pageContext.request.contextPath}/auth/register">Register</a>
</footer>
</body>
</html>
