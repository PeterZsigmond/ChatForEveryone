package chatforeveryone.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "relationships")
public class Relationship {
	@GeneratedValue
	@Id
	private Long id;

	@OneToOne
	private User kuldo;

	@OneToOne
	private User fogado;

	@Column(columnDefinition = "tinyint(1) default 0")
	private boolean elfogadva;

	private Relationship() {}

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

	public boolean isElfogadva() {
		return elfogadva;
	}

	public void setElfogadva(boolean elfogadva) {
		this.elfogadva = elfogadva;
	}

	@Override
	public String toString() {
		return "Relationship [id=" + id + ", kuldo=" + kuldo + ", fogado=" + fogado + ", elfogadva=" + elfogadva + "]";
	}

}