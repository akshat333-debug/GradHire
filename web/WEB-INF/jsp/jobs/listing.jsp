<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Browse Jobs - GradHire</title>
    
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
    
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
        <div class="container">
            <a class="navbar-brand fw-bold text-primary" href="${pageContext.request.contextPath}/">
                <i class="fas fa-graduation-cap"></i> GradHire
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/jobs">Jobs</a>
                    </li>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item">
                                <c:choose>
                                    <c:when test="${sessionScope.userType == 'student'}">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/student/dashboard">
                                            Dashboard
                                        </a>
                                    </c:when>
                                    <c:when test="${sessionScope.userType == 'employer' or sessionScope.userType == 'admin'}">
                                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard">
                                            Dashboard
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="nav-link" href="${pageContext.request.contextPath}/">
                                            Dashboard
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link btn btn-outline-danger ms-2" href="${pageContext.request.contextPath}/logout">
                                    <i class="fas fa-sign-out-alt"></i> Logout
                                </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link btn btn-outline-primary ms-2" href="${pageContext.request.contextPath}/login">
                                    <i class="fas fa-sign-in-alt"></i> Login
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link btn btn-primary text-white ms-2" href="${pageContext.request.contextPath}/register">
                                    <i class="fas fa-user-plus"></i> Sign Up
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Page Header -->
    <div class="bg-gradient-primary text-white py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-8">
                    <h1 class="display-4 fw-bold mb-3">Find Your Dream Job</h1>
                    <p class="lead mb-0">Discover thousands of internships and entry-level positions</p>
                </div>
                <div class="col-lg-4 text-lg-end">
                    <div class="text-white">
                        <h3 class="fw-bold mb-0">${totalJobs}</h3>
                        <p class="mb-0">Jobs Available</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Main Content -->
    <div class="container my-5">
        <div class="row">
            <!-- Filters Sidebar -->
            <div class="col-lg-3 mb-4">
                <div class="card shadow-sm sticky-top" style="top: 100px;">
                    <div class="card-body">
                        <h5 class="card-title fw-bold mb-4">
                            <i class="fas fa-filter text-primary"></i> Filters
                        </h5>
                        
                        <form id="filterForm" method="GET" action="${pageContext.request.contextPath}/jobs">
                            <!-- Search Keyword -->
                            <div class="mb-3">
                                <label class="form-label fw-bold">Search</label>
                                <input type="text" class="form-control" name="keyword" 
                                       placeholder="Job title, keywords..." value="<c:out value="${param.keyword}" />">
                            </div>
                            
                            <!-- Location -->
                            <div class="mb-3">
                                <label class="form-label fw-bold">Location</label>
                                <select class="form-select" name="location">
                                    <option value="">All Locations</option>
                                    <option value="Remote" ${param.location == 'Remote' ? 'selected' : ''}>Remote</option>
                                    <option value="New York, NY" ${param.location == 'New York, NY' ? 'selected' : ''}>New York, NY</option>
                                    <option value="San Francisco, CA" ${param.location == 'San Francisco, CA' ? 'selected' : ''}>San Francisco, CA</option>
                                    <option value="Austin, TX" ${param.location == 'Austin, TX' ? 'selected' : ''}>Austin, TX</option>
                                    <option value="Seattle, WA" ${param.location == 'Seattle, WA' ? 'selected' : ''}>Seattle, WA</option>
                                    <option value="Boston, MA" ${param.location == 'Boston, MA' ? 'selected' : ''}>Boston, MA</option>
                                </select>
                            </div>
                            
                            <!-- Job Type -->
                            <div class="mb-3">
                                <label class="form-label fw-bold">Job Type</label>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="jobType" value="Internship" 
                                           id="internship" ${param.jobType == 'Internship' ? 'checked' : ''}>
                                    <label class="form-check-label" for="internship">Internship</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="jobType" value="Full-time" 
                                           id="fulltime" ${param.jobType == 'Full-time' ? 'checked' : ''}>
                                    <label class="form-check-label" for="fulltime">Full-time</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="jobType" value="Part-time" 
                                           id="parttime" ${param.jobType == 'Part-time' ? 'checked' : ''}>
                                    <label class="form-check-label" for="parttime">Part-time</label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="checkbox" name="jobType" value="Contract" 
                                           id="contract" ${param.jobType == 'Contract' ? 'checked' : ''}>
                                    <label class="form-check-label" for="contract">Contract</label>
                                </div>
                            </div>
                            
                            <!-- Experience Level -->
                            <div class="mb-3">
                                <label class="form-label fw-bold">Experience Level</label>
                                <select class="form-select" name="experienceLevel">
                                    <option value="">All Levels</option>
                                    <option value="Entry Level" ${param.experienceLevel == 'Entry Level' ? 'selected' : ''}>Entry Level</option>
                                    <option value="Internship" ${param.experienceLevel == 'Internship' ? 'selected' : ''}>Internship</option>
                                    <option value="Mid Level" ${param.experienceLevel == 'Mid Level' ? 'selected' : ''}>Mid Level</option>
                                </select>
                            </div>
                            
                            <!-- Salary Range -->
                            <div class="mb-3">
                                <label class="form-label fw-bold">Min Salary</label>
                                <select class="form-select" name="minSalary">
                                    <option value="">Any</option>
                                    <option value="30000" ${param.minSalary == '30000' ? 'selected' : ''}>$30,000+</option>
                                    <option value="50000" ${param.minSalary == '50000' ? 'selected' : ''}>$50,000+</option>
                                    <option value="70000" ${param.minSalary == '70000' ? 'selected' : ''}>$70,000+</option>
                                    <option value="90000" ${param.minSalary == '90000' ? 'selected' : ''}>$90,000+</option>
                                    <option value="100000" ${param.minSalary == '100000' ? 'selected' : ''}>$100,000+</option>
                                </select>
                            </div>
                            
                            <!-- Buttons -->
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search"></i> Apply Filters
                                </button>
                                <a href="${pageContext.request.contextPath}/jobs" class="btn btn-outline-secondary">
                                    <i class="fas fa-redo"></i> Reset
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            
            <!-- Job Listings -->
            <div class="col-lg-9">
                <!-- Sort Options -->
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div>
                        <h4 class="mb-0">${totalJobs} Jobs Found</h4>
                        <c:if test="${not empty param.keyword}">
                            <small class="text-muted">Showing results for "<c:out value="${param.keyword}" />"</small>
                        </c:if>
                    </div>
                    <div>
                        <select class="form-select" onchange="window.location.href='?sort=' + this.value">
                            <option value="recent" ${param.sort == 'recent' ? 'selected' : ''}>Most Recent</option>
                            <option value="relevant" ${param.sort == 'relevant' ? 'selected' : ''}>Most Relevant</option>
                            <option value="salary" ${param.sort == 'salary' ? 'selected' : ''}>Highest Salary</option>
                        </select>
                    </div>
                </div>
                
                <!-- Success Message -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle"></i> <c:out value="${success}" />
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <!-- Job Cards -->
                <c:choose>
                    <c:when test="${jobs == null or fn:length(jobs) == 0}">
                        <div class="card text-center py-5">
                            <div class="card-body">
                                <i class="fas fa-search fa-4x text-muted mb-3"></i>
                                <h4>No Jobs Found</h4>
                                <p class="text-muted">Try adjusting your filters or search criteria</p>
                                <a href="${pageContext.request.contextPath}/jobs" class="btn btn-primary">
                                    View All Jobs
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${jobs}" var="job">
                            <div class="job-card mb-3">
                                <div class="job-header">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <div class="flex-grow-1">
                                            <h4 class="job-title">
                                                <a href="${pageContext.request.contextPath}/job/${job.jobId}" 
                                                   class="text-decoration-none text-dark">
                                                    <c:out value="${job.title}" />
                                                </a>
                                            </h4>
                                            <p class="company-name mb-2">
                                                <i class="fas fa-building"></i> <c:out value="${job.company}" />
                                            </p>
                                        </div>
                                        <div>
                                            <span class="badge badge-job-type bg-primary"><c:out value="${job.jobType}" /></span>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="job-body">
                                    <div class="job-meta mb-3">
                                        <div class="job-meta-item">
                                            <i class="fas fa-map-marker-alt"></i> <c:out value="${job.location}" />
                                        </div>
                                        <div class="job-meta-item">
                                            <i class="fas fa-dollar-sign"></i> 
                                            <c:choose>
                                                <c:when test="${not empty job.salaryMin and not empty job.salaryMax}">
                                                    $<fmt:formatNumber value="${job.salaryMin}" pattern="#,###"/> - $<fmt:formatNumber value="${job.salaryMax}" pattern="#,###"/>
                                                </c:when>
                                                <c:otherwise>Competitive</c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="job-meta-item">
                                            <i class="fas fa-clock"></i> 
                                            Posted <fmt:formatDate value="${job.createdAt}" pattern="MMM dd, yyyy" />
                                        </div>
                                    </div>
                                    
                                    <p class="text-muted mb-3">
                                        <c:choose>
                                            <c:when test="${job.description != null && job.description.length() > 200}">
                                                <c:out value="${job.description.substring(0, 200)}" escapeXml="true" />...
                                            </c:when>
                                            <c:when test="${job.description != null}">
                                                <c:out value="${job.description}" escapeXml="true" />
                                            </c:when>
                                            <c:otherwise>
                                                No description available.
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    
                                    <c:if test="${not empty job.requiredSkills}">
                                        <div class="mb-3">
                                            <c:forEach items="${job.requiredSkills}" var="skill" varStatus="status">
                                                <c:if test="${status.index < 5}">
                                                    <span class="skill-tag"><c:out value="${skill.skillName}" /></span>
                                                </c:if>
                                            </c:forEach>
                                            <c:if test="${job.requiredSkills.size() > 5}">
                                                <span class="skill-tag">+${job.requiredSkills.size() - 5} more</span>
                                            </c:if>
                                        </div>
                                    </c:if>
                                    
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="text-muted small">
                                            <i class="fas fa-users"></i> ${job.applicationCount} applicants
                                        </div>
                                        <div>
                                            <c:choose>
                                                <c:when test="${sessionScope.userType == 'student'}">
                                                    <c:choose>
                                                        <c:when test="${job.hasApplied}">
                                                            <button class="btn btn-outline-success" disabled>
                                                                <i class="fas fa-check"></i> Applied
                                                            </button>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="${pageContext.request.contextPath}/job/${job.jobId}" 
                                                               class="btn btn-primary">
                                                                <i class="fas fa-paper-plane"></i> Apply Now
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="${pageContext.request.contextPath}/job/${job.jobId}" 
                                                       class="btn btn-outline-primary">
                                                        <i class="fas fa-eye"></i> View Details
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                            <button class="btn btn-outline-secondary ms-2" 
                                                    data-job-id="${job.jobId}"
                                                    onclick="saveJob(this)">
                                                <i class="far fa-bookmark"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        
                        <!-- Pagination -->
                        <c:if test="${totalPages > 1}">
                            <nav aria-label="Job listings pagination">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage - 1}&${queryString}">Previous</a>
                                    </li>
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&${queryString}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                        <a class="page-link" href="?page=${currentPage + 1}&${queryString}">Next</a>
                                    </li>
                                </ul>
                            </nav>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
    <script>
        function saveJob(button) {
            // Extract jobId from data attribute
            const jobId = button.dataset.jobId;
            
            showLoading();
            
            fetch('${pageContext.request.contextPath}/student/save-job', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'jobId=' + encodeURIComponent(jobId)
            })
            .then(response => response.json())
            .then(data => {
                hideLoading();
                if (data.success) {
                    showAlert('Job saved successfully!', 'success');
                } else {
                    showAlert(data.message || 'Error saving job', 'danger');
                }
            })
            .catch(error => {
                hideLoading();
                showAlert('Please login to save jobs', 'warning');
            });
        }
    </script>
</body>
</html>



