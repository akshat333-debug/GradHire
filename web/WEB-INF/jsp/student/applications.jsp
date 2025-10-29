<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Applications - GradHire</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">
    
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block dashboard-sidebar collapse" id="sidebar">
                <div class="position-sticky">
                    <div class="text-center text-white py-4">
                        <div class="mb-3">
                            <img src="${pageContext.request.contextPath}/uploads/profiles/${student.profilePicture != null ? student.profilePicture : 'default-avatar.png'}" 
                                 alt="Profile" class="rounded-circle" width="80" height="80"
                                 onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'">
                        </div>
                        <h5 class="fw-bold">${student.firstName} ${student.lastName}</h5>
                        <p class="small mb-0">${student.email}</p>
                    </div>
                    
                    <hr class="border-light opacity-25">
                    
                    <ul class="nav flex-column px-2">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/dashboard">
                                <i class="fas fa-home"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/profile">
                                <i class="fas fa-user"></i> My Profile
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/jobs">
                                <i class="fas fa-briefcase"></i> Browse Jobs
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/student/applications">
                                <i class="fas fa-file-alt"></i> My Applications
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/recommendations">
                                <i class="fas fa-star"></i> Recommendations
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/settings">
                                <i class="fas fa-cog"></i> Settings
                            </a>
                        </li>
                    </ul>
                    
                    <hr class="border-light opacity-25">
                    
                    <ul class="nav flex-column px-2">
                        <li class="nav-item">
                            <a class="nav-link text-danger" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt"></i> Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>
            
            <!-- Main Content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <!-- Top Navigation -->
                <div class="dashboard-header sticky-top">
                    <div class="d-flex justify-content-between align-items-center py-3">
                        <div>
                            <button class="btn btn-sm btn-outline-primary d-md-none" type="button" 
                                    data-bs-toggle="collapse" data-bs-target="#sidebar">
                                <i class="fas fa-bars"></i>
                            </button>
                            <h3 class="mb-0 d-inline-block ms-2">
                                <i class="fas fa-graduation-cap text-primary"></i> GradHire
                            </h3>
                        </div>
                        <div class="d-flex align-items-center">
                            <div class="position-relative me-3">
                                <a href="${pageContext.request.contextPath}/student/notifications" class="btn btn-outline-secondary position-relative">
                                    <i class="fas fa-bell"></i>
                                </a>
                            </div>
                            <div class="dropdown">
                                <button class="btn btn-outline-secondary dropdown-toggle" type="button" 
                                        id="userMenu" data-bs-toggle="dropdown">
                                    <i class="fas fa-user-circle"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/student/profile">
                                        <i class="fas fa-user"></i> Profile
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/student/settings">
                                        <i class="fas fa-cog"></i> Settings
                                    </a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="fas fa-sign-out-alt"></i> Logout
                                    </a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Page Header -->
                <div class="mt-4 mb-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h2 class="fw-bold">
                                <i class="fas fa-file-alt text-primary"></i> My Job Applications
                            </h2>
                            <p class="text-muted">Track the status of your job applications</p>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Apply for More Jobs
                            </a>
                        </div>
                    </div>
                </div>
                
                <!-- Success/Error Messages -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle"></i> ${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <!-- Applications Content -->
                <c:choose>
                    <c:when test="${empty applications}">
                        <div class="row">
                            <div class="col-12">
                                <div class="dashboard-card">
                                    <div class="card-body text-center py-5">
                                        <i class="fas fa-inbox fa-4x text-muted mb-4"></i>
                                        <h4 class="text-muted mb-3">No Applications Yet</h4>
                                        <p class="text-muted mb-4">
                                            You haven't applied to any jobs yet. Start your job search journey today!
                                        </p>
                                        <div class="d-flex justify-content-center gap-3">
                                            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary">
                                                <i class="fas fa-search"></i> Browse Jobs
                                            </a>
                                            <a href="${pageContext.request.contextPath}/student/recommendations" class="btn btn-outline-primary">
                                                <i class="fas fa-star"></i> View Recommendations
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Application Statistics -->
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="dashboard-card bg-gradient-primary text-white">
                                    <div class="card-body text-center">
                                        <i class="fas fa-file-alt fa-2x mb-2"></i>
                                        <h3>${applications.size()}</h3>
                                        <p class="mb-0">Total Applications</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="dashboard-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white;">
                                    <div class="card-body text-center">
                                        <i class="fas fa-clock fa-2x mb-2"></i>
                                        <h3>
                                            <c:set var="pendingCount" value="0" />
                                            <c:forEach items="${applications}" var="app">
                                                <c:if test="${app.status == 'PENDING'}">
                                                    <c:set var="pendingCount" value="${pendingCount + 1}" />
                                                </c:if>
                                            </c:forEach>
                                            ${pendingCount}
                                        </h3>
                                        <p class="mb-0">Pending</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="dashboard-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); color: white;">
                                    <div class="card-body text-center">
                                        <i class="fas fa-eye fa-2x mb-2"></i>
                                        <h3>
                                            <c:set var="reviewedCount" value="0" />
                                            <c:forEach items="${applications}" var="app">
                                                <c:if test="${app.status == 'REVIEWED' || app.status == 'SHORTLISTED'}">
                                                    <c:set var="reviewedCount" value="${reviewedCount + 1}" />
                                                </c:if>
                                            </c:forEach>
                                            ${reviewedCount}
                                        </h3>
                                        <p class="mb-0">Under Review</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="dashboard-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); color: white;">
                                    <div class="card-body text-center">
                                        <i class="fas fa-check-circle fa-2x mb-2"></i>
                                        <h3>
                                            <c:set var="acceptedCount" value="0" />
                                            <c:forEach items="${applications}" var="app">
                                                <c:if test="${app.status == 'ACCEPTED'}">
                                                    <c:set var="acceptedCount" value="${acceptedCount + 1}" />
                                                </c:if>
                                            </c:forEach>
                                            ${acceptedCount}
                                        </h3>
                                        <p class="mb-0">Accepted</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Applications Table -->
                        <div class="row">
                            <div class="col-12">
                                <div class="dashboard-card">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h5 class="card-title fw-bold mb-0">
                                                <i class="fas fa-list text-primary"></i> Application History
                                            </h5>
                                            <div class="btn-group" role="group">
                                                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="filterApplications('all')">
                                                    All
                                                </button>
                                                <button type="button" class="btn btn-outline-warning btn-sm" onclick="filterApplications('PENDING')">
                                                    Pending
                                                </button>
                                                <button type="button" class="btn btn-outline-info btn-sm" onclick="filterApplications('REVIEWED')">
                                                    Reviewed
                                                </button>
                                                <button type="button" class="btn btn-outline-success btn-sm" onclick="filterApplications('ACCEPTED')">
                                                    Accepted
                                                </button>
                                            </div>
                                        </div>
                                        
                                        <div class="table-responsive">
                                            <table class="table table-hover" id="applicationsTable">
                                                <thead>
                                                    <tr>
                                                        <th>Job Title</th>
                                                        <th>Company</th>
                                                        <th>Applied Date</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${applications}" var="app">
                                                        <tr data-status="${app.status}">
                                                            <td>
                                                                <div>
                                                                    <strong>${app.job.jobTitle}</strong>
                                                                    <br>
                                                                    <small class="text-muted">${app.job.jobType}</small>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <div>
                                                                    <strong>${app.job.companyName}</strong>
                                                                    <br>
                                                                    <small class="text-muted">${app.job.location}</small>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <small class="text-muted">
                                                                    <fmt:formatDate value="${app.appliedAt}" pattern="MMM dd, yyyy" />
                                                                    <br>
                                                                    <fmt:formatDate value="${app.appliedAt}" pattern="HH:mm" />
                                                                </small>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${app.status == 'PENDING'}">
                                                                        <span class="badge bg-warning text-dark">
                                                                            <i class="fas fa-clock"></i> Pending
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'REVIEWED'}">
                                                                        <span class="badge bg-info">
                                                                            <i class="fas fa-eye"></i> Reviewed
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'SHORTLISTED'}">
                                                                        <span class="badge bg-primary">
                                                                            <i class="fas fa-star"></i> Shortlisted
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'ACCEPTED'}">
                                                                        <span class="badge bg-success">
                                                                            <i class="fas fa-check"></i> Accepted
                                                                        </span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'REJECTED'}">
                                                                        <span class="badge bg-danger">
                                                                            <i class="fas fa-times"></i> Rejected
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-secondary">${app.status}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <div class="btn-group" role="group">
                                                                    <a href="${pageContext.request.contextPath}/job/${app.job.jobId}" 
                                                                       class="btn btn-sm btn-outline-primary" title="View Job Details">
                                                                        <i class="fas fa-eye"></i>
                                                                    </a>
                                                                    <button class="btn btn-sm btn-outline-info" 
                                                                            onclick="viewApplicationDetails(${app.applicationId})" 
                                                                            title="View Application Details">
                                                                        <i class="fas fa-file-alt"></i>
                                                                    </button>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                
            </main>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
    <script>
        // Filter applications by status
        function filterApplications(status) {
            const rows = document.querySelectorAll('#applicationsTable tbody tr');
            
            rows.forEach(row => {
                if (status === 'all' || row.dataset.status === status) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
            
            // Update button states
            document.querySelectorAll('.btn-group button').forEach(btn => {
                btn.classList.remove('active');
            });
            event.target.classList.add('active');
        }
        
        // View application details
        function viewApplicationDetails(applicationId) {
            // TODO: Implement application details modal or page
            alert('Application details for ID: ' + applicationId + '\n\nThis feature will show detailed application information including cover letter, resume, and status updates.');
        }
    </script>
</body>
</html>
