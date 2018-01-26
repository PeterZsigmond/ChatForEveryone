package chatforeveryone.service;

import java.util.Date;

public class MessageResponse 
{
	private String senderEmail;
	private String senderName;
	private String message;
	private Date date;
	
	public MessageResponse(String senderEmail, String senderName, String message, Date date)
	{
		this.senderEmail = senderEmail;
		this.senderName = senderName;
		this.message = message;
		this.date = date;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}