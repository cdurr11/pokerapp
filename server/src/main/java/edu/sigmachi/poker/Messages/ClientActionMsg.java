package edu.sigmachi.poker.Messages;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ClientActionMsg implements InstantGameMsg {
  
  public enum Actions {CALL, CHECK, FOLD, RAISE, ALLIN};
  
  private String playerName;
  private String action;
  private String raiseAmount; 
  
  public ClientActionMsg() {
    
  }
  
  public ClientActionMsg(String playerName, String action, String raiseAmount) {
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
  
  public Actions getAction() {
    switch (this.action) {
      case "CALL":
        return Actions.CALL;
          
      case "ALLIN":
          return Actions.ALLIN;
          
      case "CHECK":
        return Actions.CHECK;
        
      case "FOLD":
        return Actions.FOLD;
      
      case "RAISE":
        return Actions.RAISE;
        
      default:
        throw new RuntimeException("Should Select an Action Case");
    }
  }
  
  public String getPlayerName() {
    return this.playerName;
  }
  
  public BigDecimal getRaiseAmount() {
    // We should only have a raise amount when we are raising or else ""
    assert ((!raiseAmount.equals("") && this.action.equals("RAISE")) ||
             (raiseAmount.equals("") && !this.action.equals("RAISE")));
    BigDecimal bigDecimalRaiseAmount = new BigDecimal(raiseAmount);
    bigDecimalRaiseAmount = bigDecimalRaiseAmount.setScale(2, RoundingMode.CEILING);
    return bigDecimalRaiseAmount;
  }
}
