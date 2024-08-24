package com.epam.user.controller;

import com.epam.user.dto.UserDto;
import com.epam.user.exception.DuplicateUserNameException;
import com.epam.user.exception.ResourceNotFoundException;
import com.epam.user.service.UserService;
import com.epam.user.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getAUserByUserName(@PathVariable("username") String userName) throws ResourceNotFoundException {
        return new ResponseEntity<>(userService.getAUserByUserName(userName), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<UserDto> addAUser(@RequestBody @Valid UserDto userDto) throws DuplicateUserNameException {
        return new ResponseEntity<>(userService.addAUser(userDto), HttpStatus.CREATED);
    }
    @DeleteMapping("/{username}")
    public ResponseEntity<UserDto> deleteAUser(@PathVariable("username") String userName) throws ResourceNotFoundException {
        userService.deleteAUser(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateAUser(@RequestBody @Valid UserDto updatedUser,
                                          @PathVariable("username") String userName) throws ResourceNotFoundException {
        UserDto originalUser = userService.getAUserByUserName(userName);
        updatedUser.setId(originalUser.getId());
        updatedUser.setUserName(userName);
        userService.updateAUser(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
