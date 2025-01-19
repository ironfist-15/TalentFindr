package com.project.TalentFindr.controller;



import java.time.LocalDateTime;

import com.project.TalentFindr.entity.JobPostActivity;
import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.service.JobPostActivityService;
import org.springframework.ui.Model;
import com.project.TalentFindr.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class JobPostActivityController {


    @Autowired
    public UsersService usersService;
    public JobPostActivityService jobPostActivityService;

    public JobPostActivityController(UsersService usersService,JobPostActivityService jobPostActivityService) {
        this.usersService = usersService;
        this.jobPostActivityService=jobPostActivityService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model){

        Object currentUserProfile=usersService.getCurrentUserProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){    //just checking if the user is not an anonymous one
            String currentUserName=authentication.getName();
            model.addAttribute("username",currentUserName);
        }
        model.addAttribute("user",currentUserProfile);
        return "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity",new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @RequestMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity,Model model){
         Users user=usersService.getCurrentUser();
         if(user!=null){
             jobPostActivity.setPostedById(user);
         }
         jobPostActivity.setPostedDate(LocalDateTime.now());
         model.addAttribute("jobPostActivity",jobPostActivity);

         jobPostActivityService.add(jobPostActivity);
         return "redirect:/dashboard/";
    }

}
