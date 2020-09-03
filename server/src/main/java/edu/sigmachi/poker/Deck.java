package edu.sigmachi.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
  private List<Card> deck;

  public Deck() {
    deck = new ArrayList<Card>();
    for (CardSuit suit : CardSuit.values()) {
      for (CardRank rank : CardRank.values()) {
        deck.add(new Card(suit, rank));
      }
    }
    Collections.shuffle(deck, new Random(10));
  }

  public Card drawCard() {
      return deck.remove(deck.size() - 1);  
  }   
}
