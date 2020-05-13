package edu.sigmachi.poker.Messages;

public class RestartMsg implements InstantGameMsg, ConsoleMsg {
  public ConsoleAction getAction() {
    return ConsoleAction.RESTART;
  }
  
}
