package com.project.TalentFindr.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.luv2code.jobportal.util.FileUploadUtil;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.JobSeekerProfile;
import com.project.TalentFindr.entity.Skills;
import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.service.CandidateProfileService;
import com.project.TalentFindr.util.FileDownloadUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public String addNew(Model model, JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("pdf") MultipartFile pdf) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FileUploadUtil fileUploadUtil = new FileUploadUtil();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Users user = usersRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user"));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }

        model.addAttribute("profile", jobSeekerProfile);
        model.addAttribute("skills", new ArrayList<Skills>());

        boolean profileSaved = false;
        JobSeekerProfile savedProfile = null;

        // Handle image upload
        if (!image.isEmpty()) {
            String imageName = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }

        // Handle resume upload
        if (!pdf.isEmpty()) {
            String resumeName = StringUtils.cleanPath(Objects.requireNonNull(pdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

        // Save profile (once only, after setting image/resume names)
        savedProfile = candidateProfileService.addNew(jobSeekerProfile);
        profileSaved = true;

        // Save image to disk
        if (!image.isEmpty()) {
            String imageUploadDir = "D:/desktop2.0/jobportal/uploaded-files/photos/candidate/" + savedProfile.getUserAccountId();
            try {
                fileUploadUtil.saveFile(imageUploadDir, savedProfile.getProfilePhoto(), image);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Image upload failed");
            }
        }

        // Save resume to disk
        if (!pdf.isEmpty()) {
            String resumeUploadDir = "D:/desktop2.0/jobportal/uploaded-files/resumes/candidate/" + savedProfile.getUserAccountId();
            try {
                fileUploadUtil.saveFile(resumeUploadDir, savedProfile.getResume(), pdf);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Resume upload failed");
            }
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
    public ResponseEntity<?> downloadResume(@RequestParam("fileName") String fileName,
                                            @RequestParam("userID") String userId) {

        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource;

        try {

            String downloadDir = "D:/desktop2.0/jobportal/uploaded-files/resumes/candidate/" + userId;

            System.out.println("Looking for file: " + fileName + " in directory: " + downloadDir);

            resource = downloadUtil.getFileAsResource(downloadDir, fileName);

            if (resource == null || !resource.exists()) {
                System.out.println("File not found or doesn't exist: " + fileName);
                return ResponseEntity.status(404).body("Resume not found");
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace(); // 👈 add this
            return ResponseEntity.badRequest().body("Resume could not be found or accessed.");
        }
    }

}
