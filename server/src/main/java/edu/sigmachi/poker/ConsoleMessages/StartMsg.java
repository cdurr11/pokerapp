package edu.sigmachi.poker.ConsoleMessages;

public class StartMsg implements InstantGameMsg, ConsoleMsg {
  
  public ConsoleAction getAction() {
    return ConsoleAction.START;
  }
  
}
