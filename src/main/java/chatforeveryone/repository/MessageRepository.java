package chatforeveryone.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import chatforeveryone.entity.Message;

public interface MessageRepository extends CrudRepository<Message, Long>
{
	@Query(value="select * from messages where id = any(\r\n" + 
			"select id from messages where kuldo_id = (select id from users where email=?1) and fogado_id = (select id from users where email=?2)\r\n" + 
			"UNION\r\n" + 
			"select id from messages where kuldo_id = (select id from users where email=?2) and fogado_id = (select id from users where email=?1))\r\n" + 
			"order by date", nativeQuery = true)
	List<Message> getMessagesByEmails(String email1, String email2);
	
	
	@Modifying
	@Query(value = 	"insert into messages (kuldo_id, fogado_id, message, date) values ((select id from users where email=?1), (select id from users where email=?2), ?3, ?4);" , nativeQuery=true)
	@Transactional
	void createNewMessage(String email1, String email2, String message, Date date);
}