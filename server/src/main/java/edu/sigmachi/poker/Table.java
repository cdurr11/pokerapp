package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import edu.sigmachi.poker.Messages.ClientActionMsg;
import edu.sigmachi.poker.Messages.ClientActionMsg.Actions;
import edu.sigmachi.poker.Messages.EndMsg;
import edu.sigmachi.poker.Messages.InstantGameMsg;
import edu.sigmachi.poker.Messages.PrintGameStateMsg;
import edu.sigmachi.poker.Messages.RestartMsg;
import edu.sigmachi.poker.Messages.ServerActionResponseMsg;

/**
 * No Limit Texas Hold'em
 * 
 * This class is the game flow of a single poker table
 * 
 * @author markheatzig
 *
 */

public class Table {
  
  private static Integer cardsNumAtPreflop = 0;
  private static Integer cardsNumAtFlop = 3;
  private static Integer cardsNumAtTurn = 4;
  private static Integer cardsNumAtRiver = 5;
  
  // this map is going to come in to play with everything already filled out
  private final Map<String, Player> table; // This is the players map --> mapping a player name to a player object.
                                           // which contains the chip count, hand, inPlay boolean for that player

  /** The deck of cards. */
  private Deck deck;

  /** This consists of the cards on the table: Flop, Turn, and the River. */
  private CommunityCards actionCards;

  /** This will keep track of the current pot size. */
  private BigDecimal potSize;

  /** This will keep track of the total players in the game. */
  private final ArrayList<Player> players;

  /** This will keep track of which players are in the hand. */
  private final ArrayList<Player> activePlayers;

  /** This is the little blind */
  private final BigDecimal smallBlind;

  /** This is the big blind */
  private final BigDecimal bigBlind;

  /** This will keep track of the current dealer */
  private Player dealer;
  
  /** This will keep track of the current smallBlindPlayer */
  private Player smallBlindPlayer;

  /** This will keep track of the current bigBlindPlayer */
  private Player bigBlindPlayer;


  /** This will keep track of the dealer's position */
  private Integer dealerPosition;

  /** Message queue */
  private final BlockingQueue<InstantGameMsg> instantGameMsgQueue;

  /** Server queue */
  private ServerActionResponseMsg serverMessage;

  /**
   * Current player
   */
  private Player currentPlayer;
  
  /**
   * The player who made the last action after the river
   */
  private Player lastPlayerToAct;

  /**
   * Get available actions for a player
   */
  private ArrayList<Actions> availableActions;
  
  /**
   * Pass in little/big blinds
   */
  public Table(BigDecimal smallBlind, BigDecimal bigBlind, BlockingQueue<InstantGameMsg> instantGameMsgQueue) {
    this.deck = new Deck();
    this.players = new ArrayList<Player>();
    this.activePlayers = new ArrayList<Player>();

    this.smallBlind = smallBlind;
    this.bigBlind = bigBlind;
    this.table = new HashMap<String, Player>();
    this.potSize = new BigDecimal(0);
    this.actionCards = new CommunityCards();
    this.dealer = players.get(0);
    this.smallBlindPlayer = players.get(1);
    this.bigBlindPlayer = players.get(2);

    this.instantGameMsgQueue = instantGameMsgQueue;
  }
  /*
   * This will add a player to the game.
   */
  public void addPlayer(Player player) {
    table.put(player.getName(), player);
    players.add(player);
  }

  /*
   * This will remove a player from the game.
   */
  public void removePlayer(Player player) {
    table.remove(player.getName());
    players.remove(players.indexOf(player));
  }

  /**
   * 
   * Function to play one hand.
   * 
   * private void playHand()
   * 
   * --- assuming theres more than 2 people playing set dealer set little blind
   * set big blind. Preflop: This will be a function created that will go through
   * all active players and have them act appropriately if raise, then everyone
   * must act again even if acted This start with the player left of the big
   * blinds Flop: Contains active players draws 3 cards from the deck and displays
   * on the screen. Round of betting then occurs. Starts with player left of the
   * dealer. Turn: Contains active players draws 1 card and displays on the screen
   * Round of betting occurs again. Starts with the player left of the dealer
   * River Contains active players draws 1 card and displays on the screen Round
   * of betting occurs again. Starts with the player left of the dealer
   * 
   * @throws InterruptedException
   * 
   */

  
  public RoundOfBettingRetCode playHand() throws InterruptedException {
    // @MARK: set the current player here, whoever it is, person left of dealer?
    freshHand();
    postBlinds();
    while (activePlayers.size() > 1) {
      RoundOfBettingRetCode roundOfBettingResult = roundOfBetting();
      // Need to propagate this to the Game class so that game can be restarted
      if (roundOfBettingResult != RoundOfBettingRetCode.NORMAL) {
        return roundOfBettingResult;
      }
      if (actionCards.getCurrentCommunityCards().size() == cardsNumAtRiver) {
        showDown();
      }
      showDown();
    }
    return RoundOfBettingRetCode.NORMAL;
  }


  /**
   * 
   * Function to reset everything before the next hand starts. Need to iterate who
   * the dealer is shuffle the deck pass out hands to each player 
   */
  private void freshHand() {
    actionCards = new CommunityCards();
    deck = new Deck();
    dealCards();
  }

  private void dealCards() {
    for (Player player : activePlayers) {
      player.drawHand();
    }
  }

