package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.corundumstudio.socketio.SocketIOServer;

import edu.sigmachi.poker.Messages.AfterRoundMsg;
import edu.sigmachi.poker.Messages.DisconnectConnectMsg;
import edu.sigmachi.poker.Messages.InstantGameMsg;
import edu.sigmachi.poker.Messages.StartMsg;

public class Game {
  private final String gamePassword;
  private Set<String> allPlayersEver;
  private Map<String, BigDecimal> playersToBalances;
  //TODO replace this with the cards
  private Map<String, String> playersToCards;
  private final Queue<DisconnectConnectMsg> connectionMsgQueue;
  private final Queue<AfterRoundMsg> afterRoundMsgQueue;
  private final BlockingQueue<InstantGameMsg> instantGameMsgQueue;
  
  
  public Game(SocketIOServer server, String gamePassword,Queue<DisconnectConnectMsg> connectionMsgQueue, 
      Queue<AfterRoundMsg> afterRoundMsgQueue, BlockingQueue<InstantGameMsg> instantGameMsgQueue) {
    this.gamePassword = gamePassword;
    this.connectionMsgQueue = connectionMsgQueue;
    this.instantGameMsgQueue = instantGameMsgQueue;
    this.afterRoundMsgQueue = afterRoundMsgQueue;
  }
  
  public void start() throws InterruptedException {
    while (!(this.instantGameMsgQueue.take() instanceof StartMsg)) {
      System.out.println("You Must Start The Game First!");
    }
    System.out.println("STARTING GAME");
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
