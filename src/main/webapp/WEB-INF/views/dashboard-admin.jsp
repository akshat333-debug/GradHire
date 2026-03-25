<%@ page import="java.util.List" %>
<%@ page import="com.gradhire.model.Job" %>
<%@ page import="com.gradhire.model.ApplicationReviewItem" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Admin Dashboard</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .row { display: flex; gap: 2rem; align-items: flex-start; }
        .column { flex: 1; }
        .card { border: 1px solid #ddd; border-radius: 8px; padding: 1rem; margin-bottom: 1rem; }
        .error { color: #b00020; margin-bottom: 1rem; }
        .success { color: #0f5132; margin-bottom: 1rem; }
        form.inline { display: inline; }
        textarea, input, select { width: 100%; box-sizing: border-box; margin-top: .25rem; margin-bottom: .5rem; }
        button { margin-top: .25rem; }
    </style>
</head>
<body>
<h1>Admin Dashboard</h1>
<p>Welcome, <strong><%= session.getAttribute("userName") %></strong> (<%= session.getAttribute("userType") %>)</p>

<% if (request.getAttribute("dashboardError") != null) { %>
<div class="error"><%= request.getAttribute("dashboardError") %></div>
<% } %>
<% if (request.getAttribute("applicationError") != null) { %>
<div class="error"><%= request.getAttribute("applicationError") %></div>
<% } %>
<% if (request.getAttribute("applicationSuccess") != null) { %>
<div class="success"><%= request.getAttribute("applicationSuccess") %></div>
<% } %>
<% if (request.getAttribute("jobManageError") != null) { %>
<div class="error"><%= request.getAttribute("jobManageError") %></div>
<% } %>
<% if (request.getAttribute("jobManageSuccess") != null) { %>
<div class="success"><%= request.getAttribute("jobManageSuccess") %></div>
<% } %>

<form class="inline" method="post" action="${pageContext.request.contextPath}/auth/logout">
    <button type="submit">Logout</button>
</form>

<div class="row">
    <div class="column">
        <h2>Post a Job</h2>
        <div class="card">
            <form method="post" action="${pageContext.request.contextPath}/jobs/manage">
                <input type="hidden" name="action" value="create">
                <label>Job Title</label>
                <input type="text" name="jobTitle" maxlength="200" required>
                <label>Company Name</label>
                <input type="text" name="companyName" maxlength="150" required>
                <label>Job Type</label>
                <select name="jobType" required>
                    <option value="Internship">Internship</option>
                    <option value="Full-time">Full-time</option>
                    <option value="Part-time">Part-time</option>
                    <option value="Contract">Contract</option>
                </select>
                <label>Domain</label>
                <input type="text" name="domain" maxlength="100">
                <label>Description</label>
                <textarea name="description" rows="4" required></textarea>
                <label>Location</label>
                <input type="text" name="location" maxlength="150">
                <label>Application Deadline</label>
                <input type="date" name="applicationDeadline">
                <label>Job Status</label>
                <select name="jobStatus" required>
                    <option value="Active">Active</option>
                    <option value="Draft">Draft</option>
                    <option value="Closed">Closed</option>
                </select>
                <button type="submit">Create Job</button>
            </form>
        </div>

        <h2>Manage All Jobs</h2>
        <%
            List<Job> managedJobs = (List<Job>) request.getAttribute("managedJobs");
            if (managedJobs == null || managedJobs.isEmpty()) {
        %>
        <p>No jobs available.</p>
        <% } else {
            for (Job managedJob : managedJobs) {
        %>
        <% pageContext.setAttribute("managedJobTitle", managedJob.getJobTitle()); %>
        <% pageContext.setAttribute("managedJobDomain", managedJob.getDomain() == null ? "" : managedJob.getDomain()); %>
        <% pageContext.setAttribute("managedJobLocation", managedJob.getLocation() == null ? "" : managedJob.getLocation()); %>
        <% pageContext.setAttribute("managedJobDeadline", managedJob.getApplicationDeadline()); %>
        <div class="card">
            <p>Job ID: <strong><%= managedJob.getJobId() %></strong></p>
            <form method="post" action="${pageContext.request.contextPath}/jobs/manage">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="jobId" value="<%= managedJob.getJobId() %>">
                <label>Job Title</label>
                <input type="text" name="jobTitle" maxlength="200" value="${fn:escapeXml(managedJobTitle)}" required>
                <label>Domain</label>
                <input type="text" name="domain" maxlength="100" value="${fn:escapeXml(managedJobDomain)}">
                <label>Location</label>
                <input type="text" name="location" maxlength="150" value="${fn:escapeXml(managedJobLocation)}">
                <label>Application Deadline</label>
                <input type="date" name="applicationDeadline" value="${managedJobDeadline}">
                <label>Status</label>
                <select name="jobStatus" required>
                    <option value="Active" <%= "Active".equals(managedJob.getJobStatus()) ? "selected" : "" %>>Active</option>
                    <option value="Draft" <%= "Draft".equals(managedJob.getJobStatus()) ? "selected" : "" %>>Draft</option>
                    <option value="Closed" <%= "Closed".equals(managedJob.getJobStatus()) ? "selected" : "" %>>Closed</option>
                </select>
                <button type="submit">Update Job</button>
            </form>
        </div>
        <% } } %>
    </div>

    <div class="column">
        <h2>All Applications to Review</h2>
        <%
            List<ApplicationReviewItem> reviewApplications = (List<ApplicationReviewItem>) request.getAttribute("reviewApplications");
            if (reviewApplications == null || reviewApplications.isEmpty()) {
        %>
        <p>No applications to review.</p>
        <% } else {
            for (ApplicationReviewItem item : reviewApplications) {
        %>
        <div class="card">
            <p>Application ID: <%= item.getApplicationId() %></p>
            <p>Job: <strong><%= item.getJobTitle() %></strong> (ID: <%= item.getJobId() %>)</p>
            <p>Candidate: <%= item.getStudentName() %> (ID: <%= item.getStudentId() %>)</p>
            <p>Current Status: <strong><%= item.getApplicationStatus() %></strong></p>
            <p>Applied At: <%= item.getAppliedAt() %></p>
            <form method="post" action="${pageContext.request.contextPath}/applications/review">
                <input type="hidden" name="applicationId" value="<%= item.getApplicationId() %>">
                <label>Status</label>
                <select name="status" required>
                    <option value="Pending" <%= "Pending".equals(item.getApplicationStatus()) ? "selected" : "" %>>Pending</option>
                    <option value="Reviewed" <%= "Reviewed".equals(item.getApplicationStatus()) ? "selected" : "" %>>Reviewed</option>
                    <option value="Shortlisted" <%= "Shortlisted".equals(item.getApplicationStatus()) ? "selected" : "" %>>Shortlisted</option>
                    <option value="Rejected" <%= "Rejected".equals(item.getApplicationStatus()) ? "selected" : "" %>>Rejected</option>
                    <option value="Accepted" <%= "Accepted".equals(item.getApplicationStatus()) ? "selected" : "" %>>Accepted</option>
                </select>
                <label>Reviewer Notes (optional)</label>
                <textarea name="reviewerNotes" rows="3"></textarea>
                <button type="submit">Update Status</button>
            </form>
        </div>
        <% } } %>
    </div>
</div>
</body>
</html>
