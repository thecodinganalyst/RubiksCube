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

    private final int foresightCount;
    private final int bestScoreCount;
    private final int skipLastScoreCount;
    private final ForesightScoringActions<S> foresightScoringActions;

    public ForesightScoringStrategy(int limit, int foresightCount, int bestScoreCount, int skipLastScoreCount, ScoringMechanism<S> scoringMechanism) {
        super(limit, scoringMechanism);
        this.foresightCount = foresightCount;
        this.bestScoreCount = bestScoreCount;
        this.skipLastScoreCount = skipLastScoreCount;
        this.foresightScoringActions = new ForesightScoringActions<>(scoringMechanism);
    }

    public List<Pair<Action<S>, Double>> getActionScoreWithHighestScore(ScoreResult<S> scoreResult) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        List<Double> scoresToSkip = foresightScoringActions.getLastFewScoresToSkip(scoreResult, skipLastScoreCount);
        System.out.println("Last few scores to skip: " + scoresToSkip.toString());
        ScoringTask<S> scoringTask = new ScoringTask<>(ScoreResult.empty(scoreResult.subject()), foresightCount, bestScoreCount, scoresToSkip, foresightScoringActions);
        ScoreResult<S> result = commonPool.invoke(scoringTask);

        return result.actionScoreList().subList(0, 1);
    }

    @Override
    public ExecutionSummary<S> performBestScoreTrial(Subject<S> subject){
        Instant start = Instant.now();
        boolean found = false;
        ScoreResult<S> scoreResult = ScoreResult.empty(subject);

        System.out.println("Perform best score trial");
        for(int i = 0; i < limit; i++){
            System.out.println("level " + i);
            List<Pair<Action<S>, Double>> bestActionScoreList = getActionScoreWithHighestScore(scoreResult);
            List<Action<S>> bestActionList = bestActionScoreList.stream().map(Pair::getValue0).toList();
            subject.performActionList(bestActionList);
            scoreResult.actionScoreList().addAll(bestActionScoreList);

            bestActionList.forEach(action -> System.out.println(action.getName()));
            subject.print();
            System.out.println(scoringMechanism.getScore(subject));

            if(subject.isComplete()){
                found = true;
                break;
            }
        }
        Instant end = Instant.now();
        return new ExecutionSummary<>(found, found ? scoreResult.getActionList() : null, start, end);
    }

    public static class ScoringTask<S> extends RecursiveTask<ScoreResult<S>> {

        ScoreResult<S> scoreResult;
        int foresightCount;
        int bestScoreCount;
        List<Double> lastFewScoresToSkip;
        ForesightScoringActions<S> foresightScoringActions;

        public ScoringTask(ScoreResult<S> scoreResult, int foresightCount, int bestScoreCount, List<Double> lastFewScoresToSkip, ForesightScoringActions<S> foresightScoringActions){
            this.scoreResult = scoreResult;
            this.foresightCount = foresightCount;
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
            if(scoreResultList.size() > 0 && scoreResultList.get(0).actionScoreList().size() == foresightCount) return scoreResultList.get(0);

            return ForkJoinTask.invokeAll(createSubTasks(scoreResultList))
                    .stream()
                    .map(ForkJoinTask::join)
                    .max(Comparator.comparingDouble(ScoreResult::score))
                    .orElseThrow();
        }

        private List<ScoringTask<S>> createSubTasks(List<ScoreResult<S>> scoreResultList){
            return scoreResultList.stream()
                    .map(result -> new ScoringTask<>(result, foresightCount, bestScoreCount, lastFewScoresToSkip, foresightScoringActions))
                    .collect(Collectors.toList());
        }


    }

}
