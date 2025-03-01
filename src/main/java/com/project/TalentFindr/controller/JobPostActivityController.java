package com.project.TalentFindr.controller;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.project.TalentFindr.entity.*;
import com.project.TalentFindr.service.JobPostActivityService;
import com.project.TalentFindr.service.JobSeekerApplyService;
import com.project.TalentFindr.service.JobSeekerSaveService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.Model;
import com.project.TalentFindr.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JobPostActivityController {


    @Autowired
    public UsersService usersService;
    public JobPostActivityService jobPostActivityService;
    public JobSeekerApplyService jobSeekerApplyService;
    public JobSeekerSaveService jobSeekerSaveService;

    public JobPostActivityController(UsersService usersService, JobPostActivityService jobPostActivityService, JobSeekerApplyService jobSeekerApplyService, JobSeekerSaveService jobSeekerSaveService) {
        this.usersService = usersService;
        this.jobPostActivityService = jobPostActivityService;
        this.jobSeekerApplyService = jobSeekerApplyService;
        this.jobSeekerSaveService = jobSeekerSaveService;
    }

    @GetMapping("/dashboard/")
    public String searchJobs(Model model,
                             @RequestParam(value="job",required = false)String job,
                             @RequestParam(value="location",required = false)String location,
                             @RequestParam(value="partTime",required = false)String partTime,
                             @RequestParam(value="fullTime",required = false)String fullTime,
                             @RequestParam(value="freelance",required = false)String freelance,
                             @RequestParam(value="remoteOnly",required = false)String remoteOnly,
                             @RequestParam(value="officeOnly",required = false)String officeOnly,
                             @RequestParam(value="partialRemote",required = false)String partialRemote,
                             @RequestParam(value="today",required = false)boolean today,
                             @RequestParam(value="days7",required = false)boolean days7,
                             @RequestParam(value="days30",required = false)boolean days30
                             ){


        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(partTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(partTime, "Freelance"));
        model.addAttribute("remoteOnly", Objects.equals(partTime, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(partTime, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partTime, "Partial-Remote"));
        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);


        model.addAttribute("job", job);
        model.addAttribute("location", location);

        LocalDateTime searchDate=null;
        List<JobPostActivity> jobPost;
        boolean dateSearchFlag=true;
        boolean remote=true;
        boolean type=true;

        if(days30){
            searchDate=LocalDateTime.now().minusDays(30);
        }else if(days7){
            searchDate=LocalDateTime.now().minusDays(7);
        }
        else if (today){
            searchDate=LocalDateTime.now().minusHours(24);
        }
        else{
            dateSearchFlag=false;
        }

        if(partTime==null && fullTime==null && freelance ==null){
            partTime="Part-Time";
            fullTime = "Full-Time";
            freelance = "Freelance";
            remote=false;
        }

        if(officeOnly==null && remoteOnly==null && partialRemote==null){
            officeOnly="Office-Only";
             remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type=false;
        }


        if(!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost=jobPostActivityService.getAll();
        }else{
            jobPost=jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freelance),Arrays.asList(remoteOnly,officeOnly,partialRemote),searchDate);
        }


        Object currentUserProfile=usersService.getCurrentUserProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){    //just checking if the user is not an anonymous one
            String currentUserName=authentication.getName();
            model.addAttribute("username",currentUserName);
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                List<RecruiterJobsDto> recruiterJobs=jobPostActivityService.getRecruiterJobs(((RecruiterProfile)currentUserProfile).getUserAccountId());
                model.addAttribute("jobPost",recruiterJobs);
            }
            else{
                List<JobSeekerApply> jobSeekerApplyList=jobSeekerApplyService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
                List<JobSeekerSave> jobSeekerSaveList=jobSeekerSaveService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                System.out.println("Before modifying jobPost: " + jobPost);
                System.out.println("JobSeeker Apply List: " + jobSeekerApplyList);
                System.out.println("JobSeeker Save List: " + jobSeekerSaveList);


                boolean exist;
                boolean saved;
                for(JobPostActivity jobActivity : jobPost){
                    exist=false;
                    saved=false;
                    for(JobSeekerApply jobSeekerApply: jobSeekerApplyList){
                         if(Objects.equals(jobActivity.getJobPostId(),jobSeekerApply.getJob().getJobPostId())){
                             jobActivity.setIsActive(true);
                             exist=true;
                             break;
                         }
                    }

                    for(JobSeekerSave jobSeekerSave: jobSeekerSaveList){
                         if(Objects.equals(jobActivity.getJobPostId(),jobSeekerSave.getJob().getJobPostId())){
                             jobActivity.setIsSaved(true);
                             saved=true;
                             break;
                         }
                    }

                    if(!exist){
                        jobActivity.setIsActive(false);
                    }
                    if(!saved){
                        jobActivity.setIsSaved(false);
                    }


                    model.addAttribute("jobPost",jobPost);
                       System.out.println("After modifying jobPost: " + jobPost);
                }

            }
        }

        model.addAttribute("user",currentUserProfile);
        return "dashboard";
    }

     @GetMapping("global-search/")
    public String globalSearch(Model model,
                             @RequestParam(value="job",required = false)String job,
                             @RequestParam(value="location",required = false)String location,
                             @RequestParam(value="partTime",required = false)String partTime,
                             @RequestParam(value="fullTime",required = false)String fullTime,
                             @RequestParam(value="freeLance",required = false)String freeLance,
                             @RequestParam(value="remoteOnly",required = false)String remoteOnly,
                             @RequestParam(value="officeOnly",required = false)String officeOnly,
                             @RequestParam(value="partialRemote",required = false)String partialRemote,
                             @RequestParam(value="today",required = false)boolean today,
                             @RequestParam(value="days7",required = false)boolean days7,
                             @RequestParam(value="days30",required = false)boolean days30
                             ){

        model.addAttribute("job", job);
        model.addAttribute("location", location);
        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freeLance", Objects.equals(freeLance, "Freelance"));
        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));
        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        LocalDateTime searchDate=null;
        List<JobPostActivity> jobPost;
        boolean dateSearchFlag=true;
        boolean remote=true;
        boolean type=true;

        if(days30){
            searchDate=LocalDateTime.now().minusDays(30);
        }else if(days7){
            searchDate=LocalDateTime.now().minusDays(7);
        }
        else if (today){
            searchDate=LocalDateTime.now().minusHours(24);
        }
        else{
            dateSearchFlag=false;
        }

        if(partTime==null && fullTime==null && freeLance ==null){
            partTime="Part-Time";
            fullTime = "Full-Time";
            freeLance = "Freelance";
            remote=false;
        }

        if(officeOnly==null && remoteOnly==null && partialRemote==null){
            officeOnly="Office-Only";
             remoteOnly = "Remote-Only";
            partialRemote = "Partial-Remote";
            type=false;
        }


        if(!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost=jobPostActivityService.getAll();
        }else{
            jobPost=jobPostActivityService.search(job,location, Arrays.asList(partTime,fullTime,freeLance),Arrays.asList(remoteOnly,officeOnly,partialRemote),searchDate);
        }

        model.addAttribute("jobPost", jobPost);
        return "global-search";
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

    @GetMapping("/dashboard/edit/{id}")
    public String editJob(@PathVariable("id") Integer id,Model model){

        Optional<JobPostActivity> jobPostActivity=jobPostActivityService.addOne(id);
        model.addAttribute("jobPostActivity",jobPostActivity);
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }

}
