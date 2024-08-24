package com.epam.user.service;

import com.epam.user.dto.UserDto;
import com.epam.user.entity.User;
import com.epam.user.exception.DuplicateUserNameException;
import com.epam.user.exception.ResourceNotFoundException;
import com.epam.user.repository.UserRepository;
import com.epam.user.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    ModelMapper modelMapper;
    UserDto userDto;
    User user;
    @BeforeEach
    public void setUp() {
        this.userDto = Utility.createUserDto();
        this.user = Utility.createUser();
    }
    @Test
    public void addAUserTest() throws DuplicateUserNameException {
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto actual = userService.addAUser(userDto);
        assertEquals(userDto.getName(), actual.getName());
    }
    @Test
    public void addAUserWithDuplicateUserNameTest(){
        when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(user));
        Assertions.assertThrows(DuplicateUserNameException.class, () -> userService.addAUser(userDto));
    }
    @Test
    public void getAUserByUserNameTest() throws ResourceNotFoundException {
        when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        UserDto actual = userService.getAUserByUserName("hari");
        assertEquals(userDto.getName(), actual.getName());
    }
    @Test
    public void getAnInvalidUserTest(){
        when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getAUserByUserName("Shiv"));
    }
    @Test
    public void deleteAUserTest() throws ResourceNotFoundException {
        when(userRepository.findByUserName(Mockito.anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        userService.deleteAUser("hari");
        Mockito.verify(userRepository).delete(Mockito.any());
    }
    @Test
    public void updateAUserTest(){
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(userRepository.save(Mockito.any())).thenReturn(user);
        userService.updateAUser(userDto);
        Mockito.verify(userRepository).save(Mockito.any());
    }
    /*
    * List<User> users= (List<User>)userRepository.findAll();
        return users.stream().map(user->modelMapper.map(user,UserDto.class)).collect(Collectors.toList());*/
    @Test
    public void getAllUsersTest(){
        List<UserDto> userDtos = new ArrayList<UserDto>();
        List<User> users = new ArrayList<User>();
        users.add(user);
        userDtos.add(userDto);
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(userDtos.size(),userService.getAllUsers().size());
    }
}
