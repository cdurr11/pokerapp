package edu.sigmachi.poker.Messages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StartOfRoundMsg {
  private Map<String, BigDecimal> playersToBalances;
  private Map<String, List<String>> playersToCards;
  private List<String> communityCards;
  private String bigBlind;
  private String smallBlind;
  private String dealer;
  
  public StartOfRoundMsg() {
    
  }
  
  public StartOfRoundMsg(Map<String, BigDecimal> playerToBalances, Map<String, List<String>> playersToCards,
      List<String> communityCards, String bigBlind, String smallBlind, String dealer) {
    this.playersToBalances = playerToBalances;
    this.playersToCards = playersToCards;
    this.communityCards = communityCards;
    this.bigBlind = bigBlind;
    this.smallBlind = smallBlind;
    this.dealer = dealer;
  }

  public Map<String, BigDecimal> getPlayerToBalances() {
    Map<String, BigDecimal> mapCopy = 
        this.playersToBalances.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> new BigDecimal(e.getValue().toString())));
    
    return mapCopy;
  }
  
  public Map<String, List<String>> getPlayerToCards() {
    Map<String, List<String>> mapCopy = 
        this.playersToCards.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> List.copyOf(e.getValue())));
        
    return mapCopy;
  }
  
  public List<String> getCommunityCards() {
    List<String> listCopy = new ArrayList<String>(this.communityCards);
    return listCopy;
  }
  
  public String getBigBlind() {
    return this.bigBlind;
  }
  
  public String getSmallBlind() {
    return this.smallBlind;
  }
  
  public String getDealer() {
    return this.dealer;
  }
}
