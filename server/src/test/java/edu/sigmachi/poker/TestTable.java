package edu.sigmachi.poker;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import edu.sigmachi.poker.Messages.ClientActionMsg;
import edu.sigmachi.poker.Messages.GameStateMsg;
import edu.sigmachi.poker.Messages.InstantGameMsg;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;

public class TestTable {
  private Table gameTable;
  private Thread gameThread;
  private BlockingQueue<GameStateMsg> gameStateMsgQueue;
  private BlockingQueue<InstantGameMsg> instantGameMsgQueue;

  @Before
  public void setUpTable() {
    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(9092);

    SocketIOServer server = new SocketIOServer(config);
    this.instantGameMsgQueue = new LinkedBlockingQueue<>();
    this.gameStateMsgQueue = new LinkedBlockingQueue<>();
    this.gameTable = new Table(server, new BigDecimal("10.00"), new BigDecimal("20.00"),
            this.instantGameMsgQueue, this.gameStateMsgQueue, new BigDecimal("100.00"));
    this.gameTable.addPlayer("cdurr", UUID.randomUUID());
    this.gameTable.addPlayer("pruh", UUID.randomUUID());
    this.gameTable.addPlayer("blevin", UUID.randomUUID());
    this.gameTable.addPlayer("mheatzig", UUID.randomUUID());
    this.gameTable.addPlayer("oheins", UUID.randomUUID());
    gameThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          gameTable.playHand();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    });
    gameThread.start();
  }

  @After
  public void stopGameThread() {
    gameThread.interrupt();
  }

  /**
   * Tests everyone calling the big blind
   * @throws InterruptedException
   */
  @Test
  public void testBasicBigBlindCall() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    mainPot = gameState.getPots().get(0);
    assertEquals(5, gameState.getActivePlayers().size());
    assertEquals(5, gameState.getCurrentPlayersInHand().size());
    assertEquals("cdurr", gameState.getDealerPlayerName());
    assertEquals(0, gameState.getDealerIndex());
    assertEquals("pruh", gameState.getSmallBlindPlayerName());
    assertEquals(1, gameState.getSmallBlindIndex());
    assertEquals("blevin", gameState.getBigBlindPlayerName());
    assertEquals(2, gameState.getBigBlindIndex());
    assertEquals("oheins", gameState.getCurrentPlayerName());
    assertEquals(4, gameState.getCurrentPlayerIndex());
    assertEquals(new BigDecimal("50.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    mainPot = gameState.getPots().get(0);
    assertEquals(5, gameState.getActivePlayers().size());
    assertEquals(5, gameState.getCurrentPlayersInHand().size());
    assertEquals("cdurr", gameState.getDealerPlayerName());
    assertEquals(0, gameState.getDealerIndex());
    assertEquals("pruh", gameState.getSmallBlindPlayerName());
    assertEquals(1, gameState.getSmallBlindIndex());
    assertEquals("blevin", gameState.getBigBlindPlayerName());
    assertEquals(2, gameState.getBigBlindIndex());
    assertEquals("cdurr", gameState.getCurrentPlayerName());
    assertEquals(0, gameState.getCurrentPlayerIndex());
    assertEquals(new BigDecimal("70.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    mainPot = gameState.getPots().get(0);
    assertEquals("pruh", gameState.getCurrentPlayerName());
    assertEquals(1, gameState.getCurrentPlayerIndex());
    assertEquals(new BigDecimal("90.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    mainPot = gameState.getPots().get(0);
    assertEquals("blevin", gameState.getCurrentPlayerName());
    assertEquals(2, gameState.getCurrentPlayerIndex());
    assertEquals(new BigDecimal("100.00"), mainPot.getPotAmount());

    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.COMPLETION, gameState.getMsgType());
  }
}
