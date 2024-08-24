package com.epam.user.service;

import com.epam.user.dto.UserDto;
import com.epam.user.entity.User;
import com.epam.user.exception.DuplicateUserNameException;
import com.epam.user.exception.ResourceNotFoundException;
import com.epam.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements  UserService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    public List<UserDto> getAllUsers(){
        List<User> users= (List<User>)userRepository.findAll();
        return users.stream().map(user->modelMapper.map(user,UserDto.class)).collect(Collectors.toList());
    }
    public UserDto getAUserByUserName(String userName) throws ResourceNotFoundException {
        return modelMapper.map(userRepository.findByUserName(userName)
                        .orElseThrow(() -> new ResourceNotFoundException("User name:" + userName + " does not exist")),
                UserDto.class);
    }
    public UserDto addAUser(UserDto userDto) throws DuplicateUserNameException{
        if (isUserNameAlreadyExist(userDto.getUserName())) {
            throw new DuplicateUserNameException("UserName already exists, please try another UserName");
        }
        return modelMapper.map(userRepository.save(modelMapper.map(userDto, User.class)), UserDto.class);
    }
    public boolean isUserNameAlreadyExist(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }
    public void deleteAUser(String userName) throws ResourceNotFoundException {
        userRepository.delete(modelMapper.map(getAUserByUserName(userName), User.class));
    }
    public void updateAUser(UserDto userDto) {
        userRepository.save(modelMapper.map(userDto, User.class));
    }
}
