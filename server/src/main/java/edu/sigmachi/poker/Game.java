package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.corundumstudio.socketio.SocketIOServer;

import edu.sigmachi.poker.Messages.AddPlayerMoneyMsg;
import edu.sigmachi.poker.Messages.AfterRoundMsg;
import edu.sigmachi.poker.Messages.ConnectionMsg;
import edu.sigmachi.poker.Messages.DisconnectConnectMsg;
import edu.sigmachi.poker.Messages.DisconnectionMsg;
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
  
  SocketIOServer server;

  public Game(SocketIOServer server, String gamePassword, Queue<DisconnectConnectMsg> connectionMsgQueue,
      Queue<AfterRoundMsg> afterRoundMsgQueue, BlockingQueue<InstantGameMsg> instantGameMsgQueue) {
    this.gamePassword = gamePassword;
    this.connectionMsgQueue = connectionMsgQueue;
    this.instantGameMsgQueue = instantGameMsgQueue;
    this.afterRoundMsgQueue = afterRoundMsgQueue;
    this.server = server;
  }

  private void processConnectionMsg(DisconnectConnectMsg msg, Table gameTable) {
    if (msg instanceof ConnectionMsg) {
      ConnectionMsg connectMsg = (ConnectionMsg) msg;
      String playerName = connectMsg.getPlayerName();
      gameTable.addPlayer(connectMsg.getPlayerName(), connectMsg.getSessionID());
    } else {
      DisconnectionMsg disconnectMsg = (DisconnectionMsg) msg;
      gameTable.removePlayer(disconnectMsg.getSessionID());
    }
  }
  
  private void processAfterRoundMsg(AfterRoundMsg msg, Table gameTable) {
    if (msg instanceof AddPlayerMoneyMsg) {
      AddPlayerMoneyMsg moneyMsg = (AddPlayerMoneyMsg) msg;
      gameTable.modifyPlayerBalance(moneyMsg.getPlayerName(), moneyMsg.getDollarAmount());
    }
  }

  public void start() throws InterruptedException {
    // TODO eventually this should be an argument when initializing from console 
    BigDecimal initialBuyIn = new BigDecimal("10.00");
    Table gameTable = new Table(this.server, this.smallBlindValue, this.bigBlindValue, 
        this.instantGameMsgQueue, initialBuyIn);
    //This is the loading lobby, just wait until people join
    while (true) {
      if (!(this.instantGameMsgQueue.take() instanceof StartMsg)) {
        System.out.println("You Must Start The Game First!");
      }
      //process 
      while (!connectionMsgQueue.isEmpty()) {
        DisconnectConnectMsg msg = connectionMsgQueue.poll();
        assert msg != null;
        processConnectionMsg(msg, gameTable);
      }
      
      if (this.instantGameMsgQueue.take() instanceof StartMsg) {
        if (gameTable.getActivePlayers().size() >= 5) {
          System.out.println("STARTING GAME");
          break;
        }
        else {
          System.out.println("NEED 5 PLAYERS");
        }
      }
    }
    // Left the loading lobby, now into the game loop
    while (true) {
      while (!connectionMsgQueue.isEmpty()) {
        DisconnectConnectMsg msg = connectionMsgQueue.poll();
        assert msg != null;
        processConnectionMsg(msg, gameTable);
      }
      while (!afterRoundMsgQueue.isEmpty()) {
        AfterRoundMsg msg = afterRoundMsgQueue.poll();
        assert msg != null;
        processAfterRoundMsg(msg, gameTable);
      }
      gameTable.playHand();
    }
  }
}
