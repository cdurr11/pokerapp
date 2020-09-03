package edu.sigmachi.poker.handEvaluator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RankHash {

  public static final short[] rank_hash = new short[42077];
  
  public RankHash() throws IOException {
    BufferedReader objReader = new BufferedReader(new FileReader("src/main/java/edu/sigmachi/poker/handEvaluator/RankHash.txt"));
    String strCurrentLine;
    int i = 0;
    while ((strCurrentLine = objReader.readLine()) != null) {
      String[] numbers = strCurrentLine.split(",");
      for (String num : numbers) {
        rank_hash[i] = Short.parseShort(num);
        i++;
      }
    }
  }
}
