package com.example.immo.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor // generate constructor with all args
@NoArgsConstructor // generate constructor with no args
@Getter // generate getters
@Setter // generate setters
@Data
@Entity
@Builder
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "message_id")
    @Column(name = "id")
    private Long messageId;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date creation;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date update;

    /*
     * public Message(Long messageId, Long sender, Long recipient, String body) {
     * this.messageId = messageId;
     * this.sender = sender;
     * this.recipient = recipient;
     * this.body = body;
     * }
     */

}