package edu.sigmachi.poker.Messages;

import edu.sigmachi.poker.Card;
import edu.sigmachi.poker.CommunityCardState;
import edu.sigmachi.poker.Player;
import edu.sigmachi.poker.Pot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameStateMsg {
  public enum GameStateMsgType { AFTERACTION, ROBCOMPLETION, AFTERHAND, PLAYHAND }

  private final Map<String, Player> table;
  private final List<Player> activePlayers;
  private final List<Player> currentPlayersInHand;
  private final String dealerPlayerName;
  private final int dealerIndex;
  private final String smallBlindPlayerName;
  private final int smallBlindIndex;
  private final String bigBlindPlayerName;
  private final int bigBlindIndex;
  private final String currentPlayerName;
  private final int currentPlayerIndex;
  private final List<Pot> pots;
  private final List<Card> communityCards;
  private final CommunityCardState communityCardState;
  private final GameStateMsgType msgType;

  public GameStateMsg(Map<String, Player> table, List<Player> activePlayers, List<Player> currentPlayersInHand, List<Pot> pots,
                      String dealerPlayerName, int dealerIndex, String smallBlindPlayerName,
                      int smallBlindIndex, String bigBlindPlayerName, int bigBlindIndex,
                      String currentPlayerName, int currentPlayerIndex, List<Card> communityCards,
                      CommunityCardState communityCardState, GameStateMsgType msgType) {

    this.table = table;
    this.activePlayers = activePlayers;
    this.currentPlayersInHand = currentPlayersInHand;
    this.dealerPlayerName = dealerPlayerName;
    this.dealerIndex = dealerIndex;
    this.smallBlindPlayerName = smallBlindPlayerName;
    this.smallBlindIndex = smallBlindIndex;
    this.bigBlindPlayerName = bigBlindPlayerName;
    this.bigBlindIndex = bigBlindIndex;
    this.currentPlayerName = currentPlayerName;
    this.currentPlayerIndex = currentPlayerIndex;
    this.pots = pots;
    this.communityCards = communityCards;
    this.communityCardState = communityCardState;
    this.msgType = msgType;
  }

  public Map<String, Player> getTable() {
    return this.table;
  }
  public List<Player> getActivePlayers() {
    return this.activePlayers;
  }

  public List<Player> getCurrentPlayersInHand() {
    return this.currentPlayersInHand;
  }

  public List<Pot> getPots() {
    return this.pots;
  }

  public String getDealerPlayerName() {
    return this.dealerPlayerName;
  }

  public int getDealerIndex() {
    return this.dealerIndex;
  }

  public String getSmallBlindPlayerName() {
    return this.smallBlindPlayerName;
  }

  public int getSmallBlindIndex() {
    return this.smallBlindIndex;
  }

  public String getBigBlindPlayerName() {
    return this.bigBlindPlayerName;
  }

  public int getBigBlindIndex() {
    return this.bigBlindIndex;
  }

  public String getCurrentPlayerName() {
    return this.currentPlayerName;
  }

  public int getCurrentPlayerIndex() {
    return this.currentPlayerIndex;
  }

  public GameStateMsgType getMsgType() {
    return this.msgType;
  }

  public CommunityCardState getCommunityCardState() {
    return this.communityCardState;
  }

  public List<Card> getCommunityCards() {
    return this.communityCards;
  }
}
