package solutioning.strategy.scoring;

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

    private final int foresightCount;
    private final int bestScoreCount;
    private final int skipLastScoreCount;
    private final List<Double> scoreList;
    private final ForesightScoringActions<S> foresightScoringActions;

    public ForesightScoringStrategy(ForesightScoringStrategyOptions options, ScoringMechanism<S> scoringMechanism) {
        super(options.limit(), scoringMechanism);
        ForesightScoringStrategyOptions defaultOption = ForesightScoringStrategyOptions.DEFAULT();
        this.foresightCount = options.foresightCount() == null ? defaultOption.foresightCount() : options.foresightCount();
        this.bestScoreCount = options.bestScoreCount() == null ? defaultOption.bestScoreCount() : options.bestScoreCount();
        this.skipLastScoreCount = options.skipLastScoreCount() == null ? defaultOption.skipLastScoreCount() : options.skipLastScoreCount();
        scoreList = new ArrayList<>();
        this.foresightScoringActions = new ForesightScoringActions<>(scoringMechanism);
    }

    public List<Action<S>> getActionListWithHighestScore(Subject<S> subject) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        List<Double> scoresToSkip = getLastFewScoresToSkip();

        ScoringTask<S> scoringTask = new ScoringTask<>(ScoreResult.empty(subject), foresightCount, bestScoreCount, scoresToSkip, foresightScoringActions);
        ScoreResult<S> result = commonPool.invoke(scoringTask);
        scoreList.add(result.score());
        return result.actionList().subList(0, 1);
    }

    private List<Double> getLastFewScoresToSkip(){
        if(scoreList.size() <= skipLastScoreCount) return scoreList;
        return scoreList.subList(scoreList.size() - skipLastScoreCount, scoreList.size());
    }

    @Override
    public ExecutionSummary<S> performBestScoreTrial(Subject<S> subject){
        Instant start = Instant.now();
        List<Action<S>> actionList = new ArrayList<>();
        boolean found = false;

        System.out.println("Perform best score trial");
        for(int i = 0; i < limit; i++){
            System.out.println("level " + i);
            List<Action<S>> bestActionList = getActionListWithHighestScore(subject);
            subject.performActionList(bestActionList);
            actionList.addAll(bestActionList);
            bestActionList.forEach(action -> System.out.println(action.getName()));
            subject.print();
            System.out.println(scoringMechanism.getScore(subject));
            if(subject.isComplete()){
                found = true;
                break;
            }
        }
        Instant end = Instant.now();
        return new ExecutionSummary<>(found, found ? actionList : null, start, end);
    }

    public record ForesightScoringStrategyOptions(Integer limit, Integer foresightCount, Integer bestScoreCount, Integer skipLastScoreCount){
        public static ForesightScoringStrategyOptions DEFAULT() {
            return new ForesightScoringStrategyOptions(100, 10, 10, 5);
        }
    }

    public static class ScoringTask<S> extends RecursiveTask<ScoreResult<S>> {

        ScoreResult<S> scoreResult;
        int foresightCount;
        int bestScoreCount;
        Action<S> lastAction;
        List<Double> lastFewScoresToSkip;
        ForesightScoringActions<S> foresightScoringActions;

        public ScoringTask(ScoreResult<S> scoreResult, int foresightCount, int bestScoreCount, List<Double> lastFewScoresToSkip, ForesightScoringActions<S> foresightScoringActions){
            this.scoreResult = scoreResult;
            this.foresightCount = foresightCount;
            this.bestScoreCount = bestScoreCount;
            this.lastFewScoresToSkip = lastFewScoresToSkip;
            this.foresightScoringActions = foresightScoringActions;
        }

        public ScoringTask(ScoreResult<S> scoreResult, int foresightCount, int bestScoreCount, List<Double> lastFewScoresToSkip, Action<S> lastAction, ForesightScoringActions<S> foresightScoringActions){
            this.scoreResult = scoreResult;
            this.foresightCount = foresightCount;
            this.bestScoreCount = bestScoreCount;
            this.lastFewScoresToSkip = lastFewScoresToSkip;
            this.lastAction = lastAction;
            this.foresightScoringActions = foresightScoringActions;
        }

        @Override
        protected ScoreResult<S> compute() {
            List<ScoreResult<S>> scoreResultList = foresightScoringActions.getRankedResultsForAllPossibleActions(scoreResult);
            scoreResultList = foresightScoringActions.filterScoreResultListByRemovingItemsWithCertainScores(scoreResultList, lastFewScoresToSkip);
            scoreResultList = foresightScoringActions.getBestScore(scoreResultList, bestScoreCount);
            if(scoreResultList.size() == 1) return scoreResultList.get(0);
            if(scoreResultList.size() > 0 && scoreResultList.get(0).actionList().size() == foresightCount) return scoreResultList.get(0);

            return ForkJoinTask.invokeAll(createSubTasks(scoreResultList))
                    .stream()
                    .map(ForkJoinTask::join)
                    .max(Comparator.comparingDouble(ScoreResult::score))
                    .orElseThrow();
        }

        private List<ScoringTask<S>> createSubTasks(List<ScoreResult<S>> scoreResultList){
            return scoreResultList.stream()
                    .map(result -> new ScoringTask<>(result, foresightCount, bestScoreCount, lastFewScoresToSkip, result.lastAction(), foresightScoringActions))
                    .collect(Collectors.toList());
        }


    }

}
