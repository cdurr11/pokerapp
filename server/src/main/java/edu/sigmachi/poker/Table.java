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
    private final Deck deck;
    
    /** This consists of the cards on the table: Flop, Turn, and the River. */
    private final ArrayList<Card> actionCards;
    
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
	    this.actionCards = new ArrayList<Card>();

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
	
	//  public void check() {
	//	  actionCards.draw();
	//      //need the user to be able to give 
	//     
	//  }
	  
	  /*
	  public Table handlefold() { 
	        
	  }
	  public Table handleraise() {
	      
	  }
	  
	  
	    
	    */
	 }
