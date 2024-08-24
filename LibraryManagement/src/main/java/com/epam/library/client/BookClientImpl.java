package com.epam.library.client;

import com.epam.library.dto.BookDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookClientImpl implements BookClient{
	@Override
    public ResponseEntity<List<BookDto>> getAllBooks() {
       return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<BookDto> getABook(int bookId) {
        return new ResponseEntity<>(new BookDto(),HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<BookDto> addABook(BookDto bookDto) {
        return new ResponseEntity<>(new BookDto(),HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<BookDto> updateABook(int bookId, BookDto bookDto) {
        return new ResponseEntity<>(new BookDto(),HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<BookDto> deleteABook(int bookId) {
        return new ResponseEntity<>(new BookDto(),HttpStatus.NO_CONTENT);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}