<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="GradHire - Connecting college students with internships and entry-level positions">
    <title>GradHire - Your Gateway to Career Success</title>
    
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
    
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm fixed-top">
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
                        <a class="nav-link active" href="#home">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#features">Features</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#how-it-works">How It Works</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#about">About</a>
                    </li>
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
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Hero Section -->
    <section id="home" class="hero-section">
        <div class="container">
            <div class="row align-items-center min-vh-100">
                <div class="col-lg-6">
                    <div class="hero-content">
                        <h1 class="display-3 fw-bold mb-4">
                            Your Gateway to <span class="text-primary">Career Success</span>
                        </h1>
                        <p class="lead mb-4">
                            Connect with top internships and entry-level positions tailored to your skills and interests. 
                            Start your professional journey with GradHire.
                        </p>
                        <div class="d-flex gap-3 mb-4">
                            <a href="${pageContext.request.contextPath}/register?type=student" class="btn btn-primary btn-lg">
                                <i class="fas fa-user-graduate"></i> I'm a Student
                            </a>
                            <a href="${pageContext.request.contextPath}/register?type=recruiter" class="btn btn-outline-primary btn-lg">
                                <i class="fas fa-building"></i> I'm a Recruiter
                            </a>
                        </div>
                        <div class="stats-row d-flex gap-4 mt-5">
                            <div>
                                <h3 class="fw-bold text-primary mb-0">10,000+</h3>
                                <p class="text-muted">Students</p>
                            </div>
                            <div>
                                <h3 class="fw-bold text-primary mb-0">500+</h3>
                                <p class="text-muted">Companies</p>
                            </div>
                            <div>
                                <h3 class="fw-bold text-primary mb-0">5,000+</h3>
                                <p class="text-muted">Jobs Posted</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="hero-image text-center">
                        <img src="${pageContext.request.contextPath}/images/hero-illustration.svg" 
                             alt="Students and Career" 
                             class="img-fluid"
                             onerror="this.style.display='none'">
                        <div class="hero-placeholder">
                            <i class="fas fa-briefcase fa-10x text-primary opacity-25"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Features Section -->
    <section id="features" class="py-5 bg-light">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="display-5 fw-bold mb-3">Why Choose GradHire?</h2>
                <p class="lead text-muted">Everything you need to launch your career</p>
            </div>
            
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="feature-card card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon mb-3">
                                <i class="fas fa-brain fa-3x text-primary"></i>
                            </div>
                            <h4 class="card-title mb-3">Skill-Based Matching</h4>
                            <p class="card-text text-muted">
                                Advanced algorithms match your skills with the most relevant job opportunities.
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="feature-card card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon mb-3">
                                <i class="fas fa-rocket fa-3x text-primary"></i>
                            </div>
                            <h4 class="card-title mb-3">Quick Application</h4>
                            <p class="card-text text-muted">
                                Apply to multiple positions with one click. Your profile does the heavy lifting.
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="feature-card card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon mb-3">
                                <i class="fas fa-chart-line fa-3x text-primary"></i>
                            </div>
                            <h4 class="card-title mb-3">Track Progress</h4>
                            <p class="card-text text-muted">
                                Monitor your application status and get real-time updates from recruiters.
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="feature-card card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon mb-3">
                                <i class="fas fa-building fa-3x text-primary"></i>
                            </div>
                            <h4 class="card-title mb-3">Top Companies</h4>
                            <p class="card-text text-muted">
                                Connect with leading companies actively hiring college students.
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="feature-card card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon mb-3">
                                <i class="fas fa-shield-alt fa-3x text-primary"></i>
                            </div>
                            <h4 class="card-title mb-3">Secure & Private</h4>
                            <p class="card-text text-muted">
                                Your data is protected with enterprise-grade security measures.
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="feature-card card h-100 border-0 shadow-sm">
                        <div class="card-body text-center p-4">
                            <div class="feature-icon mb-3">
                                <i class="fas fa-headset fa-3x text-primary"></i>
                            </div>
                            <h4 class="card-title mb-3">24/7 Support</h4>
                            <p class="card-text text-muted">
                                Our dedicated support team is always here to help you succeed.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- How It Works Section -->
    <section id="how-it-works" class="py-5">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="display-5 fw-bold mb-3">How It Works</h2>
                <p class="lead text-muted">Get started in three simple steps</p>
            </div>
            
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="text-center">
                        <div class="step-number mb-3">
                            <span class="badge bg-primary rounded-circle p-3 fs-4">1</span>
                        </div>
                        <h4 class="mb-3">Create Your Profile</h4>
                        <p class="text-muted">
                            Sign up and build a comprehensive profile showcasing your skills, education, and experience.
                        </p>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="text-center">
                        <div class="step-number mb-3">
                            <span class="badge bg-primary rounded-circle p-3 fs-4">2</span>
                        </div>
                        <h4 class="mb-3">Browse & Apply</h4>
                        <p class="text-muted">
                            Explore personalized job recommendations and apply to positions that match your profile.
                        </p>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="text-center">
                        <div class="step-number mb-3">
                            <span class="badge bg-primary rounded-circle p-3 fs-4">3</span>
                        </div>
                        <h4 class="mb-3">Get Hired</h4>
                        <p class="text-muted">
                            Connect with recruiters, ace your interviews, and land your dream job or internship.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- CTA Section -->
    <section class="cta-section bg-primary text-white py-5">
        <div class="container text-center">
            <h2 class="display-5 fw-bold mb-4">Ready to Start Your Journey?</h2>
            <p class="lead mb-4">Join thousands of students who have found their dream jobs through GradHire</p>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-light btn-lg px-5">
                Get Started Now <i class="fas fa-arrow-right ms-2"></i>
            </a>
        </div>
    </section>
    
    <!-- Footer -->
    <footer class="bg-dark text-white py-4">
        <div class="container">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <h5 class="fw-bold mb-3">
                        <i class="fas fa-graduation-cap"></i> GradHire
                    </h5>
                    <p class="text-muted">
                        Connecting college students with their dream careers since 2025.
                    </p>
                </div>
                <div class="col-md-4 mb-3">
                    <h6 class="fw-bold mb-3">Quick Links</h6>
                    <ul class="list-unstyled">
                        <li><a href="#features" class="text-muted text-decoration-none">Features</a></li>
                        <li><a href="#how-it-works" class="text-muted text-decoration-none">How It Works</a></li>
                        <li><a href="${pageContext.request.contextPath}/login" class="text-muted text-decoration-none">Login</a></li>
                        <li><a href="${pageContext.request.contextPath}/register" class="text-muted text-decoration-none">Sign Up</a></li>
                    </ul>
                </div>
                <div class="col-md-4 mb-3">
                    <h6 class="fw-bold mb-3">Contact Us</h6>
                    <p class="text-muted">
                        <i class="fas fa-envelope"></i> support@gradhire.com<br>
                        <i class="fas fa-phone"></i> +1 (555) 123-4567
                    </p>
                    <div class="social-links">
                        <a href="#" class="text-white me-3"><i class="fab fa-facebook fa-lg"></i></a>
                        <a href="#" class="text-white me-3"><i class="fab fa-twitter fa-lg"></i></a>
                        <a href="#" class="text-white me-3"><i class="fab fa-linkedin fa-lg"></i></a>
                        <a href="#" class="text-white"><i class="fab fa-instagram fa-lg"></i></a>
                    </div>
                </div>
            </div>
            <hr class="border-secondary">
            <div class="text-center text-muted">
                <p class="mb-0">&copy; 2025 GradHire. All rights reserved.</p>
            </div>
        </div>
    </footer>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>


