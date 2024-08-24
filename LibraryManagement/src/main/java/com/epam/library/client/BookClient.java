package com.epam.library.client;

import com.epam.library.dto.BookDto;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "book-server/books")
@LoadBalancerClient(name="book-server")
public interface BookClient {
    @GetMapping
    ResponseEntity<List<BookDto>> getAllBooks();

    @GetMapping("/{bookId}")
    ResponseEntity<BookDto> getABook(@PathVariable int bookId);

    @PostMapping
    ResponseEntity<BookDto> addABook(@RequestBody BookDto bookDto);

    @PutMapping("/{bookId}")
    ResponseEntity<BookDto> updateABook(@PathVariable int bookId, @RequestBody @Valid BookDto bookDto);

    @DeleteMapping("/{bookId}")
    ResponseEntity<BookDto> deleteABook(@PathVariable int bookId);

}