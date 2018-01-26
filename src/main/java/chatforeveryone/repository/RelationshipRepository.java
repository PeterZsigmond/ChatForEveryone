package chatforeveryone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import chatforeveryone.entity.Relationship;

public interface RelationshipRepository extends CrudRepository<Relationship, Long>
{	
	@Modifying
	@Query(value = "insert into relationships (sender_id, receiver_id) values ((select id from users where email=?1), (select id from users where email=?2))", nativeQuery=true)
	@Transactional
	void createNewRelationship(String email1, String email2);
	
	@Query(value = "select * from relationships where sender_id = (select id from users where email = ?1) and receiver_id = (select id from users where email = ?2) union "
			+ "select * from relationships where sender_id = (select id from users where email = ?2) and receiver_id = (select id from users where email = ?1);", nativeQuery=true)
	Relationship findRelationshipBetweenTwoUser(String email1, String email2);
}