package com.project.TalentFindr.Repository;

import com.project.TalentFindr.entity.ChatThread;
import com.project.TalentFindr.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatThreadRepository extends JpaRepository<ChatThread,Integer> {

    ChatThread findByRecruiterAndJobSeeker(Users sender, Users receiver);
}
