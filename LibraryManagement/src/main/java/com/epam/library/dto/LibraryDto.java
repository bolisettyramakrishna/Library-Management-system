package com.epam.library.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
public class LibraryDto {
    private int id;
    @NotEmpty(message = "UserName must not empty")
    private String userName;
    @NotEmpty(message = "BookId must not empty")
    private int bookId;
}
