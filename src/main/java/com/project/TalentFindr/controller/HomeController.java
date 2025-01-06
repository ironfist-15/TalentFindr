package com.project.TalentFindr.controller;


import java.util.List;

import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


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


}
