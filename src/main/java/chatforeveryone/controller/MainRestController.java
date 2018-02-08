package chatforeveryone.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chatforeveryone.entity.Message;
import chatforeveryone.entity.User;
import chatforeveryone.service.FriendResponse;
import chatforeveryone.service.MessageResponse;
import chatforeveryone.service.MessageService;
import chatforeveryone.service.RestResponse;
import chatforeveryone.service.UserService;

@RestController
public class MainRestController
{
	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@RequestMapping("/api/getFriends")
	public RestResponse getFriends()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authUser = auth.getName();
		
		List<FriendResponse> friends = userService.findFriendsByEmail(authUser);
		
		return new RestResponse("Ok", friends);
	}
	
	@GetMapping("/api/getNumberOfMessages")
	public RestResponse getNumberOfMessages(@RequestParam(value = "with") String email)
	{
		if(!UserService.isValidEmail(email))		
			return new RestResponse("Hibás e-mail!", null);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authUser = auth.getName();
		
		if(!userService.isThereRelationshipBetweenTwo(authUser, email))
			return new RestResponse("Nem vagy vele kapcsolatban!", null);	
	
		int numberOfMessages = messageService.getNumberOfMessagesByEmails(authUser, email);
		
		return new RestResponse("Ok", numberOfMessages);		
	}

	@GetMapping("/api/getMessages")
	public RestResponse getMessages(@RequestParam(value = "with") String email)
	{
		if(!UserService.isValidEmail(email))		
			return new RestResponse("Hibás e-mail!", null);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authUser = auth.getName();
		
		if(!userService.isThereRelationshipBetweenTwo(authUser, email))
			return new RestResponse("Nem vagy vele kapcsolatban!", null);
	
		List<Message> messages = messageService.getMessagesByEmails(authUser, email);
	
		List<MessageResponse> response = new ArrayList<>();
	
		for(Message message : messages)
		{
			response.add(new MessageResponse(message.getSender().getEmail(), message.getSender().getName(), message.getMessage(), message.getDate()));
		}
	
		return new RestResponse("Ok", response);		
	}

	@GetMapping("/api/newMessage")
	public RestResponse newMessage(@RequestParam(value = "for") String email, @RequestParam(value = "message") String message)
	{
		if(!UserService.isValidEmail(email))
			return new RestResponse("Hibás e-mail!", null);
		
		if(!MessageService.isValidMessage(message))
			return new RestResponse("Hibás üzenet!", null);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authUser = auth.getName();
		
		if(authUser.equals(email))
			return new RestResponse("Magadnak nem küldhetsz üzenetet!", null);
		
		if(!userService.isThereRelationshipBetweenTwo(authUser, email))
			return new RestResponse("Nem vagy vele kapcsolatban!", null);		
		
		messageService.createNewMessage(authUser, email, message, new Date());

		return new RestResponse("Ok", null);
	}
	
	@GetMapping("/api/findWhoAuthUserSentButNotYetAccepted")
	public RestResponse findWhoAuthUserSentButNotYetAccepted()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authUser = auth.getName();	
	
		Set<User> requested = userService.findWhoAuthUserSentButNotYetAccepted(authUser);
		Set<String> emails = new LinkedHashSet<>();
		
		for(User user : requested)
		{
			emails.add(user.getEmail());
		}
		
		return new RestResponse("Ok", emails);		
	}
	
	@GetMapping("/api/findWhoWaitsForAuthUserAccept")
	public RestResponse findWhoWaitsForAuthUserAccept()
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String authUser = auth.getName();	
	
		Set<User> requested = userService.findWhoWaitsForAuthUserAccept(authUser);
		Set<String> emails = new LinkedHashSet<>();
		
		for(User user : requested)
		{
			emails.add(user.getEmail());
		}
		
		return new RestResponse("Ok", emails);		
	}
}