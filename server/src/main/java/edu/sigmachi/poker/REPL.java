package edu.sigmachi.poker;

import java.math.BigDecimal;

import edu.sigmachi.poker.Messages.AddPlayerMoneyMsg;
import edu.sigmachi.poker.Messages.ConsoleMsg;
import edu.sigmachi.poker.Messages.EndMsg;
import edu.sigmachi.poker.Messages.PrintGameStateMsg;
import edu.sigmachi.poker.Messages.RestartMsg;
import edu.sigmachi.poker.Messages.StartMsg;
import edu.sigmachi.poker.Messages.SubtractPlayerMoneyMsg;

public class REPL {
  final static int modifyMoneyInputLength = 3;
  
  public static ConsoleMsg parseConsoleMsg(String input) {
    String[] splitInput = input.split(" ");
    
    switch (splitInput[0].toUpperCase()) {
      case "START":
        if (splitInput.length == 1) {
          return new StartMsg();
        }
        throw new IllegalArgumentException("Invalid Number of Elements");
      case "RESTART":
        if (splitInput.length == 1) {
          return new RestartMsg();
        }
        throw new IllegalArgumentException("Invalid Number of Elements");
      case "ADD_MONEY":
        return handleChangeMoney(splitInput, false);
      case "SUBTRACT_MONEY":
        return handleChangeMoney(splitInput, true);
      case "END":
        if (splitInput.length == 1) {
          return new EndMsg();
        }
        throw new IllegalArgumentException("Invalid Number of Elements");
      case "GAME_STATE":
        if (splitInput.length == 1) {
          return new PrintGameStateMsg();
        }
      default:
        throw new IllegalArgumentException("Command Does Not Exist");
    }
  }
  
  private static ConsoleMsg handleChangeMoney(String[] splitInput, boolean subtractMoney) {
    if (splitInput.length == modifyMoneyInputLength) {
      try {
        BigDecimal addedMoney = new BigDecimal(splitInput[2]);
        addedMoney = addedMoney.setScale(2);
        if (subtractMoney) {
          addedMoney = addedMoney.negate();
        }
        return new AddPlayerMoneyMsg(splitInput[1], addedMoney);
      }
      catch(Exception NumberFormatException) {
        throw new IllegalArgumentException("Dollar Format is Incorrect");
      }
    }
    else {
      throw new IllegalArgumentException("Invalid Argument Length");
    }
  }
}
