package com.project.TalentFindr.entity;



import jakarta.persistence.*;


@Entity
@Table(name = "recruiter_profile")
public class RecruiterProfile {

    @Id
    private int userAccountId;

    @OneToOne
    @JoinColumn(name="user_account_id")
    @MapsId
    private Users userId;

    private String city;

    private String country;

    private String company;

    private String firstName;

    private String lastName;

    @Column(nullable = true,length = 64)
    private String profilePhoto;

    private String state;

    public RecruiterProfile() {
    }

    public RecruiterProfile(Users userId) {
        this.userId = userId;
    }




    // i have used builder class because too many parameters(9) in constructor

    public RecruiterProfile(Builder builder) {
        this.userAccountId = builder.userAccountId;
        this.userId = builder.userId;
        this.city = builder.city;
        this.country = builder.country;
        this.company = builder.company;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.profilePhoto = builder.profilePhoto;
        this.state = builder.state;
    }




    public static class Builder {
        private int userAccountId;
        private Users userId;
        private String city;
        private String country;
        private String company;
        private String firstName;
        private String lastName;
        private String profilePhoto;
        private String state;

        public Builder setUserAccountId(int userAccountId) {
            this.userAccountId = userAccountId;
            return this;
        }

        public Builder setUserId(Users userId) {
            this.userId = userId;
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

        public Builder setCompany(String company) {
            this.company = company;
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

        public Builder setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public RecruiterProfile build() {
            return new RecruiterProfile(this);
        }
    }

    public int getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(int userAccountId) {
        this.userAccountId = userAccountId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto==null)return null;
        return "D:/desktop2.0/jobportal/uploaded-files/photos/recruiter/"+userAccountId+"/"+profilePhoto;

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RecruiterProfile{" +
                "userAccountId=" + userAccountId +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
