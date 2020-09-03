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
   * Tests everyone calling the big blind with a check at the end
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

    assertEquals(new BigDecimal("90.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());

    assertEquals(0, gameState.getCommunityCards().size());
    assertEquals(CommunityCardState.PREFLOP, gameState.getCommunityCardState());
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
    assertEquals(0, gameState.getCommunityCards().size());
    assertEquals(CommunityCardState.PREFLOP, gameState.getCommunityCardState());
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
    assertEquals(0, gameState.getCommunityCards().size());
    assertEquals(CommunityCardState.PREFLOP, gameState.getCommunityCardState());
    mainPot = gameState.getPots().get(0);
    assertEquals("pruh", gameState.getCurrentPlayerName());
    assertEquals(1, gameState.getCurrentPlayerIndex());
    assertEquals(new BigDecimal("90.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    assertEquals(0, gameState.getCommunityCards().size());
    assertEquals(CommunityCardState.PREFLOP, gameState.getCommunityCardState());
    mainPot = gameState.getPots().get(0);
    assertEquals("blevin", gameState.getCurrentPlayerName());
    assertEquals(2, gameState.getCurrentPlayerIndex());
    assertEquals(new BigDecimal("100.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.ROBCOMPLETION, gameState.getMsgType());
    assertEquals(new BigDecimal("100.00"), mainPot.getPotAmount());

    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.PLAYHAND, gameState.getMsgType());
    assertEquals(3, gameState.getCommunityCards().size());
    assertEquals(CommunityCardState.FLOP, gameState.getCommunityCardState());
  }

  @Test
  public void testBalancesUpdatedCorrectlyAfterBet() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    assertEquals(new BigDecimal("100.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("90.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("100.00"), gameState.getTable().get("oheins").getBalance());

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    assertEquals(new BigDecimal("100.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("90.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("90.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();
  }
  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void testRaising() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    assertEquals(0, gameState.getDealerIndex());
    assertEquals("cdurr", gameState.getDealerPlayerName());
    assertEquals(1, gameState.getSmallBlindIndex());
    assertEquals("pruh", gameState.getSmallBlindPlayerName());
    assertEquals(2, gameState.getBigBlindIndex());
    assertEquals("blevin", gameState.getBigBlindPlayerName());

    mainPot = gameState.getPots().get(0);
    assertEquals(new BigDecimal("100.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("pruh", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    mainPot = gameState.getPots().get(0);
    Player pruh = gameState.getTable().get("pruh");
    assertEquals(new BigDecimal("70.00"), pruh.getBalance());
    assertEquals(new BigDecimal("110.00"), mainPot.getPotAmount());
  }

  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void testRaisingComesBackFullCircle() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    mainPot = gameState.getPots().get(0);
    assertEquals(new BigDecimal("110.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    // COMPLETION
    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.ROBCOMPLETION, gameState.getMsgType());
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.PLAYHAND, gameState.getMsgType());
    mainPot = gameState.getPots().get(0);
    assertEquals(new BigDecimal("150.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("oheins").getBalance());
  }

  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void testBigBlindRaiseAndDoubleRaise() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    mainPot = gameState.getPots().get(0);
    assertEquals(new BigDecimal("110.00"), mainPot.getPotAmount());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());

    clientMsg = new ClientActionMsg("mheatzig", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.ROBCOMPLETION, gameState.getMsgType());
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();
    assertEquals(GameStateMsg.GameStateMsgType.PLAYHAND, gameState.getMsgType());
    mainPot = gameState.getPots().get(0);
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("oheins").getBalance());
    assertEquals(new BigDecimal("200.00"), mainPot.getPotAmount());
  }

  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void smallBlindRaise() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
    mainPot = gameState.getPots().get(0);

    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());
    assertEquals(new BigDecimal("110.00"), mainPot.getPotAmount());

    clientMsg = new ClientActionMsg("blevin", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    assertEquals(new BigDecimal("70.00"), gameState.getTable().get("blevin").getBalance());
  }

  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void testFoldAfterRaise() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("mheatzig", "FOLD", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("cdurr", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    mainPot = gameState.getPots().get(0);

    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("60.00"), gameState.getTable().get("oheins").getBalance());
    assertEquals(new BigDecimal("180.00"), mainPot.getPotAmount());
  }

  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void testAllFoldExceptWinner() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "RAISE", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("mheatzig", "FOLD", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("oheins", "FOLD", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("cdurr", "FOLD", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    clientMsg = new ClientActionMsg("pruh", "FOLD", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();
    // AFTERHAND
    gameState = gameStateMsgQueue.take();

    assertEquals(GameStateMsg.GameStateMsgType.AFTERHAND, gameState.getMsgType());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("180.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());
  }

  /**
   * Tests raise and then everyone calling it
   * @throws InterruptedException
   */
  @Test
  public void testShowdown() throws InterruptedException {
    GameStateMsg gameState;
    ClientActionMsg clientMsg;
    Pot mainPot;

    clientMsg = new ClientActionMsg("mheatzig", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CALL", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameStateMsgQueue.take();
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("mheatzig", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CHECK", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();
//
    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("pruh", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("blevin", "CHECK", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("mheatzig", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("oheins", "CHECK", "0.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    clientMsg = new ClientActionMsg("cdurr", "CHECK", "10.00");
    instantGameMsgQueue.put(clientMsg);
    gameState = gameStateMsgQueue.take();

    // COMPLETION State
    gameStateMsgQueue.take();
    // PLAYHAND State
    gameStateMsgQueue.take();
    // AFTERHAND
    gameState = gameStateMsgQueue.take();

    assertEquals(GameStateMsg.GameStateMsgType.AFTERHAND, gameState.getMsgType());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("cdurr").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("pruh").getBalance());
    assertEquals(new BigDecimal("180.00"), gameState.getTable().get("blevin").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("mheatzig").getBalance());
    assertEquals(new BigDecimal("80.00"), gameState.getTable().get("oheins").getBalance());
  }
}
