package edu.sigmachi.poker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.sigmachi.poker.ClientActionMsg.Actions;

public class TestMessages {

  /*
   * Covers creating ClientRoundMsg with empty constructor and setting all fields
   */
  @Test
  public void testClientRoundMsgSetters() {
    ClientActionMsg msg = new ClientActionMsg();

    msg.setAction("FOLD");
    assertEquals(Actions.FOLD, msg.getAction());

    msg.setAction("CHECK");
    assertEquals(Actions.CHECK, msg.getAction());

    msg.setAction("CALL");
    assertEquals(Actions.CALL, msg.getAction());

    msg.setAction("RAISE");
    assertEquals(Actions.RAISE, msg.getAction());

    msg.setRaiseAmount("1.00");
    assertEquals(new BigDecimal("1.00"), msg.getRaiseAmount());

    msg.setPlayerName("cdurr");
    assertEquals("cdurr", msg.getPlayerName());

  }

  /*
   * Covers creating ClientRoundMsg with empty constructor and setting all fields
   */
  @Test
  public void testClientRoundMsgFullConstructor() {
    ClientActionMsg msg = new ClientActionMsg("cdurr", "RAISE", "1.00");
    assertEquals(new BigDecimal("1.00"), msg.getRaiseAmount());
    assertEquals("cdurr", msg.getPlayerName());
    assertEquals(Actions.RAISE, msg.getAction());
  }

  /*
   * Covers that we only have a raise amount when the action is Raise
   */
  @Test(expected = AssertionError.class)
  public void testClientRoundMsgFullRaiseError() {
    ClientActionMsg msg = new ClientActionMsg("cdurr", "RAISE", "");
    msg.getRaiseAmount();
  }

  @Test
  public void testEndOfRoundMsg() {
    List<String> p1Cards = new ArrayList<>(Arrays.asList("2C", "3H"));
    List<String> p2Cards = new ArrayList<>(Arrays.asList("4D", "5S"));
    Map<String, List<String>> playersToCards = new HashMap<>();
    playersToCards.put("cdurr", p1Cards);
    playersToCards.put("mheatzig", p2Cards);
    
    List<String> mainPotWinners = new ArrayList<>(Arrays.asList("cdurr", "mheatzig"));
    List<String> sidePotWinners = new ArrayList<>(Arrays.asList("blevin", "pruh"));
    
    Map<String, BigDecimal> finalBalances = new HashMap<>();
    finalBalances.put("cdurr", new BigDecimal("101.01"));
    finalBalances.put("mheatzig", new BigDecimal("84.02"));
    
    EndOfRoundMsg msg = new EndOfRoundMsg(playersToCards, mainPotWinners, sidePotWinners, finalBalances);
    
    Map<String, List<String>> returnGetFinalPlayersToCards = msg.getFinalPlayersToCards();
    assertEquals(2, returnGetFinalPlayersToCards.keySet().size());
    assertTrue(returnGetFinalPlayersToCards.keySet().contains("cdurr"));
    assertTrue(returnGetFinalPlayersToCards.keySet().contains("mheatzig"));
    assertEquals(2, returnGetFinalPlayersToCards.get("cdurr").size());
    assertTrue(returnGetFinalPlayersToCards.get("cdurr").contains("2C"));
    assertTrue(returnGetFinalPlayersToCards.get("cdurr").contains("3H"));
    
    assertEquals(2, returnGetFinalPlayersToCards.get("mheatzig").size());
    assertTrue(returnGetFinalPlayersToCards.get("mheatzig").contains("4D"));
    assertTrue(returnGetFinalPlayersToCards.get("mheatzig").contains("5S"));
    
    
    List<String> returnMainPotWinners = msg.getMainPotWinningPlayers();
    assertEquals(2, returnMainPotWinners.size());
    assertTrue(returnMainPotWinners.contains("cdurr"));
    assertTrue(returnMainPotWinners.contains("mheatzig"));
    
    List<String> returnSidePotWinners = msg.getSidePotWinningPlayers();
    assertEquals(2, returnSidePotWinners.size());
    assertTrue(returnSidePotWinners.contains("blevin"));
    assertTrue(returnSidePotWinners.contains("pruh"));
    
    Map<String, BigDecimal> returnFinalBalances = msg.getFinalBalances();
    assertEquals(2, returnFinalBalances.keySet().size());
    assertEquals(new BigDecimal("101.01"), returnFinalBalances.get("cdurr"));
    assertEquals(new BigDecimal("84.02"), returnFinalBalances.get("mheatzig"));
  }
  
