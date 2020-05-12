package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
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
import edu.sigmachi.poker.ConsoleMessages.RestartMsg;
import edu.sigmachi.poker.ConsoleMessages.StartMsg;
import edu.sigmachi.poker.ConsoleMessages.SubtractPlayerMoneyMsg;

public class Server {
  
  private final SocketIOServer server;
  private final Game game;
  private final Map<String, String> playerToSessionId = new HashMap<String, String>();
  private final Queue<LoginAttemptMsg> loginMsgQueue = new ConcurrentLinkedQueue<>(); 
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
    
    server = new SocketIOServer(config);
    
    initializeServerConsole(consoleMsgQueue);
    initializeListeners();
    
    this.game = new Game(server, gamePassword, loginMsgQueue, clientMsgQueue);
    
    server.start();
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
  }
  
  private void handlePlayerAuthenticationAttempt(LoginAttemptMsg loginMsg) {
    if (this.game.authenticatePlayer(loginMsg.getPlayerName(), loginMsg.getProvidedPassword())) {
      // Pass messages to the game class here.
      //TODO send login success msg
    }
    else {
      // send authentication failed msg
    }
    
  }
  
  private void emitEndOfRoundMessage() {
//    server.getBroadcastOperations().sendEvent("endOfRound", generateDummyServerRoundMsg());
  }
  
  private void emitStartOfRoundMessage() {
//   server.getBroadcastOperations().sendEvent("startOfRound", generateDummyServerRoundMsg());
  }
  
  private void initializeServerConsole(Queue<ConsoleMsg> consoleMsgQueue) {
    Thread inputThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          try {
            Scanner scanner = new Scanner(System.in);
            String reply = scanner.nextLine();
            if (handleREPLLine(reply, consoleMsgQueue)) {
              System.out.println("REPLY: Request has been successfully logged");
            }
            else {
              System.out.println("REPLY: Invalid Request");
            }
          } catch(Exception e) {
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
  private boolean handleREPLLine(String input, Queue<ConsoleMsg> consoleMsgQueue) {
    input = input.toUpperCase();
    String[] splitInput = input.split(" ");
    final int modifyMoneyInputLength = 3;
    
    ConsoleMsg msg;
    switch (splitInput[0]) {
      case "START":
        msg = new StartMsg();
        break;
      case "RESTART":
        msg = new RestartMsg();
        break;
        
      //TODO clean this up this is ugly
      case "ADD_MONEY":
        if (splitInput.length == modifyMoneyInputLength) {
          try {
            BigDecimal addedMoney = new BigDecimal(splitInput[2]);
            addedMoney.setScale(2);
            msg = new AddPlayerMoneyMsg(splitInput[2], addedMoney);
          }
          catch(Exception NumberFormatException) {
            return false;
          }
        }
        else {return false;}
        break;
      case "SUBTRACT_MONEY":
        if (splitInput.length == modifyMoneyInputLength) {
          try {
            BigDecimal addedMoney = new BigDecimal(splitInput[2]);
            addedMoney.setScale(2);
            msg = new SubtractPlayerMoneyMsg(splitInput[2], addedMoney);
          }
          catch(Exception NumberFormatException) {
            return false;
          }
        }
        else {return false;}
        break;  
        
      default:
        return false;
    }
    consoleMsgQueue.add(msg);
    return true;
  }
  
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
        handlePlayerAuthenticationAttempt(loginMsg);
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
