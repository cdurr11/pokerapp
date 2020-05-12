package edu.sigmachi.poker;

public class LoginAttemptMsg {
  private String playerName;
  private String providedPassword;
  
  public LoginAttemptMsg() {
    
  }
  
  public LoginAttemptMsg(String playerName, String providedPassword) {
    this.playerName = playerName;
    this.providedPassword = providedPassword;
  }
  
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
  
  public void setProvidedPassword(String providedPassword) {
    this.providedPassword = providedPassword;
  }
  
  public String getPlayerName() { 
    return this.playerName;
  }
  
  public String getProvidedPassword() {
    return this.providedPassword;
  }
}
