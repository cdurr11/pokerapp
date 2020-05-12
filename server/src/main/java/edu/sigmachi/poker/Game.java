package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.corundumstudio.socketio.SocketIOServer;

import edu.sigmachi.poker.ConsoleMessages.ConsoleMsg;

public class Game {
  private final String gamePassword;
  private Set<String> allPlayersEver;
  private Map<String, BigDecimal> playersToBalances;
  //TODO replace this with the cards
  private Map<String, String> playersToCards;
  private final Queue<LoginAttemptMsg> loginMsgQueue;
  private final Queue<ClientActionMsg> clientMsgQueue;
  private final Queue<ConsoleMsg> consoleMsgQueue;
  
  
  public Game(SocketIOServer server, String gamePassword, Queue<LoginAttemptMsg> loginMsgQueue, 
      Queue<ClientActionMsg> clientMsgQueue, Queue<ConsoleMsg> consoleMsgQueue) {
    this.loginMsgQueue = loginMsgQueue;
    this.clientMsgQueue = clientMsgQueue;
    this.gamePassword = gamePassword;
    this.consoleMsgQueue = consoleMsgQueue;
  }
  
  public void start() {
//    while (true) {
      //check and handle login requests here
//      table.addPlayer();
//      table.removePlayer();
//      //handle consoleMessages here
//        // Modify table however console messages say
//      
//      
//      //perform a round
//      table.doRound(server, clientMsgQueue)
      //send message to the front-end
//    }
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
