<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${job.title} - GradHire</title>
    
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/jobs">Jobs</a>
                    </li>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/${sessionScope.userType}/dashboard">
                                    Dashboard
                                </a>
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
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <div class="container my-5">
        <div class="row">
            <!-- Job Details -->
            <div class="col-lg-8 mb-4">
                <!-- Job Header Card -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body p-4">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <div>
                                <h1 class="h2 fw-bold mb-2">${job.title}</h1>
                                <h5 class="text-primary mb-3">
                                    <i class="fas fa-building"></i> ${job.company}
                                </h5>
                            </div>
                            <span class="badge bg-primary fs-6 p-2">${job.jobType}</span>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6 mb-2">
                                <i class="fas fa-map-marker-alt text-primary me-2"></i>
                                <strong>Location:</strong> ${job.location}
                            </div>
                            <div class="col-md-6 mb-2">
                                <i class="fas fa-dollar-sign text-primary me-2"></i>
                                <strong>Salary:</strong> 
                                <c:choose>
                                    <c:when test="${not empty job.salaryMin and not empty job.salaryMax}">
                                        $<fmt:formatNumber value="${job.salaryMin}" pattern="#,###"/> - $<fmt:formatNumber value="${job.salaryMax}" pattern="#,###"/>
                                    </c:when>
                                    <c:otherwise>Competitive</c:otherwise>
                                </c:choose>
                            </div>
                            <div class="col-md-6 mb-2">
                                <i class="fas fa-calendar text-primary me-2"></i>
                                <strong>Posted:</strong> <fmt:formatDate value="${job.postedDate}" pattern="MMM dd, yyyy" />
                            </div>
                            <div class="col-md-6 mb-2">
                                <i class="fas fa-clock text-primary me-2"></i>
                                <strong>Deadline:</strong> 
                                <c:choose>
                                    <c:when test="${not empty job.applicationDeadline}">
                                        <fmt:formatDate value="${job.applicationDeadline}" pattern="MMM dd, yyyy" />
                                    </c:when>
                                    <c:otherwise>Rolling</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="d-flex gap-2">
                            <c:choose>
                                <c:when test="${sessionScope.userType == 'student'}">
                                    <c:choose>
                                        <c:when test="${hasApplied}">
                                            <button class="btn btn-success" disabled>
                                                <i class="fas fa-check-circle"></i> Already Applied
                                            </button>
                                            <a href="${pageContext.request.contextPath}/student/applications" class="btn btn-outline-primary">
                                                View Application
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-primary btn-lg" data-bs-toggle="modal" data-bs-target="#applyModal">
                                                <i class="fas fa-paper-plane"></i> Apply Now
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:when test="${empty sessionScope.user}">
                                    <a href="${pageContext.request.contextPath}/login?redirect=job/${job.jobId}" class="btn btn-primary btn-lg">
                                        <i class="fas fa-sign-in-alt"></i> Login to Apply
                                    </a>
                                </c:when>
                            </c:choose>
                            <button class="btn btn-outline-secondary btn-lg" onclick="saveJob(${job.jobId})">
                                <i class="far fa-bookmark"></i> Save
                            </button>
                            <button class="btn btn-outline-secondary btn-lg" onclick="shareJob()">
                                <i class="fas fa-share-alt"></i> Share
                            </button>
                        </div>
                    </div>
                </div>
                
                <!-- Job Description -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body p-4">
                        <h4 class="fw-bold mb-3">Job Description</h4>
                        <div class="job-description">
                            ${job.description}
                        </div>
                    </div>
                </div>
                
                <!-- Requirements -->
                <c:if test="${not empty job.requirements}">
                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h4 class="fw-bold mb-3">Requirements</h4>
                            <div class="requirements">
                                ${job.requirements}
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Required Skills -->
                <c:if test="${not empty job.requiredSkills}">
                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h4 class="fw-bold mb-3">Required Skills</h4>
                            <div>
                                <c:forEach items="${job.requiredSkills}" var="skill">
                                    <span class="skill-tag">${skill.skillName}</span>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Benefits -->
                <c:if test="${not empty job.benefits}">
                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h4 class="fw-bold mb-3">Benefits</h4>
                            <div class="benefits">
                                ${job.benefits}
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
            
            <!-- Sidebar -->
            <div class="col-lg-4">
                <!-- Company Info -->
                <div class="card shadow-sm mb-4 sticky-top" style="top: 100px;">
                    <div class="card-body p-4">
                        <h5 class="fw-bold mb-3">About ${job.company}</h5>
                        <div class="mb-3">
                            <p class="text-muted">
                                ${job.companyDescription != null ? job.companyDescription : 'Leading company in the industry'}
                            </p>
                        </div>
                        
                        <hr>
                        
                        <h6 class="fw-bold mb-3">Job Statistics</h6>
                        <div class="mb-2">
                            <i class="fas fa-users text-primary me-2"></i>
                            <strong>${job.applicationCount}</strong> applicants
                        </div>
                        <div class="mb-2">
                            <i class="fas fa-eye text-primary me-2"></i>
                            <strong>${job.viewCount}</strong> views
                        </div>
                        
                        <hr>
                        
                        <h6 class="fw-bold mb-3">Contact Information</h6>
                        <c:if test="${not empty job.contactEmail}">
                            <div class="mb-2">
                                <i class="fas fa-envelope text-primary me-2"></i>
                                <small>${job.contactEmail}</small>
                            </div>
                        </c:if>
                        <c:if test="${not empty job.contactPhone}">
                            <div class="mb-2">
                                <i class="fas fa-phone text-primary me-2"></i>
                                <small>${job.contactPhone}</small>
                            </div>
                        </c:if>
                    </div>
                </div>
                
                <!-- Similar Jobs -->
                <c:if test="${not empty similarJobs}">
                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h5 class="fw-bold mb-3">Similar Jobs</h5>
                            <c:forEach items="${similarJobs}" var="simJob" varStatus="status">
                                <c:if test="${status.index < 3}">
                                    <div class="mb-3 pb-3 ${status.last ? '' : 'border-bottom'}">
                                        <a href="${pageContext.request.contextPath}/job/${simJob.jobId}" 
                                           class="text-decoration-none text-dark">
                                            <h6 class="fw-bold mb-1">${simJob.title}</h6>
                                        </a>
                                        <small class="text-muted">
                                            <i class="fas fa-building"></i> ${simJob.company}
                                        </small>
                                        <br>
                                        <small class="text-muted">
                                            <i class="fas fa-map-marker-alt"></i> ${simJob.location}
                                        </small>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    
    <!-- Apply Modal -->
    <div class="modal fade" id="applyModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-paper-plane"></i> Apply for ${job.title}
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/apply" method="POST" id="applyForm">
                    <input type="hidden" name="jobId" value="${job.jobId}">
                    
                    <div class="modal-body">
                        <div class="alert alert-info">
                            <i class="fas fa-info-circle"></i> Your profile information will be automatically included with this application.
                        </div>
                        
                        <div class="mb-3">
                            <label for="coverLetter" class="form-label fw-bold">
                                Cover Letter <span class="text-danger">*</span>
                            </label>
                            <textarea class="form-control" id="coverLetter" name="coverLetter" rows="8" 
                                      placeholder="Tell us why you're a great fit for this position..." 
                                      required
                                      onkeyup="updateCharCount('coverLetter', 'charCount', 1000)"></textarea>
                            <div id="charCount" class="text-muted small mt-1">1000 characters remaining</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label fw-bold">Resume</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="resumeOption" id="useExisting" value="existing" checked>
                                <label class="form-check-label" for="useExisting">
                                    Use my existing resume
                                    <c:if test="${not empty student.resume}">
                                        <span class="badge bg-success ms-2">On file</span>
                                    </c:if>
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="resumeOption" id="uploadNew" value="new">
                                <label class="form-check-label" for="uploadNew">
                                    Upload a different resume
                                </label>
                            </div>
                        </div>
                        
                        <div class="mb-3 d-none" id="resumeUploadDiv">
                            <input class="form-control" type="file" id="resumeFile" name="resume" accept=".pdf,.doc,.docx">
                            <small class="text-muted">Accepted formats: PDF, DOC, DOCX (Max 5MB)</small>
                        </div>
                        
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="confirmInfo" required>
                            <label class="form-check-label" for="confirmInfo">
                                I confirm that the information in my application is accurate and complete.
                            </label>
                        </div>
                    </div>
                    
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-paper-plane"></i> Submit Application
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
    <script>
        // Toggle resume upload field
        document.querySelectorAll('input[name="resumeOption"]').forEach(radio => {
            radio.addEventListener('change', function() {
                const uploadDiv = document.getElementById('resumeUploadDiv');
                if (this.value === 'new') {
                    uploadDiv.classList.remove('d-none');
                    document.getElementById('resumeFile').required = true;
                } else {
                    uploadDiv.classList.add('d-none');
                    document.getElementById('resumeFile').required = false;
                }
            });
        });
        
        // Form submission
        document.getElementById('applyForm').addEventListener('submit', function(e) {
            showLoading();
        });
        
        function saveJob(jobId) {
            showLoading();
            
            fetch('${pageContext.request.contextPath}/student/save-job', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'jobId=' + jobId
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
        
        function shareJob() {
            const url = window.location.href;
            const title = '${job.title} at ${job.company}';
            
            if (navigator.share) {
                navigator.share({
                    title: title,
                    url: url
                }).then(() => {
                    showAlert('Job shared successfully!', 'success');
                }).catch(console.error);
            } else {
                // Fallback: copy to clipboard
                navigator.clipboard.writeText(url).then(() => {
                    showAlert('Job link copied to clipboard!', 'success');
                });
            }
        }
    </script>
</body>
</html>



