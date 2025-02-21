package com.project.TalentFindr.Repository;

import java.util.List;

import com.project.TalentFindr.entity.JobPostActivity;
import com.project.TalentFindr.entity.JobSeekerProfile;
import com.project.TalentFindr.entity.JobSeekerSave;
import org.springframework.data.jpa.repository.JpaRepository;



public interface JobSeekerSaveRepository extends JpaRepository<JobSeekerSave,Integer> {

    List<JobSeekerSave> findByUserId(JobSeekerProfile userAccountId);

    List<JobSeekerSave> findByJob(JobPostActivity job);
}
