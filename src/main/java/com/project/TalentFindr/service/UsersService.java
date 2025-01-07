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
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    public UsersRepository usersRepository;
    public RecruiterProfileRepository recruiterProfileRepository;
    public JobSeekerRepository jobSeekerRepository;


    public UsersService(UsersRepository usersRepository, RecruiterProfileRepository recruiterProfileRepository, JobSeekerRepository jobSeekerRepository) {
        this.usersRepository = usersRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    public Users addUser(Users user){
        user.setActive(true);
        user.setRegistrationDate(LocalDateTime.now());
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

}
