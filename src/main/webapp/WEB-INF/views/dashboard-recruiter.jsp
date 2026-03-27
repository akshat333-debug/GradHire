<%@ page import="java.util.List" %>
<%@ page import="com.gradhire.model.Job" %>
<%@ page import="com.gradhire.model.ApplicationReviewItem" %>
<%@ page import="com.gradhire.model.ActivityLog" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Recruiter Dashboard</title>
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
            <h1>Recruiter Dashboard</h1>
            <p class="muted">Welcome, <strong><%= session.getAttribute("userName") %></strong>. Post roles, manage updates, and review applications.</p>
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
    <% if (request.getAttribute("jobManageError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("jobManageError") %></div>
    <% } %>
    <% if (request.getAttribute("jobManageSuccess") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("jobManageSuccess") %></div>
    <% } %>

    <div class="grid grid-2">
        <section class="stack">
            <div class="card stack">
                <div class="section-header">
                    <h2>Post a Job</h2>
                    <span class="pill">Create new role</span>
                </div>
                <form class="stack" method="post" action="${pageContext.request.contextPath}/jobs/manage">
                    <input type="hidden" name="action" value="create">
                    <div class="field">
                        <label>Job Title</label>
                        <input type="text" name="jobTitle" maxlength="200" required>
                    </div>
                    <div class="field">
                        <label>Company Name</label>
                        <input type="text" name="companyName" maxlength="150" required>
                    </div>
                    <div class="field">
                        <label>Job Type</label>
                        <select name="jobType" required>
                            <option value="Internship">Internship</option>
                            <option value="Full-time">Full-time</option>
                            <option value="Part-time">Part-time</option>
                            <option value="Contract">Contract</option>
                        </select>
                    </div>
                    <div class="field">
                        <label>Domain</label>
                        <input type="text" name="domain" maxlength="100">
                    </div>
                    <div class="field">
                        <label>Description</label>
                        <textarea name="description" rows="4" required></textarea>
                    </div>
                    <div class="field">
                        <label>Location</label>
                        <input type="text" name="location" maxlength="150">
                    </div>
                    <div class="field">
                        <label>Application Deadline</label>
                        <input type="date" name="applicationDeadline">
                    </div>
                    <div class="field">
                        <label>Job Status</label>
                        <select name="jobStatus" required>
                            <option value="Active">Active</option>
                            <option value="Draft">Draft</option>
                            <option value="Closed">Closed</option>
                        </select>
                    </div>
                    <button class="btn btn-primary" type="submit">Create Job</button>
                </form>
            </div>

            <div class="card stack">
                <div class="section-header">
                    <h2>Manage Your Jobs</h2>
                    <span class="pill">Update postings</span>
                </div>
                <%
                    List<Job> managedJobs = (List<Job>) request.getAttribute("managedJobs");
                    if (managedJobs == null || managedJobs.isEmpty()) {
                %>
                <p class="muted">No jobs posted yet.</p>
                <% } else {
                    for (Job managedJob : managedJobs) {
                %>
                <% pageContext.setAttribute("managedJobTitle", managedJob.getJobTitle()); %>
                <% pageContext.setAttribute("managedJobDomain", managedJob.getDomain() == null ? "" : managedJob.getDomain()); %>
                <% pageContext.setAttribute("managedJobLocation", managedJob.getLocation() == null ? "" : managedJob.getLocation()); %>
                <% pageContext.setAttribute("managedJobDeadline", managedJob.getApplicationDeadline()); %>
                <div class="card" style="margin-top:.5rem;">
                    <p class="muted">Job ID: <strong><%= managedJob.getJobId() %></strong></p>
                    <form class="stack" method="post" action="${pageContext.request.contextPath}/jobs/manage">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="jobId" value="<%= managedJob.getJobId() %>">
                        <div class="field">
                            <label>Job Title</label>
                            <input type="text" name="jobTitle" maxlength="200" value="${fn:escapeXml(managedJobTitle)}" required>
                        </div>
                        <div class="field">
                            <label>Domain</label>
                            <input type="text" name="domain" maxlength="100" value="${fn:escapeXml(managedJobDomain)}">
                        </div>
                        <div class="field">
                            <label>Location</label>
                            <input type="text" name="location" maxlength="150" value="${fn:escapeXml(managedJobLocation)}">
                        </div>
                        <div class="field">
                            <label>Application Deadline</label>
                            <input type="date" name="applicationDeadline" value="${managedJobDeadline}">
                        </div>
                        <div class="field">
                            <label>Status</label>
                            <select name="jobStatus" required>
                                <option value="Active" <%= "Active".equals(managedJob.getJobStatus()) ? "selected" : "" %>>Active</option>
                                <option value="Draft" <%= "Draft".equals(managedJob.getJobStatus()) ? "selected" : "" %>>Draft</option>
                                <option value="Closed" <%= "Closed".equals(managedJob.getJobStatus()) ? "selected" : "" %>>Closed</option>
                            </select>
                        </div>
                        <button class="btn btn-primary" type="submit">Update Job</button>
                    </form>
                </div>
                <% } } %>
            </div>
        </section>

        <section class="stack">
            <div class="card stack">
                <div class="section-header">
                    <h2>Applications to Review</h2>
                    <span class="pill">Move candidates forward</span>
                </div>
                <%
                    List<ApplicationReviewItem> reviewApplications = (List<ApplicationReviewItem>) request.getAttribute("reviewApplications");
                    if (reviewApplications == null || reviewApplications.isEmpty()) {
                %>
                <p class="muted">No applications to review.</p>
                <% } else {
                    for (ApplicationReviewItem item : reviewApplications) {
                %>
                <div class="card" style="margin-top:.5rem;">
                    <p><strong><%= item.getJobTitle() %></strong> (Job ID: <%= item.getJobId() %>)</p>
                    <p class="muted">Application ID: <%= item.getApplicationId() %> · Candidate: <%= item.getStudentName() %> (ID: <%= item.getStudentId() %>)</p>
                    <p>Current Status: <strong><%= item.getApplicationStatus() %></strong></p>
                    <p>Applied At: <%= item.getAppliedAt() %></p>
                    <form class="stack" method="post" action="${pageContext.request.contextPath}/applications/review">
                        <input type="hidden" name="applicationId" value="<%= item.getApplicationId() %>">
                        <div class="field">
                            <label>Status</label>
                            <select name="status" required>
                                <option value="Pending" <%= "Pending".equals(item.getApplicationStatus()) ? "selected" : "" %>>Pending</option>
                                <option value="Reviewed" <%= "Reviewed".equals(item.getApplicationStatus()) ? "selected" : "" %>>Reviewed</option>
                                <option value="Shortlisted" <%= "Shortlisted".equals(item.getApplicationStatus()) ? "selected" : "" %>>Shortlisted</option>
                                <option value="Rejected" <%= "Rejected".equals(item.getApplicationStatus()) ? "selected" : "" %>>Rejected</option>
                                <option value="Accepted" <%= "Accepted".equals(item.getApplicationStatus()) ? "selected" : "" %>>Accepted</option>
                            </select>
                        </div>
                        <div class="field">
                            <label>Reviewer Notes (optional)</label>
                            <textarea name="reviewerNotes" rows="3"></textarea>
                        </div>
                        <button class="btn btn-primary" type="submit">Update Status</button>
                    </form>
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
