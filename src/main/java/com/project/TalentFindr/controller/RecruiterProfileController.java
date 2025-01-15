package com.project.TalentFindr.controller;


import java.util.Objects;
import java.util.Optional;

import com.project.TalentFindr.Repository.RecruiterProfileRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.RecruiterProfile;
import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.service.RecruiterProfileService;
import com.project.TalentFindr.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    @Autowired
    public UsersRepository usersRepository;
    public RecruiterProfileRepository recruiterProfileRepository;
    public RecruiterProfileService recruiterProfileService;

    public RecruiterProfileController(UsersRepository usersRepository,RecruiterProfileRepository recruiterProfileRepository,RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileRepository=recruiterProfileRepository;
        this.recruiterProfileService=recruiterProfileService;
    }

    @GetMapping("/")
    public String recruiterProfile(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername=authentication.getName();
            Users users=usersRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("Could not find"+"find user"));
            Optional<RecruiterProfile> recruiterProfile= recruiterProfileRepository.findById(users.getUserId());
            if(!recruiterProfile.isEmpty()){
                model.addAttribute("profile",recruiterProfile.get());
            }

        }
        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public String addNew(RecruiterProfile recruiterProfile,@RequestParam("image") MultipartFile multipartFile,Model model){
                Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find" + "find user"));
            recruiterProfile.setUserId(users);
            recruiterProfile.setUserAccountId(users.getUserId());
          }
        model.addAttribute("profile",recruiterProfile);
            String fileName="";
            if(!"".equals(multipartFile.getOriginalFilename())){
                fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                recruiterProfile.setProfilePhoto(fileName);
            }
            RecruiterProfile savedUser=recruiterProfileService.addNew(recruiterProfile);
            String uploadDir="D:/desktop2.0/jobportal/uploaded-files/photos/recruiter/"+savedUser.getUserAccountId();
            try{
                FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return "redirect:/dashboard/";
        }

}
