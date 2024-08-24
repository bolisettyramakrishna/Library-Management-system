package com.epam.user;

import com.epam.user.dto.UserDto;
import com.epam.user.entity.User;

public class Utility {
	public static UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(1);
		userDto.setUserName("hari1234");
		userDto.setEmail("hari@gmail");
		userDto.setName("hari");
		return userDto;
	}

	public static User createUser() {
		User user = new User();
		user.setId(1);
		user.setUserName("hari1234");
		user.setEmail("hari@gmail");
		user.setName("hari");
		return user;
	}

}
