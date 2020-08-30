package edu.sigmachi.poker;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import com.corundumstudio.socketio.SocketIOServer;

import edu.sigmachi.poker.Messages.ClientActionMsg;
import edu.sigmachi.poker.Messages.ClientActionMsg.Actions;
import edu.sigmachi.poker.Messages.EndMsg;
import edu.sigmachi.poker.Messages.InstantGameMsg;
import edu.sigmachi.poker.Messages.PrintGameStateMsg;
import edu.sigmachi.poker.Messages.RestartMsg;
import edu.sigmachi.poker.handEvaluator.SevenEval;

/**
 * No Limit Texas Hold'em
 * 
 * This class is the game flow of a single poker table
 * 
 * @author codydurr
 *
 */

public class Table {
  private final SocketIOServer server;
  
  private final Map<String, Player> table = new HashMap<>();
  private Deck deck;
  private BigDecimal initialBuyIn;
  private CommunityCards communityCards;
 
  private BigDecimal currentBet;
  private List<Pot> pots = new LinkedList<>();
  
  private final List<Player> activePlayers = new ArrayList<>();
  private List<Player> currentPlayersInHand = new ArrayList<>();
  
  private final BigDecimal smallBlindAmt;
  private final BigDecimal bigBlindAmt;
  
  private Player dealerPlayer;
  private int dealerIndex;
  private Player smallBlindPlayer;
  private int smallBlindIndex;
  private Player bigBlindPlayer;
  private int bigBlindIndex;
  
  private final BlockingQueue<InstantGameMsg> instantGameMsgQueue;
  private Player currentPlayer;
  private int currentPlayerIndex;
  
  //The player who made the last action after the river 
  
  public Table(SocketIOServer server, BigDecimal smallBlind, BigDecimal bigBlind, 
      BlockingQueue<InstantGameMsg> instantGameMsgQueue, BigDecimal initialBuyIn) {
    this.server = server;
    
    this.smallBlindAmt = smallBlind;
    this.bigBlindAmt = bigBlind;

    this.instantGameMsgQueue = instantGameMsgQueue;
    this.initialBuyIn = initialBuyIn;
  }
  
  private void setDealerAndBlinds() {
    if (this.dealerIndex == 0 && this.smallBlindIndex == 0 && 
        this.bigBlindIndex == 0) {
      this.dealerIndex = 0;
      this.smallBlindIndex = 1;
      this.bigBlindIndex = 2;
      this.dealerPlayer = activePlayers.get(dealerIndex);
      this.smallBlindPlayer = activePlayers.get(smallBlindIndex);
      this.bigBlindPlayer = activePlayers.get(bigBlindIndex);
    } else {
      this.dealerPlayer = activePlayers.get((dealerIndex + 1) % activePlayers.size());
      this.smallBlindPlayer = activePlayers.get((smallBlindIndex + 1) % activePlayers.size());
      this.bigBlindPlayer = activePlayers.get((bigBlindIndex + 1) % activePlayers.size());
    }
  }
  
  /*
   * This will add a player to the game.
   */
  public void addPlayer(String playerName, UUID sessionID) {
    // Player is logging in again
    if (table.keySet().contains(playerName)) {
      table.get(playerName).setInPlay(true);
      table.get(playerName).setSessionID(sessionID);
    }
    // Need to create a new player
    else {
      Player player = new Player(playerName, sessionID, this.initialBuyIn);
      table.put(player.getName(), player);
      activePlayers.add(player);
    }
  }

  /**
   * Sets a player that is in the game to inactive in the table map and 
   * removes them from active players.
   */
  public boolean removePlayer(UUID sessionID) {
    boolean foundInTableFlag = false;
    for (String player : table.keySet()) { 
      if (table.get(player).getSessionID().equals(sessionID)) {
        table.get(player).setInPlay(false);
        foundInTableFlag = true;
      }
    }
    for (int i = 0; i < this.activePlayers.size(); i++) {
      currentPlayer = this.activePlayers.get(i);
      if (currentPlayer.getSessionID().equals(sessionID)) {
        activePlayers.remove(i);
        return foundInTableFlag;
      }
    }
    return false;
  }
  
  public RoundOfBettingRetCode playHand() throws InterruptedException {
    initHand();
    setDealerAndBlinds();
    postBlinds();
    boolean firstRound = true;
    List<Player> winners = new ArrayList<>();
//    server.getBroadcastOperations().sendEvent("startOfRound", SORMsg);
    while (currentPlayersInHand.size() > 1) {
      RoundOfBettingRetCode roundOfBettingResult = roundOfBetting(firstRound);
      communityCards.draw(this.deck);
      // Need to propagate this to the Game class so that game can be restarted
      if (roundOfBettingResult != RoundOfBettingRetCode.NORMAL) {
        return roundOfBettingResult;
      }
      if (currentPlayersInHand.size() == 1) {
        //declare winner
        winners.add(currentPlayersInHand.get(0));
        break;
      }
      if (communityCards.getState() == CommunityCardState.RIVER) {
        winners = showDown();
        break;
      }
      firstRound = false;
    }
    this.allocateWinnings(winners);
    return RoundOfBettingRetCode.NORMAL;
  }


