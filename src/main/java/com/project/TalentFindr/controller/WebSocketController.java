package com.project.TalentFindr.controller;


import java.util.List;

import com.project.TalentFindr.Repository.ChatMessageRepository;
import com.project.TalentFindr.Repository.ChatThreadRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.ChatMessage;
import com.project.TalentFindr.entity.ChatMessagesDto;
import com.project.TalentFindr.entity.ChatThread;
import com.project.TalentFindr.entity.Users;
import com.project.TalentFindr.service.ChatHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/chat")
public class WebSocketController {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final ChatThreadRepository chatThreadRepo;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final ChatMessageRepository chatMessageRepo;

    @Autowired
    private final ChatHelperService chatHelperService;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, ChatThreadRepository chatThreadRepo, UsersRepository usersRepository, ChatMessageRepository chatMessageRepo, ChatHelperService chatHelperService) {
        this.messagingTemplate = messagingTemplate;
        this.chatThreadRepo = chatThreadRepo;
        this.usersRepository = usersRepository;
        this.chatMessageRepo = chatMessageRepo;
        this.chatHelperService = chatHelperService;
    }

    @MessageMapping("/send")  // Clients send message to /app/chat/send. this is only for client to send to server
    public void sendMessage(@Payload ChatMessagesDto messageDTO) {
        // Fetch sender and thread from DB
        Users sender = usersRepository.findById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ChatThread thread = chatThreadRepo.findById(messageDTO.getThreadId())
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        // Save the message to DB
        ChatMessage message = new ChatMessage();
        message.setMessage(messageDTO.getMessage());
        message.setSender(sender);
        message.setChatThread(thread);
        chatMessageRepo.save(message);

        // Broadcast to subscribers of /topic/thread/{threadId}
        messagingTemplate.convertAndSend(
                "/topic/thread/" + thread.getId(),
                messageDTO
        );
    }

    // frontend first comes here to get the thread id and then goes to above method to send payload
    //this chaecks if  a  thread exists between the sender and receiver,
    // if it exists returns that else creates a new one only if the sender is a recruiter
    @PostMapping("/thread/new")
    public String createThread(@RequestParam Integer receiverId, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Users users = usersRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
        Integer senderId=users.getUserId();
        Users sender = usersRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = usersRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Check for existing thread (both directions)
        ChatThread existingThread = chatThreadRepo.findByRecruiterAndJobSeeker(sender, receiver);
        ChatThread existingReverseThread = chatThreadRepo.findByRecruiterAndJobSeeker(receiver, sender);

        ChatThread thread;
        if (existingThread != null || existingReverseThread != null) {
            thread = (existingThread != null) ? existingThread : existingReverseThread;
        } else {
            if (!"Recruiter".equalsIgnoreCase(sender.getUserTypeId().getUserTypeName())) {
                model.addAttribute("error", "Only recruiters can initiate chat");
                return "errorPage"; // use your custom error page here
            }

            // Create new thread
            thread = new ChatThread();
            thread.setRecruiter(sender);
            thread.setJobSeeker(receiver);
            chatThreadRepo.save(thread);
        }

        model.addAttribute("threadId", thread.getId());
        model.addAttribute("senderId", senderId);
        model.addAttribute("receiverId", receiverId);
        return "redirect:/chat/thread/" + thread.getId();
    }


    @GetMapping("/thread/{id}")
    public String getMessages(@PathVariable Integer id,Model model){
        List<ChatMessage> messages = chatMessageRepo.findByChatThread_Id(id); // ensure correct method
        List<ChatMessagesDto> dtos = chatHelperService.messagetoDto(messages);
        model.addAttribute("chat_history",dtos);
        model.addAttribute("ThreadId", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Users users = usersRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Could not find user"));
        Integer currentUserId=users.getUserId();
        model.addAttribute("currentUserId", currentUserId);
        return "chat";
    }

}
