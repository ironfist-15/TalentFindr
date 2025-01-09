package com.project.TalentFindr.controller;


import java.util.List;

import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class HomeController {


    @GetMapping("/")
    public String home(){
        return "index" ;
    }



    @Autowired
    private UsersRepository usersRepository;

    // Endpoint to get all users
    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
       return "redirect:/";
    }

}
