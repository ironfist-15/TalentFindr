package com.project.TalentFindr.controller;

import com.project.TalentFindr.Repository.JobPostActivityRepository;
import com.project.TalentFindr.entity.JobPostActivity;
import com.project.TalentFindr.service.JobPostActivityService;
import com.project.TalentFindr.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CandidateController {

    public JobPostActivityService jobPostActivityService;
    public UsersService usersService;
    public JobPostActivityRepository jobPostActivityRepository;

    public CandidateController(JobPostActivityService jobPostActivityService, UsersService usersService, JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    @GetMapping("/job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model){
        JobPostActivity jobPostActivity= jobPostActivityRepository.findById(id).orElseThrow(()->new RuntimeException("couldn't find job"));
        model.addAttribute("jobDetails",jobPostActivity);
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "job-details";
    }



}
