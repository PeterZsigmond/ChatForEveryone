package chatforeveryone.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{
	private final Log log = LogFactory.getLog(this.getClass());

	@Value("${email.welcomer}")
	private String emailWelcomer;

	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;

	@Value("${email.server.full.address}")
	private String emailServerFullAddress;

	private JavaMailSender javaMailSender;

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMessage(String email, String name, String generatedKey)
	{
		SimpleMailMessage message = null;

		try {
			message = new SimpleMailMessage();
			message.setFrom(MESSAGE_FROM);
			message.setTo(email);
			message.setSubject("Sikeres regisztrálás");
			message.setText("Kedves " + name + "!\n\nKérlek akitváld a felhasználód a következő linken: " + emailServerFullAddress + "/aktivalas/" + generatedKey + "\n\nÜdvözlettel: " + emailWelcomer);
			javaMailSender.send(message);

		} catch (Exception e) {
			log.error("Hiba e-mail küldéskor az alábbi címre: " + email + "  " + e);
		}

	}

}
