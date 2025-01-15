package com.project.TalentFindr.service;

//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import java.time.LocalDateTime;
import java.util.Optional;

import com.project.TalentFindr.Repository.JobSeekerRepository;
import com.project.TalentFindr.Repository.RecruiterProfileRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.JobSeekerProfile;
import com.project.TalentFindr.entity.RecruiterProfile;
import com.project.TalentFindr.entity.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    public UsersRepository usersRepository;
    public RecruiterProfileRepository recruiterProfileRepository;
    public JobSeekerRepository jobSeekerRepository;
    public PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, RecruiterProfileRepository recruiterProfileRepository, JobSeekerRepository jobSeekerRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users addUser(Users user){
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users saveduser=usersRepository.save(user);
        int saveduserid= user.getUserTypeId().getUserTypeId();
        if(saveduserid==1){
            recruiterProfileRepository.save(new RecruiterProfile(saveduser));
        }
        else{
            jobSeekerRepository.save(new JobSeekerProfile(saveduser));
        }

        return saveduser;
    }

    public Optional<Users> getuserbyEmailid(String email){
        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String userName= authentication.getName();
            Users users=usersRepository.findByEmail(userName).orElseThrow(()->new UsernameNotFoundException("couldn't find you"));
            int userId=users.getUserId();
            if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))){
                return recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            }
            else{
                return jobSeekerRepository.findById(userId).orElse(new JobSeekerProfile());
            }
        }
        return null;
    }
}
