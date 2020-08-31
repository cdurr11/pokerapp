package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.*;

public class Pot {
  public enum PotType {MAIN, SIDE};
  private BigDecimal potAmount = new BigDecimal("0.00");
  private PotType potType;
  private BigDecimal currentBet = new BigDecimal("0.00");
  private Map<Player, BigDecimal> playerToContribution;
  
  public Pot (PotType type) {
    this.potType = type;
    potAmount = new BigDecimal("0.00");
    this.playerToContribution = new HashMap<>();
  }
  
  public Pot (PotType type, BigDecimal currentBet, 
      Map<Player, BigDecimal> playerToContribution) {
    this.potType = type;
    this.currentBet = currentBet;
    this.playerToContribution = playerToContribution;
    updatePot();
  }
  
  public BigDecimal getPotAmount() {
    return this.potAmount;
  }
  
  private void updatePot() {
    BigDecimal pot = BigDecimal.ZERO;
    pot = pot.setScale(2);
    for (Player player : this.playerToContribution.keySet()) {
      pot = pot.add(this.playerToContribution.get(player));
    }
    this.potAmount = pot;
  }
  
  public void contributePot(Player player, BigDecimal amount) {
    // If we're raising set the new bet
    if (amount.compareTo(this.currentBet) == 1) {
      this.currentBet = amount;
    }
    this.playerToContribution.put(player, this.currentBet);
    updatePot();
  }
  
  public Pot splitPot(BigDecimal value) {
    Map<Player, BigDecimal> newPotMap = new HashMap<>();
    this.currentBet = value;
    BigDecimal newBet = new BigDecimal("0.00");
    for (Player player : this.playerToContribution.keySet()) {
      if (this.playerToContribution.get(player).compareTo(value) == 1) {
        BigDecimal difference = this.playerToContribution.get(player).subtract(value);
        this.playerToContribution.put(player, value);
        newPotMap.put(player, difference);
        if (difference.compareTo(newBet) == 1) {
          newBet = difference;
        }
      }
    }
    return new Pot(PotType.SIDE, newBet, newPotMap);
  }
  
  public void setCurrentPot(BigDecimal currentPot) {
    this.potAmount = currentPot;
  }
  
  public BigDecimal getCurrentBet() {
    return this.currentBet;
  }
  
  public void setCurrentBet(BigDecimal currentBet) {
    this.currentBet = currentBet;
  }
  
  public boolean playerInPot(Player player) {
    return this.playerToContribution.containsKey(player);
  }
  
  public BigDecimal getPlayerContribution(Player player) {
    return this.playerToContribution.get(player);
  }

  public Set<Player> getPlayersInPot() {
    return this.playerToContribution.keySet();
  }

  public Pot makeCopy() {
    HashMap<Player, BigDecimal> playerToContributionCopy = new HashMap<>();
    for (Player player: this.playerToContribution.keySet()) {
      playerToContributionCopy.put(player.makeCopy(), this.playerToContribution.get(player));
    }
    return new Pot (this.potType, this.currentBet,
            playerToContributionCopy);
  }
}
