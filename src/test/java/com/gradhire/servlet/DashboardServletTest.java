package com.gradhire.servlet;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DashboardServletTest {
    @Test
    void resolvesRoleSpecificViews() throws Exception {
        DashboardServlet servlet = new DashboardServlet();
        Method resolveDashboardView = DashboardServlet.class.getDeclaredMethod("resolveDashboardView", String.class);
        resolveDashboardView.setAccessible(true);

        assertEquals("/WEB-INF/views/dashboard-student.jsp", resolveDashboardView.invoke(servlet, "student"));
        assertEquals("/WEB-INF/views/dashboard-recruiter.jsp", resolveDashboardView.invoke(servlet, "recruiter"));
        assertEquals("/WEB-INF/views/dashboard-admin.jsp", resolveDashboardView.invoke(servlet, "admin"));
        assertEquals("/WEB-INF/views/dashboard.jsp", resolveDashboardView.invoke(servlet, "unknown"));
        assertEquals("/WEB-INF/views/dashboard.jsp", resolveDashboardView.invoke(servlet, new Object[]{null}));
    }
}
