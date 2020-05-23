package edu.sigmachi.poker.handEvaluator;

public class Constants {

public static final int DECK_SIZE = 52;
public static final int NUMBER_OF_SUITS = 4;
public static final int NUMBER_OF_FACES = 13;

public static final int SPADE = 0;
public static final int HEART = 1;
public static final int DIAMOND = 8;
public static final int CLUB = 57;

public static final int INDEX_SPADE = 0;
public static final int INDEX_HEART = 1;
public static final int INDEX_DIAMOND = 2;
public static final int INDEX_CLUB = 3;
public static final int NOT_A_SUIT = -1;

public static final int TWO_FIVE = 0;
public static final int THREE_FIVE = 1;
public static final int FOUR_FIVE = 5;
public static final int FIVE_FIVE = 22;
public static final int SIX_FIVE = 94;
public static final int SEVEN_FIVE = 312;
public static final int EIGHT_FIVE = 992;
public static final int NINE_FIVE = 2422;
public static final int TEN_FIVE = 5624;
public static final int JACK_FIVE = 12522;
public static final int QUEEN_FIVE = 19998;
public static final int KING_FIVE = 43258;
public static final int ACE_FIVE = 79415;

public static final int TWO_FLUSH = 1;
public static final int THREE_FLUSH = TWO_FLUSH << 1;
public static final int FOUR_FLUSH = THREE_FLUSH << 1;
public static final int FIVE_FLUSH = FOUR_FLUSH << 1;
public static final int SIX_FLUSH = FIVE_FLUSH << 1;
public static final int SEVEN_FLUSH = SIX_FLUSH << 1;
public static final int EIGHT_FLUSH = SEVEN_FLUSH << 1;
public static final int NINE_FLUSH = EIGHT_FLUSH << 1;
public static final int TEN_FLUSH = NINE_FLUSH << 1;
public static final int JACK_FLUSH = TEN_FLUSH << 1;
public static final int QUEEN_FLUSH = JACK_FLUSH << 1;
public static final int KING_FLUSH = QUEEN_FLUSH << 1;
public static final int ACE_FLUSH = KING_FLUSH << 1;

public static final int TWO = 0;
public static final int THREE = 1;
public static final int FOUR = 5;
public static final int FIVE = 22;
public static final int SIX = 98;
public static final int SEVEN = 453;
public static final int EIGHT = 2031;
public static final int NINE = 8698;
public static final int TEN = 22854;
public static final int JACK = 83661;
public static final int QUEEN = 262349;
public static final int KING = 636345;
public static final int ACE = 1479181;

public static final int MAX_FIVE_NONFLUSH_KEY_INT = ((4 * ACE_FIVE) + KING_FIVE);
public static final int MAX_FIVE_FLUSH_KEY_INT = (ACE_FLUSH | KING_FLUSH | QUEEN_FLUSH | JACK_FLUSH | TEN_FLUSH);
public static final int MAX_SEVEN_FLUSH_KEY_INT = (ACE_FLUSH | KING_FLUSH | QUEEN_FLUSH | JACK_FLUSH | TEN_FLUSH | NINE_FLUSH | EIGHT_FLUSH);

public static final int RANK_OFFSET_SHIFT = 9;
public static final int RANK_HASH_MOD = ((1 << RANK_OFFSET_SHIFT) - 1);

public static final int MAX_FLUSH_CHECK_SUM = (7 * CLUB);

//Bit masks
public static final long FLUSH_BIT_SHIFT = 23;
  public static final int FACE_BIT_MASK = ((1 << FLUSH_BIT_SHIFT) - 1);
}
