package chatforeveryone.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "messages")
public class Message {
	@GeneratedValue
	@Id
	private Long id;

	@OneToOne
	private User kuldo;

	@OneToOne
	private User fogado;

	@Column(columnDefinition = "TEXT")
	private String message;

	private Date date;

	private Message() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getKuldo() {
		return kuldo;
	}

	public void setKuldo(User kuldo) {
		this.kuldo = kuldo;
	}

	public User getFogado() {
		return fogado;
	}

	public void setFogado(User fogado) {
		this.fogado = fogado;
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

	@Override
	public String toString() {
		return "Message [id=" + id + ", kuldo=" + kuldo + ", fogado=" + fogado + ", message=" + message + ", date="
				+ date + "]";
	}

}