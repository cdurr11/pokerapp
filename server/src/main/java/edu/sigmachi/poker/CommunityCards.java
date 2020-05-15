package edu.sigmachi.poker;

import java.util.*;

public class CommunityCards {
    private ArrayList<Card> actionCards;
    private Deck deck;

    
    
    public void draw() {
        if(actionCards.size() == 0) {
            drawFlop();
        }
        else if(actionCards.size() == 3) {
            drawTurn();
        }
        else {
            drawRiver();
        }
    }
    
    public void drawFlop(){
    	actionCards.add(deck.drawCard());
    	actionCards.add(deck.drawCard());
    	actionCards.add(deck.drawCard());

    }
    
    public void drawTurn(){
    	actionCards.add(deck.drawCard());

    }
    
    public void drawRiver(){
    	actionCards.add(deck.drawCard());

    }

    

    public ArrayList<Card> getCurrentCommunityCards(){
        return actionCards;
    }
    
    @Override
    public String toString() {
        ArrayList<Card> getCurrentCommunityCards = getCurrentCommunityCards();
        String showFlop = "";
        for(int x = 0; x < getCurrentCommunityCards.size(); x++) {
            Card currentCard = actionCards.get(x);
            showFlop += currentCard.toString() + " ";
        }
        return showFlop;
    }
    
    
}


