package edu.sigmachi.poker.Messages;

import java.util.UUID;

public class ConnectionMsg implements DisconnectConnectMsg{
  private UUID sessionID;
  private String playerName;
  
  public ConnectionMsg(UUID sessionID, String playerName) {
    this.sessionID = sessionID;
    this.playerName = playerName;
  }
  public UUID getSessionID() {
    return this.sessionID;
  }
  public String playerName() {
    return this.playerName;
  }
}
