package com.project.TalentFindr.Repository;

import java.util.List;

import com.project.TalentFindr.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Integer> {

    List<ChatMessage> findByChatThread_Id(Integer threadId);
}
