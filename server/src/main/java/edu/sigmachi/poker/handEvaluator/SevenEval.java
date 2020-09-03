package edu.sigmachi.poker.handEvaluator;

import java.io.IOException;

public class SevenEval {
  
  public static int GetRank(byte i, byte j, byte k, byte m, byte n, byte p, byte q) throws IOException {
    RankHash rh = new RankHash();
    RankOffsets ro = new RankOffsets();
    
    int key = Deckcards.cards[i] + Deckcards.cards[j] + Deckcards.cards[k] + Deckcards.cards[m] + 
        Deckcards.cards[n] + Deckcards.cards[p] + Deckcards.cards[q];
    
    byte suit = FlushCheck.flush_check[key >>> Constants.FLUSH_BIT_SHIFT];
    if (Constants.NOT_A_SUIT != suit) {
      int[] s = Deckcards.suit_kronecker[suit];
      return FlushRanks.flush_ranks[s[i] | s[j] | s[k] | s[m] | s[n] | s[p] | s[q]];
    }
    int hash = Constants.FACE_BIT_MASK & (int)(31 * (long) key);
    return RankHash.rank_hash[RankOffsets.offsets[hash >>> Constants.RANK_OFFSET_SHIFT] + (hash & Constants.RANK_HASH_MOD)];
  }
}
