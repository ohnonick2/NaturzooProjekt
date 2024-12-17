package net.ohnonick2.naturzooprojekt.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.io.IOException;

//@Component
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        // Überprüfen, ob der Benutzer auf der Login-Seite ist
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/login")) {
            chain.doFilter(request, response); // Keine weitere Filterung auf der Login-Seite
            return;
        }

        if (session == null || session.getAttribute("user") == null) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect("/login?invalidsession");
            return;
        }

        // Überprüfen, ob die Session abgelaufen ist (z. B. nach 20 Minuten)
        Long lastAccessTime = (Long) session.getAttribute("lastAccessTime");
        if (lastAccessTime != null && System.currentTimeMillis() - lastAccessTime > 1200000) { // 20 Minuten in Millisekunden
            session.invalidate(); // Session invalidieren, wenn abgelaufen
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendRedirect("/login?sessionexpired");
            return;
        }

        // Update des lastAccessTime-Werts bei jeder Anfrage
        session.setAttribute("lastAccessTime", System.currentTimeMillis());

        // Weiter zur nächsten Filter- oder Servlet-Verarbeitung
        chain.doFilter(request, response);
    }

}




