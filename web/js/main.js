/**
 * GradHire - Main JavaScript File
 * Handles client-side interactivity and validation
 */

// ============================================
// Smooth Scrolling for Navigation Links
// ============================================
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// ============================================
// Navbar Scroll Effect
// ============================================
window.addEventListener('scroll', function() {
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        if (window.scrollY > 50) {
            navbar.classList.add('shadow');
        } else {
            navbar.classList.remove('shadow');
        }
    }
});

// ============================================
// Form Validation
// ============================================
function validateForm(formId) {
    const form = document.getElementById(formId);
    if (!form) return false;
    
    let isValid = true;
    const requiredFields = form.querySelectorAll('[required]');
    
    requiredFields.forEach(field => {
        if (!field.value.trim()) {
            isValid = false;
            field.classList.add('is-invalid');
        } else {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
        }
    });
    
    return isValid;
}

// Email Validation
function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
}

// Password Strength Checker
function checkPasswordStrength(password) {
    let strength = 0;
    if (password.length >= 8) strength++;
    if (password.match(/[a-z]+/)) strength++;
    if (password.match(/[A-Z]+/)) strength++;
    if (password.match(/[0-9]+/)) strength++;
    if (password.match(/[$@#&!]+/)) strength++;
    
    return strength;
}

// Display Password Strength
function displayPasswordStrength(password, elementId) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    const strength = checkPasswordStrength(password);
    const strengthText = ['Very Weak', 'Weak', 'Fair', 'Good', 'Strong'];
    const strengthClass = ['text-danger', 'text-warning', 'text-info', 'text-success', 'text-success'];
    
    if (password.length === 0) {
        element.innerHTML = '';
        return;
    }
    
    element.innerHTML = `<small class="${strengthClass[strength - 1]}">
        Password Strength: ${strengthText[strength - 1]}
    </small>`;
}

// ============================================
// Show/Hide Password Toggle
// ============================================
function togglePassword(inputId, iconId) {
    const input = document.getElementById(inputId);
    const icon = document.getElementById(iconId);
    
    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        input.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// ============================================
// Loading Spinner
// ============================================
function showLoading() {
    const overlay = document.createElement('div');
    overlay.className = 'spinner-overlay';
    overlay.id = 'loadingSpinner';
    overlay.innerHTML = `
        <div class="spinner-border text-light spinner-border-custom" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    `;
    document.body.appendChild(overlay);
}

function hideLoading() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.remove();
    }
}

// ============================================
// Alert Messages
// ============================================
function showAlert(message, type = 'info', duration = 5000) {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed top-0 start-50 translate-middle-x mt-3`;
    alertDiv.style.zIndex = '9999';
    alertDiv.innerHTML = `
        <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'danger' ? 'exclamation-circle' : 'info-circle'}"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.remove();
    }, duration);
}

// ============================================
// Confirm Dialog
// ============================================
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// ============================================
// File Upload Preview
// ============================================
function previewImage(input, previewId) {
    const file = input.files[0];
    const preview = document.getElementById(previewId);
    
    if (file && preview) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    }
}

// ============================================
// Character Counter
// ============================================
function updateCharCount(inputId, counterId, maxLength) {
    const input = document.getElementById(inputId);
    const counter = document.getElementById(counterId);
    
    if (input && counter) {
        const remaining = maxLength - input.value.length;
        counter.textContent = `${remaining} characters remaining`;
        
        if (remaining < 50) {
            counter.classList.add('text-warning');
        } else {
            counter.classList.remove('text-warning');
        }
        
        if (remaining < 0) {
            counter.classList.add('text-danger');
            counter.classList.remove('text-warning');
        }
    }
}

// ============================================
// Skills Tag Input
// ============================================
function initializeSkillTags(inputId, containerId) {
    const input = document.getElementById(inputId);
    const container = document.getElementById(containerId);
    
    if (!input || !container) return;
    
    let skills = [];
    
    input.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' || e.key === ',') {
            e.preventDefault();
            const skill = this.value.trim();
            
            if (skill && !skills.includes(skill)) {
                skills.push(skill);
                addSkillTag(skill, container);
                this.value = '';
            }
        }
    });
    
    function addSkillTag(skill, container) {
        const tag = document.createElement('span');
        tag.className = 'skill-tag me-2 mb-2';
        tag.innerHTML = `
            ${skill}
            <i class="fas fa-times ms-2 cursor-pointer" onclick="removeSkillTag(this, '${skill}')"></i>
        `;
        container.appendChild(tag);
    }
}

function removeSkillTag(element, skill) {
    element.parentElement.remove();
}

// ============================================
// Auto-dismiss Alerts
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
    
    // Initialize tooltips
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Initialize popovers
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
});

// ============================================
// AJAX Form Submission Helper
// ============================================
function submitFormAjax(formId, url, successCallback, errorCallback) {
    const form = document.getElementById(formId);
    if (!form) return;
    
    const formData = new FormData(form);
    
    showLoading();
    
    fetch(url, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        hideLoading();
        if (data.success) {
            if (successCallback) successCallback(data);
        } else {
            if (errorCallback) errorCallback(data);
        }
    })
    .catch(error => {
        hideLoading();
        showAlert('An error occurred. Please try again.', 'danger');
        console.error('Error:', error);
    });
}

// ============================================
// Debounce Function (for search inputs)
// ============================================
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// ============================================
// Local Storage Helpers
// ============================================
function saveToLocalStorage(key, value) {
    try {
        localStorage.setItem(key, JSON.stringify(value));
    } catch (e) {
        console.error('Error saving to localStorage:', e);
    }
}

function getFromLocalStorage(key) {
    try {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    } catch (e) {
        console.error('Error reading from localStorage:', e);
        return null;
    }
}

// ============================================
// Format Date
// ============================================
function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

// ============================================
// Format Time Ago
// ============================================
function timeAgo(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const seconds = Math.floor((now - date) / 1000);
    
    let interval = seconds / 31536000;
    if (interval > 1) return Math.floor(interval) + " years ago";
    
    interval = seconds / 2592000;
    if (interval > 1) return Math.floor(interval) + " months ago";
    
    interval = seconds / 86400;
    if (interval > 1) return Math.floor(interval) + " days ago";
    
    interval = seconds / 3600;
    if (interval > 1) return Math.floor(interval) + " hours ago";
    
    interval = seconds / 60;
    if (interval > 1) return Math.floor(interval) + " minutes ago";
    
    return Math.floor(seconds) + " seconds ago";
}

// ============================================
// Export functions for global use
// ============================================
window.GradHire = {
    validateForm,
    validateEmail,
    checkPasswordStrength,
    displayPasswordStrength,
    togglePassword,
    showLoading,
    hideLoading,
    showAlert,
    confirmAction,
    previewImage,
    updateCharCount,
    submitFormAjax,
    formatDate,
    timeAgo,
    saveToLocalStorage,
    getFromLocalStorage
};


