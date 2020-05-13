package edu.sigmachi.poker.Messages;

public class StartMsg implements InstantGameMsg, ConsoleMsg {
  
  public ConsoleAction getAction() {
    return ConsoleAction.START;
  }
  
}
