package com.smart.smartcontactmanager.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.models.User;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/test")
	@ResponseBody
	public String test()
	{
		User user=new User();
		user.setName("Nayana");
		user.setEmail("anig@gmail.com");
		userRepository.save(user);
		return "Working";
	}

	
	@RequestMapping("/home")
	public String home(Model model)
	{
		model.addAttribute("title","Smart Contact Manager App");
		
		
		return "home";
	}   
	
	
	
	@RequestMapping("/signup")
	public String signup(Model model)
	{
		User user=new User();
		model.addAttribute("title","Register-Smart Contact Manager App");
		model.addAttribute("user", user);
		
		return "signup";
	}   
	
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title"," About Smart Contact Manager App");
		
		
		return "about";
	}

    @RequestMapping(value="/do_register", method= RequestMethod.POST)
	public String registeruser(@Valid @ModelAttribute("user") User user, BindingResult result1 ,Model model,
			HttpSession session,  @RequestParam(value="agreement", defaultValue="false")boolean agreement)
   {
    	try
    	{
    		if(!agreement)
        	{
        		System.out.println("please accept terms and condition");
        		throw new Exception("you have not agreed to terms and conditioon");
        		
        	}  
    		if(result1.hasErrors())
    		{
    			System.out.println("result has errors"+result1.toString());
    			model.addAttribute("user", user);
    			return "signup";
    		}
        	
        	user.setRole("ROLE_user");
        	user.setEnabled(true); 
        	user.setPassword(PasswordEncoder.encode(user.getPassword()));
        	System.out.println(agreement);
        	System.out.println(user); 
        	
        	User result=this.userRepository.save(user);
        	model.addAttribute("user", new User());
        	//System.out.println(result);
        	//session.setAttribute("message", );

    		
    	}
    	catch (Exception e) {
			System.out.println(e);
			model.addAttribute("user", user);
			//session.setAttribute("message", new Message("..", ".."));
			
		     return "signup";
    	}
    	return "signup";
   }  
    
   @RequestMapping("/signin")
    public String customLogin(Model model)
    {
	   model.addAttribute("title", "Login Page");
    	return "login";
    }

}
