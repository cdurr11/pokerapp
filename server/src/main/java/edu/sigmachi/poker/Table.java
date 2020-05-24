package edu.sigmachi.poker;

import java.awt.Desktop.Action;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
  
  private final Map<String, Player> table = new HashMap<String, Player>(); 
  private Deck deck;
  private BigDecimal initialBuyIn;
  private CommunityCards communityCards;
  private BigDecimal potSize;
  private BigDecimal currentBet;
  
  private final List<Player> activePlayers = new ArrayList<Player>();
  private List<Player> currentPlayersInHand = new ArrayList<>();
  
  private final BigDecimal smallBlindAmt;
  private final BigDecimal bigBlindAmt;
  
  private Player dealerPlayer;
  private int dealerIndex;
  private Player smallBlindPlayer;
  private int smallBlindIndex;
  private Player bigBlindPlayer;
  private int bigBlindIndex;
  
  private Integer dealerPosition;
  private final BlockingQueue<InstantGameMsg> instantGameMsgQueue;
  private Player currentPlayer;
  private int currentPlayerIndex;
  //The player who made the last action after the river 
  private Player lastPlayerToAct;
  
  
  public Table(SocketIOServer server, BigDecimal smallBlind, BigDecimal bigBlind, 
      BlockingQueue<InstantGameMsg> instantGameMsgQueue, BigDecimal initialBuyIn) {
    this.server = server;
    
    this.smallBlindAmt = smallBlind;
    this.bigBlindAmt = bigBlind;
    
    this.potSize = new BigDecimal(0);
    this.potSize = this.potSize.setScale(2);

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
        break;
      }
      if (communityCards.getState() == CommunityCardState.RIVER) {
        showDown();
        break;
      }
      firstRound = false;
    }
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
  
  /**
   * This is going to encompass a round of betting - Take into account current
   * bet, available actions -
   * 
   * @throws InterruptedException
   */
  private RoundOfBettingRetCode roundOfBetting(boolean firstRound) throws InterruptedException {
    int toAct;
	  if (firstRound) {
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

    if (toAct == 0) {
      return RoundOfBettingRetCode.NORMAL;
    }
    
    while (toAct > 0) {
      Set<Actions> availableActions = getAvailableActions(this.currentPlayer);
      // TODO send ServerActionResponseMsg here
      InstantGameMsg instantGameMsg;
      while (true) {
        //take messages until we don't get a PrintGameStateMsg
        instantGameMsg = instantGameMsgQueue.take();
        if (instantGameMsg instanceof RestartMsg) {
          return RoundOfBettingRetCode.RESTART;
        }
        else if (instantGameMsg instanceof EndMsg) {
          return RoundOfBettingRetCode.END;
        }
        else if (instantGameMsg instanceof PrintGameStateMsg ) {
          printGameState();
        }
        else if (instantGameMsg instanceof ClientActionMsg) {
          // Check to make sure that the message we get is from the right player
          if (((ClientActionMsg) instantGameMsg).getPlayerName().equals(currentPlayer)) {
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
        toAct--;
        continue;
      } 
      else if (clientMsg.getAction() == Actions.CALL) {
        // need to keep in mind if its main pot or side pot etc
        checkIsValidAction(Actions.CALL, availableActions);
        contributePot(currentPlayer, currentBet);
      } 
      else if (clientMsg.getAction() == Actions.RAISE) {
        checkIsValidAction(Actions.RAISE, availableActions);
        toAct = currentPlayersInHand.size();
        currentBet = currentBet.add(clientMsg.getRaiseAmount());

        // need to keep in mind if its main pot or side pot etc
        contributePot(currentPlayer, currentBet);
        lastPlayerToAct = currentPlayer;
      } 
      else if (clientMsg.getAction() == Actions.ALLIN) {
        checkIsValidAction(Actions.ALLIN, availableActions);
        toAct = currentPlayersInHand.size() - 1;

        // need to keep in mind if its main pot or side pot etc -- when an all in is made, that is when the side pot is created****
        contributePot(currentPlayer, currentPlayer.getBalance());
        lastPlayerToAct = currentPlayer;
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
  private Set<Actions> getAvailableActions(Player player) {
    Set<Actions> available = new HashSet<>();
    
    //TODO need to add a check to check if a player has enough money to raise.
    available.add(Actions.FOLD);
    if (this.currentBet.compareTo(BigDecimal.ZERO) == 0) {
      available.add(Actions.CHECK);
      available.add(Actions.RAISE);
      available.add(Actions.ALLIN);
    } 
    else if (this.currentBet.compareTo(player.getBalance()) == 1) {
      available.add(Actions.ALLIN);
    } 
    else {
      available.add(Actions.CALL);
      available.add(Actions.RAISE);
      available.add(Actions.ALLIN);
    }
    return available;
  }

  private void contributePot(Player player, BigDecimal amount) {
    player.adjustBalance(amount.negate());
    potSize.add(amount);
  }                 
  
  public void modifyPlayerBalance(String playerName, BigDecimal addition) {
    // TODO
  }

  /*
   * posting the blinds for the little and the big blind
   */
  private void postBlinds() {
    contributePot(this.smallBlindPlayer, this.smallBlindAmt);
    contributePot(this.bigBlindPlayer, this.bigBlindAmt);
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
  
  /**
   * Gets the value of the pot for the game
   * @return the value of the pot
   */
  public BigDecimal getPotSize() {
    return this.potSize;
  }
}
