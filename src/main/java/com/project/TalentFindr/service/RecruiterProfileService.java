package com.project.TalentFindr.service;


import java.util.Optional;

import com.project.TalentFindr.Repository.RecruiterProfileRepository;
import com.project.TalentFindr.entity.RecruiterProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruiterProfileService {

    @Autowired
    public RecruiterProfileRepository recruiterProfileRepository;

    public Optional<RecruiterProfile> getOne(Integer id){
           return recruiterProfileRepository.findById(id);
    }

    public RecruiterProfile addNew(RecruiterProfile recruiterProfile) {
        return recruiterProfileRepository.save(recruiterProfile);
    }
}
