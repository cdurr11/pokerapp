package edu.sigmachi.poker.Messages;

import java.util.UUID;

public class DisconnectionMsg implements DisconnectConnectMsg{
  private UUID sessionID;
  
  public DisconnectionMsg(UUID sessionID) {
    this.sessionID = sessionID;
  }
  public UUID getSessionID() {
    return this.sessionID;
  }
}
