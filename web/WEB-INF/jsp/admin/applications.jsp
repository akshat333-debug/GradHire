<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Review Applications - GradHire Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="mb-0"><i class="fas fa-file-alt text-primary"></i> Applications</h3>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary">Back to Dashboard</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <c:choose>
        <c:when test="${empty applications}">
            <div class="card">
                <div class="card-body text-center py-5">
                    <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                    <p class="text-muted mb-0">No applications found.</p>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card">
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead>
                            <tr>
                                <th>Candidate</th>
                                <th>Job</th>
                                <th>Applied</th>
                                <th>Status</th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${applications}" var="app">
                                <tr>
                                    <td>
                                        <strong>${app.student.firstName} ${app.student.lastName}</strong><br>
                                        <small class="text-muted">${app.student.email}</small>
                                    </td>
                                    <td>
                                        <strong>${app.job.title}</strong><br>
                                        <small class="text-muted">${app.job.company}</small>
                                    </td>
                                    <td>
                                        <small class="text-muted">
                                            <fmt:formatDate value="${app.appliedAt}" pattern="MMM dd, yyyy"/>
                                        </small>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${app.status == 'PENDING'}"><span class="badge bg-warning text-dark">Pending</span></c:when>
                                            <c:when test="${app.status == 'REVIEWED'}"><span class="badge bg-info">Reviewed</span></c:when>
                                            <c:when test="${app.status == 'SHORTLISTED'}"><span class="badge bg-primary">Shortlisted</span></c:when>
                                            <c:when test="${app.status == 'ACCEPTED'}"><span class="badge bg-success">Accepted</span></c:when>
                                            <c:when test="${app.status == 'REJECTED'}"><span class="badge bg-danger">Rejected</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary">${app.status}</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <a href="${pageContext.request.contextPath}/admin/application/${app.applicationId}" class="btn btn-sm btn-outline-primary">
                                            Review
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
