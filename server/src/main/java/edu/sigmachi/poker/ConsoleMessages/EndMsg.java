package edu.sigmachi.poker.ConsoleMessages;

public class EndMsg implements InstantGameMsg, ConsoleMsg {

  public ConsoleAction getAction() {
    return ConsoleAction.END;
  }
  
}
