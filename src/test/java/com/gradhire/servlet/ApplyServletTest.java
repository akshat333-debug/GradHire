package com.gradhire.servlet;

import com.gradhire.util.SessionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApplyServletTest {
    @Test
    void rejectsNonStudentUser() throws Exception {
        ApplyServlet servlet = new ApplyServlet();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.USER_TYPE)).thenReturn("recruiter");
        when(session.getAttribute(SessionUtil.USER_ID)).thenReturn(10);
        when(request.getContextPath()).thenReturn("/gradhire");

        Method doPost = ApplyServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void rejectsInvalidJobId() throws Exception {
        ApplyServlet servlet = new ApplyServlet();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.USER_TYPE)).thenReturn("student");
        when(session.getAttribute(SessionUtil.USER_ID)).thenReturn(11);
        when(request.getParameter("jobId")).thenReturn("bad-id");
        when(request.getContextPath()).thenReturn("/gradhire");

        Method doPost = ApplyServlet.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPost.setAccessible(true);
        doPost.invoke(servlet, request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
        verify(response, never()).sendRedirect(anyString());
        verify(session, never()).setAttribute(eq("applicationError"), anyString());
    }
}
