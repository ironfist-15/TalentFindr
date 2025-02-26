package com.project.TalentFindr.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.*;
import com.project.TalentFindr.service.CandidateProfileService;
import com.project.TalentFindr.service.JobPostActivityService;
import com.project.TalentFindr.service.JobSeekerSaveService;
import com.project.TalentFindr.service.UsersService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JobSeekerSaveController {

    public UsersRepository usersRepository;
    public CandidateProfileService candidateProfileService;
    public JobSeekerSaveService jobSeekerSaveService;
    public JobPostActivityService jobPostActivityService;
    public UsersService usersService;

    public JobSeekerSaveController(UsersRepository usersRepository, CandidateProfileService candidateProfileService, JobSeekerSaveService jobSeekerSaveService, JobPostActivityService jobPostActivityService, UsersService usersService) {
        this.usersRepository = usersRepository;
        this.candidateProfileService = candidateProfileService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
    }

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable("id")int id, JobSeekerSave jobSeekerSave){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            Users user=usersService.findByEmail(userName);
            Optional<JobSeekerProfile> jobSeekerProfile=candidateProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity=jobPostActivityService.getOne(id);
            if (jobSeekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerSave = new JobSeekerSave();
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(jobSeekerProfile.get());

            }else{
                throw new RuntimeException("user not found");
            }
            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model){
        List<JobPostActivity> jobPostActivityList=new ArrayList<>();
        Object currentUserProfile=candidateProfileService.getCurrentSeekerProfile();

        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
        for(JobSeekerSave jobSeekerSave:jobSeekerSaveList){
            jobPostActivityList.add(jobSeekerSave.getJob());

        }
        model.addAttribute("jobPost",jobPostActivityList);
        model.addAttribute("user",currentUserProfile);
        return "saved-jobs";
    }

}
