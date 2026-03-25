package com.gradhire.servlet;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProfileServletTest {
    @Test
    void normalizeTrimsAndHandlesEmptyValues() throws Exception {
        ProfileServlet servlet = new ProfileServlet();
        Method normalize = ProfileServlet.class.getDeclaredMethod("normalize", String.class);
        normalize.setAccessible(true);

        assertNull(normalize.invoke(servlet, new Object[]{null}));
        assertNull(normalize.invoke(servlet, "   "));
        assertEquals("abc", normalize.invoke(servlet, "  abc  "));
    }
}
