<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Jobs - GradHire Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">

<div class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold mb-0">My Jobs</h3>
        <a href="${pageContext.request.contextPath}/admin/jobs/post" class="btn btn-primary">
            <i class="fas fa-plus"></i> Post New Job
        </a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:choose>
        <c:when test="${jobs == null or jobs.isEmpty()}">
            <div class="card text-center py-5">
                <div class="card-body">
                    <h5 class="mb-2">You have not posted any jobs yet.</h5>
                    <a href="${pageContext.request.contextPath}/admin/jobs/post" class="btn btn-primary">Post a Job</a>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Company</th>
                            <th>Type</th>
                            <th>Status</th>
                            <th>Posted</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${jobs}" var="job">
                            <tr>
                                <td><strong>${job.title}</strong></td>
                                <td>${job.company}</td>
                                <td>${job.jobType}</td>
                                <td>
                                    <span class="badge ${job.isActive() ? 'bg-success' : job.isClosed() ? 'bg-secondary' : 'bg-warning text-dark'}">${job.jobStatus}</span>
                                </td>
                                <td><fmt:formatDate value="${job.createdAt}" pattern="MMM dd, yyyy"/></td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/job/${job.jobId}" class="btn btn-sm btn-outline-primary">View</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="mt-3">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary">Back to Dashboard</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


