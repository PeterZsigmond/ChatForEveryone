package chatforeveryone.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import chatforeveryone.entity.Relationship;

public interface RelationshipRepository extends CrudRepository<Relationship, Long> {
	
	@Query(value = "select * from relationships where kuldo_id= (select id from users where email = ?1) and fogado_id = (select id from users where email = ?2) union "
			+ "select * from relationships where kuldo_id= (select id from users where email = ?2) and fogado_id = (select id from users where email = ?1)", nativeQuery = true)
	Relationship checkForRelationshipBetweenTwo(String g, String f);
	
	@Modifying
	@Query(value = "insert into relationships (kuldo_id, fogado_id) values ((select id from users where email=?1), (select id from users where email=?2))", nativeQuery=true)
	@Transactional
	void createNewRelationship(String one, String two);
	
	@Query(value = "select * from relationships where kuldo_id = (select id from users where email = ?1) and fogado_id = (select id from users where email = ?2) union "
			+ "select * from relationships where kuldo_id = (select id from users where email = ?2) and fogado_id = (select id from users where email = ?1);", nativeQuery=true)
	Relationship findRelationshipByTwoUser(String c, String d);
}