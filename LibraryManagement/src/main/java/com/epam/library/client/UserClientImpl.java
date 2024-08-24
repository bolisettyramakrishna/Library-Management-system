package com.epam.library.client;

import com.epam.library.dto.UserBookDto;
import com.epam.library.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserClientImpl implements UserClient {

	@Override
	public ResponseEntity<List<UserDto>> getAllUsers() {
		   return new ResponseEntity<>(new ArrayList<>(),HttpStatus.NO_CONTENT);
		    }

	@Override
	public ResponseEntity<UserBookDto> getAUser(String userName) {
	    return new ResponseEntity<>(new UserBookDto(),HttpStatus.NO_CONTENT);
	    }

	@Override
	public ResponseEntity<UserDto> addAUser(@Valid UserDto userDto) {
		  return new ResponseEntity<>(new UserDto(),HttpStatus.NO_CONTENT);
    }


	@Override
	public ResponseEntity<UserDto> updateAUser(String userName, @Valid UserDto userDto) {
		  return new ResponseEntity<>(new UserDto(),HttpStatus.NO_CONTENT);
    }


	@Override
	public ResponseEntity<UserDto> deleteAUser(String userName) {
		  return new ResponseEntity<>(new UserDto(),HttpStatus.NO_CONTENT);
    }


}
