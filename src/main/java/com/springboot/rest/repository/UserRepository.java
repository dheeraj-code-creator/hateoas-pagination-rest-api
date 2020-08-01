package com.springboot.rest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.rest.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

	public User findByUserId(String userId);
	public Page<User> findAll(Pageable pageRequest);

}
