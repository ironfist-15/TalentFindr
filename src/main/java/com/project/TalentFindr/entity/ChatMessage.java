package com.project.TalentFindr.entity;


import jakarta.persistence.*;

@Entity
@Table(name="chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "thread_id",referencedColumnName = "id")
    private ChatThread  chatThread  ;

    @ManyToOne
    @JoinColumn(name="sender_id",referencedColumnName = "userId")
    private Users sender;

    private String message;

    public ChatMessage() {
    }

    public ChatMessage(Integer id, ChatThread chatThread, Users sender, String message) {
        this.id = id;
        this.chatThread = chatThread;
        this.sender = sender;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ChatThread getChatThread() {
        return chatThread;
    }

    public void setChatThread(ChatThread chatThread) {
        this.chatThread = chatThread;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", chatThread=" + chatThread +
                ", sender=" + sender +
                ", message='" + message + '\'' +
                '}';
    }
}
