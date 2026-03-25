<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="com.gradhire.model.Job" %>
<%@ page import="com.gradhire.model.Application" %>
<%@ page import="com.gradhire.model.ActivityLog" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Student Dashboard</title>
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
        .reviewer-notes { white-space: pre-wrap; }
    </style>
</head>
<body>
<h1>Student Dashboard</h1>
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
<a href="${pageContext.request.contextPath}/profile">Manage Profile</a>

<div class="row">
    <div class="column">
        <h2>Active Jobs</h2>
        <%
            List<Job> jobs = (List<Job>) request.getAttribute("jobs");
            Set<Integer> appliedJobIds = (Set<Integer>) request.getAttribute("appliedJobIds");
            Set<Integer> savedJobIds = (Set<Integer>) request.getAttribute("savedJobIds");
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
            <% boolean alreadySaved = savedJobIds != null && savedJobIds.contains(job.getJobId()); %>
            <form class="inline" method="post" action="${pageContext.request.contextPath}/jobs/saved">
                <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
                <input type="hidden" name="action" value="<%= alreadySaved ? "unsave" : "save" %>">
                <button type="submit"><%= alreadySaved ? "Unsave Job" : "Save Job" %></button>
            </form>
        </div>
        <% } } %>
    </div>

    <div class="column">
        <h2>Recommended Jobs</h2>
        <%
            List<Job> recommendedJobs = (List<Job>) request.getAttribute("recommendedJobs");
            if (recommendedJobs == null || recommendedJobs.isEmpty()) {
        %>
        <p>No recommendations available right now.</p>
        <% } else {
            for (Job recJob : recommendedJobs) {
        %>
        <div class="card">
            <h3><%= recJob.getJobTitle() %></h3>
            <p><%= recJob.getCompanyName() %> | <%= recJob.getDomain() %></p>
            <p><a href="${pageContext.request.contextPath}/jobs/details?jobId=<%= recJob.getJobId() %>">View Details</a></p>
        </div>
        <% } } %>

        <h2>Saved Jobs</h2>
        <%
            List<Job> savedJobs = (List<Job>) request.getAttribute("savedJobs");
            if (savedJobs == null || savedJobs.isEmpty()) {
        %>
        <p>No saved jobs yet.</p>
        <% } else {
            for (Job savedJob : savedJobs) {
        %>
        <div class="card">
            <p><strong><%= savedJob.getJobTitle() %></strong> (<%= savedJob.getCompanyName() %>)</p>
            <form class="inline" method="post" action="${pageContext.request.contextPath}/jobs/saved">
                <input type="hidden" name="jobId" value="<%= savedJob.getJobId() %>">
                <input type="hidden" name="action" value="unsave">
                <button type="submit">Remove</button>
            </form>
        </div>
        <% } } %>

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

        <h2>Recent Activity</h2>
        <%
            List<ActivityLog> activityLogs = (List<ActivityLog>) request.getAttribute("activityLogs");
            if (activityLogs == null || activityLogs.isEmpty()) {
        %>
        <p>No recent activity.</p>
        <% } else {
            for (ActivityLog log : activityLogs) {
        %>
        <div class="card">
            <p><strong><%= log.getActivityType() %></strong> - <%= log.getCreatedAt() %></p>
            <p><%= log.getActivityDescription() %></p>
        </div>
        <% } } %>
    </div>
</div>
</body>
</html>
