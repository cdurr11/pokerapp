package edu.sigmachi.poker;

import java.util.ArrayList;
import java.util.List;

public class CommunityCards {
    private List<Card> communityCards = new ArrayList<>();
    private CommunityCardState state = CommunityCardState.PREFLOP; 
    
    public void draw(Deck deck) {
      switch(communityCards.size()) {
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
      checkRep();
    }
    
    private void drawFlop(Deck deck){
    	communityCards.add(deck.drawCard());
    	communityCards.add(deck.drawCard());
    	communityCards.add(deck.drawCard());
    	checkRep();
    }
    
    private void drawCard(Deck deck){
    	communityCards.add(deck.drawCard());
    	checkRep();
    }

    public List<Card> getCurrentCommunityCards(){
      List<Card> communityCardsCopy = new ArrayList<Card>(this.communityCards);
      checkRep();
      return communityCardsCopy;
    }
    
    public CommunityCardState getState() {
      return this.state;
    }
    
    @Override
    public String toString() {
      List<Card> getCurrentCommunityCards = getCurrentCommunityCards();
      String showFlop = "";
      for(int i = 0; i < getCurrentCommunityCards.size(); i++) {
          Card currentCard = communityCards.get(i);
          showFlop += currentCard.toString() + " ";
      }
      checkRep();
      return showFlop;
    }
    
    private void checkRep() {
      assert communityCards.size() == 0 || communityCards.size() == 3
          || communityCards.size() == 4 || communityCards.size() == 5;

      if (this.getState() == CommunityCardState.PREFLOP) {
        assert this.getCurrentCommunityCards().size() == 0;
      } else if (this.getState() == CommunityCardState.FLOP) {
        assert this.getCurrentCommunityCards().size() == 3;
      } else if (this.getState() == CommunityCardState.TURN) {
        assert this.getCurrentCommunityCards().size() == 4;
      } else {
        assert this.getCurrentCommunityCards().size() == 5;
      }
    }
}


