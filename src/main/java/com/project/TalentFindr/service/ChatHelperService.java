package com.project.TalentFindr.service;


import java.util.ArrayList;
import java.util.List;

import com.project.TalentFindr.Repository.ChatThreadRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.ChatMessage;
import com.project.TalentFindr.entity.ChatMessagesDto;
import com.project.TalentFindr.entity.ChatThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatHelperService {

    @Autowired
    public ChatThreadRepository chatThreadRepository;
    public UsersService usersService;

    public ChatHelperService(ChatThreadRepository chatThreadRepository, UsersService usersService) {
        this.chatThreadRepository = chatThreadRepository;
        this.usersService = usersService;
    }

    public List<ChatMessagesDto> messagetoDto(List<ChatMessage> RawMessages){
        List<ChatMessagesDto> result=new ArrayList<>();
        for(ChatMessage rawMessage : RawMessages){
            ChatMessagesDto chatMessagedto=new ChatMessagesDto(rawMessage.getChatThread().getId(), rawMessage.getSender().getUserId(), rawMessage.getMessage());
            result.add(chatMessagedto);
        }
        return result;
    }

    public List<ChatThread> chatThreadList(){
        return chatThreadRepository.findByRecruiter_UserIdOrJobSeeker_UserId(usersService.getCurrentUser().getUserId(),usersService.getCurrentUser().getUserId());
    }
}
