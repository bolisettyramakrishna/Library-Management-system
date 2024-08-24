package com.epam.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;
    @NotEmpty(message = "UserName must not empty")
    private String userName;
    @NotEmpty(message = "email must not empty")
    private String email;
    @NotEmpty(message = "Name must not empty")
    private String name;
}
