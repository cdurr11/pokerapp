package edu.sigmachi.poker.Messages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EndOfRoundMsg {
  private Map<String, List<String>> playersToCards;
  private List<String> mainPotWinningPlayers;
  private List<String> sidePotWinningPlayers;
  private Map<String, BigDecimal> playersToBalances;
  
  
  public EndOfRoundMsg() {
    
  }
  
  public EndOfRoundMsg(Map<String, List<String>> finalPlayersToCards, List<String> mainPotWinningPlayers,
      List<String> sidePotWinningPlayers, Map<String, BigDecimal> playersToBalances) {
    this.playersToCards = finalPlayersToCards;
    this.mainPotWinningPlayers = mainPotWinningPlayers;
    this.sidePotWinningPlayers = sidePotWinningPlayers;
    this.playersToBalances = playersToBalances;
  }
  
  public Map<String, List<String>> getFinalPlayersToCards() {
    Map<String, List<String>> mapCopy = 
        this.playersToCards.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> List.copyOf(e.getValue())));
        
    return mapCopy;
  }
  
  public List<String> getMainPotWinningPlayers() {
    List<String> listCopy = new ArrayList<String>(this.mainPotWinningPlayers);
    return listCopy;
  }
  
  public List<String> getSidePotWinningPlayers() {
    List<String> listCopy = new ArrayList<String>(sidePotWinningPlayers);
    return listCopy;
  }
  
  public Map<String, BigDecimal> getFinalBalances() {
    Map<String, BigDecimal> mapCopy = 
        this.playersToBalances.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey(), e -> new BigDecimal(e.getValue().toString())));
        
    return mapCopy;
  }
}
