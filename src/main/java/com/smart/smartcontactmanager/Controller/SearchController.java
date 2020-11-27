package com.smart.smartcontactmanager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.models.Contact;
import com.smart.smartcontactmanager.models.User;

@RestController
public class SearchController {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepository contactRepo;
	
		
	//search controller
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
	{
		User user= this.userRepo.getUserByUsername(principal.getName());
		List<Contact> contacts= this.contactRepo.findByNameContainingAndUser(query, user);
		return ResponseEntity.ok(contacts);

		
	}

}
