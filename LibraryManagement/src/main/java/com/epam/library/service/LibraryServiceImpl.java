package com.epam.library.service;

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
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {
	@Autowired
	LibraryRepository libraryRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	BookClient bookClient;
	@Autowired
	UserClient userClient;
	private static final Logger LOGGER= LoggerFactory.getLogger(LibraryServiceImpl.class);
	@Override
	public List<BookDto> getAllBooks(){
		return Optional.ofNullable(bookClient.getAllBooks()).map(ResponseEntity::getBody).orElseGet(ArrayList::new);
	}
	@Override
	public BookDto getABook(int bookId){
		LOGGER.info("getABook() is executed");
		return Optional.ofNullable(bookClient.getABook(bookId))
				.map(ResponseEntity::getBody)
				.orElseGet(BookDto::new);
	}

	@Override
	public BookDto addABook(BookDto bookDto){
		LOGGER.info("addABook() is executed");
		return Optional.ofNullable(bookClient.addABook(bookDto)).map(ResponseEntity::getBody).orElseGet(BookDto::new);
	}

	@Override
	public BookDto updateABook(int bookId, BookDto bookDto){
		LOGGER.info("updateABook() is executed");
		return Optional.ofNullable(bookClient.updateABook(bookId, bookDto)).map(ResponseEntity::getBody)
				.orElseGet(BookDto::new);
	}
	@Override
	public void deleteABook(int bookId){
		LOGGER.info("deleteABook() is executed");
		release(bookId);
		bookClient.deleteABook(bookId);
	}

	@Override
	public List<UserDto> getAllUsers(){
		LOGGER.info("getAllUsers() is executed");
		return Optional.ofNullable(userClient.getAllUsers()).map(ResponseEntity::getBody).orElseGet(ArrayList::new);
	}
	@Override
	public UserBookDto getAUser(String userName){
		LOGGER.info("getAUser() is executed");
		List<BookDto> books = new ArrayList<>();
		fetchByUserName(userName).forEach(library -> books
				.add(bookClient.getABook(library.getBookId()).getBody()));
		UserBookDto userBookDto = Optional.ofNullable(userClient.getAUser(userName).getBody()).orElseGet(UserBookDto::new);
		userBookDto.setBookDtoList(books);
		return userBookDto;
	}

	public List<LibraryDto> fetchByUserName(String userName) {
		LOGGER.info("fetchByUserName() is executed");
		return libraryRepository.findByUserName(userName)
				.stream()
				.map(library->modelMapper.map(library,LibraryDto.class))
				.collect(Collectors.toList()) ;
	}
	@Override
	public UserDto addAUser(UserDto userDto){
		LOGGER.info("addAUser() is executed");
		return Optional.ofNullable(userClient.addAUser(userDto)).map(ResponseEntity::getBody).orElseGet(UserDto::new);
	}
	@Override
	public UserDto updateAUser(String userName, UserDto userDto){
		LOGGER.info("updateAUser() is executed");
		return Optional.ofNullable(userClient.updateAUser(userName, userDto)).map(ResponseEntity::getBody)
				.orElseGet(UserDto::new);
	}
	@Override
	@Transactional
	public String release(String userName, int bookId) throws ResourceNotFoundException {
		LOGGER.info("release(String userName, int bookId) is executed");
		return Optional.ofNullable(libraryRepository.deleteByUserNameAndBookId(userName, bookId))
				.filter(i->i==1)
				.map(i->"Book is released")
				.orElseThrow(()-> new ResourceNotFoundException("No record found for user:" + userName + " and book:" + bookId));
	}
	@Override
	public void deleteAUser(String userName){
		LOGGER.info("deleteAUser() is executed");
		release(userName);
		userClient.deleteAUser(userName);
	}
	private void release(String userName){
		LOGGER.info("release(String userName) is executed");
		libraryRepository.deleteByUserName(userName);
	}
	private void release(int bookId){
		LOGGER.info("release(int bookId) is executed");
		libraryRepository.deleteByBookId(bookId);
	}
	@Override
	public LibraryDto issueBook(String userName, int bookId) throws BookAlreadyIssuedException {
		LOGGER.info("issueBook() is executed");
		LibraryDto libraryDto=LibraryDto.builder().userName(getAUser(userName).getUserName())
				.bookId(getABook(bookId).getId()).build();
		if(libraryRepository.findByUserNameAndBookId(userName,bookId).isPresent()){
			throw new BookAlreadyIssuedException("book is already issued to the user "+ userName);
		}
		//libraryRepository.findByUserNameAndBookId(userName,bookId).orElseThrow(()->new BookAlreadyIssuedException("book is already issued to the user "+ userName));
		return modelMapper.map(libraryRepository.save(modelMapper.map(libraryDto, Library.class)),LibraryDto.class);
	}
}


