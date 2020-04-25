package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class StartOfRoundMsg {
  private Map<String, BigDecimal> playerToBalances;
  private Map<String, List<String>> playerToCards;
  private List<String> communityCards;
  private String bigBlind;
  private String smallBlind;
  private String dealer;
  
  public StartOfRoundMsg() {
    
  }
  
  public StartOfRoundMsg(Map<String, BigDecimal> playerToBalances, Map<String, List<String>> playerToCards,
      List<String> communityCards, String bigBlind, String smallBlind, String dealer) {
    this.playerToBalances = playerToBalances;
    this.playerToCards = playerToCards;
    this.communityCards = communityCards;
    this.bigBlind = bigBlind;
    this.smallBlind = smallBlind;
    this.dealer = dealer;
  }
  //TODO some gnarly rep exposure in these guys gotta fix
  public Map<String, BigDecimal> getPlayerToBalances() {
    return this.playerToBalances;
  }
  
  public Map<String, List<String>> getPlayerToCards() {
    return this.playerToCards;
  }
  
  public List<String> getCommunityCards() {
    return this.communityCards;
  }
  
  public String getBigBlind() {
    return this.bigBlind;
  }
  
  public String getSmallBling() {
    return this.smallBlind;
  }
  
  public String getDealer() {
    return this.dealer;
  }
}
