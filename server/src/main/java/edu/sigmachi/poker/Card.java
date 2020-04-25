package edu.sigmachi.poker;

public class Card {
    
    private Integer rank, suit;
    
    private static String[] suits = { "hearts", "spades", "diamonds", "clubs" };
    private static String[] ranks  = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
    
    
    public static String rankAsString( int __rank ) {
        return ranks[__rank];
    }

    public Card(int suit, int rank){
        this.suit = suit;
        this.rank = rank;
    }
    
    @Override
    public String toString() {
        return ranks[rank] + " of " + suits[suit];
    }
    
    
    
    public Integer getRank() {
        return rank;
      
    }
    
    public Integer getSuit() {
        return suit;
    }
    

    
}
