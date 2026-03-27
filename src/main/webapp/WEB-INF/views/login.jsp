<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Login</title>
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
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/auth/register">Create account</a>
        </div>
    </div>
</header>

<main class="content">
    <div class="grid grid-2" style="align-items: center;">
        <section class="hero">
            <span class="badge">Welcome back</span>
            <h1>Sign in to your workspace</h1>
            <p class="muted">Use the role, email, and password seeded in your MySQL data. Each role lands on its own dashboard.</p>
            <div class="pill-group">
                <span class="pill">Student</span>
                <span class="pill">Recruiter</span>
                <span class="pill">Admin</span>
            </div>
        </section>

        <section class="card stack">
            <div class="section-header">
                <h2>Login</h2>
                <a class="btn btn-link" href="${pageContext.request.contextPath}/auth/register">New student? Register</a>
            </div>

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error"><%= request.getAttribute("error") %></div>
            <% } %>

            <form class="stack" method="post" action="${pageContext.request.contextPath}/auth/login">
                <div class="field">
                    <label for="role">Role</label>
                    <select id="role" name="role" required>
                        <option value="student">Student</option>
                        <option value="recruiter">Recruiter</option>
                        <option value="admin">Admin</option>
                    </select>
                </div>

                <div class="field">
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email" required>
                </div>

                <div class="field">
                    <label for="password">Password</label>
                    <input id="password" name="password" type="password" required>
                </div>

                <button class="btn btn-primary" type="submit">Sign In</button>
            </form>
        </section>
    </div>
</main>
</body>
</html>
