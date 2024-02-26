package com.example.immo.services;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.immo.dto.ReturnableMessageDto;
import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.Message;
import com.example.immo.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        return messages;
    }

    public Iterable<ReturnableMessageDto> getReturnableMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        Iterable<ReturnableMessageDto> returnableMessages = StreamSupport.stream(messages.spliterator(), false)
                .map(message -> {
                    return new ReturnableMessageDto(message);
                })
                .collect(Collectors.toList());
        return returnableMessages;
    }

    public Message getMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message can't be found."));
        return message;
    }

    public ReturnableMessageDto getReturnableMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message can't be found."));
        return new ReturnableMessageDto(message);
    }

    public Message saveMessage(Message message) {
        Message savedMessage = messageRepository.save(message);
        return savedMessage;
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message can't be found."));
        messageRepository.delete(message);
    }
}