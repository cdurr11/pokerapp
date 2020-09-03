package edu.sigmachi.poker.handEvaluator;

public class Deckcards {
  final static int[] cards = {
      Constants.ACE + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.ACE + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.ACE + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.ACE + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.KING + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.KING + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.KING + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.KING + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.QUEEN + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.QUEEN + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.QUEEN + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.QUEEN + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.JACK + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.JACK + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.JACK + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.JACK + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.TEN + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.TEN + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.TEN + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.TEN + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.NINE + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.NINE + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.NINE + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.NINE + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.EIGHT + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.EIGHT + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.EIGHT + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.EIGHT + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.SEVEN + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.SEVEN + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.SEVEN + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.SEVEN + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.SIX + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.SIX + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.SIX + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.SIX + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.FIVE + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.FIVE + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.FIVE + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.FIVE + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.FOUR + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.FOUR + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.FOUR + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.FOUR + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.THREE + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.THREE + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.THREE + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.THREE + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT),

      Constants.TWO + (Constants.SPADE << Constants.FLUSH_BIT_SHIFT),
      Constants.TWO + (Constants.HEART << Constants.FLUSH_BIT_SHIFT),
      Constants.TWO + (Constants.DIAMOND << Constants.FLUSH_BIT_SHIFT),
      Constants.TWO + (Constants.CLUB << Constants.FLUSH_BIT_SHIFT)
    };
  
  public static final int[][] suit_kronecker = {
      {
        Constants.ACE_FLUSH,   0, 0, 0,
        Constants.KING_FLUSH,  0, 0, 0,
        Constants.QUEEN_FLUSH, 0, 0, 0,
        Constants.JACK_FLUSH,  0, 0, 0,
        Constants.TEN_FLUSH,   0, 0, 0,
        Constants.NINE_FLUSH,  0, 0, 0,
        Constants.EIGHT_FLUSH, 0, 0, 0,
        Constants.SEVEN_FLUSH, 0, 0, 0,
        Constants.SIX_FLUSH,   0, 0, 0,
        Constants.FIVE_FLUSH,  0, 0, 0,
        Constants.FOUR_FLUSH,  0, 0, 0,
        Constants.THREE_FLUSH, 0, 0, 0,
        Constants.TWO_FLUSH,   0, 0, 0
      },
      {
        0, Constants.ACE_FLUSH,   0, 0,
        0, Constants.KING_FLUSH,  0, 0,
        0, Constants.QUEEN_FLUSH, 0, 0,
        0, Constants.JACK_FLUSH,  0, 0,
        0, Constants.TEN_FLUSH,   0, 0,
        0, Constants.NINE_FLUSH,  0, 0,
        0, Constants.EIGHT_FLUSH, 0, 0,
        0, Constants.SEVEN_FLUSH, 0, 0,
        0, Constants.SIX_FLUSH,   0, 0,
        0, Constants.FIVE_FLUSH,  0, 0,
        0, Constants.FOUR_FLUSH,  0, 0,
        0, Constants.THREE_FLUSH, 0, 0,
        0, Constants.TWO_FLUSH,   0, 0
      },
      {
        0, 0, Constants.ACE_FLUSH,   0,
        0, 0, Constants.KING_FLUSH,  0,
        0, 0, Constants.QUEEN_FLUSH, 0,
        0, 0, Constants.JACK_FLUSH,  0,
        0, 0, Constants.TEN_FLUSH,   0,
        0, 0, Constants.NINE_FLUSH,  0,
        0, 0, Constants.EIGHT_FLUSH, 0,
        0, 0, Constants.SEVEN_FLUSH, 0,
        0, 0, Constants.SIX_FLUSH,   0,
        0, 0, Constants.FIVE_FLUSH,  0,
        0, 0, Constants.FOUR_FLUSH,  0,
        0, 0, Constants.THREE_FLUSH, 0,
        0, 0, Constants.TWO_FLUSH,   0
      },
      {
        0, 0, 0, Constants.ACE_FLUSH,
        0, 0, 0, Constants.KING_FLUSH,
        0, 0, 0, Constants.QUEEN_FLUSH,
        0, 0, 0, Constants.JACK_FLUSH,
        0, 0, 0, Constants.TEN_FLUSH,
        0, 0, 0, Constants.NINE_FLUSH,
        0, 0, 0, Constants.EIGHT_FLUSH,
        0, 0, 0, Constants.SEVEN_FLUSH,
        0, 0, 0, Constants.SIX_FLUSH,
        0, 0, 0, Constants.FIVE_FLUSH,
        0, 0, 0, Constants.FOUR_FLUSH,
        0, 0, 0, Constants.THREE_FLUSH,
        0, 0, 0, Constants.TWO_FLUSH
      }
    };
}
