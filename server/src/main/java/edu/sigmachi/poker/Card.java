package edu.sigmachi.poker;

public class Card {
    
    private Integer rank, suit;
    // CODY : I think it is best to represent these with H S D C
    private static String[] suits = { "hearts", "spades", "diamonds", "clubs" };
    // CODY : Ace = A, Jack = J, Queen = Q, King = K, makes it easier to parse in several places
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
