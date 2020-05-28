package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.List;

import edu.sigmachi.poker.Messages.ClientActionMsg.Actions;

public class BettingCalculator {
  
  public static void bet(Player currentPlayer, Actions playerAction, List<Pot> pots, List<Player> activePlayers, BigDecimal betAmount) {
    // All In
    if (playerAction == Actions.ALLIN) {
      BigDecimal rollingBetAmount = betAmount;
      for (int i = 0; i < pots.size(); i++) {
        Pot curPot = pots.get(i);
        // Didn't meet next threshold, need to split and insert
        if (rollingBetAmount.compareTo(curPot.getCurrentBet()) != 1 || 
            (i == pots.size() - 1)) {
          Pot newPot = curPot.splitPot(rollingBetAmount);
          if (i == pots.size() - 1) {
            pots.add(newPot);
            // If this is not the last pot and the new pot we are creating is just 
            // zero then we don't actually need this new pot
          } else if (!newPot.getCurrentBet().equals(new BigDecimal("0.00"))) {
            pots.add(i + 1, newPot);
          }  
          curPot.contributePot(currentPlayer, rollingBetAmount);
          return;
        } 
        // This pot is not the one they are going all in on
        else {
          curPot.contributePot(currentPlayer, curPot.getCurrentBet());
          rollingBetAmount = rollingBetAmount.subtract(curPot.getCurrentBet());
        }
      }
    } 
    // Call
    else if (playerAction == Actions.CALL) {
      for (Pot pot : pots) {
        // if players pot contribution is less than current pot bet
        if (!pot.playerInPot(currentPlayer)) {
          pot.contributePot(currentPlayer, pot.getCurrentBet());
        }
        else if (pot.getPlayerContribution(currentPlayer).
            compareTo(pot.getCurrentBet()) == -1) {
          pot.contributePot(currentPlayer, pot.getCurrentBet());
        }
      }
    }
    // Raise
    else if (playerAction == Actions.RAISE) {
      BigDecimal rollingBetAmount = betAmount;
      
      for (int i = 0; i <  pots.size(); i++) {
        Pot curPot = pots.get(i);
        if (i == pots.size() - 1) {
          curPot.contributePot(currentPlayer, rollingBetAmount);
        }
        else {
          curPot.contributePot(currentPlayer, curPot.getCurrentBet());
          rollingBetAmount = rollingBetAmount.subtract(curPot.getCurrentBet());
        }
      }
    }
  }
  
  public static void balancePots(int startingIndex) {
    
  }
}
