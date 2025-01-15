package com.project.TalentFindr.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSucessHandler  implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String username=userDetails.getUsername();
        System.out.println("User "+username+" is logged in");
        boolean hasjobseekerrole=authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("Job Seeker"));
        boolean hasrecruiterrole=authentication.getAuthorities().stream().anyMatch(r->r.getAuthority().equals("Recruiter"));
        if(hasrecruiterrole||hasjobseekerrole){
            response.sendRedirect("/dashboard/");
        }
    }
}
