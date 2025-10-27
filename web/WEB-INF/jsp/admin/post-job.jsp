<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Post New Job - GradHire</title>
    
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
                <i class="fas fa-graduation-cap"></i> GradHire Admin
            </a>
            <div class="ms-auto">
                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-primary me-2">
                    <i class="fas fa-arrow-left"></i> Back to Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>
        </div>
    </nav>
    
    <!-- Main Content -->
    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h3 class="mb-0">
                            <i class="fas fa-plus-circle"></i> Post a New Job
                        </h3>
                    </div>
                    <div class="card-body p-4">
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
                        
                        <form action="${pageContext.request.contextPath}/admin/jobs/post" method="POST" id="jobForm">
                            <h5 class="fw-bold mb-3">Basic Information</h5>
                            
                            <div class="row">
                                <div class="col-md-8 mb-3">
                                    <label for="title" class="form-label fw-bold">
                                        Job Title <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="title" name="title" 
                                           placeholder="e.g., Software Engineering Intern" required>
                                </div>
                                
                                <div class="col-md-4 mb-3">
                                    <label for="jobType" class="form-label fw-bold">
                                        Job Type <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" id="jobType" name="jobType" required>
                                        <option value="">Select Type</option>
                                        <option value="Internship">Internship</option>
                                        <option value="Full-time">Full-time</option>
                                        <option value="Part-time">Part-time</option>
                                        <option value="Contract">Contract</option>
                                    </select>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="company" class="form-label fw-bold">
                                        Company Name <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="company" name="company" 
                                           value="${admin.company}" required>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="location" class="form-label fw-bold">
                                        Location <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="location" name="location" 
                                           placeholder="e.g., San Francisco, CA or Remote" required>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label fw-bold">
                                    Job Description <span class="text-danger">*</span>
                                </label>
                                <textarea class="form-control" id="description" name="description" rows="6" 
                                          placeholder="Describe the role, responsibilities, and what the candidate will be doing..." 
                                          required></textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label for="requirements" class="form-label fw-bold">
                                    Requirements
                                </label>
                                <textarea class="form-control" id="requirements" name="requirements" rows="6" 
                                          placeholder="List the qualifications, education, and experience required..."></textarea>
                            </div>
                            
                            <hr class="my-4">
                            <h5 class="fw-bold mb-3">Compensation & Benefits</h5>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="salaryMin" class="form-label fw-bold">
                                        Minimum Salary
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text">$</span>
                                        <input type="number" class="form-control" id="salaryMin" name="salaryMin" 
                                               placeholder="50000" min="0">
                                    </div>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="salaryMax" class="form-label fw-bold">
                                        Maximum Salary
                                    </label>
                                    <div class="input-group">
                                        <span class="input-group-text">$</span>
                                        <input type="number" class="form-control" id="salaryMax" name="salaryMax" 
                                               placeholder="70000" min="0">
                                    </div>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="benefits" class="form-label fw-bold">
                                    Benefits
                                </label>
                                <textarea class="form-control" id="benefits" name="benefits" rows="4" 
                                          placeholder="List benefits such as health insurance, 401k, remote work options, etc."></textarea>
                            </div>
                            
                            <hr class="my-4">
                            <h5 class="fw-bold mb-3">Skills & Qualifications</h5>
                            
                            <div class="mb-3">
                                <label for="skills" class="form-label fw-bold">
                                    Required Skills
                                </label>
                                <input type="text" class="form-control" id="skillInput" 
                                       placeholder="Type a skill and press Enter or comma">
                                <div id="skillsContainer" class="mt-2"></div>
                                <input type="hidden" id="skills" name="skills">
                                <small class="text-muted">Press Enter or type comma after each skill</small>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="experienceLevel" class="form-label fw-bold">
                                        Experience Level
                                    </label>
                                    <select class="form-select" id="experienceLevel" name="experienceLevel">
                                        <option value="">Select Level</option>
                                        <option value="Internship">Internship</option>
                                        <option value="Entry Level">Entry Level</option>
                                        <option value="Mid Level">Mid Level</option>
                                        <option value="Senior Level">Senior Level</option>
                                    </select>
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="educationLevel" class="form-label fw-bold">
                                        Education Level
                                    </label>
                                    <select class="form-select" id="educationLevel" name="educationLevel">
                                        <option value="">Select Level</option>
                                        <option value="High School">High School</option>
                                        <option value="Associate Degree">Associate Degree</option>
                                        <option value="Bachelor's Degree">Bachelor's Degree</option>
                                        <option value="Master's Degree">Master's Degree</option>
                                        <option value="PhD">PhD</option>
                                    </select>
                                </div>
                            </div>
                            
                            <hr class="my-4">
                            <h5 class="fw-bold mb-3">Application Details</h5>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="applicationDeadline" class="form-label fw-bold">
                                        Application Deadline
                                    </label>
                                    <input type="date" class="form-control" id="applicationDeadline" name="applicationDeadline">
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="positions" class="form-label fw-bold">
                                        Number of Positions
                                    </label>
                                    <input type="number" class="form-control" id="positions" name="positions" 
                                           value="1" min="1">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="contactEmail" class="form-label fw-bold">
                                        Contact Email
                                    </label>
                                    <input type="email" class="form-control" id="contactEmail" name="contactEmail" 
                                           value="${admin.email}">
                                </div>
                                
                                <div class="col-md-6 mb-3">
                                    <label for="contactPhone" class="form-label fw-bold">
                                        Contact Phone
                                    </label>
                                    <input type="tel" class="form-control" id="contactPhone" name="contactPhone">
                                </div>
                            </div>
                            
                            <hr class="my-4">
                            
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="checkbox" id="isActive" name="isActive" checked>
                                <label class="form-check-label fw-bold" for="isActive">
                                    Publish job immediately
                                </label>
                            </div>
                            
                            <div class="d-flex gap-3">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-paper-plane"></i> Post Job
                                </button>
                                <button type="button" class="btn btn-outline-secondary btn-lg" onclick="saveDraft()">
                                    <i class="fas fa-save"></i> Save as Draft
                                </button>
                                <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-danger btn-lg">
                                    <i class="fas fa-times"></i> Cancel
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
    <script>
        // Skills management
        let skills = [];
        const skillInput = document.getElementById('skillInput');
        const skillsContainer = document.getElementById('skillsContainer');
        const skillsHidden = document.getElementById('skills');
        
        skillInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ',') {
                e.preventDefault();
                addSkill(this.value.trim());
                this.value = '';
            }
        });
        
        function addSkill(skill) {
            if (skill && !skills.includes(skill)) {
                skills.push(skill);
                updateSkillsDisplay();
            }
        }
        
        function removeSkill(skill) {
            skills = skills.filter(s => s !== skill);
            updateSkillsDisplay();
        }
        
        function updateSkillsDisplay() {
            skillsContainer.innerHTML = '';
            skills.forEach(skill => {
                const tag = document.createElement('span');
                tag.className = 'skill-tag me-2 mb-2';
                tag.innerHTML = `
                    ${skill}
                    <i class="fas fa-times ms-2" style="cursor: pointer;" onclick="removeSkill('${skill}')"></i>
                `;
                skillsContainer.appendChild(tag);
            });
            skillsHidden.value = skills.join(',');
        }
        
        // Form validation
        document.getElementById('jobForm').addEventListener('submit', function(e) {
            const salaryMin = parseInt(document.getElementById('salaryMin').value) || 0;
            const salaryMax = parseInt(document.getElementById('salaryMax').value) || 0;
            
            if (salaryMin > 0 && salaryMax > 0 && salaryMin > salaryMax) {
                e.preventDefault();
                showAlert('Minimum salary cannot be greater than maximum salary', 'danger');
                return false;
            }
            
            showLoading();
        });
        
        function saveDraft() {
            const form = document.getElementById('jobForm');
            const formData = new FormData(form);
            formData.set('isActive', 'false');
            
            showLoading();
            
            fetch('${pageContext.request.contextPath}/admin/jobs/save-draft', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                hideLoading();
                if (data.success) {
                    showAlert('Job saved as draft successfully', 'success');
                    setTimeout(() => {
                        window.location.href = '${pageContext.request.contextPath}/admin/jobs';
                    }, 1500);
                } else {
                    showAlert('Error saving draft: ' + data.message, 'danger');
                }
            })
            .catch(error => {
                hideLoading();
                showAlert('An error occurred', 'danger');
                console.error('Error:', error);
            });
        }
    </script>
</body>
</html>



