package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.corundumstudio.socketio.SocketIOServer;

public class Game {
  private final String gamePassword;
  private Set<String> allPlayersEver;
  private Map<String, BigDecimal> playersToBalances;
  //TODO replace this with the cards
  private Map<String, String> playersToCards;
  private final Queue<LoginAttemptMsg> loginMsgQueue;
  private final Queue<ClientActionMsg> clientMsgQueue;
  
  
  public Game(SocketIOServer server, String gamePassword, Queue<LoginAttemptMsg> loginMsgQueue, 
      Queue<ClientActionMsg> clientMsgQueue) {
    this.loginMsgQueue = loginMsgQueue;
    this.clientMsgQueue = clientMsgQueue;
    this.gamePassword = gamePassword;
  }
  
  
  
  
  /**
   * Authenticate and add the player to the game
   * @return true if the player is successfully authenticated and added to the game, false otherwise
   */
  public boolean authenticatePlayer(String playerName, String providedPassword) {
    if (providedPassword.equals(gamePassword)) {
      if (allPlayersEver.contains(playerName)) {
        //put the player in where they left off
      }
      else {
        //add new player and stuff
      }
      return true;
    }
    
    return false;
  }
}
