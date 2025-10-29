<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recruiter Dashboard - GradHire</title>
    
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
                            <i class="fas fa-user-tie fa-4x"></i>
                        </div>
                        <h5 class="fw-bold">${admin.name}</h5>
                        <p class="small mb-0">${admin.email}</p>
                    </div>
                    
                    <hr class="border-light opacity-25">
                    
                    <ul class="nav flex-column px-2">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/admin/dashboard">
                                <i class="fas fa-tachometer-alt"></i> Dashboard
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/jobs">
                                <i class="fas fa-briefcase"></i> Manage Jobs
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/post-job">
                                <i class="fas fa-plus-circle"></i> Post New Job
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/applications">
                                <i class="fas fa-file-alt"></i> Applications
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/candidates">
                                <i class="fas fa-users"></i> Candidates
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/analytics">
                                <i class="fas fa-chart-bar"></i> Analytics
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/settings">
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
                                <i class="fas fa-graduation-cap text-primary"></i> GradHire Admin
                            </h3>
                        </div>
                        <div class="d-flex align-items-center">
                            <div class="position-relative me-3">
                                <a href="${pageContext.request.contextPath}/admin/notifications" class="btn btn-outline-secondary position-relative">
                                    <i class="fas fa-bell"></i>
                                    <c:if test="${notificationCount > 0}">
                                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                            ${notificationCount}
                                        </span>
                                    </c:if>
                                </a>
                            </div>
                            <div class="dropdown">
                                <button class="btn btn-outline-secondary dropdown-toggle" type="button" 
                                        id="userMenu" data-bs-toggle="dropdown">
                                    <i class="fas fa-user-circle"></i>
                                </button>
                                <ul class="dropdown-menu dropdown-menu-end">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/profile">
                                        <i class="fas fa-user"></i> Profile
                                    </a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/settings">
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
                
                <!-- Welcome Section -->
                <div class="mt-4 mb-4">
                    <h2 class="fw-bold">Welcome back, ${admin.name}! 👋</h2>
                    <p class="text-muted">Here's an overview of your recruitment activities</p>
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
                
                <!-- Statistics Cards -->
                <div class="row mb-4">
                    <div class="col-md-3 mb-3">
                        <div class="stat-card bg-gradient-primary">
                            <i class="fas fa-briefcase fa-2x mb-2"></i>
                            <h3>${stats.totalJobs}</h3>
                            <p>Active Jobs</p>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="fas fa-file-alt fa-2x mb-2"></i>
                            <h3>${stats.totalApplications}</h3>
                            <p>Total Applications</p>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="fas fa-clock fa-2x mb-2"></i>
                            <h3>${stats.pendingReview}</h3>
                            <p>Pending Review</p>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="fas fa-users fa-2x mb-2"></i>
                            <h3>${stats.shortlisted}</h3>
                            <p>Shortlisted</p>
                        </div>
                    </div>
                </div>
                
                <!-- Quick Actions -->
                <div class="row mb-4">
                    <div class="col-12">
                        <div class="dashboard-card">
                            <div class="card-body">
                                <h5 class="card-title fw-bold mb-3">
                                    <i class="fas fa-bolt text-warning"></i> Quick Actions
                                </h5>
                                <div class="row">
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/admin/post-job" class="btn btn-primary w-100">
                                            <i class="fas fa-plus"></i> Post New Job
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/admin/applications" class="btn btn-outline-info w-100">
                                            <i class="fas fa-eye"></i> Review Applications
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/admin/candidates" class="btn btn-outline-success w-100">
                                            <i class="fas fa-search"></i> Search Candidates
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/admin/analytics" class="btn btn-outline-secondary w-100">
                                            <i class="fas fa-chart-bar"></i> View Analytics
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <!-- Recent Applications -->
                    <div class="col-lg-8 mb-4">
                        <div class="dashboard-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5 class="card-title fw-bold mb-0">
                                        <i class="fas fa-inbox text-primary"></i> Recent Applications
                                    </h5>
                                    <a href="${pageContext.request.contextPath}/admin/applications" class="btn btn-sm btn-outline-primary">
                                        View All
                                    </a>
                                </div>
                                
                                <c:choose>
                                    <c:when test="${empty recentApplications}">
                                        <div class="text-center py-5">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">No recent applications</p>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="table-responsive">
                                            <table class="table table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Candidate</th>
                                                        <th>Job Position</th>
                                                        <th>Applied</th>
                                                        <th>Status</th>
                                                        <th>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${recentApplications}" var="app">
                                                        <tr>
                                                            <td>
                                                                <div class="d-flex align-items-center">
                                                                    <img src="${pageContext.request.contextPath}/uploads/profiles/${app.student.profilePicture != null ? app.student.profilePicture : 'default-avatar.png'}" 
                                                                         class="rounded-circle me-2" width="40" height="40"
                                                                         onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'">
                                                                    <div>
                                                                        <strong>${app.student.firstName} ${app.student.lastName}</strong>
                                                                        <br><small class="text-muted">${app.student.university}</small>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <strong>${app.job.title}</strong>
                                                            </td>
                                                            <td>
                                                                <small class="text-muted">
                                                                    <fmt:formatDate value="${app.appliedAt}" pattern="MMM dd, yyyy" />
                                                                </small>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${app.status == 'PENDING'}">
                                                                        <span class="badge bg-warning text-dark">Pending</span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'REVIEWED'}">
                                                                        <span class="badge bg-info">Reviewed</span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'SHORTLISTED'}">
                                                                        <span class="badge bg-primary">Shortlisted</span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'ACCEPTED'}">
                                                                        <span class="badge bg-success">Accepted</span>
                                                                    </c:when>
                                                                    <c:when test="${app.status == 'REJECTED'}">
                                                                        <span class="badge bg-danger">Rejected</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-secondary">${app.status}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <div class="btn-group btn-group-sm">
                                                                    <a href="${pageContext.request.contextPath}/admin/application/${app.applicationId}" 
                                                                       class="btn btn-outline-primary">
                                                                        <i class="fas fa-eye"></i>
                                                                    </a>
                                                                    <c:if test="${app.status == 'PENDING'}">
                                                                        <button class="btn btn-outline-success" 
                                                                                onclick="updateStatus(${app.applicationId}, 'REVIEWED')">
                                                                            <i class="fas fa-check"></i>
                                                                        </button>
                                                                    </c:if>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Active Jobs Summary -->
                    <div class="col-lg-4 mb-4">
                        <div class="dashboard-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5 class="card-title fw-bold mb-0">
                                        <i class="fas fa-briefcase text-success"></i> Active Jobs
                                    </h5>
                                    <a href="${pageContext.request.contextPath}/admin/jobs" class="btn btn-sm btn-outline-primary">
                                        View All
                                    </a>
                                </div>
                                
                                <c:choose>
                                    <c:when test="${empty activeJobs}">
                                        <div class="text-center py-4">
                                            <i class="fas fa-briefcase fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">No active jobs</p>
                                            <a href="${pageContext.request.contextPath}/admin/post-job" class="btn btn-sm btn-primary">
                                                Post a Job
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${activeJobs}" var="job" varStatus="status">
                                            <c:if test="${status.index < 5}">
                                                <div class="mb-3 p-3 border rounded hover-shadow">
                                                    <h6 class="fw-bold mb-1">${job.title}</h6>
                                                    <p class="text-muted small mb-2">
                                                        <i class="fas fa-map-marker-alt"></i> ${job.location}
                                                    </p>
                                                    <div class="d-flex justify-content-between align-items-center">
                                                        <span class="badge bg-info">${job.applicationCount} applications</span>
                                                        <a href="${pageContext.request.contextPath}/admin/job/${job.jobId}" 
                                                           class="btn btn-sm btn-outline-primary">
                                                            Manage
                                                        </a>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <!-- Application Status Chart -->
                        <div class="dashboard-card mt-3">
                            <div class="card-body">
                                <h5 class="card-title fw-bold mb-3">
                                    <i class="fas fa-chart-pie text-info"></i> Application Status
                                </h5>
                                <div class="mb-2">
                                    <div class="d-flex justify-content-between mb-1">
                                        <span>Pending</span>
                                        <strong>${stats.pendingReview}</strong>
                                    </div>
                                    <div class="progress mb-3" style="height: 10px;">
                                        <div class="progress-bar bg-warning" style="width: ${stats.pendingPercentage}%"></div>
                                    </div>
                                </div>
                                <div class="mb-2">
                                    <div class="d-flex justify-content-between mb-1">
                                        <span>Shortlisted</span>
                                        <strong>${stats.shortlisted}</strong>
                                    </div>
                                    <div class="progress mb-3" style="height: 10px;">
                                        <div class="progress-bar bg-primary" style="width: ${stats.shortlistedPercentage}%"></div>
                                    </div>
                                </div>
                                <div class="mb-2">
                                    <div class="d-flex justify-content-between mb-1">
                                        <span>Accepted</span>
                                        <strong>${stats.accepted}</strong>
                                    </div>
                                    <div class="progress mb-3" style="height: 10px;">
                                        <div class="progress-bar bg-success" style="width: ${stats.acceptedPercentage}%"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
            </main>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
    <script>
        function updateStatus(applicationId, newStatus) {
            if (confirm('Are you sure you want to update this application status?')) {
                showLoading();
                
                fetch('${pageContext.request.contextPath}/admin/application/update-status', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'applicationId=' + applicationId + '&status=' + newStatus
                })
                .then(response => response.json())
                .then(data => {
                    hideLoading();
                    if (data.success) {
                        showAlert('Application status updated successfully', 'success');
                        setTimeout(() => location.reload(), 1500);
                    } else {
                        showAlert('Error updating status: ' + data.message, 'danger');
                    }
                })
                .catch(error => {
                    hideLoading();
                    showAlert('An error occurred', 'danger');
                    console.error('Error:', error);
                });
            }
        }
    </script>
</body>
</html>



