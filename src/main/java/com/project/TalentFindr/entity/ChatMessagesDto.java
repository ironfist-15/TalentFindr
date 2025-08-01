package com.project.TalentFindr.entity;

public class ChatMessagesDto {

    private Integer ThreadId;
    private Integer SenderId;
    private String message;

    public ChatMessagesDto(Integer threadId, Integer senderId, String message) {
        ThreadId = threadId;
        SenderId = senderId;
        this.message = message;
    }

    public Integer getThreadId() {
        return ThreadId;
    }

    public void setThreadId(Integer threadId) {
        ThreadId = threadId;
    }

    public Integer getSenderId() {
        return SenderId;
    }

    public void setSenderId(Integer senderId) {
        SenderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
