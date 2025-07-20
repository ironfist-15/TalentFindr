package com.project.TalentFindr.controller;



import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
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
                             @RequestParam(value = "job", required = false) String job,
                             @RequestParam(value = "location", required = false) String location,
                             @RequestParam(value = "partTime", required = false) String partTime,
                             @RequestParam(value = "fullTime", required = false) String fullTime,
                             @RequestParam(value = "freelance", required = false) String freelance,
                             @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                             @RequestParam(value = "officeOnly", required = false) String officeOnly,
                             @RequestParam(value = "partialRemote", required = false) String partialRemote,
                             @RequestParam(value = "today", required = false, defaultValue = "false") boolean today,
                             @RequestParam(value = "days7", required = false, defaultValue = "false") boolean days7,
                             @RequestParam(value = "days30", required = false, defaultValue = "false") boolean days30) {

        // Bind form state to view
        model.addAttribute("partTime", Objects.equals(partTime, "Part-Time"));
        model.addAttribute("fullTime", Objects.equals(fullTime, "Full-Time"));
        model.addAttribute("freelance", Objects.equals(freelance, "Freelance"));
        model.addAttribute("remoteOnly", Objects.equals(remoteOnly, "Remote-Only"));
        model.addAttribute("officeOnly", Objects.equals(officeOnly, "Office-Only"));
        model.addAttribute("partialRemote", Objects.equals(partialRemote, "Partial-Remote"));
        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);
        model.addAttribute("job", job);
        model.addAttribute("location", location);

        // Default fallback
        job = (job == null || job.trim().isEmpty()) ? "%" : job.trim();
        location = (location == null || location.trim().isEmpty()) ? "%" : location.trim();

        // Determine search date
        LocalDateTime searchDate = null;
        if (days30) searchDate = LocalDateTime.now().minusDays(30);
        else if (days7) searchDate = LocalDateTime.now().minusDays(7);
        else if (today) searchDate = LocalDateTime.now().minusHours(24);

        // Build type list
        List<String> typeList = new ArrayList<>();
        if (partTime != null) typeList.add("Part-Time");
        if (fullTime != null) typeList.add("Full-Time");
        if (freelance != null) typeList.add("Freelance");
        if (typeList.isEmpty()) {
            typeList = List.of("Part-Time", "Full-Time", "Freelance");
        }

        // Build remote list
        List<String> remoteList = new ArrayList<>();
        if (remoteOnly != null) remoteList.add("Remote-Only");
        if (officeOnly != null) remoteList.add("Office-Only");
        if (partialRemote != null) remoteList.add("Partial-Remote");
        if (remoteList.isEmpty()) {
            remoteList = List.of("Remote-Only", "Office-Only", "Partial-Remote");
        }
        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (!(authentication instanceof AnonymousAuthenticationToken)) {
             String currentUsername = authentication.getName();
             model.addAttribute("username", currentUsername);
             if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
                 List<RecruiterJobsDto> recruiterJobs = jobPostActivityService.getRecruiterJobs(((RecruiterProfile) currentUserProfile).getUserAccountId());
                 model.addAttribute("jobPost", recruiterJobs);
             } else {
                 List<JobSeekerApply> applied = jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                 List<JobSeekerSave> saved = jobSeekerSaveService.getCandidatesJob((JobSeekerProfile) currentUserProfile);
                 // Fetch jobs
                 List<JobPostActivity> jobPost;
                 if (isNoFilter(job, location, searchDate, typeList, remoteList)) {
                     jobPost = jobPostActivityService.getAll();
                 } else {
                     jobPost = jobPostActivityService.search(job, location, remoteList, typeList, searchDate);
                 }

                 // Annotate applied/saved

                 for (JobPostActivity jobActivity : jobPost) {
                     boolean isApplied = applied.stream()
                             .anyMatch(a -> a.getJob().getJobPostId().equals(jobActivity.getJobPostId()));
                     boolean isSaved = saved.stream()
                             .anyMatch(s -> s.getJob().getJobPostId().equals(jobActivity.getJobPostId()));

                     jobActivity.setIsActive(isApplied);
                     jobActivity.setIsSaved(isSaved);
                 }

                 model.addAttribute("jobPost", jobPost);
          }
         }

        model.addAttribute("user", currentUserProfile);

        return "dashboard";
    }

    private boolean isNoFilter(String job, String location, LocalDateTime searchDate,
                           List<String> typeList, List<String> remoteList) {
        return "%".equals(job)
                && "%".equals(location)
                && searchDate == null
                && typeList.containsAll(List.of("Part-Time", "Full-Time", "Freelance"))
                && remoteList.containsAll(List.of("Remote-Only", "Office-Only", "Partial-Remote"));
    }


    private static boolean isaBoolean(String job, String location, String partTime, String fullTime, String freelance, String remoteOnly, String officeOnly, String partialRemote, LocalDateTime searchDate) {
        return job == null && location == null && searchDate == null &&
                partTime != null && fullTime != null && freelance != null &&
                remoteOnly != null && officeOnly != null && partialRemote != null;
    }

    @GetMapping("global-search/")
    public String globalSearch(Model model,
                               @RequestParam(value = "job", required = false) String job,
                               @RequestParam(value = "location", required = false) String location,
                               @RequestParam(value = "partTime", required = false) String partTime,
                               @RequestParam(value = "fullTime", required = false) String fullTime,
                               @RequestParam(value = "freeLance", required = false) String freeLance,
                               @RequestParam(value = "remoteOnly", required = false) String remoteOnly,
                               @RequestParam(value = "officeOnly", required = false) String officeOnly,
                               @RequestParam(value = "partialRemote", required = false) String partialRemote,
                               @RequestParam(value = "today", required = false, defaultValue = "false") boolean today,
                               @RequestParam(value = "days7", required = false, defaultValue = "false") boolean days7,
                               @RequestParam(value = "days30", required = false, defaultValue = "false") boolean days30) {

        // Preserve filter states in model
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

        // Normalize job/location to allow broad matches
        job = (job == null || job.trim().isEmpty()) ? "%" : job.trim();
        location = (location == null || location.trim().isEmpty()) ? "%" : location.trim();

        // Date filter
        LocalDateTime searchDate = null;
        if (days30) searchDate = LocalDateTime.now().minusDays(30);
        else if (days7) searchDate = LocalDateTime.now().minusDays(7);
        else if (today) searchDate = LocalDateTime.now().minusHours(24);

        // Build job type list
        List<String> typeList = new ArrayList<>();
        if (partTime != null) typeList.add("Part-Time");
        if (fullTime != null) typeList.add("Full-Time");
        if (freeLance != null) typeList.add("Freelance");
        if (typeList.isEmpty()) {
            typeList = List.of("Part-Time", "Full-Time", "Freelance");
        }

        // Build remote list
        List<String> remoteList = new ArrayList<>();
        if (remoteOnly != null) remoteList.add("Remote-Only");
        if (officeOnly != null) remoteList.add("Office-Only");
        if (partialRemote != null) remoteList.add("Partial-Remote");
        if (remoteList.isEmpty()) {
            remoteList = List.of("Remote-Only", "Office-Only", "Partial-Remote");
        }

        // Search
        List<JobPostActivity> jobPost;
        if ("%".equals(job) && "%".equals(location) && searchDate == null &&
            typeList.containsAll(List.of("Part-Time", "Full-Time", "Freelance")) &&
            remoteList.containsAll(List.of("Remote-Only", "Office-Only", "Partial-Remote"))) {
            jobPost = jobPostActivityService.getAll();
        } else {
            jobPost = jobPostActivityService.search(job, location, remoteList, typeList, searchDate);
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

         JobPostActivity saved = jobPostActivityService.addNew(jobPostActivity);
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
