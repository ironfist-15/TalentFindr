package com.project.TalentFindr.Repository;

import com.project.TalentFindr.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Integer> {
}