  /**
   * This is going to evaluate all of the hands and allocate the money to the winner
   * 
   * 
   * On the last round after the river:
   * 
   * The last player that made an aggressive action (bet or raise) must show first. 
   * If no aggressive action occurred on the final betting round,
   * it would be the last aggressive action from the round of betting before that, and so on.
   * 
   * 
   * Otherwise, the person to the left of the dealer shows first...
   * 
   * 
   */
  private void showDown() {
	  int temporary = activePlayers.indexOf(lastPlayerToAct);
	  int nextTemp = -1;
	  int x = 1;
	  Player tempWinner = null;
	  while (x < activePlayers.size()) {
		  temporary = temporary % activePlayers.size();
		  nextTemp = (temporary + 1) %activePlayers.size();
		  tempWinner = evaluateHand(activePlayers.get(temporary), activePlayers.get(nextTemp));
		  if (tempWinner.equals(null)) {
			  x++;
		  }
		  else if (tempWinner.equals(activePlayers.get(temporary))){
				  activePlayers.remove(activePlayers.get(nextTemp));
			  }
		  else {
			  activePlayers.remove(activePlayers.get(temporary));
		  }
	  }
  }

  
  /**
   * @param player this will be one player
   * @param player2 another
   * @return the player whose hand is better
   */
  private Player evaluateHand(Player player, Player player2) {
	// TODO Auto-generated method stub
	  
	  
	  // return null ifa tie 
	return null;
}

/**
   * This is going to just reset the board and get ready for the next hand
   */
  private void resetBoard() {
    // TODO Auto-generated method stub
  }
  
  /**
   * This is going to encompass a round of betting - Take into account current
   * bet, available actions -
   * 
   * @throws InterruptedException
   */
  private RoundOfBettingRetCode roundOfBetting() throws InterruptedException {
    // server.getBroadcastOperations().sendEvent("startOfRound", new
    // StartOfRoundMsg);
	  
    BigDecimal currentBet = BigDecimal.ZERO; // this is only for the start of preflop
    int toAct = activePlayers.size();
    if (actionCards.getCurrentCommunityCards().size() == cardsNumAtPreflop) {
      currentBet = bigBlind;
    }

    if (toAct == 0) {
      return RoundOfBettingRetCode.NORMAL;
    }

    while (toAct > 0) {
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
      
      availableActions = getAvailableAction(currentPlayer, currentBet);
      
      if (clientMsg.getAction() == Actions.FOLD) {
        activePlayers.remove(activePlayers.indexOf(currentPlayer));
      }
      else if (clientMsg.getAction() == Actions.CHECK) {
        toAct--;
        continue;
      } 
      else if (clientMsg.getAction() == Actions.CALL) {
        // need to keep in mind if its main pot or side pot etc
        contributePot(currentPlayer, currentBet);
      } 
      else if (clientMsg.getAction() == Actions.RAISE) {
        toAct = activePlayers.size();
        currentBet = currentBet.add(clientMsg.getRaiseAmount());

        // need to keep in mind if its main pot or side pot etc
        contributePot(currentPlayer, currentBet);
        lastPlayerToAct = currentPlayer;
      } 
      else if (clientMsg.getAction() == Actions.ALLIN) {
        toAct = activePlayers.size();

        // need to keep in mind if its main pot or side pot etc -- when an all in is made, that is when the side pot is created****
        contributePot(currentPlayer, currentPlayer.getChipCount());
        lastPlayerToAct = currentPlayer;
      }
      toAct--;
      // @MARK : Increment the current player here or somewhere
    }
    
    return RoundOfBettingRetCode.NORMAL;
  }

  /**
   * This will get the available action for a player
   * 
   * @return
   */
  private ArrayList<Actions> getAvailableAction(Player player, BigDecimal currentBet) {
    ArrayList<Actions> available = new ArrayList<>();
    
    available.add(Actions.FOLD);
    if (currentBet.compareTo(BigDecimal.ZERO) == 0) {
      available.add(Actions.CHECK);
      available.add(Actions.BET);
      available.add(Actions.ALLIN);

      // CODY : This is saying if currentBet is less than chip count, I think you have it backwards
    } 
    else if (currentBet.compareTo(bigBlind) == 0 && currentPlayer.equals(bigBlind)) {
        available.add(Actions.CHECK);
        available.add(Actions.BET);   
        available.add(Actions.ALLIN);

        }
    else if (currentBet.compareTo(player.getChipCount()) == 1) {
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
    player.adjustStackSize(amount);
    potSize.add(amount);
  }

  /*
   * posting the blinds for the little and the big blind
   */
  private void postBlinds() {
    // find the dealer, get the next two active players, first one is going to be
    // small blind, second will be the big blind
    int dealerSeatNumber = -1;
    int smallBlindSeatNumber = -1;
    int bigBlindSeatNumber = -1;


    for (Player i : players) {
      if (i.equals(dealer)) {
        dealerSeatNumber = players.indexOf(i);
      }
    }
    smallBlindSeatNumber = (dealerSeatNumber + 1) % players.size();
    bigBlindSeatNumber = (dealerSeatNumber + 2) % players.size();
    
    smallBlindPlayer = players.get(smallBlindSeatNumber);
    bigBlindPlayer = players.get(bigBlindSeatNumber);

    contributePot(smallBlindPlayer, smallBlind);
    contributePot(bigBlindPlayer, bigBlind);
  }
  
  
  private void printGameState() {
    //TODO
  }
}