  @Test
  public void ServerRoundMsg() {
    BigDecimal p1Balance = new BigDecimal("1.01");
    BigDecimal p2Balance = new BigDecimal("2.01");
    Map<String, BigDecimal> playerBalances = new HashMap<>();
    playerBalances.put("cdurr", p1Balance);
    playerBalances.put("mheatzig", p2Balance);
    
    BigDecimal p1Bet = new BigDecimal("4.01");
    BigDecimal p2Bet = new BigDecimal("5.01");
    Map<String, BigDecimal> currentBets = new HashMap<>();
    currentBets.put("cdurr", p1Bet);
    currentBets.put("mheatzig", p2Bet);
    
    String currentTurn = "cdurr";
    
    List<String> communityCards = new ArrayList<>(Arrays.asList("AH", "2S", "JD"));
    List<String> mainPotContenders = new ArrayList<>(Arrays.asList("cdurr", "mheatzig"));
    List<String> sidePotContenders = new ArrayList<>(Arrays.asList("blevin", "pruh"));
    
    BigDecimal mainPotValue = new BigDecimal("1000.00");
    BigDecimal sidePotValue = new BigDecimal("500.00");
    
    ServerActionResponseMsg msg = new ServerActionResponseMsg(playerBalances,currentBets,currentTurn,
        communityCards, mainPotContenders,mainPotValue,sidePotContenders,sidePotValue);
    
    Map<String, BigDecimal> returnPlayerBalances = msg.getPlayerBalances();
    assertEquals(2, returnPlayerBalances.keySet().size());
    assertEquals(new BigDecimal("1.01"), returnPlayerBalances.get("cdurr"));
    assertEquals(new BigDecimal("2.01"), returnPlayerBalances.get("mheatzig"));
    
    Map<String, BigDecimal> returnCurrentBets = msg.getCurrentBets();
    assertEquals(2, returnCurrentBets.keySet().size());
    assertEquals(new BigDecimal("4.01"), returnCurrentBets.get("cdurr"));
    assertEquals(new BigDecimal("5.01"), returnCurrentBets.get("mheatzig"));
    
    String returnCurrentTurn = msg.getCurrentTurn();
    assertEquals("cdurr", returnCurrentTurn);
    
    List<String> returnCommunityCards = msg.getCommunityCards();
    assertEquals(3, returnCommunityCards.size());
    assertTrue(returnCommunityCards.contains("AH"));
    assertTrue(returnCommunityCards.contains("2S"));
    assertTrue(returnCommunityCards.contains("JD"));
    
    List<String> returnMainPotContenders = msg.getMainPotContenders();
    assertEquals(2, returnMainPotContenders.size());
    assertTrue(returnMainPotContenders.contains("cdurr"));
    assertTrue(returnMainPotContenders.contains("mheatzig"));
    
    List<String> returnSidePotContenders = msg.getSidePotContenders();
    assertEquals(2, returnSidePotContenders.size());
    assertTrue(returnSidePotContenders.contains("pruh"));
    assertTrue(returnSidePotContenders.contains("blevin"));
    
    assertEquals(new BigDecimal("1000.00"), msg.getMainPotValue());
    assertEquals(new BigDecimal("500.00"), msg.getSidePotValue());
  }
  
  @Test
  public void StartOfRoundMsg() {
    Map<String, BigDecimal> playerToBalances = new HashMap<>();
    playerToBalances.put("cdurr", new BigDecimal("2.00"));
    playerToBalances.put("mheatzig", new BigDecimal("4.00"));
    
    Map<String, List<String>> playerToCards = new HashMap<>();
    List<String> p1Cards = new ArrayList<>(Arrays.asList("4D", "5C"));
    List<String> p2Cards = new ArrayList<>(Arrays.asList("6H", "7S"));
    playerToCards.put("cdurr", p1Cards);
    playerToCards.put("mheatzig", p2Cards);
    
    List<String> communityCards = new ArrayList<>(Arrays.asList("10D", "4H", "5C"));
    
    String bigBlind = "cdurr";
    String smallBlind = "blevin";
    String dealer = "mheatzig";
    
    StartOfRoundMsg msg = new StartOfRoundMsg(playerToBalances, playerToCards, communityCards,
        bigBlind, smallBlind, dealer);
    
    Map<String, BigDecimal> returnPlayerToBalances = msg.getPlayerToBalances();
    
    assertEquals(2, returnPlayerToBalances.keySet().size());
    assertTrue(returnPlayerToBalances.containsKey("cdurr"));
    assertTrue(returnPlayerToBalances.containsKey("mheatzig"));
    assertEquals(new BigDecimal("2.00"), returnPlayerToBalances.get("cdurr"));
    assertEquals(new BigDecimal("4.00"), returnPlayerToBalances.get("mheatzig"));
    
    Map<String, List<String>> returnPlayerToCards = msg.getPlayerToCards();
    assertEquals(2, returnPlayerToCards.keySet().size());
    assertTrue(returnPlayerToCards.containsKey("cdurr"));
    assertTrue(returnPlayerToCards.containsKey("mheatzig"));
    assertEquals(2, returnPlayerToCards.get("cdurr").size());
    assertTrue(returnPlayerToCards.get("cdurr").contains("4D"));
    assertTrue(returnPlayerToCards.get("cdurr").contains("5C"));
    assertEquals(2, returnPlayerToCards.get("mheatzig").size());
    assertTrue(returnPlayerToCards.get("mheatzig").contains("6H"));
    assertTrue(returnPlayerToCards.get("mheatzig").contains("7S"));
    
    List<String> returnCommunityCards = msg.getCommunityCards();
    assertEquals(3, returnCommunityCards.size());
    assertTrue(returnCommunityCards.contains("10D"));
    assertTrue(returnCommunityCards.contains("4H"));
    assertTrue(returnCommunityCards.contains("5C"));
    
    assertEquals("cdurr", msg.getBigBlind());
    assertEquals("blevin", msg.getSmallBlind());
    assertEquals("mheatzig", msg.getDealer());
  }
}
