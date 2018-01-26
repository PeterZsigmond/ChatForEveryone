package chatforeveryone.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import chatforeveryone.entity.User;

public interface UserRepository extends CrudRepository<User, Long>
{
	User findByEmail(String email);
	
	User findByName(String name);

	User findByActivationCode(String code);
	
	@Query(value = "select * from users where id = ANY (" + 
			"select receiver_id from relationships where accepted=1 and sender_id = (select id from users where email = ?1) union "
			+ "select sender_id from relationships where accepted=1 and receiver_id = (select id from users where email = ?1))", nativeQuery = true)
	Set<User> findRelationshipsByEmail(String email);
	
	@Query(value = "select * from users where id = any (" + 
			"select receiver_id from relationships where sender_id=(select id from users where email=?1) and accepted=0)", nativeQuery = true)
	Set<User> findWhoAuthUserSentButNotYetAccepted(String email);
	
	@Query(value="select * from users where id = any (" + 
			"select sender_id from relationships where receiver_id=(select id from users where email=?1) and accepted=0)", nativeQuery = true)
	Set<User> findWhoWaitsForAuthUserAccept(String email);
	
	@Query(value="select * from users where id = any(" + 
			"select sender_id from relationships where receiver_id = (select id from users where email=?1) and accepted=1 union "
			+ "select receiver_id from relationships where sender_id = (select id from users where email=?1) and accepted=1)", nativeQuery = true)
	List<User> findFriendsByEmail(String email);
}