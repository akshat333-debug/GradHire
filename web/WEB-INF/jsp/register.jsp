<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up - GradHire</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    
    <div class="auth-container">
        <div class="container py-5">
            <div class="row justify-content-center">
                <div class="col-md-11 col-lg-10">
                    <div class="auth-card">
                        <div class="row g-0">
                            <!-- Left Side - Branding -->
                            <div class="col-md-5 d-none d-md-block auth-header d-flex align-items-center justify-content-center flex-column">
                                <div class="p-4 text-center">
                                    <i class="fas fa-user-plus fa-5x mb-4"></i>
                                    <h2 class="fw-bold mb-3">Join GradHire!</h2>
                                    <p class="lead">Create your account and start your journey to success</p>
                                    <div class="mt-4">
                                        <div class="mb-3">
                                            <i class="fas fa-check-circle me-2"></i> Free to join
                                        </div>
                                        <div class="mb-3">
                                            <i class="fas fa-check-circle me-2"></i> Personalized job matches
                                        </div>
                                        <div class="mb-3">
                                            <i class="fas fa-check-circle me-2"></i> Track applications
                                        </div>
                                        <div>
                                            <i class="fas fa-check-circle me-2"></i> Connect with recruiters
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Right Side - Registration Form -->
                            <div class="col-md-7">
                                <div class="auth-body p-4">
                                    <div class="text-center mb-4">
                                        <h3 class="fw-bold">Create Account</h3>
                                        <p class="text-muted">Fill in the details below to get started</p>
                                    </div>
                                    
                                    <!-- Error Message -->
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <i class="fas fa-exclamation-circle"></i> ${error}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Registration Form -->
                                    <form action="${pageContext.request.contextPath}/register" method="POST" id="registerForm">
                                        
                                        <!-- User Type Selector -->
                                        <div class="mb-4">
                                            <label class="form-label fw-bold">I am a:</label>
                                            <div class="btn-group w-100" role="group">
                                                <input type="radio" class="btn-check" name="userType" id="studentType" value="student" 
                                                       ${param.type == 'recruiter' ? '' : 'checked'}>
                                                <label class="btn btn-outline-primary" for="studentType">
                                                    <i class="fas fa-user-graduate"></i> Student
                                                </label>
                                                
                                                <input type="radio" class="btn-check" name="userType" id="recruiterType" value="recruiter"
                                                       ${param.type == 'recruiter' ? 'checked' : ''}>
                                                <label class="btn btn-outline-primary" for="recruiterType">
                                                    <i class="fas fa-building"></i> Recruiter
                                                </label>
                                            </div>
                                        </div>
                                        
                                        <div class="row">
                                            <div class="col-md-6 mb-3">
                                                <label for="firstName" class="form-label">
                                                    <i class="fas fa-user"></i> First Name <span class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control" id="firstName" name="firstName" 
                                                       placeholder="John" required>
                                            </div>
                                            
                                            <div class="col-md-6 mb-3">
                                                <label for="lastName" class="form-label">
                                                    <i class="fas fa-user"></i> Last Name <span class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control" id="lastName" name="lastName" 
                                                       placeholder="Doe" required>
                                            </div>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="email" class="form-label">
                                                <i class="fas fa-envelope"></i> Email Address <span class="text-danger">*</span>
                                            </label>
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   placeholder="your.email@example.com" required>
                                            <small class="text-muted">We'll never share your email with anyone else.</small>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="phone" class="form-label">
                                                <i class="fas fa-phone"></i> Phone Number <span class="text-danger">*</span>
                                            </label>
                                            <input type="tel" class="form-control" id="phone" name="phone" 
                                                   placeholder="(555) 123-4567" required>
                                        </div>
                                        
                                        <!-- Student-specific fields -->
                                        <div id="studentFields">
                                            <div class="mb-3">
                                                <label for="university" class="form-label">
                                                    <i class="fas fa-university"></i> University <span class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control" id="university" name="university" 
                                                       placeholder="Your University Name">
                                            </div>
                                            
                                            <div class="row">
                                                <div class="col-md-6 mb-3">
                                                    <label for="major" class="form-label">
                                                        <i class="fas fa-book"></i> Major <span class="text-danger">*</span>
                                                    </label>
                                                    <input type="text" class="form-control" id="major" name="major" 
                                                           placeholder="e.g., Computer Science">
                                                </div>
                                                
                                                <div class="col-md-6 mb-3">
                                                    <label for="graduationYear" class="form-label">
                                                        <i class="fas fa-calendar"></i> Graduation Year <span class="text-danger">*</span>
                                                    </label>
                                                    <select class="form-select" id="graduationYear" name="graduationYear">
                                                        <option value="">Select Year</option>
                                                        <c:forEach var="year" begin="2024" end="2030">
                                                            <option value="${year}">${year}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            
                                            <div class="mb-3">
                                                <label for="gpa" class="form-label">
                                                    <i class="fas fa-chart-line"></i> GPA
                                                </label>
                                                <input type="number" class="form-control" id="gpa" name="gpa" 
                                                       placeholder="3.5" step="0.01" min="0" max="4.0">
                                            </div>
                                        </div>
                                        
                                        <!-- Recruiter-specific fields -->
                                        <div id="recruiterFields" style="display: none;">
                                            <div class="mb-3">
                                                <label for="company" class="form-label">
                                                    <i class="fas fa-building"></i> Company Name <span class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control" id="company" name="company" 
                                                       placeholder="Your Company Name">
                                            </div>
                                            
                                            <div class="mb-3">
                                                <label for="position" class="form-label">
                                                    <i class="fas fa-briefcase"></i> Position/Title <span class="text-danger">*</span>
                                                </label>
                                                <input type="text" class="form-control" id="position" name="position" 
                                                       placeholder="e.g., HR Manager, Recruiter">
                                            </div>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="password" class="form-label">
                                                <i class="fas fa-lock"></i> Password <span class="text-danger">*</span>
                                            </label>
                                            <div class="input-group">
                                                <input type="password" class="form-control" id="password" name="password" 
                                                       placeholder="Create a strong password" required
                                                       onkeyup="displayPasswordStrength(this.value, 'passwordStrength')">
                                                <button class="btn btn-outline-secondary" type="button" 
                                                        onclick="togglePassword('password', 'toggleIcon1')">
                                                    <i class="fas fa-eye" id="toggleIcon1"></i>
                                                </button>
                                            </div>
                                            <div id="passwordStrength" class="mt-1"></div>
                                            <small class="text-muted">Minimum 8 characters with letters and numbers</small>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="confirmPassword" class="form-label">
                                                <i class="fas fa-lock"></i> Confirm Password <span class="text-danger">*</span>
                                            </label>
                                            <div class="input-group">
                                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                                       placeholder="Re-enter your password" required>
                                                <button class="btn btn-outline-secondary" type="button" 
                                                        onclick="togglePassword('confirmPassword', 'toggleIcon2')">
                                                    <i class="fas fa-eye" id="toggleIcon2"></i>
                                                </button>
                                            </div>
                                        </div>
                                        
                                        <div class="mb-3 form-check">
                                            <input type="checkbox" class="form-check-input" id="terms" name="terms" required>
                                            <label class="form-check-label" for="terms">
                                                I agree to the <a href="#" class="auth-link">Terms and Conditions</a> and 
                                                <a href="#" class="auth-link">Privacy Policy</a>
                                            </label>
                                        </div>
                                        
                                        <div class="d-grid mb-3">
                                            <button type="submit" class="btn btn-primary btn-auth">
                                                <i class="fas fa-user-plus"></i> Create Account
                                            </button>
                                        </div>
                                    </form>
                                    
                                    <hr class="my-3">
                                    
                                    <div class="text-center">
                                        <p class="mb-0">Already have an account? 
                                            <a href="${pageContext.request.contextPath}/login" class="auth-link fw-bold">
                                                Login Here
                                            </a>
                                        </p>
                                    </div>
                                    
                                    <div class="text-center mt-3">
                                        <a href="${pageContext.request.contextPath}/" class="text-muted">
                                            <i class="fas fa-arrow-left"></i> Back to Home
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
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
        // Toggle between student and recruiter fields
        const studentFields = document.getElementById('studentFields');
        const recruiterFields = document.getElementById('recruiterFields');
        
        document.querySelectorAll('input[name="userType"]').forEach(radio => {
            radio.addEventListener('change', function() {
                if (this.value === 'student') {
                    studentFields.style.display = 'block';
                    recruiterFields.style.display = 'none';
                    
                    // Make student fields required
                    document.getElementById('university').required = true;
                    document.getElementById('major').required = true;
                    document.getElementById('graduationYear').required = true;
                    document.getElementById('company').required = false;
                    document.getElementById('position').required = false;
                } else {
                    studentFields.style.display = 'none';
                    recruiterFields.style.display = 'block';
                    
                    // Make recruiter fields required
                    document.getElementById('university').required = false;
                    document.getElementById('major').required = false;
                    document.getElementById('graduationYear').required = false;
                    document.getElementById('company').required = true;
                    document.getElementById('position').required = true;
                }
            });
        });
        
        // Form validation
        document.getElementById('registerForm').addEventListener('submit', function(e) {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            const phone = document.getElementById('phone').value;
            const terms = document.getElementById('terms').checked;
            
            // Validate email
            if (!validateEmail(email)) {
                e.preventDefault();
                showAlert('Please enter a valid email address', 'danger');
                return false;
            }
            
            // Validate password
            if (password.length < 8) {
                e.preventDefault();
                showAlert('Password must be at least 8 characters long', 'danger');
                return false;
            }
            
            // Validate password match
            if (password !== confirmPassword) {
                e.preventDefault();
                showAlert('Passwords do not match', 'danger');
                return false;
            }
            
            // Validate phone
            const phoneRegex = /^[\d\s\-\(\)]+$/;
            if (!phoneRegex.test(phone)) {
                e.preventDefault();
                showAlert('Please enter a valid phone number', 'danger');
                return false;
            }
            
            // Validate terms
            if (!terms) {
                e.preventDefault();
                showAlert('You must agree to the Terms and Conditions', 'danger');
                return false;
            }
            
            // Show loading
            showLoading();
        });
        
        // Initialize with correct view based on URL parameter
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('type') === 'recruiter') {
            document.getElementById('recruiterType').click();
        }
    </script>
</body>
</html>



