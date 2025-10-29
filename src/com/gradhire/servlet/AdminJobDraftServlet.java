package com.gradhire.servlet;

import com.gradhire.dao.JobDAO;
import com.gradhire.dao.SkillDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Job;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Saves a job as Draft for the current recruiter.
 * URL: /admin/jobs/save-draft
 * Method: POST (returns JSON)
 */
public class AdminJobDraftServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AdminJobDraftServlet.class.getName());

    private JobDAO jobDAO;
    private SkillDAO skillDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        jobDAO = new JobDAO();
        skillDAO = new SkillDAO();
        logger.info("AdminJobDraftServlet initialized");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("{\"success\":false,\"message\":\"Unauthorized\"}");
            return;
        }

        try {
            Admin admin = (Admin) session.getAttribute("user");

            String title = request.getParameter("title");
            String jobType = request.getParameter("jobType");
            String company = request.getParameter("company");
            String location = request.getParameter("location");
            String description = request.getParameter("description");
            String requirements = request.getParameter("requirements");
            String salaryMinStr = request.getParameter("salaryMin");
            String salaryMaxStr = request.getParameter("salaryMax");
            String benefits = request.getParameter("benefits");
            String skillsStr = request.getParameter("skills");
            String experienceLevel = request.getParameter("experienceLevel");
            String educationLevel = request.getParameter("educationLevel");
            String applicationDeadlineStr = request.getParameter("applicationDeadline");
            String positionsStr = request.getParameter("positions");
            String contactEmail = request.getParameter("contactEmail");
            String contactPhone = request.getParameter("contactPhone");

            // Minimal validation for drafts: require at least title and company
            if (title == null || title.isEmpty() || company == null || company.isEmpty()) {
                out.print("{\"success\":false,\"message\":\"Title and company are required to save a draft\"}");
                return;
            }

            Job job = new Job();
            job.setAdminId(admin.getAdminId());
            job.setTitle(title);
            job.setJobType(jobType);
            job.setCompany(company);
            job.setLocation(location);
            job.setDescription(description);
            job.setRequirements(requirements);
            job.setBenefits(benefits);
            job.setExperienceLevel(experienceLevel);
            job.setEducationLevel(educationLevel);
            job.setContactEmail(contactEmail);
            job.setContactPhone(contactPhone);

            if (salaryMinStr != null && !salaryMinStr.isEmpty()) {
                try { job.setSalaryMin(new java.math.BigDecimal(salaryMinStr)); } catch (NumberFormatException ignore) {}
            }
            if (salaryMaxStr != null && !salaryMaxStr.isEmpty()) {
                try { job.setSalaryMax(new java.math.BigDecimal(salaryMaxStr)); } catch (NumberFormatException ignore) {}
            }
            if (applicationDeadlineStr != null && !applicationDeadlineStr.isEmpty()) {
                try { job.setApplicationDeadline(Date.valueOf(applicationDeadlineStr)); } catch (IllegalArgumentException ignore) {}
            }
            if (positionsStr != null && !positionsStr.isEmpty()) {
                try { job.setPositionsAvailable(Integer.parseInt(positionsStr)); } catch (NumberFormatException ignore) {}
            }

            // Force Draft status
            job.setJobStatus("Draft");

            Integer jobId = jobDAO.create(job);
            if (jobId != null) {
                if (skillsStr != null && !skillsStr.isEmpty()) {
                    String[] skillNames = skillsStr.split(",");
                    for (String skillName : skillNames) {
                        skillName = skillName.trim();
                        if (!skillName.isEmpty()) {
                            skillDAO.addJobSkill(jobId, skillName, true);
                        }
                    }
                }
                out.print("{\"success\":true,\"jobId\":" + jobId + "}");
            } else {
                out.print("{\"success\":false,\"message\":\"Failed to save draft\"}");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving job draft", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\":false,\"message\":\"Server error\"}");
        }
    }
}


