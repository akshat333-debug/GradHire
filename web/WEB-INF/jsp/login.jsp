<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - GradHire</title>
    
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
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-10 col-lg-8">
                    <div class="auth-card">
                        <div class="row g-0">
                            <!-- Left Side - Branding -->
                            <div class="col-md-6 d-none d-md-block auth-header d-flex align-items-center justify-content-center flex-column">
                                <div class="p-5 text-center">
                                    <i class="fas fa-graduation-cap fa-5x mb-4"></i>
                                    <h2 class="fw-bold mb-3">Welcome Back!</h2>
                                    <p class="lead">Sign in to continue your journey to career success</p>
                                    <div class="mt-4">
                                        <i class="fas fa-briefcase fa-2x mx-2"></i>
                                        <i class="fas fa-rocket fa-2x mx-2"></i>
                                        <i class="fas fa-chart-line fa-2x mx-2"></i>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Right Side - Login Form -->
                            <div class="col-md-6">
                                <div class="auth-body p-4 p-md-5">
                                    <div class="text-center mb-4">
                                        <h3 class="fw-bold">Login to GradHire</h3>
                                        <p class="text-muted">Enter your credentials to access your account</p>
                                    </div>
                                    
                                    <!-- Error Message -->
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                            <i class="fas fa-exclamation-circle"></i> ${error}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Success Message -->
                                    <c:if test="${not empty success}">
                                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                                            <i class="fas fa-check-circle"></i> ${success}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                        </div>
                                    </c:if>
                                    
                                    <!-- User Type Selector -->
                                    <div class="mb-4">
                                        <div class="btn-group w-100" role="group">
                                            <input type="radio" class="btn-check" name="userType" id="studentType" value="student" 
                                                   ${param.type == 'admin' ? '' : 'checked'}>
                                            <label class="btn btn-outline-primary" for="studentType">
                                                <i class="fas fa-user-graduate"></i> Student
                                            </label>
                                            
                                            <input type="radio" class="btn-check" name="userType" id="adminType" value="admin"
                                                   ${param.type == 'admin' ? 'checked' : ''}>
                                            <label class="btn btn-outline-primary" for="adminType">
                                                <i class="fas fa-user-tie"></i> Recruiter
                                            </label>
                                        </div>
                                    </div>
                                    
                                    <!-- Login Form -->
                                    <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm">
                                        <input type="hidden" name="userType" id="userTypeField" value="${param.type == 'admin' ? 'admin' : 'student'}">
                                        
                                        <div class="mb-3">
                                            <label for="email" class="form-label">
                                                <i class="fas fa-envelope"></i> Email Address
                                            </label>
                                            <input type="email" class="form-control" id="email" name="email" 
                                                   placeholder="your.email@example.com" required>
                                        </div>
                                        
                                        <div class="mb-3">
                                            <label for="password" class="form-label">
                                                <i class="fas fa-lock"></i> Password
                                            </label>
                                            <div class="input-group">
                                                <input type="password" class="form-control" id="password" name="password" 
                                                       placeholder="Enter your password" required>
                                                <button class="btn btn-outline-secondary" type="button" 
                                                        onclick="togglePassword('password', 'toggleIcon')">
                                                    <i class="fas fa-eye" id="toggleIcon"></i>
                                                </button>
                                            </div>
                                        </div>
                                        
                                        <div class="mb-3 form-check">
                                            <input type="checkbox" class="form-check-input" id="rememberMe" name="rememberMe">
                                            <label class="form-check-label" for="rememberMe">
                                                Remember me
                                            </label>
                                        </div>
                                        
                                        <div class="d-grid mb-3">
                                            <button type="submit" class="btn btn-primary btn-auth">
                                                <i class="fas fa-sign-in-alt"></i> Login
                                            </button>
                                        </div>
                                        
                                        <div class="text-center">
                                            <a href="${pageContext.request.contextPath}/forgot-password" class="auth-link">
                                                Forgot Password?
                                            </a>
                                        </div>
                                    </form>
                                    
                                    <hr class="my-4">
                                    
                                    <div class="text-center">
                                        <p class="mb-0">Don't have an account? 
                                            <a href="${pageContext.request.contextPath}/register" class="auth-link fw-bold">
                                                Sign Up Here
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
        // Update hidden field when user type changes
        document.querySelectorAll('input[name="userType"]').forEach(radio => {
            radio.addEventListener('change', function() {
                document.getElementById('userTypeField').value = this.value;
            });
        });
        
        // Form validation
        document.getElementById('loginForm').addEventListener('submit', function(e) {
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            if (!validateEmail(email)) {
                e.preventDefault();
                showAlert('Please enter a valid email address', 'danger');
                return false;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                showAlert('Password must be at least 6 characters long', 'danger');
                return false;
            }
        });
    </script>
</body>
</html>



