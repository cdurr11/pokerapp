package edu.sigmachi.poker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import edu.sigmachi.poker.Pot.PotType;
import edu.sigmachi.poker.Messages.ClientActionMsg.Actions;

public class BettingCalculatorTest {
  private Player player1;
  private Player player2;
  private Player player3;
  private Player player4;
  private Player player5;
  private List<Pot> pots;
  private List<Player> playerList;
  
  @Before
  public void setUpPlayers() {
    BigDecimal initialBalance = new BigDecimal("10.00");
    
    this.player1 = new Player("cdurr", UUID.randomUUID(), initialBalance);
    this.player2 = new Player("mheatzig", UUID.randomUUID(), initialBalance);
    this.player3 = new Player("pruh", UUID.randomUUID(), initialBalance);
    this.player4 = new Player("blevin", UUID.randomUUID(), initialBalance);
    this.player5 = new Player("oheins", UUID.randomUUID(), initialBalance);
    
    this.pots = new LinkedList<>();
    BigDecimal currentBet = new BigDecimal("0.00");
    currentBet = currentBet.setScale(2);
    
    this.playerList = new ArrayList<Player>( 
        Arrays.asList(this.player1, this.player2, this.player3,
            this.player4, this.player5)); 
    
    this.pots.add(new Pot(PotType.MAIN));
  }
  
