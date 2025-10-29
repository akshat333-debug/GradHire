<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Job Recommendations - GradHire</title>
    
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
                            <a class="nav-link" href="${pageContext.request.contextPath}/student/applications">
                                <i class="fas fa-file-alt"></i> My Applications
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link active" href="${pageContext.request.contextPath}/student/recommendations">
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
                                <i class="fas fa-star text-warning"></i> Smart Job Recommendations
                            </h2>
                            <p class="text-muted">Personalized job suggestions based on your skills and profile</p>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-primary">
                                <i class="fas fa-search"></i> Browse All Jobs
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
                
                <!-- Recommendations Content -->
                <c:choose>
                    <c:when test="${empty recommendedJobs}">
                        <div class="row">
                            <div class="col-12">
                                <div class="dashboard-card">
                                    <div class="card-body text-center py-5">
                                        <i class="fas fa-lightbulb fa-4x text-muted mb-4"></i>
                                        <h4 class="text-muted mb-3">No Recommendations Available</h4>
                                        <p class="text-muted mb-4">
                                            We need more information about your skills and preferences to provide personalized recommendations.
                                        </p>
                                        <div class="d-flex justify-content-center gap-3">
                                            <a href="${pageContext.request.contextPath}/student/profile" class="btn btn-primary">
                                                <i class="fas fa-user-edit"></i> Update Profile
                                            </a>
                                            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-primary">
                                                <i class="fas fa-search"></i> Browse Jobs
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Recommendation Stats -->
                        <div class="row mb-4">
                            <div class="col-md-4">
                                <div class="dashboard-card bg-gradient-primary text-white">
                                    <div class="card-body text-center">
                                        <i class="fas fa-star fa-2x mb-2"></i>
                                        <h3>${recommendedJobs.size()}</h3>
                                        <p class="mb-0">Recommended Jobs</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="dashboard-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); color: white;">
                                    <div class="card-body text-center">
                                        <i class="fas fa-chart-line fa-2x mb-2"></i>
                                        <h3>
                                            <c:set var="avgScore" value="0" />
                                            <c:forEach items="${recommendedJobs}" var="rec">
                                                <c:set var="avgScore" value="${avgScore + rec.scorePercentage}" />
                                            </c:forEach>
                                            <fmt:formatNumber value="${avgScore / recommendedJobs.size()}" maxFractionDigits="0" />%
                                        </h3>
                                        <p class="mb-0">Average Match</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="dashboard-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); color: white;">
                                    <div class="card-body text-center">
                                        <i class="fas fa-tags fa-2x mb-2"></i>
                                        <h3>
                                            <c:set var="totalSkills" value="0" />
                                            <c:forEach items="${recommendedJobs}" var="rec">
                                                <c:set var="totalSkills" value="${totalSkills + rec.matchingSkills.size()}" />
                                            </c:forEach>
                                            ${totalSkills}
                                        </h3>
                                        <p class="mb-0">Skill Matches</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Job Recommendations Grid -->
                        <div class="row">
                            <c:forEach items="${recommendedJobs}" var="recommendation" varStatus="status">
                                <div class="col-lg-6 col-xl-4 mb-4">
                                    <div class="dashboard-card h-100">
                                        <div class="card-body">
                                            <!-- Match Score Badge -->
                                            <div class="d-flex justify-content-between align-items-start mb-3">
                                                <div>
                                                    <h5 class="card-title fw-bold mb-1">${recommendation.job.jobTitle}</h5>
                                                    <p class="text-muted mb-2">
                                                        <i class="fas fa-building"></i> ${recommendation.job.companyName}
                                                        <c:if test="${recommendation.job.isRemote}">
                                                            <span class="badge bg-success ms-2">Remote</span>
                                                        </c:if>
                                                    </p>
                                                </div>
                                                <span class="badge ${recommendation.scorePercentage >= 80 ? 'bg-success' : 
                                                                    recommendation.scorePercentage >= 60 ? 'bg-info' : 
                                                                    recommendation.scorePercentage >= 40 ? 'bg-primary' : 'bg-secondary'}" 
                                                      title="${recommendation.matchQuality}">
                                                    ${recommendation.scorePercentage}% Match
                                                </span>
                                            </div>
                                            
                                            <!-- Job Description Preview -->
                                            <p class="card-text text-muted small mb-3">
                                                ${fn:substring(recommendation.job.description, 0, 120)}...
                                            </p>
                                            
                                            <!-- Matching Skills -->
                                            <c:if test="${not empty recommendation.matchingSkills}">
                                                <div class="mb-3">
                                                    <small class="text-muted fw-bold">Matching Skills:</small>
                                                    <div class="mt-1">
                                                        <c:forEach items="${recommendation.matchingSkills}" var="skill" varStatus="skillStatus">
                                                            <c:if test="${skillStatus.index < 4}">
                                                                <span class="badge bg-light text-dark me-1 mb-1">${skill}</span>
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:if test="${recommendation.matchingSkills.size() > 4}">
                                                            <span class="badge bg-light text-muted">+${recommendation.matchingSkills.size() - 4} more</span>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </c:if>
                                            
                                            <!-- Job Details -->
                                            <div class="row mb-3">
                                                <div class="col-6">
                                                    <small class="text-muted">
                                                        <i class="fas fa-briefcase"></i> ${recommendation.job.jobType}
                                                    </small>
                                                </div>
                                                <div class="col-6">
                                                    <small class="text-muted">
                                                        <i class="fas fa-map-marker-alt"></i> ${recommendation.job.location}
                                                    </small>
                                                </div>
                                            </div>
                                            
                                            <!-- Action Buttons -->
                                            <div class="d-flex gap-2">
                                                <a href="${pageContext.request.contextPath}/job/${recommendation.job.jobId}" 
                                                   class="btn btn-primary btn-sm flex-fill">
                                                    <i class="fas fa-eye"></i> View Details
                                                </a>
                                                <a href="${pageContext.request.contextPath}/apply?jobId=${recommendation.job.jobId}" 
                                                   class="btn btn-success btn-sm">
                                                    <i class="fas fa-paper-plane"></i> Apply
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        
                        <!-- Load More Button -->
                        <div class="row mt-4">
                            <div class="col-12 text-center">
                                <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-primary btn-lg">
                                    <i class="fas fa-search"></i> Browse More Jobs
                                </a>
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
</body>
</html>
