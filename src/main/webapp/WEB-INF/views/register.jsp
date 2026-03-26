<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Register</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .container { max-width: 500px; margin: auto; }
        .error { color: #b00020; margin-bottom: 1rem; }
        label { display: block; margin-top: 1rem; }
        input { width: 100%; padding: .6rem; margin-top: .25rem; box-sizing: border-box; }
        button { margin-top: 1rem; padding: .7rem 1rem; }
    </style>
</head>
<body>
<div class="container">
    <h1>Create Student Account</h1>
    <p>Register to start applying for jobs.</p>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/auth/register">
        <label for="fullName">Full Name</label>
        <input id="fullName" name="fullName" type="text" maxlength="100" required>

        <label for="email">Email</label>
        <input id="email" name="email" type="email" maxlength="100" required>

        <label for="collegeName">College Name</label>
        <input id="collegeName" name="collegeName" type="text" maxlength="150">

        <label for="password">Password</label>
        <input id="password" name="password" type="password" minlength="8" maxlength="72" required>

        <button type="submit">Register</button>
    </form>

    <p style="margin-top:1rem;"><a href="${pageContext.request.contextPath}/auth/login">Already have an account? Sign in</a></p>
    <p><a href="${pageContext.request.contextPath}/">Back to home</a></p>
</div>
</body>
</html>
