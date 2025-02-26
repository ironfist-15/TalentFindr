package com.project.TalentFindr.entity;



import java.time.LocalDateTime;


import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="job_post_activity")
public class JobPostActivity {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer jobPostId;

    @ManyToOne
    @JoinColumn(name="postedById",referencedColumnName = "userId")
    private Users postedById;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="jobLocationId",referencedColumnName = "Id")
    private JobLocation jobLocationId;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="jobCompanyId",referencedColumnName = "Id")
    private JobCompany jobCompanyId;

    @Length(max=10000)
    private String descriptionOfJob;

    @Transient
    private Boolean isActive;

    @Transient
    private Boolean isSaved;

    private String jobType;
    private String salary;

    private String remote;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime postedDate;

    private String jobTitle;

    public JobPostActivity() {
    }

   public JobPostActivity(Builder builder) {
        jobPostId = builder.jobPostId;
        postedById = builder.postedById;
        jobLocationId = builder.jobLocationId;
        jobCompanyId = builder.jobCompanyId;
        descriptionOfJob = builder.descriptionOfJob;
        isActive = builder.isActive;
        isSaved = builder.isSaved;
        jobType = builder.jobType;
        salary = builder.salary;
        remote = builder.remote;
        postedDate = builder.postedDate;
        jobTitle = builder.jobTitle;
    }

    public static class Builder {
        private Integer jobPostId;
        private Users postedById;
        private JobLocation jobLocationId;
        private JobCompany jobCompanyId;
        private String descriptionOfJob;
        private Boolean isActive;
        private Boolean isSaved;
        private String jobType;
        private String salary;
        private String remote;
        private LocalDateTime postedDate;
        private String jobTitle;

        public Builder setJobPostId(Integer jobPostId) {
            this.jobPostId = jobPostId;
            return this;
        }

        public Builder setPostedById(Users postedById) {
            this.postedById = postedById;
            return this;
        }

        public Builder setJobLocationId(JobLocation jobLocationId) {
            this.jobLocationId = jobLocationId;
            return this;
        }

        public Builder setJobCompanyId(JobCompany jobCompanyId) {
            this.jobCompanyId = jobCompanyId;
            return this;
        }

        public Builder setDescriptionOfJob(String descriptionOfJob) {
            this.descriptionOfJob = descriptionOfJob;
            return this;
        }

        public Builder setIsActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder setIsSaved(Boolean isSaved) {
            this.isSaved = isSaved;
            return this;
        }

        public Builder setJobType(String jobType) {
            this.jobType = jobType;
            return this;
        }

        public Builder setSalary(String salary) {
            this.salary = salary;
            return this;
        }

        public Builder setRemote(String remote) {
            this.remote = remote;
            return this;
        }

        public Builder setPostedDate(LocalDateTime postedDate) {
            this.postedDate = postedDate;
            return this;
        }

        public Builder setJobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public JobPostActivity build() {
            return new JobPostActivity(this);
        }
    }

    public Integer getJobPostId() {
        return jobPostId;
    }

    public void setJobPostId(Integer jobPostId) {
        this.jobPostId = jobPostId;
    }

    public Users getPostedById() {
        return postedById;
    }

    public void setPostedById(Users postedById) {
        this.postedById = postedById;
    }

    public JobLocation getJobLocationId() {
        return jobLocationId;
    }

    public void setJobLocationId(JobLocation jobLocationId) {
        this.jobLocationId = jobLocationId;
    }

    public JobCompany getJobCompanyId() {
        return jobCompanyId;
    }

    public void setJobCompanyId(JobCompany jobCompanyId) {
        this.jobCompanyId = jobCompanyId;
    }

    public @Length(max = 10000) String getDescriptionOfJob() {
        return descriptionOfJob;
    }

    public void setDescriptionOfJob(@Length(max = 10000) String descriptionOfJob) {
        this.descriptionOfJob = descriptionOfJob;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean isSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String toString() {
        return "JobPostActivity{" +
                "joPostId=" + jobPostId +
                ", postedById=" + postedById +
                ", jobLocationId=" + jobLocationId +
                ", jobCompanyId=" + jobCompanyId +
                ", descriptionOfJob='" + descriptionOfJob + '\'' +
                ", isActive=" + isActive +
                ", isSaved=" + isSaved +
                ", jobType='" + jobType + '\'' +
                ", salary='" + salary + '\'' +
                ", remote='" + remote + '\'' +
                ", postedDate=" + postedDate +
                ", jobTitle='" + jobTitle + '\'' +
                '}';
    }
}
