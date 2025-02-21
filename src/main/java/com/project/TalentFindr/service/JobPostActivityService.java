package com.project.TalentFindr.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.project.TalentFindr.Repository.JobPostActivityRepository;
import com.project.TalentFindr.entity.*;
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

    public List<RecruiterJobsDto> getRecruiterJobs(int recruiter){
          List<IRecruiterJobs> recruiterJobsDtos=jobPostActivityRepository.getRecruiterJobs(recruiter);
          List<RecruiterJobsDto> recruiterJobsDtoList=new ArrayList<>();
          for(IRecruiterJobs rec :recruiterJobsDtos){
              JobLocation loc=new JobLocation(rec.getLocation_id(), rec.getCity(),rec.getState(), rec.getCountry());
              JobCompany comp=new JobCompany(rec.getCompanyId(),"",rec.getName());
              recruiterJobsDtoList.add(new RecruiterJobsDto(rec.getTotalCandidates(),rec.getJob_post_id(), rec.getJob_title(), loc,comp));
          }
          return recruiterJobsDtoList;
    }

    public Optional<JobPostActivity> addOne(Integer id){
        return jobPostActivityRepository.findById(id);
    }

    public List<JobPostActivity> search(String job, String location, List<String> remote, List<String> type, LocalDateTime searchDate) {
        if(Objects.isNull(searchDate)){
            return jobPostActivityRepository.searchWithoutDate(job,location,remote,type);
        }
        return jobPostActivityRepository.search(job,location,remote,type,searchDate);
    }

    public List<JobPostActivity> getAll() {
        return jobPostActivityRepository.findAll();
    }

    public JobPostActivity getOne(int id) {

        return jobPostActivityRepository.findById(id).orElseThrow(()->new RuntimeException("Job not found"));

    }
}
