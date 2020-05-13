package edu.sigmachi.poker;


import java.math.BigDecimal;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import edu.sigmachi.poker.Messages.*;
import edu.sigmachi.poker.Messages.ClientActionMsg.Actions;


/**
 * No Limit Texas Hold'em
 * 
 * This class is the game flow of a single poker table
 * 
 * @author markheatzig
 *
 */

public class Table {
    
    //this map is going to come in to play with everything already filled out
    private final Map<String, Player> table; //This is the players map --> mapping a player name to a player object. which contains the chip count, hand, inPlay boolean for that player
    
    
    /** The deck of cards.*/
    private Deck deck;
    
    /** This consists of the cards on the table: Flop, Turn, and the River. */
    private ActionCards actionCards;
    
    /** This will keep track of the current pot size. */
    private final BigDecimal potSize;
    
    
    /** This will keep track of the total players in the game.*/
    private final ArrayList<Player> players;
    
    /** This will keep track of which players are in the hand. */
    private final ArrayList<Player> activePlayers;
    
    /** This is the little blind */
    private final BigDecimal littleBlind;
    
    
    /** This is the big blind */
    private final BigDecimal bigBlind;
    
    /** This will keep track of the current dealer*/
    private Player dealer;
    
    /** This will keep track of the dealer's position*/
    private Integer dealerPosition;
    
    /** Message queue */
    private BlockingQueue<ClientActionMsg> messageQueue;
    
    /** Server queue */
    private ServerActionResponseMsg serverMessage;
    
    /** A single message from the client */
    private ClientActionMsg playerMessage;

    /**
     * Current player
     */
    private Player currentPlayer;
    
    
    /**
     * Get available actions for a player
     */
    private ArrayList<String> availableActions;

