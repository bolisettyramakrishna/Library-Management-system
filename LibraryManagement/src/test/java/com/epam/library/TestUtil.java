package com.epam.library;


import com.epam.library.dto.BookDto;
import com.epam.library.dto.LibraryDto;
import com.epam.library.dto.UserBookDto;
import com.epam.library.dto.UserDto;
import com.epam.library.entity.Library;
import java.util.ArrayList;

public class TestUtil {
    public static Library createLibrary() {
        Library library = new Library();
        library.setId(0);
        library.setUserName("hari");
        library.setBookId(2);
        return library;
    }

    public static UserDto createUserDto() {
        return UserDto.builder().id(1).name("hari").userName("hari").email("hari@123").build();
    }

    public static UserBookDto createUserBookDto() {
        return UserBookDto.builder().id(1).name("hari").userName("hari").email("hari@123").bookDtoList(new ArrayList<>()).build();
    }

    public static LibraryDto createLibraryDto() {
        return LibraryDto.builder().id(0).userName("hari").bookId(2).build();
    }

    public static BookDto createBookDto() {
        return BookDto.builder().id(2).author("hari").name("Book1").publisher("hari").build();
    }
}
