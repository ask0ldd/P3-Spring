package com.example.immo.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.immo.dto.responses.ResponseMessageDto;
import com.example.immo.exceptions.UserNotFoundException;
import com.example.immo.models.Message;
import com.example.immo.repositories.MessageRepository;
import com.example.immo.services.interfaces.IMessageService;

@Service
public class MessageService implements IMessageService {

    @Autowired
    MessageRepository messageRepository;

    public Iterable<Message> getMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        return messages;
    }

    public Iterable<ResponseMessageDto> getReturnableMessages() {
        Iterable<Message> messages = messageRepository.findAll();
        if (!messages.iterator().hasNext())
            throw new UserNotFoundException("No message can be found.");
        Iterable<ResponseMessageDto> returnableMessages = StreamSupport.stream(messages.spliterator(), false)
                .map(message -> {
                    return new ResponseMessageDto(message);
                })
                .collect(Collectors.toList());
        return returnableMessages;
    }

    public Message getMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message cannot be found."));
        return message;
    }

    public ResponseMessageDto getReturnableMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message cannot be found."));
        return new ResponseMessageDto(message);
    }

    public Message saveMessage(Message message) {
        return Optional.of(messageRepository.save(message))
                .orElseThrow(() -> new RuntimeException("Failed to save the message."));
        // should verify si message.rental_id & message.user_id exists
        /*
         * try {
         * Message savedMessage = messageRepository.save(message);
         * return savedMessage;
         * } catch (Exception e) {
         * throw new RuntimeException("Failed to save message: " + e.getMessage());
         * }
         */
    }

    public void deleteMessage(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Target message cannot be found."));
        messageRepository.delete(message);
    }
}