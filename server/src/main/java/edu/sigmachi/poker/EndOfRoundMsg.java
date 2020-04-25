package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class EndOfRoundMsg {
  private Map<String, List<String>> finalPlayersToCards;
  private List<String> mainPotWinningPlayers;
  private List<String> sidePotWinningPlayers;
  private Map<String, BigDecimal> finalBalances;
  
  
  public EndOfRoundMsg() {
    
  }
  
  public EndOfRoundMsg(Map<String, List<String>> finalPlayersToCards, List<String> mainPotWinningPlayers,
      List<String> sidePotWinningPlayers, Map<String, BigDecimal> finalBalances) {
    this.finalPlayersToCards = finalPlayersToCards;
    this.mainPotWinningPlayers = mainPotWinningPlayers;
    this.sidePotWinningPlayers = sidePotWinningPlayers;
    this.finalBalances = finalBalances;
  }
  
  public Map<String, List<String>> getFinalPlayersToCards() {
    return this.finalPlayersToCards;
  }
  
  public List<String> getMainPotWinningPlayers() {
    return this.mainPotWinningPlayers;
  }
  
  public List<String> getSidePotWinningPlayers() {
    return this.sidePotWinningPlayers;
  }
  
  public Map<String, BigDecimal> finalBalances() {
    return this.finalBalances;
  }
}
