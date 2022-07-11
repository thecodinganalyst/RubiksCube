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
    private int bestScoreCount;
    private int skipLastScoreCount;
    private Double thresholdScoreToIncreaseForesightCount;
    private final ForesightScoringActions<S> foresightScoringActions;

    public static final int DEFAULT_FORESIGHT_COUNT = 4;
    public static final int DEFAULT_MAX_FORESIGHT_COUNT = 8;
    public static final int DEFAULT_BEST_SCORE_COUNT = 6;
    public static final int DEFAULT_SKIP_LAST_SCORE_COUNT = 6;
    public static final double DEFAULT_THRESHOLD_SCORE_TO_INCREASE_FORESIGHT_COUNT = 0.5;

    public ForesightScoringStrategy(int limit, ScoringMechanism<S> scoringMechanism){
        super(limit, scoringMechanism);
        this.foresightCount = DEFAULT_FORESIGHT_COUNT;
        this.maxForesightCount = DEFAULT_MAX_FORESIGHT_COUNT;
        this.bestScoreCount = DEFAULT_BEST_SCORE_COUNT;
        this.skipLastScoreCount = DEFAULT_SKIP_LAST_SCORE_COUNT;
        this.thresholdScoreToIncreaseForesightCount = DEFAULT_THRESHOLD_SCORE_TO_INCREASE_FORESIGHT_COUNT;
        this.foresightScoringActions = new ForesightScoringActions<>(scoringMechanism);
    }

    public ForesightScoringStrategy(int limit, int foresightCount, int maxForesightCount, Double thresholdScoreToIncreaseForesightCount, int bestScoreCount, int skipLastScoreCount, ScoringMechanism<S> scoringMechanism) {
        super(limit, scoringMechanism);
        this.foresightCount = foresightCount;
        this.maxForesightCount = maxForesightCount;
        this.thresholdScoreToIncreaseForesightCount = thresholdScoreToIncreaseForesightCount;
        this.bestScoreCount = bestScoreCount;
        this.skipLastScoreCount = skipLastScoreCount;
        this.foresightScoringActions = new ForesightScoringActions<>(scoringMechanism);
    }

    public Pair<Action<S>, Double> getActionScoreWithHighestScore(ScoreResult<S> scoreResult) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        List<Double> scoresToSkip = foresightScoringActions.getLastFewScoresToSkip(scoreResult, skipLastScoreCount);
        System.out.println("Last few scores to skip: " + scoresToSkip.toString());
        ScoringTask<S> scoringTask = new ScoringTask<>(ScoreResult.empty(scoreResult.getSubject(), scoringMechanism.getScore(scoreResult.getSubject())), foresightCount, maxForesightCount, scoreResult.getScore(), thresholdScoreToIncreaseForesightCount, bestScoreCount, scoresToSkip, foresightScoringActions);
        ScoreResult<S> result = commonPool.invoke(scoringTask);

        return result.getActionScoreList().get(0);
    }

    @Override
    public ExecutionSummary<S> performBestScoreTrial(Subject<S> subject){
        Instant start = Instant.now();
        boolean found = false;
        ScoreResult<S> scoreResult = ScoreResult.empty(subject, scoringMechanism.getScore(subject));

        System.out.println("Perform best score trial");
        for(int i = 0; i < limit; i++){
            System.out.println("level " + i);
            System.out.println("Last Score: " + scoreResult.getScore());
            Pair<Action<S>, Double> bestActionScore = getActionScoreWithHighestScore(scoreResult);
            subject.performAction(bestActionScore.getValue0());
            scoreResult.addActionScore(bestActionScore);

            System.out.println(bestActionScore.getValue0().getName());
            subject.print();
            System.out.println(scoringMechanism.getScore(subject));
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

    public int getBestScoreCount() {
        return bestScoreCount;
    }

    public void setBestScoreCount(int bestScoreCount) {
        this.bestScoreCount = bestScoreCount;
    }

    public int getSkipLastScoreCount() {
        return skipLastScoreCount;
    }

    public void setSkipLastScoreCount(int skipLastScoreCount) {
        this.skipLastScoreCount = skipLastScoreCount;
    }

    public Double getThresholdScoreToIncreaseForesightCount() {
        return thresholdScoreToIncreaseForesightCount;
    }

    public void setThresholdScoreToIncreaseForesightCount(Double thresholdScoreToIncreaseForesightCount) {
        this.thresholdScoreToIncreaseForesightCount = thresholdScoreToIncreaseForesightCount;
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

    public int incrementBestScoreCount(){
        int incremented = getBestScoreCount() + 1;
        setBestScoreCount(incremented);
        return incremented;
    }

    public static class ScoringTask<S> extends RecursiveTask<ScoreResult<S>> {

        ScoreResult<S> scoreResult;
        int foresightCount;
        int maxForesightCount;
        int bestScoreCount;
        List<Double> lastFewScoresToSkip;
        ForesightScoringActions<S> foresightScoringActions;
        Double lastScore;
        Double thresholdScoreToIncreaseForesightCount;

        public ScoringTask(ScoreResult<S> scoreResult, int foresightCount, int maxForesightCount, Double lastScore, Double thresholdScoreToIncreaseForesightCount, int bestScoreCount, List<Double> lastFewScoresToSkip, ForesightScoringActions<S> foresightScoringActions){
            this.scoreResult = scoreResult;
            this.foresightCount = foresightCount;
            this.maxForesightCount = maxForesightCount;
            this.lastScore = lastScore;
            this.thresholdScoreToIncreaseForesightCount = thresholdScoreToIncreaseForesightCount;
            this.bestScoreCount = bestScoreCount;
            this.lastFewScoresToSkip = lastFewScoresToSkip;
            this.foresightScoringActions = foresightScoringActions;
        }

        @Override
        protected ScoreResult<S> compute() {
            List<ScoreResult<S>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(scoreResult);
            scoreResultList = foresightScoringActions.filterScoreResultListByRemovingItemsWithCertainScores(scoreResultList, lastFewScoresToSkip);
            scoreResultList = foresightScoringActions.getBestScore(scoreResultList, bestScoreCount);
            if(scoreResultList.size() == 1) return scoreResultList.get(0);
            if(scoreResultList.size() > 0){
                if(scoreResultList.get(0).getActionCount() >= foresightCount) {
                    if(scoreResultList.get(0).getScore() < thresholdScoreToIncreaseForesightCount || scoreResultList.get(0).getScore() > lastScore) return scoreResultList.get(0);
                }
                if(scoreResultList.get(0).getActionCount() >= maxForesightCount) return scoreResultList.get(0);
            }

            return ForkJoinTask.invokeAll(createSubTasks(scoreResultList))
                    .stream()
                    .map(ForkJoinTask::join)
                    .max(Comparator.comparingDouble(ScoreResult::getScore))
                    .orElseThrow();
        }

        private List<ScoringTask<S>> createSubTasks(List<ScoreResult<S>> scoreResultList){
            return scoreResultList.stream()
                    .map(result -> new ScoringTask<>(result, foresightCount, maxForesightCount, lastScore, thresholdScoreToIncreaseForesightCount, bestScoreCount, lastFewScoresToSkip, foresightScoringActions))
                    .collect(Collectors.toList());
        }

    }

}
