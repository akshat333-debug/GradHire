<%@ page import="com.gradhire.model.Job" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Job Details</title>
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
        <h1>Job Details</h1>
        <span class="pill">Review the role then apply</span>
    </div>

    <% if (request.getAttribute("jobDetailsError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("jobDetailsError") %></div>
    <% } %>
    <% if (request.getAttribute("applicationError") != null) { %>
    <div class="alert alert-error"><%= request.getAttribute("applicationError") %></div>
    <% } %>
    <% if (request.getAttribute("applicationSuccess") != null) { %>
    <div class="alert alert-success"><%= request.getAttribute("applicationSuccess") %></div>
    <% } %>

    <%
        Job job = (Job) request.getAttribute("job");
        Boolean alreadyApplied = (Boolean) request.getAttribute("alreadyApplied");
        if (job != null) {
    %>
    <div class="card stack">
        <div class="section-header">
            <h2><%= job.getJobTitle() %></h2>
            <span class="badge"><%= job.getJobType() %></span>
        </div>
        <p><strong><%= job.getCompanyName() %></strong></p>
        <div class="pill-group">
            <span class="tag"><%= job.getDomain() %></span>
            <span class="tag"><%= job.getLocation() %></span>
            <span class="tag">Deadline: <%= job.getApplicationDeadline() %></span>
        </div>
    </div>

    <% if ("student".equalsIgnoreCase((String) session.getAttribute("userType"))) { %>
    <div class="card stack">
        <% if (Boolean.TRUE.equals(alreadyApplied)) { %>
        <div class="alert alert-success">You have already applied for this job.</div>
        <% } else { %>
        <form class="stack" method="post" action="${pageContext.request.contextPath}/applications/apply">
            <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
            <div class="field">
                <label for="coverLetter">Cover Letter (optional)</label>
                <textarea id="coverLetter" name="coverLetter" rows="5"></textarea>
            </div>
            <div class="row">
                <button class="btn btn-primary" type="submit">Apply Now</button>
                <a class="btn btn-ghost" href="${pageContext.request.contextPath}/dashboard">Back to Dashboard</a>
            </div>
        </form>
        <% } %>
    </div>
    <% } %>
    <% } %>
</main>
</body>
</html>
