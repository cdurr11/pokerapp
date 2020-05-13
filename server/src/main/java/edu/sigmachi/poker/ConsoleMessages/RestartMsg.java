package edu.sigmachi.poker.ConsoleMessages;

public class RestartMsg implements InstantGameMsg, ConsoleMsg {
  public ConsoleAction getAction() {
    return ConsoleAction.RESTART;
  }
  
}
