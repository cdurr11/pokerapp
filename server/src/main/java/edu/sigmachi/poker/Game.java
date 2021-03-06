package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.corundumstudio.socketio.SocketIOServer;

import edu.sigmachi.poker.Messages.*;

public class Game {
  private final String gamePassword;
  private Set<String> allPlayersEver;
  private Map<String, BigDecimal> playersToLastBalance;

  private final Queue<DisconnectConnectMsg> connectionMsgQueue;
  private final Queue<AfterRoundMsg> afterRoundMsgQueue;
  private final BlockingQueue<InstantGameMsg> instantGameMsgQueue;
  private final BlockingQueue<GameStateMsg> gameStateMsgQueue;
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
    this.gameStateMsgQueue = new LinkedBlockingQueue<>();
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
      gameTable.adjustPlayerBalance(moneyMsg.getPlayerName(), moneyMsg.getDollarAmount());
    }
  }

  public void start() throws InterruptedException {
    // TODO eventually this should be an argument when initializing from console 
    BigDecimal initialBuyIn = new BigDecimal("10.00");
    Table gameTable = new Table(this.server, this.smallBlindValue, this.bigBlindValue, 
        this.instantGameMsgQueue, this.gameStateMsgQueue, initialBuyIn);
    // This is the loading lobby, just wait until people join
    while (true) {
      InstantGameMsg instantGameMsg = this.instantGameMsgQueue.take();
      // process
      while (!connectionMsgQueue.isEmpty()) {
        DisconnectConnectMsg msg = connectionMsgQueue.poll();
        assert msg != null;
        processConnectionMsg(msg, gameTable);
      }
      if (!(instantGameMsg instanceof StartMsg)) {
        System.out.println("You Must Start The Game First!");
      }
      else {
        if (gameTable.getActivePlayers().size() >= 5) {
          System.out.println("STARTING GAME");
          break;
        }
        else {
          System.out.println("5 PLAYERS ARE NEEDED TO START THE GAME");
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
