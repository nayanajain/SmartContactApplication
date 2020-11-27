package com.smart.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.models.User;

public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from username
		User user=userRepository.getUserByUsername(username); 
		if(user==null)
		{
			throw new UsernameNotFoundException("Email is not valid");
		}  
		
		CustomUserDetail customUserDetail= new CustomUserDetail(user);
		return customUserDetail;
	}

}
