package com.example.immo.dto.responses;

import com.example.immo.models.Message;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseMessageDto {
    private Long messageId;
    private ResponseRentalDto rental;
    private ResponseUserDto user;
    private String message;

    public ResponseMessageDto(Message message) {
        super();
        this.messageId = message.getMessageId();
        this.message = message.getMessage();
        this.user = new ResponseUserDto(message.getUser());
        this.rental = new ResponseRentalDto(message.getRental());
    }
}