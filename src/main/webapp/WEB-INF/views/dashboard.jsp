<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.gradhire.model.Job" %>
<%@ page import="com.gradhire.model.Application" %>
<%@ page import="com.gradhire.model.ApplicationReviewItem" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
        .success { color: #0f5132; margin-bottom: 1rem; }
        form.inline { display: inline; }
        textarea { width: 100%; box-sizing: border-box; }
        select, button { margin-top: .5rem; }
        .reviewer-notes { white-space: pre-wrap; }
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
<% if (request.getAttribute("applicationSuccess") != null) { %>
<div class="success"><%= request.getAttribute("applicationSuccess") %></div>
<% } %>

<form class="inline" method="post" action="${pageContext.request.contextPath}/auth/logout">
    <button type="submit">Logout</button>
</form>

<div class="row">
    <div class="column">
        <h2>Active Jobs</h2>
        <%
            List<Job> jobs = (List<Job>) request.getAttribute("jobs");
            Set<Integer> appliedJobIds = (Set<Integer>) request.getAttribute("appliedJobIds");
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
            <p>Deadline: <%= job.getApplicationDeadline() %></p>
            <p><a href="${pageContext.request.contextPath}/jobs/details?jobId=<%= job.getJobId() %>">View Details</a></p>
            <% if ("student".equalsIgnoreCase((String) session.getAttribute("userType"))) { %>
            <% boolean alreadyApplied = appliedJobIds != null && appliedJobIds.contains(job.getJobId()); %>
            <% if (alreadyApplied) { %>
            <p><strong>Already applied</strong></p>
            <% } else { %>
            <form method="post" action="${pageContext.request.contextPath}/applications/apply">
                <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
                <label>Cover letter (optional)</label>
                <textarea name="coverLetter" rows="3"></textarea>
                <button type="submit">Apply</button>
            </form>
            <% } %>
            <% } %>
        </div>
        <% } } %>
    </div>

    <div class="column">
        <% if ("student".equalsIgnoreCase((String) session.getAttribute("userType"))) { %>
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
            <% if (application.getReviewedAt() != null) { %>
            <p>Reviewed At: <%= application.getReviewedAt() %></p>
            <% } %>
            <% if (application.getReviewerNotes() != null && !application.getReviewerNotes().trim().isEmpty()) { %>
            <% pageContext.setAttribute("reviewerNotes", application.getReviewerNotes()); %>
            <p class="reviewer-notes">Reviewer Notes: ${fn:escapeXml(reviewerNotes)}</p>
            <% } %>
        </div>
        <% } } %>
        <% } else { %>
        <h2>Applications to Review</h2>
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
        <% } %>
    </div>
</div>
</body>
</html>
