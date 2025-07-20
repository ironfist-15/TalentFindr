package com.project.TalentFindr.entity;


import jakarta.persistence.*;

@Entity
@Table(name="chat_thread")
public class ChatThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="recruiter_id",referencedColumnName = "userId")
    private Users recruiter;

    @ManyToOne
    @JoinColumn(name = "jobSeeker_id",referencedColumnName = "userId")
    private Users jobSeeker;

    public ChatThread() {
    }

    public ChatThread(Integer id, Users recruiter, Users jobSeeker) {
        this.id = id;
        this.recruiter = recruiter;
        this.jobSeeker = jobSeeker;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Users recruiter) {
        this.recruiter = recruiter;
    }

    public Users getJobSeeker() {
        return jobSeeker;
    }

    public void setJobSeeker(Users jobSeeker) {
        this.jobSeeker = jobSeeker;
    }

    @Override
    public String toString() {
        return "ChatThread{" +
                "id=" + id +
                ", recruiter=" + recruiter +
                ", jobSeeker=" + jobSeeker +
                '}';
    }
}
