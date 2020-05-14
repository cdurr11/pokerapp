package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Player {

    private String id;
    
    private BigDecimal stackSize;

    private Card left;
    private Card right;
    private Deck deck;
    
    
    private final boolean inPlay;

    
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
    public Player(String id, BigDecimal stackSize, boolean inPlay){
    	this.id = id;
      this.stackSize = stackSize;
      this.inPlay = inPlay;
//      this.deck = new Deck();

    }
    
    private void checkRep() {
      // CODY : Max Goldman would be proud :,)
        assert (this.stackSize.compareTo(BigDecimal.ZERO) > 0);
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
    	this.stackSize = stackSize.subtract(callSize);
    }
    
    
    /**
     * @return the value 
     */
    public BigDecimal getChipCount() {
        return this.stackSize;
    }
    
    
    /**
     * @return the name of the player 
     */
    public String getName() {
        return id;
    }


    
}


