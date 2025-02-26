package com.project.TalentFindr.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project.TalentFindr.Repository.JobPostActivityRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.*;
import com.project.TalentFindr.service.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CandidateApplyController {

    public JobPostActivityService jobPostActivityService;
    public UsersService usersService;
    public JobPostActivityRepository jobPostActivityRepository;
    public JobSeekerApplyService jobSeekerApplyService;
    public JobSeekerSaveService jobSeekerSaveService;
    public RecruiterProfileService recruiterProfileService;
    public CandidateProfileService candidateProfileService;
    public UsersRepository usersRepository;

    public CandidateApplyController(JobPostActivityService jobPostActivityService, UsersService usersService, JobPostActivityRepository jobPostActivityRepository, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService, RecruiterProfileService recruiterProfileService, CandidateProfileService candidateProfileService, UsersRepository usersRepository) {
        this.jobPostActivityService = jobPostActivityService;
        this.usersService = usersService;
        this.jobPostActivityRepository = jobPostActivityRepository;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
        this.recruiterProfileService = recruiterProfileService;
        this.candidateProfileService = candidateProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/job-details-apply/{id}")
    public String display(@PathVariable("id") int id, Model model){
        System.out.println("User in model: " + usersService.getCurrentUserProfile());

        JobPostActivity jobDetails = jobPostActivityService.getOne(id);
        List<JobSeekerApply> jobSeekerApplyList=jobSeekerApplyService.getJobCandidates(jobDetails);
        List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getJobsCandidates(jobDetails);


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                RecruiterProfile user=recruiterProfileService.getCurrentRecruiterProfile();
                if(user!=null){
                    model.addAttribute("applyList",jobSeekerApplyList);
                }
            }else{
                JobSeekerProfile user=candidateProfileService.getCurrentSeekerProfile();
                if(user!=null){
                    boolean exists=false;
                    boolean saved=false;
                    for(JobSeekerApply jobSeekerApply : jobSeekerApplyList){
                        if(jobSeekerApply.getUserId().getUserAccountId() == user.getUserAccountId()){
                            exists=true;
                            break;
                        }
                    }
                    for(JobSeekerSave  jobSeekerSave : jobSeekerSaveList){
                        if(jobSeekerSave.getUserId().getUserAccountId() == user.getUserAccountId()){
                            saved=true;
                            break;
                        }
                    }
                    model.addAttribute("alreadyApplied",exists);
                    model.addAttribute("alreadySaved",saved);
                }
            }
        }
        JobSeekerApply jobSeekerApply=new JobSeekerApply();
        model.addAttribute("applyJob",jobSeekerApply);
        model.addAttribute("jobDetails", jobDetails);
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "job-details";
    }

   @PostMapping("job-details/apply/{id}")
    public String apply(@PathVariable("id") int id, JobSeekerApply jobSeekerApply) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> seekerProfile = candidateProfileService.getOne(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if (seekerProfile.isPresent() && jobPostActivity != null) {
                jobSeekerApply = new JobSeekerApply();
                jobSeekerApply.setUserId(seekerProfile.get());
                jobSeekerApply.setJob(jobPostActivity);
                jobSeekerApply.setApplyDate(LocalDateTime.now());
            } else {
                throw new RuntimeException("User not found");
            }
            jobSeekerApplyService.addNew(jobSeekerApply);
        }

        return "redirect:/dashboard/";
    }



}
