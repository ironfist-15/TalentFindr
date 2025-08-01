package com.project.TalentFindr.controller;


import com.project.TalentFindr.Repository.ChatMessageRepository;
import com.project.TalentFindr.Repository.ChatThreadRepository;
import com.project.TalentFindr.Repository.UsersRepository;
import com.project.TalentFindr.entity.ChatMessage;
import com.project.TalentFindr.entity.ChatMessagesDto;
import com.project.TalentFindr.entity.ChatThread;
import com.project.TalentFindr.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("chat")
public class WebSocketController {

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private final ChatThreadRepository chatThreadRepo;

    @Autowired
    private final UsersRepository usersRepository;

    @Autowired
    private final ChatMessageRepository chatMessageRepo;

    public WebSocketController(SimpMessagingTemplate messagingTemplate, ChatThreadRepository chatThreadRepo, UsersRepository usersRepository, ChatMessageRepository chatMessageRepo) {
        this.messagingTemplate = messagingTemplate;
        this.chatThreadRepo = chatThreadRepo;
        this.usersRepository = usersRepository;
        this.chatMessageRepo = chatMessageRepo;
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
    @PostMapping("/thread")
    public ResponseEntity<?> createThread(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        Users sender = usersRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        Users receiver = usersRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));


        ChatThread existingThread = chatThreadRepo.findByRecruiterAndJobSeeker(sender, receiver);
        ChatThread existingReverseThread = chatThreadRepo.findByRecruiterAndJobSeeker(receiver, sender);
        if (existingThread != null || existingReverseThread != null) {
            return ResponseEntity.ok(
                existingThread != null ? existingThread.getId() : existingReverseThread.getId()
            );
        }
        if (!"Recruiter".equalsIgnoreCase(sender.getUserTypeId().getUserTypeName())) {
            return ResponseEntity.badRequest().body("Only recruiters can initiate chat");
        }

        // Create new thread
        ChatThread thread = new ChatThread();
        thread.setRecruiter(sender);
        thread.setJobSeeker(receiver);
        chatThreadRepo.save(thread);
        System.out.println("✅ Reached createThread endpoint: senderId=" + senderId + ", receiverId=" + receiverId);
        return ResponseEntity.ok(thread.getId());
    }

}
