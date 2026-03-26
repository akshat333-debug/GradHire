<%@ page import="com.gradhire.model.Student" %>
<%@ page import="com.gradhire.model.Admin" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .container { max-width: 700px; margin: auto; }
        .card { border: 1px solid #ddd; border-radius: 8px; padding: 1rem; }
        .error { color: #b00020; margin-bottom: 1rem; }
        .success { color: #0f5132; margin-bottom: 1rem; }
        label { display: block; margin-top: .75rem; }
        input, select { width: 100%; box-sizing: border-box; margin-top: .25rem; padding: .5rem; }
        button { margin-top: 1rem; }
    </style>
</head>
<body>
<div class="container">
    <h1>Profile</h1>
    <p><a href="${pageContext.request.contextPath}/dashboard">Back to Dashboard</a></p>

    <% if (request.getAttribute("profileError") != null) { %>
    <div class="error"><%= request.getAttribute("profileError") %></div>
    <% } %>
    <% if (request.getAttribute("profileSuccess") != null) { %>
    <div class="success"><%= request.getAttribute("profileSuccess") %></div>
    <% } %>

    <div class="card">
        <% String userType = (String) session.getAttribute("userType"); %>
        <% if ("student".equalsIgnoreCase(userType)) { %>
        <% Student student = (Student) request.getAttribute("profileStudent"); %>
        <% if (student != null) { %>
        <% pageContext.setAttribute("studentFullName", student.getFullName()); %>
        <% pageContext.setAttribute("studentCollegeName", student.getCollegeName() == null ? "" : student.getCollegeName()); %>
        <form method="post" action="${pageContext.request.contextPath}/profile">
            <label>Email</label>
            <% pageContext.setAttribute("studentEmail", student.getEmail()); %>
            <input type="email" value="${fn:escapeXml(studentEmail)}" disabled>
            <label>Full Name</label>
            <input type="text" name="fullName" value="${fn:escapeXml(studentFullName)}" maxlength="150" required>
            <label>College Name</label>
            <input type="text" name="collegeName" value="${fn:escapeXml(studentCollegeName)}" maxlength="150">
            <button type="submit">Update Profile</button>
        </form>
        <% } %>
        <% } else { %>
        <% Admin admin = (Admin) request.getAttribute("profileAdmin"); %>
        <% if (admin != null) { %>
        <% pageContext.setAttribute("adminFullName", admin.getFullName()); %>
        <% pageContext.setAttribute("adminCompanyName", admin.getCompanyName() == null ? "" : admin.getCompanyName()); %>
        <form method="post" action="${pageContext.request.contextPath}/profile">
            <label>Email</label>
            <% pageContext.setAttribute("adminEmail", admin.getEmail()); %>
            <input type="email" value="${fn:escapeXml(adminEmail)}" disabled>
            <label>Full Name</label>
            <input type="text" name="fullName" value="${fn:escapeXml(adminFullName)}" maxlength="150" required>
            <label>Company Name</label>
            <input type="text" name="companyName" value="${fn:escapeXml(adminCompanyName)}" maxlength="150">
            <label>Role</label>
            <select name="role" <%= "recruiter".equalsIgnoreCase(userType) ? "disabled" : "" %>>
                <option value="recruiter" <%= "recruiter".equalsIgnoreCase(admin.getRole()) ? "selected" : "" %>>Recruiter</option>
                <option value="admin" <%= "admin".equalsIgnoreCase(admin.getRole()) ? "selected" : "" %>>Admin</option>
            </select>
            <% if ("recruiter".equalsIgnoreCase(userType)) { %>
            <input type="hidden" name="role" value="recruiter">
            <p>Role: Recruiter (role changes require admin privileges).</p>
            <% } %>
            <button type="submit">Update Profile</button>
        </form>
        <% } %>
        <% } %>
    </div>
</div>
</body>
</html>
