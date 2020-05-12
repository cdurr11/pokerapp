package edu.sigmachi.poker.ConsoleMessages;

public class PrintGameStateMsg implements ConsoleMsg {

  public ConsoleAction getAction() {
    return ConsoleAction.PRINT_GAME_STATE;
  }
  
}
