package com.epam.library.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.epam.library.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.epam.library.dto.BookDto;
import com.epam.library.dto.LibraryDto;
import com.epam.library.dto.UserBookDto;
import com.epam.library.dto.UserDto;
import com.epam.library.entity.Library;
import com.epam.library.exception.ExceptionResponse;
import com.epam.library.service.LibraryService;
import com.google.gson.Gson;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LibraryRestController.class)
 class TestLibraryController {
	@Autowired
	MockMvc mockMvc;
	@MockBean
	 LibraryService libraryService;
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
	void testGetAllBooks() throws Exception {
		List<BookDto> listDto = new ArrayList<>();
		Mockito.when(libraryService.getAllBooks()).thenReturn(listDto);
		mockMvc.perform(get("/library/books")).andExpect(status().isOk());
	}

	@Test
	void testGetBook() throws Exception {
		Mockito.when(libraryService.getABook(1)).thenReturn(bookDto);
		mockMvc.perform(get("/library/books/1")).andExpect(status().isOk());
	}
	@Test
	void testBookNonExisting() throws Exception {
		Gson gson = new Gson();
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus("NOT_FOUND");
		HttpClientErrorException http = HttpClientErrorException.NotFound.create(HttpStatus.NOT_FOUND, "", null,
				gson.toJson(exceptionResponse).getBytes(), null);
		Mockito.doThrow(http).when(libraryService).getABook(1);
		mockMvc.perform(get("/library/books/1")).andExpect(status().is5xxServerError());
	}
	@Test
	void testAddBook() throws Exception {
		Mockito.when(libraryService.addABook(bookDto)).thenReturn(bookDto);
		String bookJson = new Gson().toJson(bookDto);
		mockMvc.perform(post("/library/books").contentType(MediaType.APPLICATION_JSON).content(bookJson))
				.andExpect(status().isCreated());
	}

	@Test
	void testUpdateBook() throws Exception {
		Mockito.when(libraryService.updateABook(1, bookDto)).thenReturn(bookDto);
		String bookJson = new Gson().toJson(bookDto);
		mockMvc.perform(put("/library/books/1").contentType(MediaType.APPLICATION_JSON).content(bookJson))
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteBook() throws Exception {
		Mockito.doNothing().when(libraryService).deleteABook(1);
		mockMvc.perform(delete("/library/books/1")).andExpect(status().isNoContent());
	}

	@Test
	void testGetAllUsers() throws Exception {
		Mockito.when(libraryService.getAllUsers()).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/library/users")).andExpect(status().isOk());
	}

	@Test
	void testDeleteUser() throws Exception {
		Mockito.doNothing().when(libraryService).deleteAUser("hari");
		mockMvc.perform(delete("/library/users/hari")).andExpect(status().isOk());
	}

	@Test
	void testGetUser() throws Exception {
		Mockito.when(libraryService.getAUser("hari")).thenReturn(userBookDto);
		mockMvc.perform(get("/library/users/hari")).andExpect(status().isOk());
	}

	@Test
	void testUpdateUser() throws Exception {
		Mockito.when(libraryService.updateAUser("hari", userDto)).thenReturn(userDto);
		String userJson = new Gson().toJson(userDto);
		mockMvc.perform(put("/library/users/1").contentType(MediaType.APPLICATION_JSON).content(userJson))
				.andExpect(status().isOk());
	}

	@Test
	void testAddUser() throws Exception {
		Mockito.when(libraryService.addAUser(userDto)).thenReturn(userDto);
		String userJson = new Gson().toJson(userDto);
		mockMvc.perform(post("/library/users").contentType(MediaType.APPLICATION_JSON).content(userJson))
				.andExpect(status().isCreated());
	}

	@Test
	void testIssueBook() throws Exception {
		Mockito.when(libraryService.issueBook("hari", 2)).thenReturn(libraryDto);
		mockMvc.perform(post("/library/users/hari/books/2")).andExpect(status().isCreated());
	}
	@Test
	void testIssueBookInvalidUserName() throws Exception {
		Gson gson = new Gson();
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus("NOT_FOUND");
		Request request = Request.create(Request.HttpMethod.GET, "url", new HashMap<>(), null, new RequestTemplate());
		Mockito.doThrow(
				new FeignException.NotFound("", request, gson.toJson(exceptionResponse).getBytes(), new HashMap<>()))
				.when(libraryService).issueBook("hari", 2);
		mockMvc.perform(post("/library/users/hari/books/2")).andExpect(status().isNotFound());
	}

	@Test
	void testReleaseBook() throws Exception {
		Mockito.when(libraryService.release(Mockito.anyString(), Mockito.anyInt())).thenReturn("Book is released");
		mockMvc.perform(delete("/library/users/hari/books/1")).andExpect(status().isOk());
	}
	@Test
	void testReleaseNonExistingBook() throws Exception {
		Mockito.when(libraryService.release(Mockito.anyString(), Mockito.anyInt())).thenReturn("No record found for user:" );
		mockMvc.perform(delete("/library/users/hari/books/1")).andExpect(status().isOk());
	}

}
/*
*
* /*@Test
	void testIssueBookLimitExceeded() throws Exception {
		Mockito.when(libraryService.issueBook("hari", 2)).thenThrow(BookLimitExceedException.class);
		mockMvc.perform(post("/library/users/hari/books/2")).andExpect(status().isBadRequest());
	}*/
