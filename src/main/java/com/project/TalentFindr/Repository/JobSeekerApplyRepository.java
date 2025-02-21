package com.project.TalentFindr.Repository;


import java.util.List;

import com.project.TalentFindr.entity.JobPostActivity;
import com.project.TalentFindr.entity.JobSeekerApply;
import com.project.TalentFindr.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply,Integer> {

       List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

       List<JobSeekerApply> findByJob(JobPostActivity job);
}
