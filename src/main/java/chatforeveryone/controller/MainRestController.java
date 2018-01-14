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
import chatforeveryone.service.MessageResponse;
import chatforeveryone.service.MessageService;
import chatforeveryone.service.RESTResponse;
import chatforeveryone.service.UserService;

@RestController
public class MainRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@GetMapping(value = "/baratok")
	public RESTResponse getBaratok() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();

		List<String> baratok = userService.findFriendsByEmail(user);

		RESTResponse response = new RESTResponse("Done", baratok);

		return response;
	}

	@RequestMapping(value = "/beszelgetes")
	public RESTResponse getBeszelgetes(@RequestParam(value = "vele") String name) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();

		List<Message> messages = messageService.getMessagesByEmails(user, name);

		List<MessageResponse> response = new ArrayList<>();

		for (Message m : messages) {
			response.add(new MessageResponse(m.getKuldo().getEmail(), m.getMessage()));
		}

		return new RESTResponse("Done", response);
	}

	@RequestMapping(value = "/uzenet")
	public RESTResponse getUzenet(@RequestParam(value = "kinek") String kinek,
			@RequestParam(value = "szoveg") String message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = auth.getName();

		messageService.createNewMessage(user, kinek, message, new Date());

		return new RESTResponse("Done", "Sending message done.");
	}
}