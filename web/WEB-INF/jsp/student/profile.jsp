<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile - GradHire</title>
    
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
            <div class="ms-auto">
                <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-outline-primary me-2">
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
        <div class="row">
            <!-- Sidebar -->
            <div class="col-lg-3 mb-4">
                <div class="card shadow-sm sticky-top" style="top: 100px;">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <img src="${pageContext.request.contextPath}/uploads/profiles/${student.profilePicture != null ? student.profilePicture : 'default-avatar.png'}" 
                                 alt="Profile" class="rounded-circle img-fluid mb-3" width="120" height="120"
                                 id="profilePreview"
                                 onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'">
                            <button class="btn btn-sm btn-outline-primary" onclick="document.getElementById('profilePictureInput').click()">
                                <i class="fas fa-image"></i> Change Photo
                            </button>
                            <input type="file" id="profilePictureInput" name="profilePicture" class="d-none" accept="image/*" 
                                   onchange="previewImage(this, 'profilePreview')">
                        </div>
                        <h5 class="fw-bold">${student.firstName} ${student.lastName}</h5>
                        <p class="text-muted small mb-3">${student.email}</p>
                        
                        <div class="progress mb-2" style="height: 20px;">
                            <div class="progress-bar bg-success" role="progressbar" 
                                 style="width: ${profileCompletion}%;" 
                                 aria-valuenow="${profileCompletion}" aria-valuemin="0" aria-valuemax="100">
                                ${profileCompletion}%
                            </div>
                        </div>
                        <small class="text-muted">Profile Completion</small>
                    </div>
                </div>
            </div>
            
            <!-- Main Form -->
            <div class="col-lg-9">
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
                
                <form action="${pageContext.request.contextPath}/student/profile" method="POST" enctype="multipart/form-data" id="profileForm">
                    <!-- Personal Information -->
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">
                                <i class="fas fa-user"></i> Personal Information
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="firstName" class="form-label fw-bold">First Name</label>
                                    <input type="text" class="form-control" id="firstName" name="firstName" 
                                           value="${student.firstName}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="lastName" class="form-label fw-bold">Last Name</label>
                                    <input type="text" class="form-control" id="lastName" name="lastName" 
                                           value="${student.lastName}" required>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label fw-bold">Email</label>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="${student.email}" required readonly>
                                    <small class="text-muted">Email cannot be changed</small>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label fw-bold">Phone Number</label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           value="${student.phone}">
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Education -->
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">
                                <i class="fas fa-graduation-cap"></i> Education
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="university" class="form-label fw-bold">University</label>
                                <input type="text" class="form-control" id="university" name="university" 
                                       value="${student.university}" required>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="major" class="form-label fw-bold">Major</label>
                                    <input type="text" class="form-control" id="major" name="major" 
                                           value="${student.major}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="minor" class="form-label fw-bold">Minor (Optional)</label>
                                    <input type="text" class="form-control" id="minor" name="minor" 
                                           value="${student.minor}">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="gpa" class="form-label fw-bold">GPA</label>
                                    <input type="number" class="form-control" id="gpa" name="gpa" 
                                           value="${student.gpa}" step="0.01" min="0" max="4.0">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="graduationYear" class="form-label fw-bold">Graduation Year</label>
                                    <select class="form-select" id="graduationYear" name="graduationYear" required>
                                        <c:forEach var="year" begin="2024" end="2030">
                                            <option value="${year}" ${student.graduationYear == year ? 'selected' : ''}>${year}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="degreeLevel" class="form-label fw-bold">Degree Level</label>
                                    <select class="form-select" id="degreeLevel" name="degreeLevel">
                                        <option value="Bachelor's" ${student.degreeLevel == "Bachelor's" ? 'selected' : ''}>Bachelor's</option>
                                        <option value="Master's" ${student.degreeLevel == "Master's" ? 'selected' : ''}>Master's</option>
                                        <option value="PhD" ${student.degreeLevel == 'PhD' ? 'selected' : ''}>PhD</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Skills -->
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">
                                <i class="fas fa-code"></i> Skills
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="skillInput" class="form-label fw-bold">Add Skills</label>
                                <input type="text" class="form-control" id="skillInput" 
                                       placeholder="Type a skill and press Enter">
                                <small class="text-muted">Press Enter after each skill</small>
                            </div>
                            <div id="skillsContainer" class="mb-3">
                                <c:forEach items="${student.skills}" var="skill">
                                    <span class="skill-tag">${skill.skillName} <i class="fas fa-times" onclick="removeSkill('${skill.skillName}')"></i></span>
                                </c:forEach>
                            </div>
                            <input type="hidden" id="skills" name="skills">
                        </div>
                    </div>
                    
                    <!-- Resume -->
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">
                                <i class="fas fa-file-alt"></i> Resume
                            </h5>
                        </div>
                        <div class="card-body">
                            <c:if test="${not empty student.resume}">
                                <div class="alert alert-info">
                                    <i class="fas fa-file-pdf"></i> Current resume: 
                                    <a href="${pageContext.request.contextPath}/uploads/resumes/${student.resume}" target="_blank">
                                        ${student.resume}
                                    </a>
                                </div>
                            </c:if>
                            
                            <div class="mb-3">
                                <label for="resume" class="form-label fw-bold">Upload New Resume</label>
                                <input class="form-control" type="file" id="resume" name="resume" 
                                       accept=".pdf,.doc,.docx">
                                <small class="text-muted">Accepted formats: PDF, DOC, DOCX (Max 5MB)</small>
                            </div>
                        </div>
                    </div>
                    
                    <!-- About Me -->
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0">
                                <i class="fas fa-info-circle"></i> About Me
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="bio" class="form-label fw-bold">Bio</label>
                                <textarea class="form-control" id="bio" name="bio" rows="5" 
                                          placeholder="Tell us about yourself, your interests, and career goals..."
                                          onkeyup="updateCharCount('bio', 'bioCount', 500)">${student.bio}</textarea>
                                <div id="bioCount" class="text-muted small mt-1">500 characters remaining</div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="linkedIn" class="form-label fw-bold">LinkedIn Profile</label>
                                    <input type="url" class="form-control" id="linkedIn" name="linkedIn" 
                                           value="${student.linkedIn}" placeholder="https://linkedin.com/in/yourprofile">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="github" class="form-label fw-bold">GitHub Profile</label>
                                    <input type="url" class="form-control" id="github" name="github" 
                                           value="${student.githubUrl}" placeholder="https://github.com/yourusername">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="portfolio" class="form-label fw-bold">Portfolio/Website</label>
                                <input type="url" class="form-control" id="portfolio" name="portfolio" 
                                       value="${student.portfolio}" placeholder="https://yourportfolio.com">
                            </div>
                        </div>
                    </div>
                    
                    <!-- Action Buttons -->
                    <div class="d-flex gap-3 mb-4">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="fas fa-save"></i> Save Changes
                        </button>
                        <button type="reset" class="btn btn-outline-secondary btn-lg">
                            <i class="fas fa-undo"></i> Reset
                        </button>
                        <a href="${pageContext.request.contextPath}/student/dashboard" class="btn btn-outline-danger btn-lg">
                            <i class="fas fa-times"></i> Cancel
                        </a>
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
        // Skills management
        let skills = [];
        const skillInput = document.getElementById('skillInput');
        const skillsContainer = document.getElementById('skillsContainer');
        const skillsHidden = document.getElementById('skills');
        
        // Load existing skills
        document.querySelectorAll('#skillsContainer .skill-tag').forEach(tag => {
            const skillName = tag.textContent.trim().replace(/×$/, '').trim();
            if (skillName) skills.push(skillName);
        });
        
        skillInput.addEventListener('keydown', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const skill = this.value.trim();
                if (skill && !skills.includes(skill)) {
                    skills.push(skill);
                    updateSkillsDisplay();
                    this.value = '';
                }
            }
        });
        
        function removeSkill(skill) {
            skills = skills.filter(s => s !== skill);
            updateSkillsDisplay();
        }
        
        function updateSkillsDisplay() {
            skillsContainer.innerHTML = '';
            skills.forEach(skill => {
                const tag = document.createElement('span');
                tag.className = 'skill-tag me-2 mb-2';
                tag.innerHTML = `${skill} <i class="fas fa-times" style="cursor: pointer;" onclick="removeSkill('${skill}')"></i>`;
                skillsContainer.appendChild(tag);
            });
            skillsHidden.value = skills.join(',');
        }
        
        // Initialize skills hidden field
        updateSkillsDisplay();
        
        // Form submission
        document.getElementById('profileForm').addEventListener('submit', function(e) {
            showLoading();
        });
    </script>
</body>
</html>