  @Test
  public void testBasicRaise() {
    BigDecimal betAmount = new BigDecimal("1.00");
    Pot mainPot = this.pots.get(0);
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, betAmount);
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("1.00")));
    assertEquals(0, mainPot.getPotAmount().compareTo(new BigDecimal("1.00")));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player1));
  }
  
  @Test
  public void testSubsequentRaise() {
    Pot mainPot = this.pots.get(0);
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("1.00")));
    assertEquals(0, mainPot.getPotAmount().compareTo(new BigDecimal("1.00")));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player1));
    
    BettingCalculator.bet(this.player2, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("2.00"));
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("2.00")));
    assertEquals(0, mainPot.getPotAmount().compareTo(new BigDecimal("3.00")));
    assertEquals(new BigDecimal("2.00"), mainPot.getPlayerContribution(this.player2));
    
    BettingCalculator.bet(this.player3, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("3.00"));
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("3.00")));
    assertEquals(0, mainPot.getPotAmount().compareTo(new BigDecimal("6.00")));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player3));
  }
  
  @Test
  public void testRaiseThenCalls() {
    Pot mainPot = this.pots.get(0);
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    
    BettingCalculator.bet(this.player2, Actions.CALL, this.pots, this.playerList, 
        new BigDecimal("0.00"));
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("1.00")));
    assertEquals(0, mainPot.getPotAmount().compareTo(new BigDecimal("2.00")));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player2));
    
    BettingCalculator.bet(this.player3, Actions.CALL, this.pots, this.playerList, 
        new BigDecimal("0.00"));
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("1.00")));
    assertEquals(0, mainPot.getPotAmount().compareTo(new BigDecimal("3.00")));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player3));
  }
  
  @Test
  public void testBasicAllIn() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("5.00"));
    
    BettingCalculator.bet(this.player2, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("3.00"));
    
    Pot sidePot = this.pots.get(1);
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("3.00")));
    assertEquals(2, this.pots.size());
    assertEquals(new BigDecimal("6.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player2));
    
    assertEquals(0, sidePot.getCurrentBet().compareTo(new BigDecimal("2.00")));
    assertEquals(new BigDecimal("2.00"), sidePot.getPotAmount());
    assertEquals(new BigDecimal("2.00"), sidePot.getPlayerContribution(this.player1));
    assertTrue(sidePot.playerInPot(this.player1));
    assertFalse(sidePot.playerInPot(this.player2));
  }
  
  @Test
  public void testCallingSidePot() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("5.00"));
    
    BettingCalculator.bet(this.player2, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("3.00"));
    
    BettingCalculator.bet(this.player3, Actions.CALL, this.pots, this.playerList, 
        new BigDecimal("0.00"));
    
    Pot sidePot = this.pots.get(1);
    
    assertTrue(mainPot.playerInPot(this.player2));
    assertTrue(mainPot.playerInPot(this.player3));
    assertTrue(mainPot.playerInPot(this.player1));
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("3.00")));
    assertEquals(2, this.pots.size());
    assertEquals(new BigDecimal("9.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player2));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player3));
    
    assertFalse(sidePot.playerInPot(this.player2));
    assertTrue(sidePot.playerInPot(this.player3));
    assertTrue(sidePot.playerInPot(this.player1));
    
    assertEquals(0, sidePot.getCurrentBet().compareTo(new BigDecimal("2.00")));
    assertEquals(new BigDecimal("4.00"), sidePot.getPotAmount());
    assertEquals(new BigDecimal("2.00"), sidePot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("2.00"), sidePot.getPlayerContribution(this.player3));
  }
  
  @Test
  public void testRaisingSidePot() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("5.00"));
    
    BettingCalculator.bet(this.player2, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("3.00"));
    
    BettingCalculator.bet(this.player3, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("6.00"));
    
    Pot sidePot = this.pots.get(1);
    
    assertTrue(mainPot.playerInPot(this.player2));
    assertTrue(mainPot.playerInPot(this.player3));
    assertTrue(mainPot.playerInPot(this.player1));
    
    assertEquals(0, mainPot.getCurrentBet().compareTo(new BigDecimal("3.00")));
    assertEquals(2, this.pots.size());
    assertEquals(new BigDecimal("9.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player2));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player3));
    
    assertFalse(sidePot.playerInPot(this.player2));
    assertTrue(sidePot.playerInPot(this.player3));
    assertTrue(sidePot.playerInPot(this.player1));
    
    assertEquals(0, sidePot.getCurrentBet().compareTo(new BigDecimal("3.00")));
    assertEquals(new BigDecimal("5.00"), sidePot.getPotAmount());
    assertEquals(new BigDecimal("2.00"), sidePot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("3.00"), sidePot.getPlayerContribution(this.player3));
  }
  
  @Test
  public void testMultipleSidePots() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    
    BettingCalculator.bet(this.player2, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    
    BettingCalculator.bet(this.player3, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("2.00"));
    
    
    BettingCalculator.bet(this.player4, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("3.00"));
    
    assertEquals(4, this.pots.size());
    
    Pot sidePot1 = this.pots.get(1);
    Pot sidePot2 = this.pots.get(2);
    
    assertTrue(mainPot.playerInPot(this.player2));
    assertTrue(mainPot.playerInPot(this.player3));
    assertTrue(mainPot.playerInPot(this.player4));
    
    assertEquals(new BigDecimal("4.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player2));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player4));
    
    assertFalse(sidePot1.playerInPot(this.player2));
    assertTrue(sidePot1.playerInPot(this.player3));
    assertTrue(sidePot1.playerInPot(this.player4));
    
    assertEquals(new BigDecimal("2.00"), sidePot1.getPotAmount());
    assertEquals(new BigDecimal("1.00"), sidePot1.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("1.00"), sidePot1.getPlayerContribution(this.player4));
    
    assertFalse(sidePot2.playerInPot(this.player2));
    assertFalse(sidePot2.playerInPot(this.player3));
    assertTrue(sidePot2.playerInPot(this.player4));
    
    assertEquals(new BigDecimal("1.00"), sidePot2.getPotAmount());
    assertEquals(new BigDecimal("1.00"), sidePot2.getPlayerContribution(this.player4));
  }
  
  @Test
  public void testSplitMainPotWithLowerAllIn() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    
    BettingCalculator.bet(this.player2, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("2.00"));
    
    BettingCalculator.bet(this.player3, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("4.00"));
    
    
    BettingCalculator.bet(this.player4, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("5.00"));
    
    BettingCalculator.bet(this.player5, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("3.00"));
    
    assertEquals(2, this.pots.size());
    
    Pot sidePot1 = this.pots.get(1);
    
    assertTrue(mainPot.playerInPot(this.player1));
    assertTrue(mainPot.playerInPot(this.player2));
    assertTrue(mainPot.playerInPot(this.player3));
    assertTrue(mainPot.playerInPot(this.player4));
    assertTrue(mainPot.playerInPot(this.player5));
    
    assertEquals(new BigDecimal("12.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("2.00"), mainPot.getPlayerContribution(this.player2));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player4));
    assertEquals(new BigDecimal("3.00"), mainPot.getPlayerContribution(this.player5));
    
    assertTrue(sidePot1.playerInPot(this.player3));
    assertTrue(sidePot1.playerInPot(this.player4));
    
    assertEquals(new BigDecimal("3.00"), sidePot1.getPotAmount());
    assertEquals(new BigDecimal("1.00"), sidePot1.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("2.00"), sidePot1.getPlayerContribution(this.player4));
  }
  
  @Test
  public void testCallSidePotManyPlayers() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    
    BettingCalculator.bet(this.player2, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("2.00"));
    
    BettingCalculator.bet(this.player3, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("5.00"));
    
    
    BettingCalculator.bet(this.player4, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("4.00"));
    
    BettingCalculator.bet(this.player5, Actions.CALL, this.pots, this.playerList, 
        new BigDecimal("0.00"));
    
    assertEquals(2, this.pots.size());
    
    Pot sidePot1 = this.pots.get(1);
    
    assertTrue(mainPot.playerInPot(this.player1));
    assertTrue(mainPot.playerInPot(this.player2));
    assertTrue(mainPot.playerInPot(this.player3));
    assertTrue(mainPot.playerInPot(this.player4));
    assertTrue(mainPot.playerInPot(this.player5));
    
    assertEquals(new BigDecimal("15.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("2.00"), mainPot.getPlayerContribution(this.player2));
    assertEquals(new BigDecimal("4.00"), mainPot.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("4.00"), mainPot.getPlayerContribution(this.player4));
    assertEquals(new BigDecimal("4.00"), mainPot.getPlayerContribution(this.player4));
    
    assertTrue(sidePot1.playerInPot(this.player3));
    assertTrue(sidePot1.playerInPot(this.player5));
    
    assertEquals(new BigDecimal("2.00"), sidePot1.getPotAmount());
    assertEquals(new BigDecimal("1.00"), sidePot1.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("1.00"), sidePot1.getPlayerContribution(this.player5));
  }
  
  @Test
  public void testTwoPlayersAllInWithSameAmount() {
    Pot mainPot = this.pots.get(0);
    
    BettingCalculator.bet(this.player1, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("1.00"));
    
    BettingCalculator.bet(this.player2, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("2.00"));
    
    BettingCalculator.bet(this.player3, Actions.RAISE, this.pots, this.playerList, 
        new BigDecimal("5.00"));
    
    BettingCalculator.bet(this.player4, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("4.00"));
    
    BettingCalculator.bet(this.player5, Actions.ALLIN, this.pots, this.playerList, 
        new BigDecimal("4.00"));
    
    assertEquals(2, this.pots.size());
    
    Pot sidePot1 = this.pots.get(1);
    
    assertTrue(mainPot.playerInPot(this.player1));
    assertTrue(mainPot.playerInPot(this.player2));
    assertTrue(mainPot.playerInPot(this.player3));
    assertTrue(mainPot.playerInPot(this.player4));
    assertTrue(mainPot.playerInPot(this.player5));
    
    assertEquals(new BigDecimal("15.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("1.00"), mainPot.getPlayerContribution(this.player1));
    assertEquals(new BigDecimal("2.00"), mainPot.getPlayerContribution(this.player2));
    assertEquals(new BigDecimal("4.00"), mainPot.getPlayerContribution(this.player3));
    assertEquals(new BigDecimal("4.00"), mainPot.getPlayerContribution(this.player4));
    assertEquals(new BigDecimal("4.00"), mainPot.getPlayerContribution(this.player4));
    
    assertTrue(sidePot1.playerInPot(this.player3));
    
    assertEquals(new BigDecimal("1.00"), sidePot1.getPotAmount());
    assertEquals(new BigDecimal("1.00"), sidePot1.getPlayerContribution(this.player3));
  }
}
