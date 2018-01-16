package chatforeveryone.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import chatforeveryone.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
	User findByNickName(String nick);

	User findByActivationCode(String code);
	
	@Query(value = "select * from users where id = ANY (" + 
			"select fogado_id from relationships where elfogadva=1 and kuldo_id = (select id from users where email = ?1) union "
			+ "select kuldo_id from relationships where elfogadva=1 and fogado_id = (select id from users where email = ?1))", nativeQuery = true)
	Set<User> findRelationshipsByEmail(String mail);
	
	@Query(value = "select * from users where id = any (\n" + 
			"select fogado_id from relationships where kuldo_id=(select id from users where email=?1) and elfogadva=0)", nativeQuery = true)
	Set<User> findWhoUserSentButNotYetElfogadva(String email);
	
	@Query(value="select * from users where id = any (\n" + 
			"select kuldo_id from relationships where fogado_id=(select id from users where email=?1) and elfogadva=0)", nativeQuery = true)
	Set<User> findWhoWaitsForMyElfogadva(String email);
	
	@Query(value="select email from users where id = any(\n" + 
			"select kuldo_id from relationships where fogado_id = (select id from users where email=?1) and elfogadva=1 union "
			+ "select fogado_id from relationships where kuldo_id = (select id from users where email=?1) and elfogadva=1)", nativeQuery = true)
	List<String> findFriendsByEmail(String mail);
}