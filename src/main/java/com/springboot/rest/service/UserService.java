package com.springboot.rest.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.rest.dto.UserDto;
import com.springboot.rest.entity.User;
import com.springboot.rest.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ConverterService converterService;

	public Page<User> getAllUserInfo(Pageable pageRequest) {
		List<User> userList = Arrays.asList(
				new User("111", "First Demo"),
				new User("222", "Second Demo"),
				new User("333", "third Demo"),
				new User("444", "Second Demo"),
				new User("555", "fourth Demo"),
				new User("666", "fifte Demo"),
				new User("77", "sixth Demo"),
				new User("888", "Seventh Demo"));
		userRepository.saveAll(userList);
		return userRepository.findAll(pageRequest);
	}
	
	 public UserDto getUserByUserId(String userId) { 
		  User userObj = userRepository.findById(userId).orElse(null);
		  return converterService.convertToDto(userObj);
	  }
}