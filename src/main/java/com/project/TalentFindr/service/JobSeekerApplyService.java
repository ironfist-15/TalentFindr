package com.project.TalentFindr.service;


import java.util.List;

import com.project.TalentFindr.Repository.JobSeekerApplyRepository;
import com.project.TalentFindr.entity.JobPostActivity;
import com.project.TalentFindr.entity.JobSeekerApply;
import com.project.TalentFindr.entity.JobSeekerProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerApplyService {

    public JobSeekerApplyRepository jobSeekerApplyRepository;


    @Autowired
    public JobSeekerApplyService(JobSeekerApplyRepository jobSeekerApplyRepository) {
        this.jobSeekerApplyRepository = jobSeekerApplyRepository;
    }

    public List<JobSeekerApply> getCandidatesJob(JobSeekerProfile userAccountId){
        return jobSeekerApplyRepository.findUserId(userAccountId);
    }

    public List<JobSeekerApply> getJobCandidates(JobPostActivity job){
        return jobSeekerApplyRepository.findByJob(job);
    }
}
