package com.project.TalentFindr.service;


import com.project.TalentFindr.Repository.JobPostActivityRepository;
import com.project.TalentFindr.entity.JobPostActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobPostActivityService {

    @Autowired
    public JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivityService(JobPostActivityRepository jobPostActivityRepository) {
        this.jobPostActivityRepository = jobPostActivityRepository;
    }

    public String add(JobPostActivity jobPostActivity){
        jobPostActivityRepository.save(jobPostActivity);
        return "saved";
    }

}
