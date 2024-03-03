package com.example.immo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.immo.dto.payloads.PayloadMessageDto;
import com.example.immo.dto.responses.ResponseDefaultDto;
import com.example.immo.dto.responses.ResponseMessageDto;
import com.example.immo.models.Message;
import com.example.immo.models.Rental;
import com.example.immo.models.User;
import com.example.immo.services.MessageService;
import com.example.immo.services.RentalService;
import com.example.immo.services.UserService;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    // Create a new Message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody PayloadMessageDto message) {
        try {
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Access");
            }
            User user = userService.getUser(message.getUser_id());
            Rental rental = rentalService.getRental(message.getRental_id());
            Message newMessage = Message.builder().message(message.getMessage()).user(user)
                    .rental(rental).build();
            messageService.saveMessage(newMessage);
            return new ResponseEntity<ResponseDefaultDto>(new ResponseDefaultDto("Message sent with success"),
                    HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>("Can't create the target Message.", HttpStatus.BAD_REQUEST);
        }
    }

    // Not Required
    @GetMapping("/messages")
    public ResponseEntity<?> getMessages() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Iterable<ResponseMessageDto> messages = messageService.getReturnableMessages();
            return new ResponseEntity<>(messages, headers, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find any Message.", HttpStatus.NOT_FOUND);
        }
    }

    // Not Required
    @GetMapping("/message/{id}")
    public ResponseEntity<?> getMessage(@PathVariable("id") final Long id) {
        try {
            ResponseMessageDto message = messageService.getReturnableMessage(id);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Message.", HttpStatus.NOT_FOUND);
        }
    }

    // Not Required
    @DeleteMapping("/message/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") final Long id) {
        try {
            messageService.deleteMessage(id);
            return new ResponseEntity<String>("Message deleted.", HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>("Can't find the requested Message.",
                    HttpStatus.NOT_FOUND);
        }
    }

}
