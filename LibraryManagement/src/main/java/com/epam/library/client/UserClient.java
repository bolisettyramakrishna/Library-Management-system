package com.epam.library.client;

import com.epam.library.dto.UserBookDto;
import com.epam.library.dto.UserDto;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@FeignClient(name = "user-server/users")
@LoadBalancerClient(name="user-server")
public interface UserClient {
    @GetMapping
    ResponseEntity<List<UserDto>> getAllUsers();

    @GetMapping("/{userName}")
    ResponseEntity<UserBookDto> getAUser(@PathVariable String userName);

    @PostMapping
    ResponseEntity<UserDto> addAUser(@RequestBody UserDto userDto);

    @PutMapping("/{userName}")
    ResponseEntity<UserDto> updateAUser(@PathVariable String userName, @RequestBody UserDto userDto);

    @DeleteMapping("/{userName}")
    ResponseEntity<UserDto> deleteAUser(@PathVariable String userName);
}