  /**
   * Function to reset everything before the next hand starts. Need to iterate who
   * the dealer is shuffle the deck pass out hands to each player 
   */
  private void initHand() {
    this.deck = new Deck();
    communityCards = new CommunityCards();
    for (Player player : this.activePlayers) {
      player.drawHand(this.deck);
    }
  }

  private List<Player> showDown() {
    Map<Player, Integer> playerToHandHash = new HashMap<>();
    List<Card> commCard = this.communityCards.getCurrentCommunityCards();
    List<Player> winners = new ArrayList<>();
	  for (Player player : this.currentPlayersInHand) {
	    try {
          int hash = SevenEval.GetRank(player.getCard1().getValue(), player.getCard2().getValue(),
              commCard.get(0).getValue(), commCard.get(1).getValue(),
              commCard.get(2).getValue(), commCard.get(3).getValue(),
              commCard.get(4).getValue());
          playerToHandHash.put(player, hash);
      } catch (IOException e) {
        e.printStackTrace();
      }
	  }
	  int maxHash = Collections.max(playerToHandHash.values());
	  for (Player player2 : playerToHandHash.keySet()) {
	    if (playerToHandHash.get(player2) == maxHash) {
	      winners.add(player2);
	    }
	  }
	  return winners;
  }

  private void allocateWinnings(List<Player> winners) {
    for (Pot pot: this.pots) {
      Set<Player> contributors = pot.getPlayersInPot();
      List<Player> winnersInPot = new ArrayList<>();
      for (Player winner: winners) {
        if (contributors.contains(winner)) {
          winnersInPot.add(winner);
        }
      }
      if (winnersInPot.size() == 0) {
        for (Player nonWinner: pot.getPlayersInPot()) {
          nonWinner.adjustBalance(pot.getPlayerContribution(nonWinner));
        }
      } else {
        for (Player winnerInPot: winnersInPot) {
          BigDecimal bigDecimalPotSize = BigDecimal.valueOf(winnersInPot.size()).setScale(2);
          winnerInPot.adjustBalance(pot.getPotAmount().divide(bigDecimalPotSize));
        }
      }
    }
  }

  /**
   * This is going to encompass a round of betting - Take into account current
   * bet, available actions
   * 
   * @throws InterruptedException
   */
  private RoundOfBettingRetCode roundOfBetting(boolean firstRound) throws InterruptedException {
    int toAct;
    boolean canCheck = true;
	  if (firstRound) {
	    canCheck = false;
	    this.currentPlayer = activePlayers.get((this.bigBlindIndex + 1) % activePlayers.size());
	    toAct = currentPlayersInHand.size() - 1; // -2 for blinds but + 1 bc big blind can reraise = -1
	  }
	  else {
	    this.currentPlayer = findNextPlayerToStart();
	    toAct = currentPlayersInHand.size();
	  }
    
    if (communityCards.getState() == CommunityCardState.PREFLOP) {
      this.currentBet = this.bigBlindAmt;
    }
    
    while (toAct > 0) {
      Set<Actions> availableActions = getAvailableActions(this.currentPlayer, canCheck);
      if (currentPlayer.getAllIn()) {
        toAct--;
        incrementCurrentPlayer();
        continue;
      }
      // TODO send ServerActionResponseMsg here
      InstantGameMsg instantGameMsg;
      while (true) {
        // take messages until we don't get a PrintGameStateMsg
        instantGameMsg = instantGameMsgQueue.take();
        if (instantGameMsg instanceof RestartMsg) {
          return RoundOfBettingRetCode.RESTART;
        }
        else if (instantGameMsg instanceof EndMsg) {
          return RoundOfBettingRetCode.END;
        }
        else if (instantGameMsg instanceof PrintGameStateMsg) {
          printGameState();
        }
        else if (instantGameMsg instanceof ClientActionMsg) {
          // Check to make sure that the message we get is from the right player
          if (((ClientActionMsg) instantGameMsg).getPlayerName().equals(currentPlayer.getName())) {
            break;
          }
        }
        else {
          throw new RuntimeException("Unknown InstantGameMsg");
        }
      }
      ClientActionMsg clientMsg = (ClientActionMsg) instantGameMsg;
      
      if (clientMsg.getAction() == Actions.FOLD) {
        checkIsValidAction(Actions.FOLD, availableActions);
        this.currentPlayer.fold();
        this.currentPlayersInHand.remove(this.currentPlayer);
      }
      else if (clientMsg.getAction() == Actions.CHECK) {
        checkIsValidAction(Actions.CHECK, availableActions);
      } 
      else if (clientMsg.getAction() == Actions.CALL) {
        BettingCalculator.bet(currentPlayer, clientMsg.getAction(), pots, activePlayers, BigDecimal.ZERO);
      }
      else if (clientMsg.getAction() == Actions.RAISE) {
        canCheck = false;
        BettingCalculator.bet(currentPlayer, clientMsg.getAction(), pots, activePlayers, clientMsg.getRaiseAmount()); 
        toAct = this.currentPlayersInHand.size();
      }
      else if (clientMsg.getAction() == Actions.ALLIN) {
        canCheck = false;
        this.currentPlayer.goAllIn();
        this.currentPlayersInHand.remove(currentPlayer);
        BigDecimal playerBalance = table.get(clientMsg.getPlayerName()).getBalance();
        BettingCalculator.bet(currentPlayer, clientMsg.getAction(), pots, activePlayers, playerBalance);
      }
      toAct--;
      incrementCurrentPlayer();
    }
    return RoundOfBettingRetCode.NORMAL;
  }

