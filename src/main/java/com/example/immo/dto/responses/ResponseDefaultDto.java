package com.example.immo.dto.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ResponseDefaultDto {

    private String message;

    public ResponseDefaultDto(String message) {
        this.message = message;
    }
}
