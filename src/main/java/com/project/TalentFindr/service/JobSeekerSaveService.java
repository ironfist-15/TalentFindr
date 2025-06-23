package com.project.TalentFindr.service;


import java.util.List;

import com.project.TalentFindr.Repository.JobSeekerSaveRepository;
import com.project.TalentFindr.entity.JobPostActivity;
import com.project.TalentFindr.entity.JobSeekerProfile;
import com.project.TalentFindr.entity.JobSeekerSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerSaveService {

    @Autowired
    public JobSeekerSaveRepository jobSeekerSaveRepository;

    public JobSeekerSaveService(JobSeekerSaveRepository jobSeekerSaveRepository) {
        this.jobSeekerSaveRepository = jobSeekerSaveRepository;
    }

    public List<JobSeekerSave> getCandidatesJob(JobSeekerProfile userAccountId){
        return  jobSeekerSaveRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerSave> getJobsCandidates(JobPostActivity job){
        return  jobSeekerSaveRepository.findByJob(job);
    }


    public void addNew(JobSeekerSave jobSeekerSave) {
        jobSeekerSaveRepository.save(jobSeekerSave);
    }
}
