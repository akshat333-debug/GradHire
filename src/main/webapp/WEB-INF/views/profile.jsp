<%@ page import="com.gradhire.model.Student" %>
<%@ page import="com.gradhire.model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Profile</title>
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
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <form class="nav-actions" method="post" action="${pageContext.request.contextPath}/auth/logout" style="margin:0;">
                <button class="btn btn-primary" type="submit">Logout</button>
            </form>
        </div>
    </div>
</header>

<main class="content">
    <div class="section-header">
        <h1>Profile</h1>
        <span class="pill">Keep your details up to date</span>
    </div>

    <% if (request.getAttribute("profileError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("profileError") %></div>
    <% } %>
    <% if (request.getAttribute("profileSuccess") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("profileSuccess") %></div>
    <% } %>

    <div class="card">
        <% String userType = (String) session.getAttribute("userType"); %>
        <% if ("student".equalsIgnoreCase(userType)) { %>
        <% Student student = (Student) request.getAttribute("profileStudent"); %>
        <% if (student != null) { %>
        <% pageContext.setAttribute("studentFullName", student.getFullName()); %>
        <% pageContext.setAttribute("studentCollegeName", student.getCollegeName() == null ? "" : student.getCollegeName()); %>
        <form class="stack" method="post" action="${pageContext.request.contextPath}/profile">
            <div class="field">
                <label>Email</label>
                <% pageContext.setAttribute("studentEmail", student.getEmail()); %>
                <input type="email" value="${fn:escapeXml(studentEmail)}" disabled>
            </div>
            <div class="field">
                <label>Full Name</label>
                <input type="text" name="fullName" value="${fn:escapeXml(studentFullName)}" maxlength="150" required>
            </div>
            <div class="field">
                <label>College Name</label>
                <input type="text" name="collegeName" value="${fn:escapeXml(studentCollegeName)}" maxlength="150">
            </div>
            <button class="btn btn-primary" type="submit">Update Profile</button>
        </form>
        <% } %>
        <% } else { %>
        <% Admin admin = (Admin) request.getAttribute("profileAdmin"); %>
        <% if (admin != null) { %>
        <% pageContext.setAttribute("adminFullName", admin.getFullName()); %>
        <% pageContext.setAttribute("adminCompanyName", admin.getCompanyName() == null ? "" : admin.getCompanyName()); %>
        <form class="stack" method="post" action="${pageContext.request.contextPath}/profile">
            <div class="field">
                <label>Email</label>
                <% pageContext.setAttribute("adminEmail", admin.getEmail()); %>
                <input type="email" value="${fn:escapeXml(adminEmail)}" disabled>
            </div>
            <div class="field">
                <label>Full Name</label>
                <input type="text" name="fullName" value="${fn:escapeXml(adminFullName)}" maxlength="150" required>
            </div>
            <div class="field">
                <label>Company Name</label>
                <input type="text" name="companyName" value="${fn:escapeXml(adminCompanyName)}" maxlength="150">
            </div>
            <div class="field">
                <label>Role</label>
                <select name="role" <%= "recruiter".equalsIgnoreCase(userType) ? "disabled" : "" %>>
                    <option value="recruiter" <%= "recruiter".equalsIgnoreCase(admin.getRole()) ? "selected" : "" %>>Recruiter</option>
                    <option value="admin" <%= "admin".equalsIgnoreCase(admin.getRole()) ? "selected" : "" %>>Admin</option>
                </select>
            </div>
            <% if ("recruiter".equalsIgnoreCase(userType)) { %>
            <input type="hidden" name="role" value="recruiter">
            <p class="subtle">Role: Recruiter (role changes require admin privileges).</p>
            <% } %>
            <button class="btn btn-primary" type="submit">Update Profile</button>
        </form>
        <% } %>
        <% } %>
    </div>
</main>
</body>
</html>
