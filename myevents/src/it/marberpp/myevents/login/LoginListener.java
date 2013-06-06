package it.marberpp.myevents.login;

public interface LoginListener {
	
	public void newUserRequired();
	public void newUserCreated(String username, String password);
	public void loginCompleted(String username, String password);
	
}
