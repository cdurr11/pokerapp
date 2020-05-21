package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.UUID;

public class Player {

    private String id;
    private UUID sessionID;
    private BigDecimal balance;
    private Card left;
    private Card right;
    private Deck deck;
    private boolean inPlay = true;

    
    /*
     * AF() = mapping a player to player details which include its current hand, its chip count, and whether it is currently in play or not.
     *        
     * RI = card object must have a chip count whose value is >= 0.
     * 
     * SRE() = fields are private and final
     * 
     */
    
    /**
     * Constructor: 
     * @param Left Card Object
     * @param Right Card Object
     * @param val passed in value of the card value
     */
    public Player(String id, UUID sessionID,
        BigDecimal balance) {
    	this.id = id;
    	this.sessionID = sessionID;
      this.balance = balance;
    }
    
    private void checkRep() {
        assert (this.balance.compareTo(BigDecimal.ZERO) > 0);
    }

	public void drawHand() {
		right = deck.drawCard();
    left = deck.drawCard();		
	}
    /**
     * @return the row 
     */
    public Card left() {
        return this.left;
    }
    
    /**
     * @return the row 
     */
    public Card right() {
        return this.right;
    }
    
    /**
     * @return Return true if the player is still in the hand
     * False otherwise
     */
    public boolean inPlay() {
    	checkRep();
      return this.inPlay;
    }
    
    /**
     * Adjust the size of someone's stack after a call
     * @return 
     */
    public void adjustStackSize(BigDecimal callSize) {
    	this.balance = balance.subtract(callSize);
    }
    
    
    /**
     * @return the value 
     */
    public BigDecimal getBalance() {
        return this.balance;
    }
    
    public void setInPlay(boolean inPlay) {
      this.inPlay = inPlay;
    }
    
    public void setSessionID(UUID sessionID) {
      this.sessionID = sessionID;
    }
    
    public UUID getSessionID() {
      return this.sessionID;
    }
    
    /**
     * @return the name of the player 
     */
    public String getName() {
        return id;
    }
}


