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
  private final Queue<DisconnectConnectMsg> connectionMsgQueue;
  private final Queue<ClientActionMsg> clientMsgQueue;
  private final Queue<ConsoleMsg> consoleMsgQueue;
  
  
  public Game(SocketIOServer server, String gamePassword,Queue<DisconnectConnectMsg> connectionMsgQueue, 
      Queue<ClientActionMsg> clientMsgQueue, Queue<ConsoleMsg> consoleMsgQueue) {
    this.connectionMsgQueue = connectionMsgQueue;
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
}