  private void incrementCurrentPlayer() {
    currentPlayerIndex = (currentPlayerIndex + 1) % currentPlayersInHand.size();
    currentPlayer = currentPlayersInHand.get(currentPlayerIndex);
  }

  private void checkIsValidAction(Actions action, Set<Actions> availableActions) {
    assert availableActions.contains(action);
  }
  /**
   * Finds the next player to the left of the smallBlind for the hand
   * @return a player that is the closest to the smallBlind that is still
   * in the hand, this being the smallBlind if they are still in
   */
  private Player findNextPlayerToStart() {
    for (int i = 0; i < activePlayers.size(); i++) {
      Player curPlayer = activePlayers.get((i + smallBlindIndex) % activePlayers.size());
      if (curPlayer.getHandStatus()) {
        return curPlayer;
      }
    }
    throw new RuntimeException("Did not find a valid start player");
  }
  
  /**
   * This will get the available action for a player
   * 
   * @return
   */
  private Set<Actions> getAvailableActions(Player player, boolean canCheck) {
    Set<Actions> available = new HashSet<>();
    
    // TODO need to add a check to check if a player has enough money to raise.
    available.add(Actions.FOLD);
    if (canCheck) {
      available.add(Actions.CHECK);
    } else {
      available.add(Actions.CALL);
    }

    if (this.currentBet.compareTo(player.getBalance()) >= 0) {
      available.add(Actions.ALLIN);
    }
    else {
      available.add(Actions.RAISE);
      available.add(Actions.ALLIN);
    }
    return available;
  }         

  /*
   * posting the blinds for the little and the big blind
   */
  private void postBlinds() {
    Pot mainPot = this.pots.get(0);
    mainPot.contributePot(this.smallBlindPlayer, this.smallBlindAmt);
    mainPot.contributePot(this.bigBlindPlayer, this.bigBlindAmt);
  }

  public void adjustPlayerBalance(String playerName, BigDecimal dollarAmount) {
    this.table.get(playerName).adjustBalance(dollarAmount);
  }
  
  private void printGameState() {
    //TODO
  }
  
  public Set<String> getActivePlayers() {
    Set<String> activePlayers = new HashSet<>();
    for (String playerName : this.table.keySet()) {
      if (this.table.get(playerName).inPlay()) {
        activePlayers.add(playerName);
      }
    }
    return activePlayers;
  }
  
  /**
   * Gets the balances of each player that has ever been in the game, active
   * or inactive.  
   * @return a map of player name to their balances 
   */
  public Map<String, BigDecimal> getPlayerBalances() {
    Map<String, BigDecimal> playersToBalances = new HashMap<>();
    for (String playerName : this.table.keySet()) {
      playersToBalances.put(playerName, this.table.get(playerName).getBalance());
    }
    return playersToBalances;
  }
  
  /**
   * Gets the name of the current dealer
   * @return dealer username
   */
  public String getDealerName() {
    return this.dealerPlayer.getName();
  }
  
  /**
   * Gets the name of the current big blind
   * @return big blind username
   */
  public String getBigBlindName() {
    return this.bigBlindPlayer.getName();
  }
  
  /**
   * Gets the name of the current small blind
   * @return small blind username
   */
  public String getSmallBlindName() {
    return this.smallBlindPlayer.getName();
  }
  
  /**
   * Gets the value of the big blind for the game
   * @return big blind amount
   */
  public BigDecimal getBigBlind() {
    return this.bigBlindAmt;
  }
  
  /**
   * Gets the value of the small blind for the game
   * @return small blind
   */
  public BigDecimal getSmallBlind() {
    return this.smallBlindAmt;
  }
}
