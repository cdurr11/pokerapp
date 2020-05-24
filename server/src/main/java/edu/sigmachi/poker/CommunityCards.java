package edu.sigmachi.poker;

import java.util.ArrayList;
import java.util.List;

public class CommunityCards {
    private List<Card> commiunityCards = new ArrayList<>();
    private CommunityCardState state = CommunityCardState.PREFLOP; 
    
    public void draw(Deck deck) {
      switch(commiunityCards.size()) {
        case 0:
          drawFlop(deck);
          state = CommunityCardState.FLOP;
          break;
        case 3:
          drawCard(deck);
          state = CommunityCardState.TURN;
          break;
        case 4:
          drawCard(deck);
          state = CommunityCardState.RIVER;
          break;
        default:
          throw new RuntimeException("Invalid amount of community cards");
      }
    }
    
    private void drawFlop(Deck deck){
    	commiunityCards.add(deck.drawCard());
    	commiunityCards.add(deck.drawCard());
    	commiunityCards.add(deck.drawCard());
    	checkRep();
    }
    
    private void drawCard(Deck deck){
    	commiunityCards.add(deck.drawCard());
    	checkRep();
    }

    public List<Card> getCurrentCommunityCards(){
      List<Card> communityCardsCopy = new ArrayList<Card>(this.commiunityCards); 
      return communityCardsCopy;
    }
    
    public CommunityCardState getState() {
      return this.state;
    }
    
    @Override
    public String toString() {
      List<Card> getCurrentCommunityCards = getCurrentCommunityCards();
      String showFlop = "";
      for(int x = 0; x < getCurrentCommunityCards.size(); x++) {
          Card currentCard = commiunityCards.get(x);
          showFlop += currentCard.toString() + " ";
      }
      return showFlop;
    }
    
    private void checkRep() {
      assert commiunityCards.size() == 0 || commiunityCards.size() == 3 
          || commiunityCards.size() == 4 || commiunityCards.size() == 5;
    }
    
}


