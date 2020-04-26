package edu.sigmachi.poker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class Server {
  
  private final SocketIOServer server;
  
  public Server() {
    Configuration config = new Configuration();
    config.setHostname("localhost");
    config.setPort(9092);
    
    server = new SocketIOServer(config);
    
    initializeListeners();
    
    server.start();
    System.out.println("Server Started!");
  }
  
  private void handlePlayerAction(ClientActionMsg clientAction) {
    server.getBroadcastOperations().sendEvent("actionResponse", generateDummyServerRoundMsg());
  }
  
  private void handlePlayerConnection(SocketIOClient socketIOClient) {
    System.out.println(socketIOClient.getSessionId() + " had just connected!");
  }
  
  private void handlePlayerDisconnection(SocketIOClient socketIOClient) {
    System.out.println(socketIOClient.getSessionId() + " had just disconnected!");
  }
  
  private void emitEndOfRoundMessage() {
    
  }
  
  private void emitStartOfRoundMessage() {
    
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
