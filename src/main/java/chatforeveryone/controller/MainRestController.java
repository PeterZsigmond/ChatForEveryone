package chatforeveryone.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chatforeveryone.entity.Message;
import chatforeveryone.service.FriendResponse;
import chatforeveryone.service.MessageResponse;
import chatforeveryone.service.MessageService;
import chatforeveryone.service.RESTResponse;
import chatforeveryone.service.UserService;

@RestController
public class MainRestController
{
	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@RequestMapping("/api/baratok")
	public RESTResponse getBaratok()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();

		List<FriendResponse> friends = userService.sendFriendsByEmail(user);
		
		return new RESTResponse("Ok", friends);
	}

	@GetMapping("/api/beszelgetes")
	public RESTResponse getBeszelgetes(@RequestParam(value = "vele") String email)
	{
		if(!UserService.isValidEmail(email))		
			return new RESTResponse("Hibás e-mail!", null);		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
	
		List<Message> messages = messageService.getMessagesByEmails(user, email);
	
		List<MessageResponse> response = new ArrayList<>();
	
		for (Message m : messages)
		{
			response.add(new MessageResponse(m.getKuldo().getEmail(), m.getKuldo().getNickName(), m.getDate(), m.getMessage()));
		}
	
		return new RESTResponse("Ok", response);		
	}

	@GetMapping("/api/uzenet")
	public RESTResponse getUzenet(@RequestParam(value = "kinek") String email, @RequestParam(value = "szoveg") String message)
	{
		if(!UserService.isValidEmail(email))
			return new RESTResponse("Hibás e-mail!", null);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();
		
		if(!userService.isThereRelationshipBetweenTwo(email, user))
			return new RESTResponse("Nem vagy vele kapcsolatban!", null);
		
		if(!MessageService.isValidMessage(message))
			return new RESTResponse("Hibás üzenet!", null);
		
		messageService.createNewMessage(user, email, message, new Date());

		return new RESTResponse("Ok", null);
	}
}