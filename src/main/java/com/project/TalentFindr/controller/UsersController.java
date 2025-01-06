package com.project.TalentFindr.controller;

import java.util.List;

import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.entity.UsersType;
import com.project.TalentFindr.service.UsersService;
import com.project.TalentFindr.service.UsersTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsersController {


    public UsersTypeService usersTypeService;
    public UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService, UsersTypeService usersTypeService) {
        this.usersService = usersService;
        this.usersTypeService = usersTypeService;
    }

    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes=usersTypeService.getallusersType();
        model.addAttribute("getAllTypes",usersTypes);
        model.addAttribute("user",new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users user){
         usersService.addUser(user);
         System.out.println("hello");
         return "dashboard";
    }




}
