<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Register</title>
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
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/">Home</a>
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/auth/login">Login</a>
        </div>
    </div>
</header>

<main class="content">
    <div class="grid grid-2" style="align-items: center;">
        <section class="hero">
            <span class="badge">Student access</span>
            <h1>Create your GradHire profile</h1>
            <p class="muted">Register to start applying, saving jobs, and receiving recommendations tailored to your skills.</p>
            <div class="pill-group">
                <span class="pill">Save & apply</span>
                <span class="pill">Recommendations</span>
                <span class="pill">Activity history</span>
            </div>
        </section>

        <section class="card stack">
            <div class="section-header">
                <h2>Sign up</h2>
                <a class="btn btn-link" href="${pageContext.request.contextPath}/auth/login">Already have an account?</a>
            </div>

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
            <% } %>

            <form class="stack" method="post" action="${pageContext.request.contextPath}/auth/register">
                <div class="field">
                    <label for="fullName">Full Name</label>
                    <input id="fullName" name="fullName" type="text" maxlength="100" required>
                </div>

                <div class="field">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" maxlength="100" required>
                </div>

                <div class="field">
                    <label for="collegeName">College Name</label>
                    <input id="collegeName" name="collegeName" type="text" maxlength="150">
                </div>

                <div class="field">
                    <label for="password">Password</label>
                    <input id="password" name="password" type="password" minlength="8" maxlength="72" autocomplete="new-password" required>
                </div>

                <button class="btn btn-primary" type="submit">Create account</button>
            </form>
        </section>
    </div>
</main>
</body>
</html>
