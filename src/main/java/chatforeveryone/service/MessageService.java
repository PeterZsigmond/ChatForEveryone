package chatforeveryone.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chatforeveryone.entity.Message;
import chatforeveryone.repository.MessageRepository;

@Service
public class MessageService
{
	@Autowired
	private MessageRepository messageRepository;

	public int getNumberOfMessagesByEmails(String email1, String email2)
	{
		return messageRepository.getNumberOfMessagesByEmails(email1, email2);
	}
	
	public List<Message> getMessagesByEmails(String email1, String email2)
	{
		return messageRepository.getMessagesByEmails(email1, email2);
	}

	public void createNewMessage(String email1, String email2, String message, Date date)
	{
		messageRepository.createNewMessage(email1, email2, message, date);
	}
	
	public static boolean isValidMessage(String message)
	{
		return (message != null && !message.isEmpty() && message.length() > 0 && message.length() < 1000);
	}
}