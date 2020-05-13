package edu.sigmachi.poker.Messages;

import java.math.BigDecimal;

public class AddPlayerMoneyMsg implements AfterRoundMsg, ConsoleMsg {
  private final String playerName;
  private final BigDecimal dollarAmount;
  
  public AddPlayerMoneyMsg(String playerName, BigDecimal dollarAmount) {
    this.playerName = playerName;
    this.dollarAmount = dollarAmount;
  }
  
  public ConsoleAction getAction() {
    return ConsoleAction.ADD_PLAYER_MONEY;
  }
  
  public BigDecimal getDollarAmount() {
    return this.dollarAmount;
  }
  
  public String getPlayerName() {
    return this.playerName;
  }  
}
