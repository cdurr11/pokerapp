package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.UUID;

public class Player {
  private String playerName;
  private UUID sessionID;
  private BigDecimal balance;
  private Card card1;
  private Card card2;
  private boolean inPlay = true;
  private boolean inHand = true;
  private boolean allIn = false;

  /**
   * 
   * @param playerName
   * @param sessionID
   * @param balance
   */
  public Player(String id, UUID sessionID,
      BigDecimal balance) {
  	this.playerName = id;
  	this.sessionID = sessionID;
    this.balance = balance;
  }
  
  private void checkRep() {
      assert (this.balance.compareTo(BigDecimal.ZERO) >= 0);
  }

	public void drawHand(Deck deck) {
	  this.card1 = deck.drawCard();
	  this.card2 = deck.drawCard();
	}
  /**
   * @return the row 
   */
  public Card getCard1() {
      return this.card1;
  }
  
  /**
   * @return 
   */
  public Card getCard2() {
      return this.card2;
  }
  
  public void fold() {
    this.inHand = false;
  }
  
  public void resetHand() {
    this.inHand = false;
    this.allIn = false;
  }
  
  public boolean getAllIn() {
    return this.allIn;
  }
  
  public void goAllIn() {
    this.allIn = true;
  }
  
  public boolean getHandStatus() {
    return this.inHand;
  }
  
  /**
   * @return Return true if the player is still in the hand
   * False otherwise
   */
  public boolean inPlay() {
    return this.inPlay;
  }
  
  /**
   * Adjusts the balance by adding amount to the current balance
   * @param amount positive of negative BigDecimal with scale 2
   */
  public void adjustBalance(BigDecimal amount) {
  	this.balance = balance.add(amount);
  	checkRep();
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
      return playerName;
  }
  
  @Override 
  public boolean equals(Object that) {
    if (!(that instanceof Player)) {
      return false;
    }
    return this.playerName.equals(((Player) that).getName());
  }
  
  @Override
  public int hashCode() {
    return this.playerName.hashCode();
  }
}


