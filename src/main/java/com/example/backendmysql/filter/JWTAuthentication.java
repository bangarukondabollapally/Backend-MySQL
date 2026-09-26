package com.example.backendmysql.filter;

import com.example.backendmysql.entity.Employee;
import com.example.backendmysql.repository.EmployeeRepository;
import com.example.backendmysql.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthentication extends OncePerRequestFilter {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private EmployeeRepository repo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if(authHeader!=null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);

            if (jwtService.validateToken(token)) {
                String email = jwtService.extractUser(token);

                Employee emp = repo.findFirstByEmail(email);

                if (emp != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    emp,
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_" + emp.getRole()))
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
