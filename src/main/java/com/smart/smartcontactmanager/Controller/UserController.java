package com.smart.smartcontactmanager.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.smartcontactmanager.dao.ContactRepository;
import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.helper.Message;
import com.smart.smartcontactmanager.models.Contact;
import com.smart.smartcontactmanager.models.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepo; 
	
	@Autowired
	private ContactRepository contactRepo;
	
	@ModelAttribute
	public void addCommonData(Model model, Principal principal)
	{
		String username= principal.getName();
		User user= userRepo.getUserByUsername(username);
		System.out.println("USERNAME........" +username);
		model.addAttribute("user", user);
		
		
		
	}
	
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		return "normal/user_dash";
	}  
	
	
	@RequestMapping("/add-contact")
	public String openAddContact(Model model )
	{
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
		
	}
	
	
	@PostMapping(value="/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result , @RequestParam("profileImage")  MultipartFile file,  Principal principal, HttpSession session)
	{
		
		if(result.hasErrors())
		{
			return "normal/add_contact";
		}
		try
		{
		String name=principal.getName();
		User user= this.userRepo.getUserByUsername(name);
		contact.setUser(user); 
		
		//processing and uploading file
		if(file.isEmpty())
		{
			//if the file is empty
			System.out.println("empty file");
			contact.setImage("register.png");
			throw new Exception();
			
		} 
		else
		{
			//upload the file to folder and update the name to contact
			contact.setImage(file.getOriginalFilename()+ user.getId());
			File savefile= new ClassPathResource("static/img").getFile();
			Path path= Paths.get(savefile.getAbsolutePath()+File.separator+ file.getOriginalFilename()+ user.getId());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("image uploaded");
		}
		
		user.getContacts().add(contact);
		
		this.userRepo.save(user);
		System.out.println(contact); 
		
		//message success............
		session.setAttribute("message", new Message("added the image","success"));
		return "normal/add_contact";
		}
		catch (Exception e) {
			System.out.println("exception"); 
			//message error............
			session.setAttribute("message", new Message("something went wrong","danger"));
			
		}
		return "normal/add_contact";
	}  
	
	
	//show contacts handler
	//pagination per page=5
	//current page 0
	@GetMapping("/show_contacts/{page}")
	public String showContacts( @PathVariable("page") Integer page  ,Model model, Principal principal)
	{
		model.addAttribute("title", "Show user contacts");
		String name= principal.getName();
		User user = this.userRepo.getUserByUsername(name);
	//	 user.getContacts();   
		Pageable pageable= PageRequest.of(page, 5);
		 Page<Contact> contacts =this.contactRepo.findContactByUser(user.getId(), pageable);
		 model.addAttribute("contacts", contacts);
		 model.addAttribute("currentpage", page);
		 model.addAttribute("totalpages", contacts.getTotalPages());
		return "normal/show_contacts";
		
	}
	
	@RequestMapping("/contact/{cId}")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model,Principal principal)
	{
	Optional<Contact> contactOptional = this.contactRepo.findById(cId);
	Contact contact= contactOptional.get(); 
	String username=principal.getName();
	User user= this.userRepo.getUserByUsername(username);
	
	//check that if the user can access only his own contacts
	if(user.getId()==contact.getUser().getId())
	{
		model.addAttribute("contact", contact);
	}
	
		return "normal/contact_detail";
		
	}  
	
	@GetMapping("/delete/{cId}")
	public String DeleteContact(@PathVariable("cId") Integer id,Model model,Principal principal,HttpSession session)
	{
		Optional<Contact> contactOptional= this.contactRepo.findById(id);
		Contact contact=contactOptional.get();
		//check that if the user can delete only his own contacts
		
		String username=principal.getName();
		User user= this.userRepo.getUserByUsername(username);
		
		user.getContacts().remove(contact);
		this.userRepo.save(user);
			//contact.setUser(null);
			//this.contactRepo.delete(contact);
			
			session.setAttribute("message" , new Message("contact deleted", "success"));
			System.out.println("contact deleted");
		
		
		
			
		return "redirect:/user/show_contacts/0";
		
	} 
	
	@RequestMapping("/update-contact/{cId}")
	public String updateForm(@PathVariable("cId")Integer cid, Model m)
	{
		m.addAttribute("title", "update form");
		Contact contact= this.contactRepo.findById(cid).get();
		m.addAttribute("contact", contact);
		return "normal/update_form";
		
	}
	
//process update form
	@RequestMapping(value="/process-update", method=RequestMethod.POST )
	public String updateHandler(@ModelAttribute Contact contact, 
	@RequestParam("profileImage") MultipartFile file, Model m, HttpSession session, Principal principal )
	{
		try {
			//image
			//old conact detail
			Contact olddetail=this.contactRepo.findById(contact.getcId()).get();
			if(!file.isEmpty())
			{
				//file work
				//rewrite
				//delete old photo
				File deletefile= new ClassPathResource("static/img").getFile();
		          File file1=new File(deletefile, olddetail.getImage());
		          file1.delete();
		          System.out.println("deleted img");

				//update new
				File savefile= new ClassPathResource("static/img").getFile();
				Path path= Paths.get(savefile.getAbsolutePath()+File.separator+ file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				System.out.println("image uploaded");
			}
				 
			else
			{
				contact.setImage(olddetail.getImage());
			}
			User user= this.userRepo.getUserByUsername(principal.getName());
			contact.setUser(user);
			System.out.println("Contactname "+contact.getName());
			System.out.println("ContactId " +contact.getcId());
			this.contactRepo.save(contact);
			session.setAttribute("message", new Message("your contact isupdated", "success"));
		}
		catch (Exception e) {
			// TODO: handle exception
		} 
		return "redirect:/user/contact/" +contact.getcId();

		}
	
	@GetMapping("/profile")
	public String profilePage(Model model)
	{
		model.addAttribute("title", "Profile Page");
		return "normal/profilepage";
	}
}

