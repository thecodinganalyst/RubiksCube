package solutioning.strategy.scoring;

import org.javatuples.Pair;
import solutioning.strategy.Action;
import solutioning.strategy.ExecutionSummary;
import solutioning.strategy.Subject;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class ForesightScoringStrategy<S> extends ScoringStrategy<S> {

    private int foresightCount;
    private int maxForesightCount;
    private int skipLastScoreCount;
    private final ForesightScoringActions<S> foresightScoringActions;

    public static final int DEFAULT_FORESIGHT_COUNT = 5;
    public static final int DEFAULT_MAX_FORESIGHT_COUNT = 8;
    public static final int DEFAULT_SKIP_LAST_SCORE_COUNT = 6;

    public ForesightScoringStrategy(int limit, ScoringMechanism<S> scoringMechanism){
        super(limit, scoringMechanism);
        this.foresightCount = DEFAULT_FORESIGHT_COUNT;
        this.maxForesightCount = DEFAULT_MAX_FORESIGHT_COUNT;
        this.skipLastScoreCount = DEFAULT_SKIP_LAST_SCORE_COUNT;
        this.foresightScoringActions = new ForesightScoringActions<>(scoringMechanism);
    }

    public ForesightScoringStrategy(int limit, int foresightCount, int maxForesightCount, int skipLastScoreCount, ScoringMechanism<S> scoringMechanism) {
        super(limit, scoringMechanism);
        this.foresightCount = foresightCount;
        this.maxForesightCount = maxForesightCount;
        this.skipLastScoreCount = skipLastScoreCount;
        this.foresightScoringActions = new ForesightScoringActions<>(scoringMechanism);
    }

    public Pair<Action<S>, Double> getActionScoreWithHighestScore(ScoreResult<S> scoreResult) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        List<Double> scoresToSkip = foresightScoringActions.getLastFewScoresToSkip(scoreResult, skipLastScoreCount);
        System.out.println("Last few scores to skip: " + scoresToSkip.toString());
        ScoringTask<S> scoringTask = new ScoringTask<>(scoreResult.empty(), foresightCount, maxForesightCount, scoreResult.getScore(), scoresToSkip, foresightScoringActions);
        ScoreResult<S> result = commonPool.invoke(scoringTask);

        return result.getActionScoreList().get(0);
    }

    @Override
    public ExecutionSummary<S> performBestScoreTrial(Subject<S> subject){
        Instant start = Instant.now();
        boolean found = false;
        ScoreResult<S> scoreResult = new ScoreResult<>(scoringMechanism.getScore(subject), subject);

        System.out.println("Perform best score trial");
        for(int i = 0; i < limit; i++){
            System.out.println("level " + i);
            System.out.println("Last Score: " + scoreResult.getScore());
            Pair<Action<S>, Double> bestActionScore = getActionScoreWithHighestScore(scoreResult);
            subject.performAction(bestActionScore.getValue0());
            scoreResult.addActionScore(bestActionScore);

            System.out.println(bestActionScore.getValue0().getName() + " - " + bestActionScore.getValue1());
            subject.print();
            System.out.println(scoringMechanism.getScore(subject) + " - " + scoreResult.getScore());
            System.out.println("---------------------------------------------------------");

            if(subject.isComplete()){
                found = true;
                break;
            }
        }
        Instant end = Instant.now();
        return new ExecutionSummary<>(found, found ? scoreResult.getActionList() : null, start, end);
    }

    public int getForesightCount() {
        return foresightCount;
    }

    public void setForesightCount(int foresightCount) {
        this.foresightCount = foresightCount;
    }

    public int getMaxForesightCount() {
        return maxForesightCount;
    }

    public void setMaxForesightCount(int maxForesightCount) {
        this.maxForesightCount = maxForesightCount;
    }

    public int getSkipLastScoreCount() {
        return skipLastScoreCount;
    }

    public void setSkipLastScoreCount(int skipLastScoreCount) {
        this.skipLastScoreCount = skipLastScoreCount;
    }

    public int incrementForesightCount(){
        int incremented = getForesightCount() + 1;
        setForesightCount(incremented);
        return incremented;
    }

    public int incrementMaxForesightCount(){
        int incremented = getMaxForesightCount() + 1;
        setMaxForesightCount(incremented);
        return incremented;
    }

    public static class ScoringTask<S> extends RecursiveTask<ScoreResult<S>> {

        ScoreResult<S> scoreResult;
        int foresightCount;
        int maxForesightCount;
        List<Double> lastFewScoresToSkip;
        ForesightScoringActions<S> foresightScoringActions;
        Double lastScore;

        public ScoringTask(ScoreResult<S> scoreResult, int foresightCount, int maxForesightCount, Double lastScore, List<Double> lastFewScoresToSkip, ForesightScoringActions<S> foresightScoringActions){
            this.scoreResult = scoreResult;
            this.foresightCount = foresightCount;
            this.maxForesightCount = maxForesightCount;
            this.lastScore = lastScore;
            this.lastFewScoresToSkip = lastFewScoresToSkip;
            this.foresightScoringActions = foresightScoringActions;
        }

        @Override
        protected ScoreResult<S> compute() {
            List<ScoreResult<S>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(scoreResult);
            if(scoreResultList.size() == 1) return scoreResultList.get(0);
            scoreResultList = foresightScoringActions.filterScoreResultListByRemovingItemsWithCertainScores(scoreResultList, lastFewScoresToSkip);
            if (foresightScoringActions.processEnough(scoreResultList, foresightCount, maxForesightCount, lastScore)) return scoreResultList.get(0);

            return ForkJoinTask.invokeAll(createSubTasks(scoreResultList))
                    .stream()
                    .map(ForkJoinTask::join)
                    .max(Comparator.comparingDouble(ScoreResult::getScore))
                    .orElseThrow();
        }

        private List<ScoringTask<S>> createSubTasks(List<ScoreResult<S>> scoreResultList){
            return scoreResultList.stream()
                    .map(result -> new ScoringTask<>(result, foresightCount, maxForesightCount, lastScore, lastFewScoresToSkip, foresightScoringActions))
                    .collect(Collectors.toList());
        }

    }

}
