package edu.sigmachi.poker.Messages;

public class PrintGameStateMsg implements InstantGameMsg, ConsoleMsg {

  public ConsoleAction getAction() {
    return ConsoleAction.PRINT_GAME_STATE;
  }
  
}
