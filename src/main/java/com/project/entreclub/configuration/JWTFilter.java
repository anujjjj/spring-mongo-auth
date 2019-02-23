package com.project.entreclub.configuration;

import com.project.entreclub.service.TokenService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.project.entreclub.configuration.Constants.*;

@Configuration
public class JWTFilter extends GenericFilterBean {

    private TokenService tokenService;

    @Autowired
    JWTFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) req;

        HttpServletResponse response = (HttpServletResponse) res;
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String token = null;
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            token = header.replace(TOKEN_PREFIX, "");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_OK, "success");
            return;
        }

        if (allowRequestWithoutToken(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(req, res);
        } else {
            if (token == null || !tokenService.isTokenValid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                ObjectId userId = new ObjectId(tokenService.getUserIdFromToken(token));
                request.setAttribute("userId", userId);
                filterChain.doFilter(req, res);

            }
        }

    }

    public boolean allowRequestWithoutToken(HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        return request.getRequestURI().contains("/register");
    }
}
