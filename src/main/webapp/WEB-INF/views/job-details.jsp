<%@ page import="com.gradhire.model.Job" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GradHire - Job Details</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body { font-family: Arial, sans-serif; margin: 2rem; }
        .container { max-width: 900px; margin: auto; }
        .card { border: 1px solid #ddd; border-radius: 8px; padding: 1rem; margin-top: 1rem; }
        .error { color: #b00020; margin-bottom: 1rem; }
        .success { color: #0f5132; margin-bottom: 1rem; }
        textarea { width: 100%; box-sizing: border-box; }
        .actions { margin-top: 1rem; }
        .actions a { margin-right: 1rem; }
    </style>
</head>
<body>
<div class="container">
    <h1>Job Details</h1>

    <% if (request.getAttribute("jobDetailsError") != null) { %>
    <div class="error"><%= request.getAttribute("jobDetailsError") %></div>
    <% } %>
    <% if (request.getAttribute("applicationError") != null) { %>
    <div class="error"><%= request.getAttribute("applicationError") %></div>
    <% } %>
    <% if (request.getAttribute("applicationSuccess") != null) { %>
    <div class="success"><%= request.getAttribute("applicationSuccess") %></div>
    <% } %>

    <%
        Job job = (Job) request.getAttribute("job");
        Boolean alreadyApplied = (Boolean) request.getAttribute("alreadyApplied");
        if (job != null) {
    %>
    <div class="card">
        <h2><%= job.getJobTitle() %></h2>
        <p><strong>Company:</strong> <%= job.getCompanyName() %></p>
        <p><strong>Type:</strong> <%= job.getJobType() %></p>
        <p><strong>Domain:</strong> <%= job.getDomain() %></p>
        <p><strong>Location:</strong> <%= job.getLocation() %></p>
        <p><strong>Application Deadline:</strong> <%= job.getApplicationDeadline() %></p>
    </div>

    <% if ("student".equalsIgnoreCase((String) session.getAttribute("userType"))) { %>
    <div class="card">
        <% if (Boolean.TRUE.equals(alreadyApplied)) { %>
        <p><strong>You have already applied for this job.</strong></p>
        <% } else { %>
        <form method="post" action="${pageContext.request.contextPath}/applications/apply">
            <input type="hidden" name="jobId" value="<%= job.getJobId() %>">
            <label for="coverLetter">Cover letter (optional)</label>
            <textarea id="coverLetter" name="coverLetter" rows="5"></textarea>
            <div class="actions">
                <button type="submit">Apply Now</button>
            </div>
        </form>
        <% } %>
    </div>
    <% } %>
    <% } %>

    <div class="actions">
        <a href="${pageContext.request.contextPath}/dashboard">Back to Dashboard</a>
    </div>
</div>
</body>
</html>
