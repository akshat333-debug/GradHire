<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recruiter Analytics - GradHire</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .stat-card { border-radius: 12px; padding: 20px; color: #fff; }
    </style>
    
    <c:set var="stats" value="${requestScope.stats}"/>
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold mb-0"><i class="fas fa-chart-bar text-primary"></i> Analytics</h2>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary"><i class="fas fa-arrow-left"></i> Back to Dashboard</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="row g-3 mb-4">
        <div class="col-md-3">
            <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                <div class="d-flex align-items-center">
                    <i class="fas fa-briefcase fa-2x me-3"></i>
                    <div>
                        <div class="h4 mb-0">${stats.totalJobs}</div>
                        <small>Active Jobs</small>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                <div class="d-flex align-items-center">
                    <i class="fas fa-file-alt fa-2x me-3"></i>
                    <div>
                        <div class="h4 mb-0">${stats.totalApplications}</div>
                        <small>Total Applications</small>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                <div class="d-flex align-items-center">
                    <i class="fas fa-check-circle fa-2x me-3"></i>
                    <div>
                        <div class="h4 mb-0">${stats.accepted}</div>
                        <small>Accepted</small>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="stat-card" style="background: linear-gradient(135deg, #ffd86f 0%, #fc6262 100%);">
                <div class="d-flex align-items-center">
                    <i class="fas fa-clock fa-2x me-3"></i>
                    <div>
                        <div class="h4 mb-0">${stats.pending}</div>
                        <small>Pending</small>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="card mb-4">
        <div class="card-body">
            <h5 class="card-title fw-bold mb-3">Status Breakdown</h5>
            <div class="row text-center">
                <div class="col-6 col-md-2 mb-3">
                    <div class="fw-bold">${stats.pendingPct}%</div>
                    <small class="text-muted">Pending</small>
                </div>
                <div class="col-6 col-md-2 mb-3">
                    <div class="fw-bold">${stats.reviewedPct}%</div>
                    <small class="text-muted">Reviewed</small>
                </div>
                <div class="col-6 col-md-2 mb-3">
                    <div class="fw-bold">${stats.shortlistedPct}%</div>
                    <small class="text-muted">Shortlisted</small>
                </div>
                <div class="col-6 col-md-2 mb-3">
                    <div class="fw-bold">${stats.acceptedPct}%</div>
                    <small class="text-muted">Accepted</small>
                </div>
                <div class="col-6 col-md-2 mb-3">
                    <div class="fw-bold">${stats.rejectedPct}%</div>
                    <small class="text-muted">Rejected</small>
                </div>
            </div>
        </div>
    </div>

    <div class="card">
        <div class="card-body">
            <h5 class="card-title fw-bold mb-3">Recent Applications</h5>
            <c:choose>
                <c:when test="${empty recentApplications}">
                    <p class="text-muted mb-0">No recent applications.</p>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Application ID</th>
                                    <th>Job ID</th>
                                    <th>Student ID</th>
                                    <th>Status</th>
                                    <th>Applied</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${recentApplications}" var="a">
                                    <tr>
                                        <td>${a.applicationId}</td>
                                        <td>${a.jobId}</td>
                                        <td>${a.studentId}</td>
                                        <td>${a.applicationStatus}</td>
                                        <td><fmt:formatDate value="${a.appliedAt}" pattern="MMM dd, yyyy"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="mt-4 text-end">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-secondary">Back</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


