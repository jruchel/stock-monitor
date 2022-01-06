package com.jruchel.stockmonitor.controllers.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @Size(min = 4, max = 20)
    private String username;
    @Size(min = 6, max = 50)
    private String password;
    @Email
    private String email;

}
