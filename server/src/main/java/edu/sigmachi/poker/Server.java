package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import edu.sigmachi.poker.ConsoleMessages.AddPlayerMoneyMsg;
import edu.sigmachi.poker.ConsoleMessages.ConsoleMsg;
import edu.sigmachi.poker.ConsoleMessages.EndMsg;
import edu.sigmachi.poker.ConsoleMessages.PrintGameStateMsg;
import edu.sigmachi.poker.ConsoleMessages.RestartMsg;
import edu.sigmachi.poker.ConsoleMessages.StartMsg;
import edu.sigmachi.poker.ConsoleMessages.SubtractPlayerMoneyMsg;

public class Server {
  
  private final SocketIOServer server;
  private final String gamePassword;
  private final Map<String, String> playerToSessionId = new HashMap<String, String>();
  private final Queue<DisconnectConnectMsg> connectionMsgQueue = new ConcurrentLinkedQueue<>(); 
  private final Queue<ClientActionMsg> clientMsgQueue = new ConcurrentLinkedQueue<>();
  private final Queue<ConsoleMsg> consoleMsgQueue = new ConcurrentLinkedQueue<>();
  
  /*Thread Safety:
   *  SocketIOServer is documented as fully threadsafe
   *  loginMsgQueue is shared between server and game thread but is a threadsafe queue
   *  clientMsgQueue is shared between server and game thread but is a threadsafe queue
   *  consoleMsgQueue is shared between server, game and repl thread but it is a threadsafe queue
   */
  
  public Server(String gamePassword) {  
    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(9092);
    
    this.gamePassword = gamePassword;
    this.server = new SocketIOServer(config);
    initializeGame();
    initializeServerConsole(consoleMsgQueue);
    initializeListeners();
    
    this.server.start();
    System.out.println("Server Started!");
  }
  
  private void handlePlayerAction(ClientActionMsg clientActionMsg) {
    server.getBroadcastOperations().sendEvent("actionResponse", generateDummyServerRoundMsg());
  }
  
  private void handlePlayerConnection(SocketIOClient socketIOClient) {
    System.out.println(socketIOClient.getSessionId() + " had just connected!");
  }
  
  private void handlePlayerDisconnection(SocketIOClient socketIOClient) {
    System.out.println(socketIOClient.getSessionId() + " had just disconnected!");
    connectionMsgQueue.add(new DisconnectionMsg(socketIOClient.getSessionId()));
  }
  
  private LoginAttemptResponseMsg handlePlayerAuthenticationAttempt(LoginAttemptMsg loginMsg, 
      UUID sessionID) {
    if (loginMsg.getProvidedPassword().equals(gamePassword)) {
      connectionMsgQueue.add(new ConnectionMsg(sessionID, loginMsg.getPlayerName()));
      return new LoginAttemptResponseMsg(true, "Login Successful, you will enter the game at "
          + "the start of the next round");
    }
    else {
      return new LoginAttemptResponseMsg(false, "Incorrect Password");
    }    
  }
  
  private void initializeGame() {
    Thread gameThread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Game game = new Game(server, gamePassword, connectionMsgQueue, clientMsgQueue, consoleMsgQueue);
          game.start();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    });
    gameThread.start();
  }
  
  private void initializeServerConsole(Queue<ConsoleMsg> consoleMsgQueue) {
    Thread inputThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          try {
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            ConsoleMsg consoleResponse = REPL.parseConsoleMsg(input);
            consoleMsgQueue.add(consoleResponse);
            System.out.println("REPLY: Request has been successfully logged");
          } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
          } catch(NoSuchElementException e) {
            //Exception from the scanner
            e.printStackTrace();
          } catch(IllegalStateException e) {
            //Exception from the scanner
            e.printStackTrace();
          }
          
        }
      }
    });
    inputThread.start();
  }
  
  /*
   * Return true if message is successful and has been placed on the queue to be
   * processed, false otherwise
   */
  
  private void initializeListeners() {
    //This callback is triggered when we fire the websocket on the client side
    //This is the only event that we should need a callback for.
    server.addEventListener("playerAction", ClientActionMsg.class, new DataListener<ClientActionMsg>() {
      @Override
      public void onData(SocketIOClient client, ClientActionMsg clientAction, AckRequest ackRequest) {
        //broadcast response to clients, We'll do the same thing for the start of the round and 
        //end of the round and timer messages
        handlePlayerAction(clientAction);
      }
    });
    
    server.addEventListener("loginAttempt", LoginAttemptMsg.class, new DataListener<LoginAttemptMsg>() {
      @Override
      public void onData(SocketIOClient client, LoginAttemptMsg loginMsg, AckRequest ackRequest) {
        LoginAttemptResponseMsg response = handlePlayerAuthenticationAttempt(loginMsg, 
            client.getSessionId());
        client.sendEvent("loginResponse", response);
      }
    });
    
    //This fires whenever a client connects to the server, which currently is just when they open the web app
    server.addConnectListener(new ConnectListener() {
      public void onConnect(SocketIOClient socketIOClient) {
        handlePlayerConnection(socketIOClient);
      }
    });
    
    server.addDisconnectListener(new DisconnectListener() {
      public void onDisconnect(SocketIOClient socketIOClient) {
        handlePlayerDisconnection(socketIOClient);
      }
    });
  }
  
  public void close() {
    server.stop();
  }
  public synchronized Queue<ConsoleMsg> getConsoleMsgQueue() {
    return new LinkedList<>(consoleMsgQueue);
  }
  
  public synchronized Queue<DisconnectConnectMsg> getConnectionMsgQueue() {
    return new LinkedList<>(connectionMsgQueue);
  }
  
  public synchronized Queue<ClientActionMsg> getClientMsgQueue() {
    return new LinkedList<>(clientMsgQueue);
  }
  
  
  //Just an example of how you should make these messages, obviously a lot cleaner tho
  private static ServerActionResponseMsg generateDummyServerRoundMsg() {
    Map<String, BigDecimal> playerBalance = new HashMap<>();
    playerBalance.put("cdurr", new BigDecimal("10.00"));
    playerBalance.put("mheatzig", new BigDecimal("9.99"));
    
    Map<String, BigDecimal> currentBet= new HashMap<>();
    currentBet.put("pruh", new BigDecimal("12.00"));
    currentBet.put("mheatzig", new BigDecimal("13.00"));
    
    List<String> communityCards = new ArrayList<>();
    communityCards.add("KH");
    communityCards.add("4C");
    communityCards.add("5D");
    
    List<String> mainPotContenders = new ArrayList<>(Arrays.asList("cdurr","pruh", "mheatzig"));
    List<String> sidePotContenders = new ArrayList<>(Arrays.asList("rdelaus","blevin", "eheatzig"));
    
    BigDecimal mainPotValue = new BigDecimal("1200.01");
    BigDecimal sidePotValue = new BigDecimal("1000.00");
    
    ServerActionResponseMsg response = new ServerActionResponseMsg(playerBalance, currentBet, "cdurr", communityCards, mainPotContenders, mainPotValue, sidePotContenders, sidePotValue);
    return response;
  }
}
