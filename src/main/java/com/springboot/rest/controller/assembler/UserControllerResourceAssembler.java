package com.springboot.rest.controller.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import com.springboot.rest.controller.UserController;
import com.springboot.rest.dto.UserDto;

//Base class to implement {@link ResourceAssembler}s. Will automate {@link ResourceSupport} instance creation and make sure a self-link is always added.
// This class is to make sure the self link is always added.

@Component
public class UserControllerResourceAssembler extends ResourceAssemblerSupport<UserDto, Resource> {

	public UserControllerResourceAssembler() {
		super(UserController.class, Resource.class);
	}

	// object
	@Override
	public Resource toResource(UserDto entity) {
		String userInfoPath = linkTo(methodOn(UserController.class).getUserById(entity.getUserId())).toString();
		String relativeUserInfoPath = userInfoPath.substring(userInfoPath.indexOf("userinfo")).trim();
		return new Resource<>(entity, new Link(relativeUserInfoPath));
	}
	
	//list
	@Override
	public List<Resource> toResources(Iterable<? extends UserDto> entities) {
		List<Resource> resources = new ArrayList<>();
		for(UserDto entity: entities) {
			resources.add(toResource(entity));
		}
		return resources;
	}

}
 