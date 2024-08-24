package com.epam.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private int id;
    @NotNull(message = "user name must not be empty")
    private String userName;
    @NotNull(message = "email must not be empty")
    private String email;
    @NotNull(message = "name must not be empty")
    private String name;
}
