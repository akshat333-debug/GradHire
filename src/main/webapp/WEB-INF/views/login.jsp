<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .container { max-width: 500px; margin: auto; }
        .error { color: #b00020; margin-bottom: 1rem; }
        label { display: block; margin-top: 1rem; }
        input, select, textarea { width: 100%; padding: .6rem; margin-top: .25rem; box-sizing: border-box; }
        button { margin-top: 1rem; padding: .7rem 1rem; }
    </style>
</head>
<body>
<div class="container">
    <h1>Login</h1>
    <p>Use role + email + password from your MySQL data.</p>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/auth/login">
        <label for="role">Role</label>
        <select id="role" name="role" required>
            <option value="student">Student</option>
            <option value="recruiter">Recruiter</option>
            <option value="admin">Admin</option>
        </select>

        <label for="email">Email</label>
        <input id="email" name="email" type="email" required>

        <label for="password">Password</label>
        <input id="password" name="password" type="password" required>

        <button type="submit">Sign In</button>
    </form>

    <p style="margin-top:1rem;"><a href="${pageContext.request.contextPath}/">Back to home</a></p>
</div>
</body>
</html>
