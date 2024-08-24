package com.epam.library.service;
import com.epam.library.TestUtil;
import com.epam.library.client.BookClient;
import com.epam.library.client.UserClient;
import com.epam.library.dto.BookDto;
import com.epam.library.dto.LibraryDto;
import com.epam.library.dto.UserBookDto;
import com.epam.library.dto.UserDto;
import com.epam.library.entity.Library;
import com.epam.library.exception.BookAlreadyIssuedException;
import com.epam.library.exception.ResourceNotFoundException;
import com.epam.library.repository.LibraryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(LibraryService.class)
@OverrideAutoConfiguration(enabled = true)
class TestLibraryService {
    @MockBean
    ModelMapper modelMapper;
    @InjectMocks
    LibraryServiceImpl libraryService;
    @MockBean
    UserClient userClient;
    @MockBean
    BookClient bookClient;
    @Mock
    LibraryRepository libraryRepository;
    static Library library;
    static BookDto bookDto;
    static UserDto userDto;
    static UserBookDto userBookDto;
    static LibraryDto libraryDto;

    @BeforeAll
    static void setUpBeforeAll() {
        bookDto = TestUtil.createBookDto();
        userBookDto = TestUtil.createUserBookDto();
        userDto = TestUtil.createUserDto();
        libraryDto = TestUtil.createLibraryDto();
        library = TestUtil.createLibrary();
    }

    @Test
    void getAllUsersTest() {
        when(userClient.getAllUsers())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>()));
        assertEquals(0, libraryService.getAllUsers().size());
    }

    @Test
    void addUserTest() {
        when(userClient.addAUser(userDto)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(userDto));
        UserDto actual = libraryService.addAUser(userDto);
        assertEquals(userDto.getName(), actual.getName());
    }

    @Test
    void updateUserTest() {
        when(userClient.updateAUser(Mockito.anyString(), Mockito.any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(userDto));
        UserDto actual = libraryService.updateAUser("hari", userDto);
        assertEquals(userDto.getName(), actual.getName());
    }

    @Test
    void deleteUserTest() {
        when(libraryRepository.deleteByUserName(Mockito.anyString())).thenReturn(1);
        when(userClient.deleteAUser(Mockito.anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
        libraryService.deleteAUser("hari");
        Mockito.verify(libraryRepository).deleteByUserName(Mockito.anyString());
    }

    @Test
    void getAllBooksTest() {
        when(bookClient.getAllBooks())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>()));
        assertEquals(0, libraryService.getAllBooks().size());
    }

    @Test
    void getABookTest() {
        when(bookClient.getABook(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookDto));
        assertEquals(bookDto, libraryService.getABook(1));
    }

    @Test
    void addBookTest() {
        when(bookClient.addABook(bookDto)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(bookDto));
        BookDto actual = libraryService.addABook(bookDto);
        assertEquals(bookDto.getName(), actual.getName());
    }

    @Test
    void updateBookTest() {
        when(bookClient.updateABook(Mockito.anyInt(), Mockito.any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookDto));
        BookDto actual = libraryService.updateABook(1, bookDto);
        assertEquals(bookDto.getName(), actual.getName());
    }

    @Test
    void deleteBookTest() {
        when(libraryRepository.deleteByBookId(Mockito.anyInt())).thenReturn(1);
        when(bookClient.deleteABook(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
        libraryService.deleteABook(1);
        Mockito.verify(libraryRepository).deleteByBookId(Mockito.anyInt());
    }

    @Test
    void releaseByUserNameAndBookIdTest() throws ResourceNotFoundException {
        when(libraryRepository.deleteByUserNameAndBookId("Sk", 0)).thenReturn(1);
        libraryService.release("Sk", 0);
        Mockito.verify(libraryRepository).deleteByUserNameAndBookId("Sk", 0);
    }

    @Test
    void releaseByUserNameAndBookIdInvalidTest(){
        when(libraryRepository.deleteByUserNameAndBookId(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
        assertThrows(ResourceNotFoundException.class,()->libraryService.release(anyString(),anyInt()));
    }

    @Test
    void getAUserTest() {
        List<Library> libraryList = new ArrayList<>();
        libraryList.add(library);
        when(libraryRepository.findByUserName(Mockito.anyString())).thenReturn(libraryList);
        when(modelMapper.map(library, LibraryDto.class)).thenReturn(libraryDto);
        when(bookClient.getABook(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookDto));
        when(userClient.getAUser(Mockito.anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(userBookDto));
        UserBookDto actual = libraryService.getAUser("hari");
        assertEquals(userBookDto.getName(), actual.getName());
    }
    @Test
    void testIssueBook() throws BookAlreadyIssuedException {
        List<Library> libraryList = new ArrayList<>();
        libraryList.add(library);
        Mockito.when(modelMapper.map(libraryDto, Library.class)).thenReturn(library);
        Mockito.when(modelMapper.map(library, LibraryDto.class)).thenReturn(libraryDto);
        Mockito.when(bookClient.getABook(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookDto));
        Mockito.when(userClient.getAUser(Mockito.anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(userBookDto));
        Mockito.when(libraryRepository.save(Mockito.any())).thenReturn(library);
        libraryService.issueBook("hari", 1);
        Mockito.verify(libraryRepository).save(Mockito.any());

    }
    @Test
    void testIssueInvalidBook() {
        List<Library> libraryList = new ArrayList<>();
        libraryList.add(library);
        Mockito.when(bookClient.getABook(Mockito.anyInt()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(bookDto));
        Mockito.when(userClient.getAUser(Mockito.anyString()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(userBookDto));
        Mockito.when(libraryRepository.findByUserNameAndBookId(Mockito.anyString(),Mockito.anyInt())).thenReturn(Optional.ofNullable(library));
        assertThrows(BookAlreadyIssuedException.class, ()->libraryService.issueBook("hari", 1)) ;
    }
}