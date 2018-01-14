package chatforeveryone.repository;

import org.springframework.data.repository.CrudRepository;
import chatforeveryone.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByRole(String role);

}