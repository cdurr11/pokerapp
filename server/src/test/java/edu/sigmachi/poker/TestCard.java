package edu.sigmachi.poker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestCard {
  @Test 
  public void testCardInit() {
    Card aceOfSpades = new Card(CardSuit.SPADES, CardRank.ACE);
    assertEquals(CardSuit.SPADES, aceOfSpades.getSuit());
    assertEquals(CardRank.ACE, aceOfSpades.getRank());
  }
  
  @Test
  public void testCardsValue() {
    Card aceOfSpades = new Card(CardSuit.SPADES, CardRank.ACE);
    assertEquals((byte) 0, aceOfSpades.getValue());
    
    Card aceOfDiamonds = new Card(CardSuit.DIAMONDS, CardRank.ACE);
    assertEquals((byte) 1, aceOfDiamonds.getValue());
    
    Card aceOfHearts = new Card(CardSuit.HEARTS, CardRank.ACE);
    assertEquals((byte) 2, aceOfHearts.getValue());
    
    Card aceOfClubs = new Card(CardSuit.CLUBS, CardRank.ACE);
    assertEquals((byte) 3, aceOfClubs.getValue());
    
    Card twoOfClubs = new Card(CardSuit.CLUBS, CardRank.TWO);
    assertEquals((byte) 51, twoOfClubs.getValue());
    
    Card fiveOfClubs = new Card(CardSuit.DIAMONDS, CardRank.FIVE);
    assertEquals((byte) 37, fiveOfClubs.getValue());
    
    Card nineOfSpades = new Card(CardSuit.SPADES, CardRank.NINE);
    assertEquals((byte) 20, nineOfSpades.getValue());
    
    Card nineOfDiamonds = new Card(CardSuit.DIAMONDS, CardRank.NINE);
    assertEquals((byte) 21, nineOfDiamonds.getValue());
    
    Card nineOfHearts = new Card(CardSuit.HEARTS, CardRank.NINE);
    assertEquals((byte) 22, nineOfHearts.getValue());
    
    Card nineOfClubs = new Card(CardSuit.CLUBS, CardRank.NINE);
    assertEquals((byte) 23, nineOfClubs.getValue());
  }
  
  @Test
  public void testCardRank() {
    assertEquals("Two", CardRank.TWO.getName());
    assertEquals("Three", CardRank.THREE.getName());
    assertEquals("Four", CardRank.FOUR.getName());
    assertEquals("Five", CardRank.FIVE.getName());
    assertEquals("Six", CardRank.SIX.getName());
    assertEquals("Seven", CardRank.SEVEN.getName());
    assertEquals("Eight", CardRank.EIGHT.getName());
    assertEquals("Nine", CardRank.NINE.getName());
    assertEquals("Ten", CardRank.TEN.getName());
    assertEquals("Jack", CardRank.JACK.getName());
    assertEquals("Queen", CardRank.QUEEN.getName());
    assertEquals("King", CardRank.KING.getName());
    assertEquals("Ace", CardRank.ACE.getName());
    
    assertEquals("2", CardRank.TWO.getSymbol());
    assertEquals("3", CardRank.THREE.getSymbol());
    assertEquals("4", CardRank.FOUR.getSymbol());
    assertEquals("5", CardRank.FIVE.getSymbol());
    assertEquals("6", CardRank.SIX.getSymbol());
    assertEquals("7", CardRank.SEVEN.getSymbol());
    assertEquals("8", CardRank.EIGHT.getSymbol());
    assertEquals("9", CardRank.NINE.getSymbol());
    assertEquals("T", CardRank.TEN.getSymbol());
    assertEquals("J", CardRank.JACK.getSymbol());
    assertEquals("Q", CardRank.QUEEN.getSymbol());
    assertEquals("K", CardRank.KING.getSymbol());
    assertEquals("A", CardRank.ACE.getSymbol());
    
    assertEquals(12, CardRank.TWO.getValue());
    assertEquals(11, CardRank.THREE.getValue());
    assertEquals(10, CardRank.FOUR.getValue());
    assertEquals(9, CardRank.FIVE.getValue());
    assertEquals(8, CardRank.SIX.getValue());
    assertEquals(7, CardRank.SEVEN.getValue());
    assertEquals(6, CardRank.EIGHT.getValue());
    assertEquals(5, CardRank.NINE.getValue());
    assertEquals(4, CardRank.TEN.getValue());
    assertEquals(3, CardRank.JACK.getValue());
    assertEquals(2, CardRank.QUEEN.getValue());
    assertEquals(1, CardRank.KING.getValue());
    assertEquals(0, CardRank.ACE.getValue()); 
  }
  
  @Test
  public void testCardSuit() {
    assertEquals("Spades", CardSuit.SPADES.getName());
    assertEquals("Diamonds", CardSuit.DIAMONDS.getName());
    assertEquals("Hearts", CardSuit.HEARTS.getName());
    assertEquals("Clubs", CardSuit.CLUBS.getName());
    
    assertEquals("S", CardSuit.SPADES.getSymbol());
    assertEquals("D", CardSuit.DIAMONDS.getSymbol());
    assertEquals("H", CardSuit.HEARTS.getSymbol());
    assertEquals("C", CardSuit.CLUBS.getSymbol());
    
    assertEquals(0, CardSuit.SPADES.getValue());
    assertEquals(1, CardSuit.DIAMONDS.getValue());
    assertEquals(2, CardSuit.HEARTS.getValue());
    assertEquals(3, CardSuit.CLUBS.getValue());
  }
}
