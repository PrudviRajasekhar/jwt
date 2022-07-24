package com.raja.service;

import java.util.Optional;

import com.raja.model.User;

public interface IUserService {

	Integer saveUser(User user);
	Optional<User> findByUsername(String username);
}
