package cribbage;

public class ScoringSystemFactory {
    // Singleton
    private static final ScoringSystemFactory SCORING_CREATING = new ScoringSystemFactory();

    IScoringSystem scoring = null;
    private ScoringSystemFactory() { }
    public static ScoringSystemFactory getInstance() {
        return SCORING_CREATING;
    }

    public IScoringSystem create(String type) {
        switch (type) {
            case "play":
                ScoringComposite scoringComposite = new PlayComposite();
                scoringComposite.add(new PlaySames());
                scoringComposite.add(new PlayRuns());
                scoringComposite.add(new PlayCardSum());
                scoring = scoringComposite;
                break;
            case "show":
                scoringComposite = new ShowComposite();
                scoringComposite.add(new ShowJack());
                scoringComposite.add(new ShowSames());
                scoringComposite.add(new ShowRuns());
                scoringComposite.add(new ShowFifteen());
                scoringComposite.add(new ShowFlush());
                scoring = scoringComposite;
                break;
        }
        return scoring;
    }
}
