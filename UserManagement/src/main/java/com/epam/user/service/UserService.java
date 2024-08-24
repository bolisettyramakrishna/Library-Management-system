package com.epam.user.service;

import com.epam.user.dto.UserDto;
import com.epam.user.exception.DuplicateUserNameException;
import com.epam.user.exception.ResourceNotFoundException;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getAUserByUserName(String userName) throws ResourceNotFoundException;
    UserDto addAUser(UserDto userDto) throws DuplicateUserNameException;
    void deleteAUser(String userName) throws ResourceNotFoundException;
    boolean isUserNameAlreadyExist(String userName);
    void updateAUser(UserDto userDto);
}
