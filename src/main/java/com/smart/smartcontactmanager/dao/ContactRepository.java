package com.smart.smartcontactmanager.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.smartcontactmanager.models.Contact;
import com.smart.smartcontactmanager.models.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

        //pagination
	
	@Query("from Contact as c where c.user.id =:userid")
	
	//Pageable->current page-page
	//Contact per page -5
	public Page<Contact> findContactByUser(@Param("userid")int userid,Pageable perpage);
	
	//search method already present in spring data jpa
	public List<Contact> findByNameContainingAndUser(String name,User user);

}
