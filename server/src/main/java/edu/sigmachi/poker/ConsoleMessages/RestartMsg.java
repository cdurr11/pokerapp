package edu.sigmachi.poker.ConsoleMessages;

public class RestartMsg implements ConsoleMsg {
  public ConsoleAction getAction() {
    return ConsoleAction.RESTART;
  }
  
}
