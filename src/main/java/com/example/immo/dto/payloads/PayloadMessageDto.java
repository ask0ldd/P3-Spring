package com.example.immo.dto.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PayloadMessageDto {
    private Long rental_id;
    private Long user_id;
    private String message;
}
