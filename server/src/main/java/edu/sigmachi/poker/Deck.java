package edu.sigmachi.poker;


import java.util.ArrayList;
import java.util.*; 


public class Deck {
    private ArrayList<Card> deck;


    public Deck() {
        
 
        
    deck = new ArrayList<Card>();
    
    for(int x = 1; x <= 13; x++) {
        for(int y = 1; y <= 4; y++) {
            deck.add(new Card(x, y));
        }
    }

    Collections.shuffle(deck);
    
    }

    public Card drawCard() {
        return deck.remove(0);
        
    }
    
}
