package edu.sigmachi.poker;

import java.math.BigDecimal;

public class ClientRoundMsg {
  
  enum actions {CALL, CHECK, FOLD, RAISE};
  
  private String playerName;
  private String action;
  private String raiseAmount; 
  
  public ClientRoundMsg() {
    
  }
  
  public ClientRoundMsg(String playerName, String action, String raiseAmount) {
    this.playerName = playerName;
    this.action = action;
    this.raiseAmount = raiseAmount;
  }
  
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }
  
  public void setAction(String action) {
    this.action = action;
  }
  
  public void setRaiseAmount(String raiseAmount) {
    this.raiseAmount = raiseAmount;
  }
  
  public actions getAction() {
    switch (this.action) {
      case "CALL":
        return actions.CALL;
      
      case "CHECK":
        return actions.CHECK;
        
      case "FOLD":
        return actions.FOLD;
      
      case "Raise":
        return actions.RAISE;
        
      default:
        throw new RuntimeException("Should Select an Action Case");
    }
  }
  
  public String getPlayerName() {
    return this.playerName;
  }
  
  //TODO probs should handle some kind of error here
  public BigDecimal getRaiseAmount() {
    return new BigDecimal(raiseAmount);
  }
}
