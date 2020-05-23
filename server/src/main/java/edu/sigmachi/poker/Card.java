package edu.sigmachi.poker;

public class Card {    
  private CardSuit suit;
  private CardRank rank;

  public Card(CardSuit suit, CardRank rank){
    this.suit = suit;
    this.rank = rank;
  }
  
  public CardRank getRank() {
    return rank; 
  }
  
  public CardSuit getSuit() {
    return suit;
  }
  
  public byte getValue() {
    return (byte) ((rank.getValue() * 4) + suit.getValue());
  }    
}
