package edu.sigmachi.poker;

public enum CardRank {
  ACE(0, "Ace", "A"), KING(1, "King", "K"), QUEEN(2, "Queen", "Q"), 
  JACK(3, "Jack", "J"), TEN(4, "Ten", "T"), NINE(5, "Nine", "9"),
  EIGHT(6, "Eight", "8"), SEVEN(7, "Seven", "7"), SIX(8, "Six", "6"),
  FIVE(9, "Five", "5"), FOUR(10, "Four", "4"), THREE(11, "Three", "3"),
  TWO(12, "Two", "2");
  
  private final int id;
  private final String name;
  private final String symbol;
  
  CardRank(int id, String name, String symbol) {
    this.id = id;
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
