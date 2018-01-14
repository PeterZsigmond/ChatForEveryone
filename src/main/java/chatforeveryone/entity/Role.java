package chatforeveryone.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "roles")
public class Role {
	
	@GeneratedValue
	@Id
	private Long id;

	private String role;

	@SuppressWarnings("unused")
	private Role() {
	}

	public Role(String role) {
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
