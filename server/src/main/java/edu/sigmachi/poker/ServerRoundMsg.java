package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerRoundMsg {
  private Map<String, BigDecimal> playersToBalances;
  private Map<String, BigDecimal> playersToBets;
  private String currentTurn;
  private List<String> communityCards;
  private List<String> mainPotContenders;
  private BigDecimal mainPotValue;
  private List<String> sidePotContenders;
  private BigDecimal sidePotValue;
  
  
  public ServerRoundMsg() {
    
  }
  
  public ServerRoundMsg(Map<String, BigDecimal> playersToBalances, Map<String, BigDecimal> playersToBets, 
      String currentTurn, List<String> communityCards,
      List<String> mainPotContenders, BigDecimal mainPotValue, 
      List<String> sidePotContenders, BigDecimal sidePotValue) {
    
    this.playersToBalances = playersToBalances;
    this.playersToBets = playersToBets;
    this.currentTurn = currentTurn;
    this.communityCards = communityCards;
    this.mainPotContenders = mainPotContenders;
    this.mainPotValue = mainPotValue;
    this.sidePotContenders = sidePotContenders;
    this.sidePotValue = sidePotValue;
  }
  
  
  public Map<String, BigDecimal> getPlayerBalances() {
    Map<String, BigDecimal> mapCopy = 
        this.playersToBalances.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> new BigDecimal(e.getValue().toString())));
    
    return mapCopy;
  }
  
  public Map<String, BigDecimal> getCurrentBets() {
    Map<String, BigDecimal> mapCopy = 
        this.playersToBets.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> new BigDecimal(e.getValue().toString())));
    
    return mapCopy;
  }
  
  public String getCurrentTurn() {
    return this.currentTurn;
  }
  
  public List<String> getCommunityCards() {
    List<String> listCopy = new ArrayList<String>(this.communityCards);
    return listCopy;
  }
  
  public List<String> getMainPotContenders() {
    List<String> listCopy = new ArrayList<String>(this.mainPotContenders);
    return listCopy;
  }
  
  public BigDecimal getMainPotValue() {
    return new BigDecimal(this.mainPotValue.toString());
  }
  
  public List<String> getSidePotContenders() {
    List<String> listCopy = new ArrayList<String>(this.sidePotContenders);
    return listCopy;
  }
  
  public BigDecimal getSidePotValue() {
    return new BigDecimal(this.sidePotValue.toString());
  }
}
