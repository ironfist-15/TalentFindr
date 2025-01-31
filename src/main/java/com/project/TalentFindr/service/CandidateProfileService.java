package com.project.TalentFindr.service;

import java.util.Optional;

import com.project.TalentFindr.Repository.CandidateProfileRepository;
import com.project.TalentFindr.entity.JobSeekerProfile;
import org.springframework.stereotype.Service;

@Service
public class CandidateProfileService {

    public CandidateProfileRepository candidateProfileRepository;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
    }

    public Optional<JobSeekerProfile> addOne(Integer id){
        return candidateProfileRepository.findById(id);
    }

    public JobSeekerProfile addNew(JobSeekerProfile jobSeekerProfile){
         return candidateProfileRepository.save(jobSeekerProfile);
    }
}
