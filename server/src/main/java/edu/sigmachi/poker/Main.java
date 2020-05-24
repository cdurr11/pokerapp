package edu.sigmachi.poker;

import java.io.IOException;

import edu.sigmachi.poker.handEvaluator.RankOffsets;

public class Main {
  
	public static void main(String[] args) {
	  //TODO eventually this will be passed in as a an argument
//		Server s = new Server("asdf");
	    
      try {
        RankOffsets rh = new RankOffsets();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
	}
}
