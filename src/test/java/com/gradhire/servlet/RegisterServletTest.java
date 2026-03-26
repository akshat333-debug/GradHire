package com.gradhire.servlet;

import com.gradhire.util.SessionUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterServletTest {
    @Test
    void redirectsToDashboardWhenAlreadyLoggedIn() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.USER_ID)).thenReturn(1);
        when(session.getAttribute(SessionUtil.USER_TYPE)).thenReturn("student");
        when(request.getContextPath()).thenReturn("/gradhire");

        Method doGet = RegisterServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(response).sendRedirect("/gradhire/dashboard");
    }

    @Test
    void rendersRegisterPageForAnonymousUser() throws Exception {
        RegisterServlet servlet = new RegisterServlet();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getSession(false)).thenReturn(null);
        when(request.getRequestDispatcher("/WEB-INF/views/register.jsp")).thenReturn(dispatcher);

        Method doGet = RegisterServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGet.setAccessible(true);
        doGet.invoke(servlet, request, response);

        verify(dispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}
