package com.example.immo.services.interfaces;

import com.example.immo.dto.responses.ResponseMessageDto;
import com.example.immo.models.Message;

public interface IMessageService {
    Iterable<Message> getMessages();

    Iterable<ResponseMessageDto> getReturnableMessages();

    Message getMessage(Long id);

    ResponseMessageDto getReturnableMessage(Long id);

    Message saveMessage(Message message);

    void deleteMessage(Long id);
}
