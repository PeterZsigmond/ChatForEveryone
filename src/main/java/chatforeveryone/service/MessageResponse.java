package chatforeveryone.service;

import java.util.Date;

public class MessageResponse {
	private String email;
	private String name;
	private Date date;
	private String message;

	public MessageResponse(String email, String name, Date date, String message) {

		this.email = email;
		this.name = name;
		this.date = date;
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}