	/**
	 * Pass in little/big blinds
	 */
	public Table(BigDecimal littleBlind, BigDecimal bigBlind) {
	    this.deck = new Deck();
	    this.players = new ArrayList<Player>();
	    this.activePlayers = new ArrayList<Player>();
	    
	    this.littleBlind = littleBlind;
	    this.bigBlind = bigBlind;
	    this.table = new HashMap<String, Player>();
	    this.potSize = new BigDecimal(0);
	    this.actionCards = new ActionCards();
	    this.dealer = new Player(null, BigDecimal.ZERO, false);
	    this.messageQueue =  new LinkedBlockingDeque<>();
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
	 * 	--- assuming theres more than 2 people playing
	 	* set dealer
	 	* set little blind
	 	* set big blind.
	 		* Preflop:
	 			* This will be a function created that will go through all active players and have them act appropriately
	 			* if raise, then everyone must act again even if acted
	 			* This start with the player left of the big blinds
 			* Flop:
 				* Contains active players
 				* draws 3 cards from the deck and displays on the screen.
 				* Round of betting then occurs. Starts with player left of the dealer.
			* Turn:
				* Contains active players
				* draws 1 card and displays on the screen
				* Round of betting occurs again. Starts with the player left of the dealer
			* River
				* Contains active players
				* draws 1 card and displays on the screen
				* Round of betting occurs again. Starts with the player left of the dealer
	 * @throws InterruptedException 
	 	* 
	 	* 
	 * 
	 * 
	 * 
	 * 
	 */
	
	private void playHand() throws InterruptedException {
		freshHand();
		
		//This will post the blinds for the correct players
		postBlinds();
		int count = 0;
		int river = 4;
		while (activePlayers.size() > 1) {
			roundOfBetting();
			count+=1;
			if (count == river) {
				showDown();
			}
		showDown();
		}
	}
		
//		if (activePlayers.size() > 1) {
//			//this is now preflop
//			roundOfBetting();
//			
//			if (activePlayers.size() > 1) {
//				//this is the flop
//				actionCards.drawFlop();
//				roundOfBetting();
//
//				if (activePlayers.size() > 1) {
//					//this is the turn
//					actionCards.drawTurn();
//					roundOfBetting();
//
//					if (activePlayers.size() > 1) {
//						actionCards.drawRiver();
//						roundOfBetting();
//						//need to figure out when there is a showdown on the last card
//						showDown();
//
//						
//					}
//				}
//			}
				
//		}
		
//		showDown(); //calculate the pot and evaluate everyones hand.
		

//	}

	/**
	 * 
	 * Function to reset everything before the next hand starts.
	 	* Need to iterate who the dealer is
	 	* shuffle the deck
	 	* pass out hands to each player
	 	* 
	 * 
	 */
	
	private void freshHand() {
		actionCards = new ActionCards();
		deck = new Deck();
		
		dealCards();
		
		
	}


	private void dealCards() {
		for(Player player: activePlayers) {
			player.drawHand();
		}
		
	}


	private void showDown() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * This is going to just reset the board and get ready for the next hand
	 */
	private void resetBoard() {
		// TODO Auto-generated method stub
	}

	/**
	 * This is going to encompass a round of betting
	 * 	- Take into account current bet, available actions
	 * 	-
	 * @throws InterruptedException 
	 */
	private void roundOfBetting() throws InterruptedException {
		int toAct = activePlayers.size();
		BigDecimal currentBet = BigDecimal.ZERO;
		if (toAct == 0) {
			return;
		}
		
		//here we need to pop off the queue
		//take in the message from the player and parse into action, bet, etc
		
		//need to keep track of current bet
		// pot size
		// side pot
		// all in, number of players to act
//	    this.players = new ArrayList<Player>();
//	    this.activePlayers = new ArrayList<Player>();
//	    
//	    this.littleBlind = littleBlind;
//	    this.bigBlind = bigBlind;
//	    this.table = new HashMap<String, Player>();
//	    this.potSize = new BigDecimal(0);

		
		while (toAct > 0) {
			// whose turn is it, then we would need to get avaialble acitons
			ServerActionResponseMsg serverMessage =  getMessage();
			
			currentPlayer = getPlayer(serverMessage.getCurrentTurn());
			availableActions = getAvailableAction(currentPlayer, currentBet);
			
			
			//here we finally send the current table situation along with the players actions to the player... then the player responds accordingly
			
			
			// after message is sent back to the user and then they respond
			playerMessage = messageQueue.take();
			if (playerMessage.getAction() == Actions.FOLD) {
				activePlayers.remove(activePlayers.indexOf(currentPlayer));
			}
			
			else if (playerMessage.getAction() == Actions.CHECK) {
				continue;
				
			}
			
			else if (playerMessage.getAction() == Actions.CALL) {
				currentPlayer.adjustStackSize(currentBet);
				
				//need to keep in mind if its main pot or side pot etc
				potSize.add(currentBet);
				
			}
			else if (playerMessage.getAction() == Actions.RAISE) {
				toAct = activePlayers.size();
				currentBet = currentBet.add(playerMessage.getRaiseAmount());
				currentPlayer.adjustStackSize(currentBet);
				
				//need to keep in mind if its main pot or side pot etc
				potSize.add(currentBet);

			}
			
			
			toAct--;
			
		}
		
		
		
		
		
		
		
	}
	/**
	 * This will get the available action for a player
	 * @return
	 */
	private ArrayList<String> getAvailableAction(Player player, BigDecimal currentBet) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 
	 * @param currentTurn which is a string then will go through players, match the string and return the player object
	 * @return player object
	 */
	private Player getPlayer(String currentTurn) {
		Player playerMatch = null;
		for(Player i: players) {
			if(i.getName().equals(currentTurn)){
				playerMatch = i;
				break;
			}
		}
		return playerMatch;
	}


	/**
	 * This is just here temp
	 * @return null
	 */
	private ServerActionResponseMsg getMessage() {
		// TODO Auto-generated method stub
		return null;
	}


	private void postBlinds() {
		// TODO Auto-generated method stub
		
	}
	

	
}
