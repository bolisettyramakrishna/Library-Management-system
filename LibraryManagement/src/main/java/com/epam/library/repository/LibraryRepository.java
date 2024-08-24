package com.epam.library.repository;

import com.epam.library.entity.Library;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends CrudRepository<Library,Integer> {
    List<Library> findByUserName(String userName);
    Integer deleteByUserNameAndBookId(String userName, int bookId);
    Optional<Library> findByUserNameAndBookId(String userName,int bookId);
    Integer deleteByBookId(int bookId);
    Integer deleteByUserName(String userName);
}

