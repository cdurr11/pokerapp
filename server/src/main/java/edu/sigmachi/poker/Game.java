package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.corundumstudio.socketio.SocketIOServer;

import edu.sigmachi.poker.Messages.AfterRoundMsg;
import edu.sigmachi.poker.Messages.ConnectionMsg;
import edu.sigmachi.poker.Messages.DisconnectConnectMsg;
import edu.sigmachi.poker.Messages.InstantGameMsg;
import edu.sigmachi.poker.Messages.StartMsg;

public class Game {
  private final String gamePassword;
  private Set<String> allPlayersEver;
  private Map<String, BigDecimal> playersToLastBalance;

  private final Queue<DisconnectConnectMsg> connectionMsgQueue;
  private final Queue<AfterRoundMsg> afterRoundMsgQueue;
  private final BlockingQueue<InstantGameMsg> instantGameMsgQueue;
  // TODO probably want to set blinds as parameter to start message.
  private BigDecimal bigBlindValue = new BigDecimal("20.00");
  private BigDecimal smallBlindValue = new BigDecimal("10.00");
  
  public Game(SocketIOServer server, String gamePassword, Queue<DisconnectConnectMsg> connectionMsgQueue, 
      Queue<AfterRoundMsg> afterRoundMsgQueue, BlockingQueue<InstantGameMsg> instantGameMsgQueue) {
    this.gamePassword = gamePassword;
    this.connectionMsgQueue = connectionMsgQueue;
    this.instantGameMsgQueue = instantGameMsgQueue;
    this.afterRoundMsgQueue = afterRoundMsgQueue;
    
  }
  
  private void processConnectionMsg(DisconnectConnectMsg msg) {
    if (msg instanceof ConnectionMsg) {
      ConnectionMsg connectMsg = (ConnectionMsg) msg;
      String playerName = connectMsg.getPlayerName();
      if (allPlayersEver.contains(playerName)) {
        // This is a reconnection, so we should give them the balance they had before
      }
      else {
        // New player that has yet to connect during this game.
        allPlayersEver.add(playerName);
      }
    }
  }
  
  public void start() throws InterruptedException {
    // This is the loading lobby, just wait until people join
    Table gameTable = new Table(this.smallBlindValue, this.bigBlindValue, this.instantGameMsgQueue);
    while (true) {
      if (!(this.instantGameMsgQueue.take() instanceof StartMsg)) {
        System.out.println("You Must Start The Game First!");
      }
      // TODO investigate thread safety of this, I'm not convinced
      if (!connectionMsgQueue.isEmpty()) {
        while (!connectionMsgQueue.isEmpty()) {
          DisconnectConnectMsg msg = connectionMsgQueue.poll();
          processConnectionMsg(msg);
        }
      }
      
    }
    System.out.println("STARTING GAME");
    while (true) {
     
    }
  }
}
