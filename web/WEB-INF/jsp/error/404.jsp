<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Page Not Found | GradHire</title>
    
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
    
    <div class="container">
        <div class="row min-vh-100 align-items-center justify-content-center">
            <div class="col-md-8 col-lg-6 text-center">
                <div class="error-page">
                    <h1 class="display-1 fw-bold text-primary mb-4">404</h1>
                    <div class="mb-4">
                        <i class="fas fa-search fa-5x text-muted"></i>
                    </div>
                    <h2 class="fw-bold mb-3">Page Not Found</h2>
                    <p class="lead text-muted mb-4">
                        Oops! The page you're looking for doesn't exist or has been moved.
                    </p>
                    
                    <div class="d-flex gap-3 justify-content-center">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                            <i class="fas fa-home"></i> Go Home
                        </a>
                        <button onclick="history.back()" class="btn btn-outline-secondary btn-lg">
                            <i class="fas fa-arrow-left"></i> Go Back
                        </button>
                    </div>
                    
                    <div class="mt-5">
                        <p class="text-muted">Looking for something specific?</p>
                        <div class="d-flex gap-2 justify-content-center">
                            <a href="${pageContext.request.contextPath}/jobs" class="btn btn-sm btn-outline-primary">
                                Browse Jobs
                            </a>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-outline-primary">
                                Login
                            </a>
                            <a href="${pageContext.request.contextPath}/register" class="btn btn-sm btn-outline-primary">
                                Sign Up
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>



