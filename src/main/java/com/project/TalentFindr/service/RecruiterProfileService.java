package com.project.TalentFindr.service;


import java.util.Optional;

import com.project.TalentFindr.Repository.RecruiterProfileRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.RecruiterProfile;
import com.project.TalentFindr.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileService {

    @Autowired
    public RecruiterProfileRepository recruiterProfileRepository;
    public UsersRepository usersRepository;

    public RecruiterProfileService(RecruiterProfileRepository recruiterProfileRepository, UsersRepository usersRepository) {
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<RecruiterProfile> getOne(Integer id){
           return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }

    public RecruiterProfile getCurrentRecruiterProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            Users user=usersRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("couldn't find you"));
            Optional<RecruiterProfile> recruiterProfile=getOne(user.getUserId());
            return recruiterProfile.orElse(null);
        }else{
            return null;
        }
    }
}
