package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pot {
  public enum PotType {MAIN, SIDE};
  private BigDecimal potAmount;
  private List<Player> involvedPlayers;
  private PotType potType;
  private BigDecimal currentBet;
  
  public Pot (PotType type) {
    this.potType = type;
    potAmount = BigDecimal.ZERO;
    potAmount = potAmount.setScale(2);
    this.involvedPlayers = new ArrayList<>();
  }
  
  public Pot (PotType type, BigDecimal currentBet, BigDecimal currentPotAmount, List<Player> players) {
    this.potType = type;
    this.potAmount = currentPotAmount;
    this.currentBet = currentBet;
    this.involvedPlayers = players;
  }
  
  public BigDecimal getPotAmount() {
    return this.potAmount;
  }
  
  public void clearInvolvedPlayers() {
    this.involvedPlayers = new ArrayList<>();
  }
  
  public List<Player> getPlayers() {
    List<Player> players = new ArrayList<>(this.involvedPlayers);
    return players;
  }
  
  public void contributePot(Player player, BigDecimal amount) {
    this.potAmount = this.potAmount.add(amount);
    player.adjustBalance(amount.negate());
    if (involvedPlayers.contains(player)) {
      involvedPlayers.add(player);
    }
  }
  
  public void setCurrentPotBet(BigDecimal currentBet) {
    this.currentBet = currentBet;
  }
  
  public BigDecimal getCurrentBet() {
    return this.currentBet;
  }
  
  
  
  
}
