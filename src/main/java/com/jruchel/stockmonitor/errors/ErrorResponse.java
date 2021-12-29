package com.jruchel.stockmonitor.errors;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private HttpStatus httpStatus;
    private String message;
    private String value;
    private String timestamp;

}
