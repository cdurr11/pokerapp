package edu.sigmachi.poker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import edu.sigmachi.poker.ConsoleMessages.AddPlayerMoneyMsg;
import edu.sigmachi.poker.ConsoleMessages.ConsoleMsg;
import edu.sigmachi.poker.ConsoleMessages.EndMsg;
import edu.sigmachi.poker.ConsoleMessages.PrintGameStateMsg;
import edu.sigmachi.poker.ConsoleMessages.RestartMsg;
import edu.sigmachi.poker.ConsoleMessages.StartMsg;

public class TestREPL {

  /*
   * Covers the START command, 
   *    lowercase and upper case
   */
  @Test
  public void testSTART() throws InterruptedException {
    assertTrue(REPL.parseConsoleMsg("START") instanceof StartMsg);
    assertTrue(REPL.parseConsoleMsg("start") instanceof StartMsg);
    assertTrue(REPL.parseConsoleMsg("STaRt") instanceof StartMsg);
  }

  /*
   * Covers the RESTART command, 
   *    lowercase and upper case
   */
  @Test
  public void testRESTART() throws InterruptedException {
    assertTrue(REPL.parseConsoleMsg("RESTART") instanceof RestartMsg);
    assertTrue(REPL.parseConsoleMsg("restart") instanceof RestartMsg);
    assertTrue(REPL.parseConsoleMsg("rEsTarT") instanceof RestartMsg);
  }
  
  /*
   * Covers the END command, 
   *    lowercase and upper case
   */
  @Test
  public void testEND() throws InterruptedException {
    assertTrue(REPL.parseConsoleMsg("END") instanceof EndMsg);
    assertTrue(REPL.parseConsoleMsg("end") instanceof EndMsg);
    assertTrue(REPL.parseConsoleMsg("EnD") instanceof EndMsg);
  }
  
  /*
   * Covers the GAME_STATE command, 
   *    lowercase and upper case
   */
  @Test
  public void testGAME_STATE() throws InterruptedException {
    assertTrue(REPL.parseConsoleMsg("GAME_STATE") instanceof PrintGameStateMsg);
    assertTrue(REPL.parseConsoleMsg("game_state") instanceof PrintGameStateMsg);
    assertTrue(REPL.parseConsoleMsg("gAmE_STaTe") instanceof PrintGameStateMsg);
  }
  
  /*
   * Covers an invalid command being inputted
   */
  @Test
  public void testInvalidCommand() throws InterruptedException {
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("HELLO");
    });
  }
  
  /*
   * Covers handleChangeMoney, 
   *    add and subtract, 
   *    lowercase, uppercase command, 
   *    incorrect precision entered, 
   *    incorrect length, 
   *    third element is not a number
   */
  @Test
  public void testCHANGE_MONEY() throws InterruptedException {
    ConsoleMsg output = REPL.parseConsoleMsg("AdD_MoNEy cdurr 1.0");
    assertTrue(output instanceof AddPlayerMoneyMsg);
    AddPlayerMoneyMsg castedOutput = (AddPlayerMoneyMsg) output;
    assertEquals("cdurr", castedOutput.getPlayerName());
    assertEquals(new BigDecimal("1.00"), castedOutput.getDollarAmount());
    assertEquals(2, castedOutput.getDollarAmount().scale());

    ConsoleMsg output2 = REPL.parseConsoleMsg("AdD_MoNEy cdurr 1");
    assertTrue(output2 instanceof AddPlayerMoneyMsg);
    AddPlayerMoneyMsg castedOutput2 = (AddPlayerMoneyMsg) output2;
    assertEquals("cdurr", castedOutput2.getPlayerName());
    assertEquals(new BigDecimal("1.00"), castedOutput2.getDollarAmount());
    assertEquals(2, castedOutput2.getDollarAmount().scale());
    
    // Not enough arguments
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("AdD_MoNEy cdurr");
    });
    
    // Too many arguments
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("AdD_MoNEy cdurr 4.0 mheatzig");
    });
    
    // Third argument cannot be converted to Big Decimal
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("AdD_MoNEy cdurr mheatzig");
    });
    
    ConsoleMsg output3 = REPL.parseConsoleMsg("subTrAct_MoNEy cdurr 1.0");
    assertTrue(output3 instanceof AddPlayerMoneyMsg);
    AddPlayerMoneyMsg castedOutput3 = (AddPlayerMoneyMsg) output3;
    assertEquals("cdurr", castedOutput3.getPlayerName());
    assertEquals(new BigDecimal("-1.00"), castedOutput3.getDollarAmount());
    assertEquals(2, castedOutput3.getDollarAmount().scale());

    ConsoleMsg output4 = REPL.parseConsoleMsg("subTrAct_MoNEy cdurr 1");
    assertTrue(output4 instanceof AddPlayerMoneyMsg);
    AddPlayerMoneyMsg castedOutput4 = (AddPlayerMoneyMsg) output4;
    assertEquals("cdurr", castedOutput4.getPlayerName());
    assertEquals(new BigDecimal("-1.00"), castedOutput4.getDollarAmount());
    assertEquals(2, castedOutput4.getDollarAmount().scale());
    
    // Not enough arguments
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("subTrAct_MoNEy cdurr");
    });
    
    // Too many arguments
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("subTrAct_MoNEy cdurr 4.0 mheatzig");
    });
    
    // Third argument cannot be converted to Big Decimal
    assertThrows(IllegalArgumentException.class, () -> {
      REPL.parseConsoleMsg("AdD_MoNEy cdurr mheatzig");
    });
  }
}
