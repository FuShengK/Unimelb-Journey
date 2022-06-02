package cribbage;

import ch.aplu.jcardgame.Hand;

public class ShowFifteen extends IScoringSystem {

    @Override
    public int getScore() {

        Cribbage.Segment seg = segment.copy();
        int score = 0;
        int K = 15;
        score += find(seg, K);

        return score;
    }

    // print sum sub-Segment if they are the same to K
    public int sums(Cribbage.Segment seg, int numCards, int target) {
        Hand hand = seg.segment;
        int sum = 0;
        int score = 0;
        int[] handCards = new int[hand.getNumberOfCards()];
        int j = hand.getNumberOfCards() - 1;
        final int SUM_POINT = 2;
        final int DEALER = 1;

        // Convert into binary
        while (numCards > 0) {
            handCards[j] = numCards % 2;
            numCards = numCards / 2;
            j--;
        }

        // Calculate the sum
        for (int i = 0; i < hand.getNumberOfCards(); i++)
            if (handCards[i] == DEALER){ sum = sum + ((Cribbage.Rank) hand.get(i).getRank()).value; }

        // Check whether sum is equal to target and print it out
        if (sum == target) {
            // Make a new hand
            Hand fifteen = new Hand(Cribbage.deck);
            for (int i = 0; i < hand.getNumberOfCards(); i++) {
                if (handCards[i] == 1) {
                    fifteen.insert(hand.get(i).getCardNumber(), false);
                }
            }
            score = SUM_POINT;
            player.setScore(player.getScore() + score);
            logger.log(String.format("score,P%d,%d,%d,fifteen,%s", seg.lastPlayer, player.getScore(), score, Cribbage.canonical(fifteen)));
        }
        return score;
    }


    // Function to find sum K
    public int find(Cribbage.Segment seg, int K) {
        int score = 0;
        Hand hand = seg.segment;
        // Calculate the total number of cards
        int x = (int) Math.pow(2, hand.getNumberOfCards());

        for (int i = 1; i < x; i++) {
            score += sums(seg, i, K);
        }
        return score;
    }
}
