<%@ page import="java.util.List" %>
<%@ page import="com.gradhire.model.Job" %>
<%@ page import="com.gradhire.model.Application" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .row { display: flex; gap: 2rem; align-items: flex-start; }
        .column { flex: 1; }
        .card { border: 1px solid #ddd; border-radius: 8px; padding: 1rem; margin-bottom: 1rem; }
        .error { color: #b00020; margin-bottom: 1rem; }
        form.inline { display: inline; }
    </style>
</head>
<body>
<h1>Dashboard</h1>
<p>Welcome, <strong><%= session.getAttribute("userName") %></strong> (<%= session.getAttribute("userType") %>)</p>

<% if (request.getAttribute("dashboardError") != null) { %>
<div class="error"><%= request.getAttribute("dashboardError") %></div>
<% } %>
<% if (request.getAttribute("applicationError") != null) { %>
<div class="error"><%= request.getAttribute("applicationError") %></div>
<% } %>

<form class="inline" method="post" action="${pageContext.request.contextPath}/auth/logout">
    <button type="submit">Logout</button>
</form>

<div class="row">
    <div class="column">
        <h2>Active Jobs</h2>
        <%
            List<Job> jobs = (List<Job>) request.getAttribute("jobs");
            if (jobs == null || jobs.isEmpty()) {
        %>
        <p>No jobs available.</p>
        <% } else {
            for (Job job : jobs) {
        %>
        <div class="card">
            <h3><%= job.getJobTitle() %></h3>
            <p><%= job.getCompanyName() %> | <%= job.getJobType() %></p>
            <p><%= job.getDomain() %> | <%= job.getLocation() %></p>
            <% if ("student".equalsIgnoreCase((String) session.getAttribute("userType"))) { %>
            <form method="post" action="${pageContext.request.contextPath}/applications/apply">
                <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
                <label>Cover letter (optional)</label>
                <textarea name="coverLetter" rows="3"></textarea>
                <button type="submit">Apply</button>
            </form>
            <% } %>
        </div>
        <% } } %>
    </div>

    <div class="column">
        <h2>Your Applications</h2>
        <%
            List<Application> applications = (List<Application>) request.getAttribute("applications");
            if (applications == null || applications.isEmpty()) {
        %>
        <p>No applications yet.</p>
        <% } else {
            for (Application application : applications) {
        %>
        <div class="card">
            <p>Application ID: <%= application.getApplicationId() %></p>
            <p>Job ID: <%= application.getJobId() %></p>
            <p>Status: <strong><%= application.getApplicationStatus() %></strong></p>
            <p>Applied At: <%= application.getAppliedAt() %></p>
        </div>
        <% } } %>
    </div>
</div>
</body>
</html>
