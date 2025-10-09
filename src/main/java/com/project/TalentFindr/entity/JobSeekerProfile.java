package com.project.TalentFindr.entity;


import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="job_seeker_profile")
public class JobSeekerProfile {

    @Id
    private Integer userAccountId;

    @OneToOne
    @JoinColumn(name="user_account_id")
    @MapsId
    private Users userId;

    private String firstName;

    private String lastName;
    private String city;
    private String country;
    private String state;
    private String workAuthorization;
    private String resume;

    @Column(nullable = true,length = 64)
    private String profilePhoto;
    private String employmentType;

    @OneToMany(targetEntity =Skills.class,cascade = CascadeType.ALL,mappedBy="jobSeekerProfile")
    private List<Skills> skills;

    public JobSeekerProfile() {
    }

    public JobSeekerProfile(Users userId) {
        this.userId = userId;
    }

    private JobSeekerProfile(Builder builder) {
        this.userAccountId = builder.userAccountId;
        this.userId = builder.userId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.city = builder.city;
        this.country = builder.country;
        this.state = builder.state;
        this.workAuthorization = builder.workAuthorization;
        this.resume = builder.resume;
        this.profilePhoto = builder.profilePhoto;
        this.employmentType = builder.employmentType;
        this.skills = builder.skills;
    }


    public static class Builder {
        private Integer userAccountId;
        private Users userId;
        private String firstName;
        private String lastName;
        private String city;
        private String country;
        private String state;
        private String workAuthorization;
        private String resume;
        private String profilePhoto;
        private String employmentType;
        private List<Skills> skills;

        public Builder setUserAccountId(Integer userAccountId) {
            this.userAccountId = userAccountId;
            return this;
        }

        public Builder setUserId(Users userId) {
            this.userId = userId;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setWorkAuthorization(String workAuthorization) {
            this.workAuthorization = workAuthorization;
            return this;
        }

        public Builder setResume(String resume) {
            this.resume = resume;
            return this;
        }

        public Builder setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
            return this;
        }

        public Builder setEmploymentType(String employmentType) {
            this.employmentType = employmentType;
            return this;
        }

        public Builder setSkills(List<Skills> skills) {
            this.skills = skills;
            return this;
        }

        public JobSeekerProfile build() {
            return new JobSeekerProfile(this);
        }
    }

    public Integer getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(Integer userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWorkAuthorization() {
        return workAuthorization;
    }

    public void setWorkAuthorization(String workAuthorization) {
        this.workAuthorization = workAuthorization;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public List<Skills> getSkills() {
        return skills;
    }

    public void setSkills(List<Skills> skills) {
        this.skills = skills;
    }


    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto==null || userAccountId==null)
            return null;
        return "/photos/candidate/"+userAccountId+"/"+profilePhoto;
     }

    @Transient
    public String getProfilePhotoUrl() {
        if (profilePhoto == null || userAccountId == 0) return null;
        return userAccountId+"_CandidateImage_" +profilePhoto;
    }

    @Override
    public String toString() {
        return "JobSeekerProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", workAuthorization='" + workAuthorization + '\'' +
                ", resume='" + resume + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", employmentType='" + employmentType + '\'' +
                '}';
    }
}
