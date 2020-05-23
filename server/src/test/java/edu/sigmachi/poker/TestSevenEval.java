package edu.sigmachi.poker;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import edu.sigmachi.poker.handEvaluator.SevenEval;

public class TestSevenEval {
  @Test
  public void testSevenEval() throws NumberFormatException, IOException {
    BufferedReader objReader = new BufferedReader(new FileReader("src/test/java/edu/sigmachi/poker/SKPokerEvalResults.txt"));
    String strCurrentLine;
    int i = 0;
    while ((strCurrentLine = objReader.readLine()) != null) {
      if (i % 1000 == 0) {
        System.out.println(i);
      }
      String[] numbers = strCurrentLine.split(",");
//      System.out.println(strCurrentLine);
      int ans = SevenEval.GetRank(Byte.parseByte(numbers[0]), Byte.parseByte(numbers[1]), Byte.parseByte(numbers[2]),
          Byte.parseByte(numbers[3]), Byte.parseByte(numbers[4]), Byte.parseByte(numbers[5]), Byte.parseByte(numbers[6]));
      
      assertEquals(Integer.parseInt(numbers[7]),ans);
      i++;
    }
  }
}
