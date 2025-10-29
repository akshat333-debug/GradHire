<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard - GradHire</title>
    
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
                            <i class="fas fa-user-circle" style="font-size: 80px; line-height: 1;"></i>
                        </div>
                        <h5 class="fw-bold">${student.firstName} ${student.lastName}</h5>
                        <p class="small mb-0">${student.email}</p>
                    </div>
                    
                    <hr class="border-light opacity-25">
                    
                    <ul class="nav flex-column px-2">
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/student/dashboard">
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
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/applications">
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
                
                <!-- Welcome Section -->
                <div class="mt-4 mb-4">
                    <h2 class="fw-bold">Welcome back, ${student.firstName}! 👋</h2>
                    <p class="text-muted">Here's what's happening with your job search today</p>
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
                            <i class="fas fa-file-alt fa-2x mb-2"></i>
                            <h3>${applicationStats.totalApplications}</h3>
                            <p>Total Applications</p>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="stat-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <i class="fas fa-clock fa-2x mb-2"></i>
                            <h3>${applicationStats.pendingApplications}</h3>
                            <p>Pending Review</p>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="stat-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <i class="fas fa-eye fa-2x mb-2"></i>
                            <h3>${applicationStats.reviewedApplications}</h3>
                            <p>Under Review</p>
                        </div>
                    </div>
                    <div class="col-md-3 mb-3">
                        <div class="stat-card" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <i class="fas fa-check-circle fa-2x mb-2"></i>
                            <h3>${applicationStats.acceptedApplications}</h3>
                            <p>Accepted</p>
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
                                        <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-primary w-100">
                                            <i class="fas fa-search"></i> Browse Jobs
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-outline-success w-100">
                                            <i class="fas fa-user-edit"></i> Update Profile
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/student/recommendations" class="btn btn-outline-info w-100">
                                            <i class="fas fa-star"></i> View Recommendations
                                        </a>
                                    </div>
                                    <div class="col-md-3 mb-2">
                                        <a href="${pageContext.request.contextPath}/student/applications" class="btn btn-outline-secondary w-100">
                                            <i class="fas fa-file-alt"></i> My Applications
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row">
                    <!-- Recent Applications -->
                    <div class="col-lg-7 mb-4">
                        <div class="dashboard-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5 class="card-title fw-bold mb-0">
                                        <i class="fas fa-history text-primary"></i> Recent Applications
                                    </h5>
                                    <a href="${pageContext.request.contextPath}/student/applications" class="btn btn-sm btn-outline-primary">
                                        View All
                                    </a>
                                </div>
                                
                                <c:choose>
                                    <c:when test="${empty recentApplications}">
                                        <div class="text-center py-5">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">You haven't applied to any jobs yet</p>
                                            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary">
                                                Browse Jobs
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="table-responsive">
                                            <table class="table table-hover">
                                                <thead>
                                                    <tr>
                                                        <th>Job Title</th>
                                                        <th>Company</th>
                                                        <th>Applied</th>
                                                        <th>Status</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${recentApplications}" var="app">
                                                        <tr>
                                                            <td>
                                                                <strong>${app.job.title}</strong>
                                                            </td>
                                                            <td>${app.job.company}</td>
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
                                                                <a href="${pageContext.request.contextPath}/student/application/${app.applicationId}" 
                                                                   class="btn btn-sm btn-outline-primary">
                                                                    View
                                                                </a>
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
                    
                    <!-- Recommended Jobs -->
                    <div class="col-lg-5 mb-4">
                        <div class="dashboard-card">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-3">
                                    <h5 class="card-title fw-bold mb-0">
                                        <i class="fas fa-star text-warning"></i> Smart Recommendations
                                    </h5>
                                    <a href="${pageContext.request.contextPath}/student/recommendations" class="btn btn-sm btn-outline-primary">
                                        View All
                                    </a>
                                </div>
                                <p class="text-muted small mb-3">
                                    <i class="fas fa-info-circle"></i> Based on your skills and profile
                                </p>
                                
                                <c:choose>
                                    <c:when test="${empty recommendedJobs}">
                                        <div class="text-center py-4">
                                            <i class="fas fa-lightbulb fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">Update your profile and add skills to get personalized job recommendations</p>
                                            <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-sm btn-primary">
                                                Update Profile
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${recommendedJobs}" var="recommendation" varStatus="status">
                                            <c:if test="${status.index < 5}">
                                                <div class="mb-3 p-3 border rounded hover-shadow" style="position: relative;">
                                                    <!-- Match Score Badge -->
                                                    <div class="position-absolute top-0 end-0 m-2">
                                                        <span class="badge ${recommendation.scorePercentage >= 80 ? 'bg-success' : 
                                                                            recommendation.scorePercentage >= 60 ? 'bg-info' : 
                                                                            recommendation.scorePercentage >= 40 ? 'bg-primary' : 'bg-secondary'}" 
                                                              title="${recommendation.matchQuality}">
                                                            ${recommendation.scorePercentage}% Match
                                                        </span>
                                                    </div>
                                                    
                                                    <h6 class="fw-bold mb-1 pe-5">${recommendation.job.jobTitle}</h6>
                                                    <p class="text-muted small mb-2">
                                                        <i class="fas fa-building"></i> ${recommendation.job.companyName}
                                                        <c:if test="${recommendation.job.isRemote}">
                                                            <span class="badge bg-success ms-1">Remote</span>
                                                        </c:if>
                                                    </p>
                                                    
                                                    <!-- Matching Skills Display -->
                                                    <c:if test="${not empty recommendation.matchingSkills}">
                                                        <div class="mb-2">
                                                            <small class="text-muted">Matching Skills:</small>
                                                            <div class="mt-1">
                                                                <c:forEach items="${recommendation.matchingSkills}" var="skill" varStatus="skillStatus">
                                                                    <c:if test="${skillStatus.index < 3}">
                                                                        <span class="badge bg-light text-dark me-1">${skill}</span>
                                                                    </c:if>
                                                                </c:forEach>
                                                                <c:if test="${recommendation.matchingSkills.size() > 3}">
                                                                    <span class="badge bg-light text-muted">+${recommendation.matchingSkills.size() - 3} more</span>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    
                                                    <div class="d-flex justify-content-between align-items-center mt-2">
                                                        <span class="badge bg-primary">${recommendation.job.jobType}</span>
                                                        <a href="${pageContext.request.contextPath}/job/${recommendation.job.jobId}" 
                                                           class="btn btn-sm btn-outline-primary">
                                                            View Details
                                                        </a>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Profile Completion -->
                <c:if test="${profileCompletion < 100}">
                    <div class="row mb-4">
                        <div class="col-12">
                            <div class="dashboard-card border-warning">
                                <div class="card-body">
                                    <h5 class="card-title fw-bold mb-3">
                                        <i class="fas fa-tasks text-warning"></i> Complete Your Profile
                                    </h5>
                                    <p class="text-muted">A complete profile increases your chances of getting noticed by recruiters!</p>
                                    <div class="progress mb-3" style="height: 25px;">
                                        <div class="progress-bar bg-warning" role="progressbar" 
                                             style="width: ${profileCompletion}%;" 
                                             aria-valuenow="${profileCompletion}" aria-valuemin="0" aria-valuemax="100">
                                            ${profileCompletion}%
                                        </div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-warning">
                                        <i class="fas fa-arrow-right"></i> Complete Profile
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                
            </main>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>

