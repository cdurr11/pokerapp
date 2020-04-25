package edu.sigmachi.poker;


import java.math.BigDecimal;
import java.util.*;
import java.util.Map;


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
	    this.dealer = new Player(null, 0, false);

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
	 	* 
	 	* 
	 * 
	 * 
	 * 
	 * 
	 */
	
	private void playHand() {
		freshHand();
		
		//This will post the blinds for the correct players
		postBlinds();
		
		if (activePlayers.size() > 1) {
			//this is now preflop
			roundOfBetting();
			
			if (activePlayers.size() > 1) {
				//this is the flop
				actionCards.drawFlop();
				roundOfBetting();

				if (activePlayers.size() > 1) {
					//this is the turn
					actionCards.drawTurn();
					roundOfBetting();

					if (activePlayers.size() > 1) {
						actionCards.drawRiver();
						roundOfBetting();
						//need to figure out when there is a showdown on the last card
						showDown();

						
					}
				}
			}
				
		}
		
		showDown(); //calculate the pot and evaluate everyones hand.
		

	}

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


	private void roundOfBetting() {
		// TODO Auto-generated method stub
		
	}


	private void postBlinds() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
