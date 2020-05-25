package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.List;

import edu.sigmachi.poker.Pot.PotType;
import edu.sigmachi.poker.Messages.ClientActionMsg.Actions;

public class BettingCalculator {
  
  public static void bet(Player currentPlayer, Actions playerAction, List<Pot> pots, List<Player> activePlayers, BigDecimal betAmount) {
    BigDecimal currentBet = pots.get(pots.size() - 1).getCurrentBet();
    // All In
    if (playerAction == Actions.ALLIN) {
      Pot lastPot = pots.get(pots.size() - 1);
      Pot nextPot = new Pot(PotType.SIDE, lastPot.getCurrentBet(), lastPot.getPotAmount(), lastPot.getPlayers());
      lastPot.contributePot(currentPlayer, betAmount);
      pots.add(nextPot);
    } 
    // Call
    else if (playerAction == Actions.CALL) {
      Pot lastPot = pots.get(pots.size() - 1);
      lastPot.contributePot(currentPlayer, lastPot.getCurrentBet());
    }
    // Raise
    else if (playerAction == Actions.RAISE) {
      Pot lastPot = pots.get(pots.size() - 1);
      lastPot.clearInvolvedPlayers();
      lastPot.contributePot(currentPlayer, betAmount);
    }
  }
}
