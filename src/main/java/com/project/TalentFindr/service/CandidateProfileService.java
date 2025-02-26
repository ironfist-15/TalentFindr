package com.project.TalentFindr.service;

import java.util.Optional;

import com.project.TalentFindr.Repository.CandidateProfileRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.JobSeekerProfile;
import com.project.TalentFindr.entity.RecruiterProfile;
import com.project.TalentFindr.entity.Users;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CandidateProfileService {

    public CandidateProfileRepository candidateProfileRepository;
    public UsersRepository usersRepository;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository, UsersRepository usersRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.usersRepository = usersRepository;
    }

    public Optional<JobSeekerProfile> addOne(Integer id){
        return candidateProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile){
         return candidateProfileRepository.save(jobSeekerProfile);
    }

     public JobSeekerProfile getCurrentSeekerProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String userName = authentication.getName();
            Users user=usersRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("couldn't find you"));
            Optional<JobSeekerProfile> jobSeekerProfile=getOne(user.getUserId());
            return jobSeekerProfile.orElse(null);
        }else{
            return null;
        }
    }

    public  Optional<JobSeekerProfile> getOne(int userId) {
        return candidateProfileRepository.findById(userId);
    }
}
