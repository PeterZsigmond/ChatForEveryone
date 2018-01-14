package chatforeveryone.service;

public class MessageResponse
{
	private String kuldo;
	private String uzenet;
	
	public MessageResponse(String kuldo, String uzenet)
	{
		this.kuldo=kuldo;
		this.uzenet=uzenet;
	}

	public String getKuldo() {
		return kuldo;
	}

	public void setKuldo(String kuldo) {
		this.kuldo = kuldo;
	}

	public String getUzenet() {
		return uzenet;
	}

	public void setUzenet(String uzenet) {
		this.uzenet = uzenet;
	}
}