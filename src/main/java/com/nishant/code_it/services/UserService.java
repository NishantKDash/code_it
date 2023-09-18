package com.nishant.code_it.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nishant.code_it.enums.Role;
import com.nishant.code_it.exceptions.UserAlreadyExists;
import com.nishant.code_it.exceptions.UserDoesNotExist;
import com.nishant.code_it.exceptions.WrongPassword;
import com.nishant.code_it.models.UserEntity;
import com.nishant.code_it.repositories.UserRepository;

@Service
public class UserService {
   
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder encoder;
	public UserEntity register(String username , String name , String password , Role role) throws UserAlreadyExists
	{
		if(userRepo.findByusername(username) != null)
			throw new UserAlreadyExists("This username is already taken");
		
		UserEntity user = new UserEntity();
		user.setName(name);
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setRole(role);
		userRepo.save(user);
		return user;
	}
	
	public UserEntity verify(String username , String password) throws UserDoesNotExist, WrongPassword
	{
		if(userRepo.findByusername(username) == null)
			throw new UserDoesNotExist("This username does not exist");
		
		UserEntity user = userRepo.findByusername(username);
		if(!encoder.matches(password, user.getPassword()))
		throw new WrongPassword("The provided password is wrong");
		
		return user;
	}

}
