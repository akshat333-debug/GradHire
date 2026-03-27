<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .container { max-width: 900px; margin: auto; }
        .card { border: 1px solid #ddd; border-radius: 8px; padding: 1rem; margin-top: 1rem; }
        a.button { display: inline-block; padding: .6rem 1rem; background: #0b5ed7; color: #fff; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>
<div class="container">
    <h1>GradHire</h1>
    <p>Phase 5 JSP frontend pages are active with role-based dashboards.</p>
    <div class="card">
        <p><strong>Available endpoints:</strong></p>
        <ul>
            <li><code>/health</code></li>
            <li><code>/auth/login</code></li>
            <li><code>/auth/register</code></li>
            <li><code>/dashboard</code> (after login)</li>
            <li><code>/jobs/details?jobId=&lt;id&gt;</code> (after login)</li>
        </ul>
        <a class="button" href="${pageContext.request.contextPath}/auth/login">Go to Login</a>
        <a class="button" href="${pageContext.request.contextPath}/auth/register" style="margin-left:.5rem;background:#198754;">Register</a>
    </div>
</div>
</body>
</html>
