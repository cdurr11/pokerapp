package edu.sigmachi.poker.handEvaluator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RankOffsets {
  
  public static final int[] offsets = new int[16384];
  
  public RankOffsets() throws IOException {
    BufferedReader objReader = new BufferedReader(new FileReader("src/main/java/edu/sigmachi/poker/handEvaluator/RankOffsets.txt"));
    String strCurrentLine;
    int i = 0;
    while ((strCurrentLine = objReader.readLine()) != null) {
      String[] numbers = strCurrentLine.split(",");
      for (String num : numbers) {
        offsets[i] = Integer.parseInt(num);
        i++;
      }
    }
  }
}
