package edu.sigmachi.poker;

public enum CardSuit {
  SPADES(0, "Spades", "S"), DIAMONDS(1, "Diamonds", "D"), HEARTS(2, "Hearts", "H"), CLUBS(3, "Clubs", "C");
  
  private final int id;
  private final String name;
  private final String symbol;
  
  CardSuit(int id, String name, String symbol) {
    this.id =  id;
    this.name = name;
    this.symbol = symbol;
  } 
  
  public int getValue() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getSymbol() {
    return this.symbol;
  }
}
