package edu.sigmachi.poker.Messages;

public class EndMsg implements InstantGameMsg, ConsoleMsg {

  public ConsoleAction getAction() {
    return ConsoleAction.END;
  }
  
}
