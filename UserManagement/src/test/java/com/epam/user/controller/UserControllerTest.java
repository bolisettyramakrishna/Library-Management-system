package com.epam.user.controller;

import com.epam.user.Utility;
import com.epam.user.dto.UserDto;
import com.epam.user.entity.User;
import com.epam.user.exception.DuplicateUserNameException;
import com.epam.user.exception.ResourceNotFoundException;
import com.epam.user.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@OverrideAutoConfiguration(enabled = true)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    UserDto userDto;
    User user;

    @BeforeEach
    public void setUp() {
        this.userDto = Utility.createUserDto();
        this.user = Utility.createUser();
    }

    @Test
    void addAUserTest() throws Exception {
        Mockito.when(userService.addAUser(userDto)).thenReturn(userDto);
        Gson gson = new Gson();
        String userJson = gson.toJson(userDto);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isCreated());
    }

    @Test
    void addUserWithDuplicateNameTest() throws Exception {
        Mockito.when(userService.addAUser(any())).thenThrow(DuplicateUserNameException.class);
        Gson gson = new Gson();
        String userJson = gson.toJson(userDto);
        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAUserTest() throws Exception {
        Mockito.when(userService.getAUserByUserName(Mockito.anyString())).thenReturn(userDto);
        mockMvc.perform(get("/users/hari123")).andExpect(status().isOk());
    }

    @Test
     void getAnInvalidUserTest() throws Exception {
        Mockito.when(userService.getAUserByUserName(Mockito.anyString())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/users/hari123")).andExpect(status().isBadRequest());
    }

    @Test
     void deleteAUserTest() throws Exception {
        Mockito.doNothing().when(userService).deleteAUser(Mockito.anyString());
        mockMvc.perform(delete("/users/hari123")).andExpect(status().isNoContent());
    }

    @Test
     void updateAUserTest() throws Exception {
        Mockito.when(userService.getAUserByUserName(Mockito.anyString())).thenReturn(userDto);
        Gson gson = new Gson();
        String userJson = gson.toJson(userDto);
        mockMvc.perform(put("/users/hari").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk());
    }

    @Test
     void updateAUserWithException() throws Exception {
        Mockito.when(userService.getAUserByUserName(Mockito.anyString())).thenReturn(userDto);
        Gson gson = new Gson();
        userDto.setEmail(null);
        String userJson = gson.toJson(userDto);
        mockMvc.perform(put("/users/hari").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
     void getAllUsersTest() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto);
        Mockito.when(userService.getAllUsers()).thenReturn(userDtos);
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }
}
