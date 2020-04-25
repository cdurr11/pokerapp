package edu.sigmachi.poker;

public class Player {

    private String id;
    
    private float stackSize;

    private final Card left;
    private final Card right;
    
    
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
    public Player(String id, Card left, Card right, float stackSize, boolean inPlay){
    	this.id = id;
        this.left = left;
        this.right = right;
        this.stackSize = stackSize;
        this.inPlay = inPlay;

    }
    
    private void checkRep() {
        assert this.stackSize >= 0;
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
     * @return the value 
     */
    public float getChipCount() {
        return this.stackSize;
    }
    
    
    /**
     * @return the name of the player 
     */
    public String getName() {
        return id;
    }
    
    
    
}


