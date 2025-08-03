package com.project.TalentFindr.Repository;

import java.util.List;
import com.project.TalentFindr.entity.ChatMessage;
import com.project.TalentFindr.entity.ChatThread;
import com.project.TalentFindr.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatThreadRepository extends JpaRepository<ChatThread,Integer> {

    ChatThread findByRecruiterAndJobSeeker(Users sender, Users receiver);

     List<ChatThread> findByRecruiter_UserIdOrJobSeeker_UserId(Integer userId1, Integer userId2);
}




