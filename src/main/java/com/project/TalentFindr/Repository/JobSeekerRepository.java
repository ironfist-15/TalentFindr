package com.project.TalentFindr.Repository;

import com.project.TalentFindr.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeekerProfile,Integer> {

}
