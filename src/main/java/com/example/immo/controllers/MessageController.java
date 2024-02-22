package com.example.immo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<ReturnableMessageDto> messages = messageService.getReturnableMessages();
            return new ResponseEntity<>(messages, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Message.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/message/{id}")
    public ResponseEntity<?> getMessage(@PathVariable("id") final Long id) {
        try {
            ReturnableMessageDto message = messageService.getReturnableMessage(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Message.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/message")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.saveMessage(message);
            return new ResponseEntity<>(new ReturnableMessageDto(createdMessage), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>("Can't create the target Message.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/message/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") final Long id) {
        try {
            messageService.deleteMessage(id);
            return new ResponseEntity<String>("Message deleted.", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Message.", HttpStatus.NOT_FOUND);
        }
    }
}
