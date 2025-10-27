<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Internal Server Error | GradHire</title>
    
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
                    <h1 class="display-1 fw-bold text-warning mb-4">500</h1>
                    <div class="mb-4">
                        <i class="fas fa-exclamation-triangle fa-5x text-muted"></i>
                    </div>
                    <h2 class="fw-bold mb-3">Internal Server Error</h2>
                    <p class="lead text-muted mb-4">
                        Oops! Something went wrong on our end. We're working to fix it.
                    </p>
                    
                    <div class="alert alert-info" role="alert">
                        <i class="fas fa-info-circle"></i>
                        Our team has been notified and is looking into this issue.
                    </div>
                    
                    <div class="d-flex gap-3 justify-content-center">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
                            <i class="fas fa-home"></i> Go Home
                        </a>
                        <button onclick="location.reload()" class="btn btn-outline-secondary btn-lg">
                            <i class="fas fa-redo"></i> Try Again
                        </button>
                    </div>
                    
                    <div class="mt-5">
                        <p class="text-muted small">
                            Error ID: <%= System.currentTimeMillis() %><br>
                            Please reference this ID when contacting support.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>



