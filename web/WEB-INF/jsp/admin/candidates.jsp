<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Candidates - GradHire Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="mb-0"><i class="fas fa-users text-success"></i> Candidates</h3>
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-secondary">Back to Dashboard</a>
    </div>

    <form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/admin/candidates">
        <div class="col-sm-10">
            <input type="text" name="q" class="form-control" placeholder="Search by name, email, or college" value="${query}">
        </div>
        <div class="col-sm-2 d-grid">
            <button class="btn btn-primary" type="submit">Search</button>
        </div>
    </form>

    <c:choose>
        <c:when test="${empty candidates}">
            <div class="card"><div class="card-body text-center py-5">
                <i class="fas fa-user-slash fa-3x text-muted mb-3"></i>
                <p class="text-muted mb-0">No candidates found.</p>
            </div></div>
        </c:when>
        <c:otherwise>
            <div class="card"><div class="card-body">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Email</th>
                            <th>College</th>
                            <th>Degree</th>
                            <th>Grad Year</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${candidates}" var="s">
                            <tr>
                                <td><strong>${s.fullName}</strong></td>
                                <td>${s.email}</td>
                                <td>${s.collegeName}</td>
                                <td>${s.degree}</td>
                                <td>${s.graduationYear}</td>
                                <td class="text-end">
                                    <a href="${pageContext.request.contextPath}/student/profile?sid=${s.studentId}" class="btn btn-sm btn-outline-primary">View</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div></div>
        </c:otherwise>
    </c:choose>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
