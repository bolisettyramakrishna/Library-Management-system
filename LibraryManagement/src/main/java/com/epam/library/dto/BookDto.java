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
public class BookDto {
    private int id;
    @NotEmpty(message = "Name must not empty")
    private String name;
    @NotEmpty(message = "publisher must not empty")
    private String publisher;
    @NotEmpty(message = "author must not empty")
    private String author;
}
