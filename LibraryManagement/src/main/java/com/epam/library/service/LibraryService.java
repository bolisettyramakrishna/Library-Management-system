package com.epam.library.service;

import com.epam.library.dto.BookDto;
import com.epam.library.dto.LibraryDto;
import com.epam.library.dto.UserBookDto;
import com.epam.library.dto.UserDto;
import com.epam.library.exception.BookAlreadyIssuedException;
import com.epam.library.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface LibraryService {
    List<BookDto> getAllBooks();

    BookDto getABook(int bookId);

    BookDto addABook(BookDto bookDto);

    BookDto updateABook(int bookId, BookDto bookDto);

    void deleteABook(int bookId);

    List<UserDto> getAllUsers();

    UserBookDto getAUser(String userName);

    UserDto addAUser(UserDto userDto);

    UserDto updateAUser(String userName, UserDto userDto);

    LibraryDto issueBook(String userName, int bookId) throws BookAlreadyIssuedException;

    @Transactional
    String release(String userName, int bookId) throws ResourceNotFoundException;

    void deleteAUser(String userName);
}
