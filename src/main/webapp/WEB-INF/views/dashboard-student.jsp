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
            <a class="btn btn-ghost" href="${pageContext.request.contextPath}/profile">Profile</a>
            <form class="nav-actions" method="post" action="${pageContext.request.contextPath}/auth/logout" style="margin:0;">
                <button class="btn btn-primary" type="submit">Logout</button>
            </form>
        </div>
    </div>
</header>

<main class="content">
    <div class="section-header">
        <div>
            <h1>Student Dashboard</h1>
            <p class="muted">Welcome, <strong><%= session.getAttribute("userName") %></strong> — explore roles, save favorites, and track applications.</p>
        </div>
        <span class="pill">Role: <%= session.getAttribute("userType") %></span>
    </div>

    <% if (request.getAttribute("dashboardError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("dashboardError") %></div>
    <% } %>
    <% if (request.getAttribute("applicationError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("applicationError") %></div>
    <% } %>
    <% if (request.getAttribute("applicationSuccess") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("applicationSuccess") %></div>
    <% } %>
    <% if (request.getAttribute("savedJobError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("savedJobError") %></div>
    <% } %>
    <% if (request.getAttribute("savedJobSuccess") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("savedJobSuccess") %></div>
    <% } %>

    <div class="grid grid-2">
        <section class="stack">
            <div class="section-header">
                <h2>Active Jobs</h2>
                <span class="pill">Apply directly</span>
            </div>
            <%
                List<Job> jobs = (List<Job>) request.getAttribute("jobs");
                Set<Integer> appliedJobIds = (Set<Integer>) request.getAttribute("appliedJobIds");
                Set<Integer> savedJobIds = (Set<Integer>) request.getAttribute("savedJobIds");
                if (jobs == null || jobs.isEmpty()) {
            %>
            <div class="card">
                <p class="muted">No jobs available.</p>
            </div>
            <% } else {
                for (Job job : jobs) {
            %>
            <div class="card stack">
                <div class="section-header">
                    <div>
                        <h3><%= job.getJobTitle() %></h3>
                        <p class="muted"><%= job.getCompanyName() %> · <%= job.getJobType() %></p>
                    </div>
                    <span class="badge">Deadline: <%= job.getApplicationDeadline() %></span>
                </div>
                <div class="pill-group">
                    <span class="tag"><%= job.getDomain() %></span>
                    <span class="tag"><%= job.getLocation() %></span>
                </div>
                <a class="btn btn-link" href="${pageContext.request.contextPath}/jobs/details?jobId=<%= job.getJobId() %>">View Details</a>
                <% boolean alreadyApplied = appliedJobIds != null && appliedJobIds.contains(job.getJobId()); %>
                <% boolean alreadySaved = savedJobIds != null && savedJobIds.contains(job.getJobId()); %>
                <% if (alreadyApplied) { %>
                <div class="alert alert-success">Already applied</div>
                <% } else { %>
                <form class="stack" method="post" action="${pageContext.request.contextPath}/applications/apply">
                    <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
                    <div class="field">
                        <label>Cover letter (optional)</label>
                        <textarea name="coverLetter" rows="3"></textarea>
                    </div>
                    <button class="btn btn-primary" type="submit">Apply</button>
                </form>
                <% } %>
                <form class="nav-actions" method="post" action="${pageContext.request.contextPath}/jobs/saved" style="margin-top:.5rem;">
                    <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
                    <input type="hidden" name="action" value="<%= alreadySaved ? "unsave" : "save" %>">
                    <button class="btn btn-ghost" type="submit"><%= alreadySaved ? "Unsave Job" : "Save Job" %></button>
                </form>
            </div>
            <% } } %>
        </section>

        <section class="stack">
            <div class="card stack">
                <div class="section-header">
                    <h2>Recommended Jobs</h2>
                    <span class="pill">Matches for you</span>
                </div>
                <%
                    List<Job> recommendedJobs = (List<Job>) request.getAttribute("recommendedJobs");
                    if (recommendedJobs == null || recommendedJobs.isEmpty()) {
                %>
                <p class="muted">No recommendations available right now.</p>
                <% } else {
                    for (Job recJob : recommendedJobs) {
                %>
                <div class="card" style="margin-top:.5rem;">
                    <% pageContext.setAttribute("recJobTitle", recJob.getJobTitle()); %>
                    <% pageContext.setAttribute("recCompany", recJob.getCompanyName()); %>
                    <% pageContext.setAttribute("recDomain", recJob.getDomain()); %>
                    <h3>${fn:escapeXml(recJobTitle)}</h3>
                    <p class="muted">${fn:escapeXml(recCompany)} · ${fn:escapeXml(recDomain)}</p>
                    <a class="btn btn-link" href="${pageContext.request.contextPath}/jobs/details?jobId=<%= recJob.getJobId() %>">View Details</a>
                </div>
                <% } } %>
            </div>

            <div class="card stack">
                <div class="section-header">
                    <h2>Saved Jobs</h2>
                    <span class="pill">Your shortlist</span>
                </div>
                <%
                    List<Job> savedJobs = (List<Job>) request.getAttribute("savedJobs");
                    if (savedJobs == null || savedJobs.isEmpty()) {
                %>
                <p class="muted">No saved jobs yet.</p>
                <% } else {
                    for (Job savedJob : savedJobs) {
                %>
                <div class="card" style="margin-top:.5rem;">
                    <% pageContext.setAttribute("savedJobTitle", savedJob.getJobTitle()); %>
                    <% pageContext.setAttribute("savedCompany", savedJob.getCompanyName()); %>
                    <p><strong>${fn:escapeXml(savedJobTitle)}</strong> (${fn:escapeXml(savedCompany)})</p>
                    <form class="nav-actions" method="post" action="${pageContext.request.contextPath}/jobs/saved">
                        <input type="hidden" name="jobId" value="<%= savedJob.getJobId() %>">
                        <input type="hidden" name="action" value="unsave">
                        <button class="btn btn-ghost" type="submit">Remove</button>
                    </form>
                </div>
                <% } } %>
            </div>

            <div class="card stack">
                <div class="section-header">
                    <h2>Your Applications</h2>
                    <span class="pill">Status updates</span>
                </div>
                <%
                    List<Application> applications = (List<Application>) request.getAttribute("applications");
                    if (applications == null || applications.isEmpty()) {
                %>
                <p class="muted">No applications yet.</p>
                <% } else {
                    for (Application application : applications) {
                %>
                <div class="card" style="margin-top:.5rem;">
                    <p class="muted">Application ID: <%= application.getApplicationId() %> · Job ID: <%= application.getJobId() %></p>
                    <p>Status: <strong><%= application.getApplicationStatus() %></strong></p>
                    <p>Applied At: <%= application.getAppliedAt() %></p>
                    <% if (application.getReviewedAt() != null) { %>
                    <p>Reviewed At: <%= application.getReviewedAt() %></p>
                    <% } %>
                    <% if (application.getReviewerNotes() != null && !application.getReviewerNotes().trim().isEmpty()) { %>
                    <% pageContext.setAttribute("reviewerNotes", application.getReviewerNotes()); %>
                    <p class="muted">Reviewer Notes:</p>
                    <p>${fn:escapeXml(reviewerNotes)}</p>
                    <% } %>
                </div>
                <% } } %>
            </div>

            <div class="card stack">
                <div class="section-header">
                    <h2>Recent Activity</h2>
                    <span class="pill">Latest events</span>
                </div>
                <%
                    List<ActivityLog> activityLogs = (List<ActivityLog>) request.getAttribute("activityLogs");
                    if (activityLogs == null || activityLogs.isEmpty()) {
                %>
                <p class="muted">No recent activity.</p>
                <% } else {
                    for (ActivityLog log : activityLogs) {
                %>
                <div class="card" style="margin-top:.5rem;">
                    <% pageContext.setAttribute("activityType", log.getActivityType()); %>
                    <% pageContext.setAttribute("activityDesc", log.getActivityDescription()); %>
                    <p><strong>${fn:escapeXml(activityType)}</strong> — <%= log.getCreatedAt() %></p>
                    <p class="muted">${fn:escapeXml(activityDesc)}</p>
                </div>
                <% } } %>
            </div>
        </section>
    </div>
</main>
</body>
</html>
