package com.springboot.rest.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rest.controller.assembler.UserControllerResourceAssembler;
import com.springboot.rest.dto.UserDto;
import com.springboot.rest.entity.User;
import com.springboot.rest.service.ConverterService;
import com.springboot.rest.service.UserService;

@RestController
@RequestMapping(value = "/userinfo")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ConverterService converterService;
	
	@Autowired
	private UserControllerResourceAssembler userControllerResourceAssembler;
	
	/* by default sorting is in Ascending order, if you want to perform sorting in descending order than use below parameters with URL:
                    http://localhost:9091/user-management/userinfo/alluser?sort=userId,desc
                    http://localhost:9091/user-management/userinfo/alluser?page=1&size=4&sort=userId,desc
    */	
	
	// PagedResourcesAssembler easily convert (Page) instances into (PagedResources)
	@GetMapping(value = "/alluser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedResources<UserDto>> getUserinfo(@PageableDefault(page=0, size=4) Pageable pageRequest,
			                                        PagedResourcesAssembler<UserDto> pagedResourcAssembler) {
		
		Page<User> userPage = userService.getAllUserInfo(pageRequest);
		List<User> userResultList = userPage.getContent();
		List<UserDto> userDtoList = userResultList.stream().map(converterService::convertToDto).collect(Collectors.toList());
		Page<UserDto> userDtoPage = new PageImpl<>(userDtoList, pageRequest, userPage.getTotalElements());
		String userInfoPath = linkTo(methodOn(UserController.class).getUserinfo(pageRequest, pagedResourcAssembler)).toString();
		String relativeUserInfoPath = userInfoPath.substring(userInfoPath.indexOf("userinfo")).trim();
		// Resource will wrap domain objects and add links into it.
		PagedResources<Resource> resource = pagedResourcAssembler.toResource(userDtoPage, userControllerResourceAssembler, new Link(relativeUserInfoPath));
		 return new ResponseEntity(resource, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/alluser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDto getUserById(@PathVariable("userId") String userId) {
		return userService.getUserByUserId(userId);
	}
	 	
}
