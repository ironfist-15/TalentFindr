package com.project.TalentFindr.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.JobSeekerProfile;
import com.project.TalentFindr.entity.Skills;
import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.service.CandidateProfileService;
import com.project.TalentFindr.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("job-seeker-profile")
public class CandidateProfileController {

    @Autowired
    public CandidateProfileService candidateProfileService;
    public UsersRepository usersRepository;

    public CandidateProfileController(CandidateProfileService candidateProfileService, UsersRepository usersRepository) {
        this.candidateProfileService = candidateProfileService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("/")
    public String candidateprofile(Model model){
        JobSeekerProfile jobSeekerProfile=new JobSeekerProfile();
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
          Users user=  usersRepository.findByEmail(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("couldn't find user"));
            Optional<JobSeekerProfile> seekerProfile = candidateProfileService.addOne(user.getUserId());
            List<Skills> skills = new ArrayList<>();
            if(seekerProfile.isPresent()){
                jobSeekerProfile=seekerProfile.get();
                if(jobSeekerProfile.getSkills().isEmpty()){
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }
            model.addAttribute("skills",skills);
            model.addAttribute("profile",jobSeekerProfile);
        }
        return "job-seeker-profile";
    }

    @PostMapping("/addNew")
    public String addNew(Model model, JobSeekerProfile jobSeekerProfile, @RequestParam("image")MultipartFile image,@RequestParam("pdf")MultipartFile pdf) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("couldn't find user"));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        model.addAttribute("profile",jobSeekerProfile);
        List<Skills> skillsList = new ArrayList<>();
        model.addAttribute("skills",skillsList);

        String imageName="";
        String resume="";

        if(!Objects.equals(image.getOriginalFilename(),"")){
           imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }

        if(!Objects.equals(pdf.getOriginalFilename(),"")){
             resume = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resume);
        }

        JobSeekerProfile seekerProfile=candidateProfileService.addNew(jobSeekerProfile);
            try{
                String uploadDir="D:/desktop2.0/jobportal/uploaded-files/photos/candidate/"+jobSeekerProfile.getUserAccountId();
                if(!Objects.equals(image.getOriginalFilename(),"")){
                    FileUploadUtil.saveFile(uploadDir,imageName,image);
                }
                if(!Objects.equals(pdf.getOriginalFilename(),"")){
                    FileUploadUtil.saveFile(uploadDir,resume,pdf);
                }


            } catch (IOException ex){
                throw new RuntimeException();
            }



        return "redirect:/dashboard/";
    }

    @GetMapping("/{id}")
    public String candidateProfile(@PathVariable("id") int id,Model model){
        Optional<JobSeekerProfile> seekerProfile=candidateProfileService.getOne(id);
        model.addAttribute("profile",seekerProfile.get());
        return "job-seeker-profile";

    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value = "fileName") String fileName,@RequestParam(value = "userId") String userId){
        
    }

}
