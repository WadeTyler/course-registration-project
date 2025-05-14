package net.tylerwade.registrationsystem.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtCookieToAuthorizationFilter implements Filter {

    private static final String COOKIE_NAME = "AuthToken";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // If http request
        if (servletRequest instanceof HttpServletRequest req && servletResponse instanceof HttpServletResponse) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    // if is AuthToken Cookie
                    if (cookie.getName().equals(COOKIE_NAME)) {
                        // Add the header if not present
                        if (req.getHeader("Authorization") == null) {
                            String token = cookie.getValue();

                            // Wrap the request to add the header
                            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req) {
                                @Override
                                public String getHeader(String name) {
                                    if (name.equalsIgnoreCase("Authorization")) {
                                        return "Bearer " + token;
                                    }
                                    return super.getHeader(name);
                                }
                            };

                            filterChain.doFilter(wrappedRequest, servletResponse);
                            return;
                        }
                    }
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
