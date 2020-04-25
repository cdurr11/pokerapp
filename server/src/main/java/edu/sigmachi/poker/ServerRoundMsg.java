package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ServerRoundMsg {
  private Map<String, BigDecimal> playerBalance;
  private Map<String, BigDecimal> currentBets;
  private String currentTurn;
  private List<String> communityCards;
  private List<String> mainPotContenders;
  private BigDecimal mainPotValue;
  private List<String> sidePotContenders;
  private BigDecimal sidePotValue;
  
  
  public ServerRoundMsg() {
    
  }
  
  public ServerRoundMsg(Map<String, BigDecimal> playerBalance, Map<String, BigDecimal> currentBets, 
      String currentTurn, List<String> communityCards,
      List<String> mainPotContenders, BigDecimal mainPotValue, 
      List<String> sidePotContenders, BigDecimal sidePotValue) {
    
    this.playerBalance = playerBalance;
    this.currentBets = currentBets;
    this.currentTurn = currentTurn;
    this.communityCards = communityCards;
    this.mainPotContenders = mainPotContenders;
    this.mainPotValue = mainPotValue;
    this.sidePotContenders = sidePotContenders;
    this.sidePotValue = sidePotValue;
  }
  
  
  public Map<String, BigDecimal> getPlayerBalance() {
    return this.playerBalance;
  }
  
  public Map<String, BigDecimal> getCurrentBet() {
    return this.currentBets;
  }
  
  public String getCurrentTurn() {
    return this.currentTurn;
  }
  
  public List<String> getCommunityCards() {
    return this.communityCards;
  }
  
  public List<String> getMainPotContenders() {
    return this.mainPotContenders;
  }
  
  public BigDecimal getMainPotValues() {
    return this.mainPotValue;
  }
  
  public List<String> getSidePotContenders() {
    return this.sidePotContenders;
  }
  
  public BigDecimal getSidePotValue() {
    return this.sidePotValue;
  }
}
