package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cribbage extends CardGame {
    static public final int nPlayers = 2;

    public static final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
    static private final IPlayer[] players = new IPlayer[nPlayers];
    static Cribbage cribbage;  // Provide access to singleton

    static Logger logger = Logger.getInstance();
    static Random random;
    static boolean ANIMATE;
    static int SEED;
    public final int nStartCards = 6;
    public final int nDiscards = 2;
    final Font normalFont = new Font("Serif", Font.BOLD, 24);
    final Font bigFont = new Font("Serif", Font.BOLD, 36);
    private final String version = "0.1";
    private final int handWidth = 400;
    private final int cribWidth = 150;
    private final int segmentWidth = 180;
    private final Location[] handLocations = {
            new Location(360, 75), new Location(360, 625)
    };
    private final Location[] scoreLocations = {
            new Location(590, 25), new Location(590, 675)
    };
    private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
            new Location(150, 350), new Location(400, 350), new Location(650, 350)
    };
    private final Location starterLocation = new Location(50, 625);
    private final Location cribLocation = new Location(700, 625);
    private final Location seedLocation = new Location(5, 25);
    // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
    private final Actor[] scoreActors = {null, null}; //, null, null };
    private final Location textLocation = new Location(350, 450);
    private final Hand[] hands = new Hand[nPlayers];

    private final Hand[] anotherSameHand = new Hand[nPlayers];
    private final int[] scores = new int[nPlayers];
    ScoringSystemFactory scoringSystemFactory = ScoringSystemFactory.getInstance();
    private Hand starter;
    private Hand crib;
    public Cribbage() {
        super(850, 700, 30);
        cribbage = this;
        setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScore();

        Hand pack = deck.toHand(false);
        RowLayout layout = new RowLayout(starterLocation, 0);
        layout.setRotationAngle(0);
        pack.setView(this, layout);
        pack.setVerso(true);
        pack.draw();
        addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

        /* Play the round */
        deal(pack, hands);
        discardToCrib();
        starter(pack);
        play();
        showHandsCrib();

        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText("Game over.");
        refresh();
    }

    static int cardValue(Card c) {
        return ((Cribbage.Rank) c.getRank()).value;
    }

    /*
    Canonical String representations of Suit, Rank, Card, and Hand
    */

    static String canonical(Suit s) {
        return s.toString().substring(0, 1);
    }
    static String canonical(Rank r) {
        switch (r) {
            case ACE:
            case KING:
            case QUEEN:
            case JACK:
            case TEN:
                return r.toString().substring(0, 1);
            default:
                return String.valueOf(r.value);
        }
    }

    static String canonical(Card c) {
        return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit());
    }

    static String canonical(Hand h) {
        Hand h1 = new Hand(Cribbage.deck); // Clone to sort without changing the original hand
        for (Card C : h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
        h1.sort(Hand.SortType.POINTPRIORITY, false);
        return "[" + h1.getCardList().stream().map(x -> Cribbage.canonical(x)).collect(Collectors.joining(",")) + "]";
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public static Card randomCard(Hand hand) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    public static void setStatus(String string) {
        cribbage.setStatusText(string);
    }

    static int total(Hand hand) {
        int total = 0;
        for (Card c : hand.getCardList()) total += cardValue(c);
        return total;
    }

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        /* Handle Properties */
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Properties cribbageProperties = new Properties();
        // Default properties
        cribbageProperties.setProperty("Animate", "true");
        cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
        cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

        // Read properties
        try (FileReader inStream = new FileReader("cribbage.properties")) {
            cribbageProperties.load(inStream);
        }

        // Control Graphics
        ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

        // Control Randomisation
        /* Read the first argument and save it as a seed if it exists */
        if (args.length > 0) { // Use arg seed - overrides property
            SEED = Integer.parseInt(args[0]);
        } else { // No arg
            String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
            if (seedProp != null) { // Use property seed
                SEED = Integer.parseInt(seedProp);
            } else { // and no property
                SEED = new Random().nextInt(); // so randomise
            }
        }
        random = new Random(SEED);

        // Control Player Types
        Class<?> clazz;
        clazz = Class.forName(cribbageProperties.getProperty("Player0"));
        players[0] = (IPlayer) clazz.getConstructor().newInstance();
        clazz = Class.forName(cribbageProperties.getProperty("Player1"));
        players[1] = (IPlayer) clazz.getConstructor().newInstance();
        // End properties

        logger.log("seed," + SEED);
        logger.log(cribbageProperties.getProperty("Player0") + ",P0");
        logger.log(cribbageProperties.getProperty("Player1") + ",P1");

        new Cribbage();
    }

    void transfer(Card c, Hand h) {
        if (ANIMATE) {
            c.transfer(h, true);
        } else {
            c.removeFromHand(true);
            h.insert(c, true);
        }
    }

    private void dealingOut(Hand pack, Hand[] hands) {
        for (int i = 0; i < nStartCards; i++) {
            for (int j = 0; j < nPlayers; j++) {
                Card dealt = randomCard(pack);
                dealt.setVerso(false);  // Show the face
                transfer(dealt, hands[j]);
            }
        }
    }

    private void initScore() {
        for (int i = 0; i < nPlayers; i++) {
            scores[i] = 0;
            scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        scoreActors[player] = new TextActor(String.valueOf(players[player].score), Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void deal(Hand pack, Hand[] hands) {
        for (int i = 0; i < nPlayers; i++) {
            hands[i] = new Hand(deck);
            // Make a Copy of players' cards
            anotherSameHand[i] = new Hand(deck);
            // players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
            players[i].setId(i);
            players[i].startSegment(deck, hands[i]);
            players[i].setScore(0);
        }
        RowLayout[] layouts = new RowLayout[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(0);
            // layouts[i].setStepDelay(10);
            hands[i].setView(this, layouts[i]);
            hands[i].draw();
        }
        layouts[0].setStepDelay(0);

        dealingOut(pack, hands);
        for (int i = 0; i < nPlayers; i++) {
            hands[i].sort(Hand.SortType.POINTPRIORITY, true);
        }
        layouts[0].setStepDelay(0);

        // Log our the deals
        for (int i = 0; i < nPlayers; i++) {
            logger.log(String.format("deal,P%d,%s", i, canonical(hands[i])));
        }
    }

    private void discardToCrib() {
        crib = new Hand(deck);
        RowLayout layout = new RowLayout(cribLocation, cribWidth);
        layout.setRotationAngle(0);
        crib.setView(this, layout);
        // crib.setTargetArea(cribTarget);
        crib.draw();

        // store the discarded hand
        for (int player = 0; player < nPlayers; player++) {
            Hand discarded = new Hand(deck);
            for (int i = 0; i < nDiscards; i++) {
                Card discard = players[player].discard();
                transfer(discard, crib);
                discarded.insert(discard, false);
            }
            // log out the discard card
            logger.log(String.format("discard,P%d,%s", player, canonical(discarded)));
            crib.sort(Hand.SortType.POINTPRIORITY, true);

            // Make a copy of our hand cards
            for (int n = 0; n < 4; n++) {
                anotherSameHand[player].insert(hands[player].get(n).getCardNumber(), false);
            }
        }
    }

    private void starter(Hand pack) {
        final int DEALER = 1;
        starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
        RowLayout layout = new RowLayout(starterLocation, 0);
        layout.setRotationAngle(0);
        starter.setView(this, layout);
        starter.draw();
        Card dealt = randomCard(pack);
        dealt.setVerso(false);
        transfer(dealt, starter);

        // Log out the starter card
        logger.log(String.format("starter,%s", canonical(dealt)));
        ScoringStarter checkStarter = new ScoringStarter();
        checkStarter.getScore(dealt, players[DEALER]);
        updateScore(DEALER);
    }

    private void play() {
        final int THIRTY_ONE = 31;
        final int PLAYER = 0;
        final int DEALER = 1;
        final int TWO_PLAYERS = 2;
        List<Hand> segments = new ArrayList<>();
        int currentPlayer = 0; // Always starts from player 0
        Segment seg = new Segment();
        seg.reset(segments);
        while (!(players[PLAYER].emptyHand() && players[DEALER].emptyHand())) {
            // System.out.println("segments.size() = " + segments.size());
            Card nextCard = players[currentPlayer].lay(THIRTY_ONE - total(seg.segment));
            if (nextCard == null) {
                if (seg.go) {
                    // Last player gets 1 from one more go after with no intervening cards
                    seg.newSegment = true;
                } else {
                    // Player says "go"
                    seg.go = true;
                }
                currentPlayer = (currentPlayer + 1) % TWO_PLAYERS;
            } else {
                seg.lastPlayer = currentPlayer; // last Player to play a card in this segment
                transfer(nextCard, seg.segment);
                // log display
                logger.log(String.format("play,P%d,%d,%s", currentPlayer, total((seg.segment)), canonical(nextCard)));
                if (total(seg.segment) == THIRTY_ONE) {
                    // if total(segment) == 31 ,ast player gets 2 for a 31
                    seg.newSegment = true;
                    currentPlayer = (currentPlayer + 1) % TWO_PLAYERS;
                } else {
                    // if total(segment) == 15, last Player gets 2
                    if (!seg.go) {
                        currentPlayer = (currentPlayer + 1) % TWO_PLAYERS;
                    }
                }
                // Last play get point while players have no cards
                if ((players[PLAYER].emptyHand() && players[DEALER].emptyHand())) {
                    seg.go = true;
                }
            }

            // Score our card, using the segment and the discarded cards
            IScoringSystem playing = scoringSystemFactory.create("play");
            ScoringContext scoringContextNew = new ScoringContext(playing);
            scoringContextNew.setSegment(seg);
            scoringContextNew.setPlayer(players[seg.lastPlayer]);
            scoringContextNew.getScore();

            // displaying the updating data
            updateScore(seg.lastPlayer);

            if (seg.newSegment) {
                segments.add(seg.segment);
                seg.reset(segments);
            }
        }
    }

    void showHandsCrib() {
        ScoringSystemFactory scoringSystemFactory = ScoringSystemFactory.getInstance();
        final int PLAYER = 0;
        final int DEALER = 1;
        IScoringSystem showScoring = scoringSystemFactory.create("show");
        ScoringContext scoringContextNew = new ScoringContext(showScoring);

        // score player 0 (non dealer)
        logger.log(String.format("show,P0,%s+%s", canonical(starter.getLast()), canonical(anotherSameHand[0])));
        Segment s = new Segment();
        anotherSameHand[PLAYER].insert(starter, false);
        s.segment = anotherSameHand[PLAYER];
        s.lastPlayer = PLAYER;
        scoringContextNew.setSegment(s);
        scoringContextNew.setPlayer(players[PLAYER]);
        scoringContextNew.getScore();

        // score player 1 (dealer)
        logger.log(String.format("show,P1,%s+%s", canonical(starter.getLast()), canonical(anotherSameHand[1])));
        s = new Segment();
        anotherSameHand[DEALER].insert(starter, false);
        s.segment = anotherSameHand[DEALER];
        s.lastPlayer = DEALER;
        scoringContextNew.setSegment(s);
        scoringContextNew.setPlayer(players[DEALER]);
        scoringContextNew.getScore();

        // score crib (for dealer)
        logger.log(String.format("show,P1,%s+%s", canonical(starter.getLast()), canonical(crib)));
        s = new Segment();
        crib.insert(starter, false);
        s.segment = crib;
        s.lastPlayer = DEALER;
        scoringContextNew.setSegment(s);
        scoringContextNew.setPlayer(players[DEALER]);
        scoringContextNew.getScore();

        updateScore(PLAYER); updateScore(DEALER);
    }

    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum Rank {
        // Order of cards is tied to card images
        ACE(1, 1), KING(13, 10), QUEEN(12, 10), JACK(11, 10), TEN(10, 10), NINE(9, 9), EIGHT(8, 8), SEVEN(7, 7), SIX(6, 6), FIVE(5, 5), FOUR(4, 4), THREE(3, 3), TWO(2, 2);
        public final int order;
        public final int value;

        Rank(int order, int value) { this.order = order; this.value = value; }
    }

    static class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
        public int[] values(Enum suit) {  // Returns the value for each card in the suit
            return Stream.of(Rank.values()).mapToInt(r -> (r.order - 1) * (Suit.values().length) + suit.ordinal()).toArray();
        }
    }

    class Segment {
        Hand segment;
        boolean go;
        int lastPlayer;
        boolean newSegment;

        void reset(final List<Hand> segments) {
            segment = new Hand(deck);
            segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
            segment.draw();
            go = false;        // No-one has said "go" yet
            lastPlayer = -1;   // No-one has played a card yet in this segment
            newSegment = false;  // Not ready for new segment yet
        }

        // Create new method to handle our card, avoid change original data
        Segment copy() {
            Segment the_copy = new Segment();
            Hand seg_clone = new Hand(deck);
            List<Card> card_copy = new ArrayList<>(segment.getCardList());
            for (Card c : card_copy) {
                seg_clone.insert(c.getCardNumber(), false);
            }
            the_copy.newSegment = newSegment;
            the_copy.lastPlayer = lastPlayer;
            the_copy.segment = seg_clone;
            the_copy.go = go;
            return the_copy;
        }
    }
}
