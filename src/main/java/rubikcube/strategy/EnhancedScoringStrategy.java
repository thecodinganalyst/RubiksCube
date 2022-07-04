package rubikcube.strategy;

import rubikcube.RubikCube;
import rubikcube.action.RubikCubeAction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class EnhancedScoringStrategy extends ScoringStrategy {

    private final int forkLimit;

    public EnhancedScoringStrategy(int limit, int forkLimit) {
        super(limit);
        this.forkLimit = forkLimit;
    }

    public List<RubikCubeAction> getActionListWithHighestScore(RubikCube cube) {
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        ScoringTask scoringTask = new ScoringTask(ScoreResult.empty(cube), forkLimit);
        ScoreResult result = commonPool.invoke(scoringTask);
        return result.actionList;
    }

    @Override
    public ExecutionSummary performBestScoreTrial(RubikCube rubikCube){
        Instant start = Instant.now();
        List<RubikCubeAction> actionList = new ArrayList<>();
        boolean found = false;

        System.out.println("Perform best score trial");
        for(int i = 0; i < limit; i++){
            System.out.println("level " + i);
            List<RubikCubeAction> bestActionList = getActionListWithHighestScore(rubikCube);
            rubikCube.performActionList(bestActionList);
            actionList.addAll(bestActionList);
            bestActionList.forEach(action -> System.out.println(action.getName()));
            rubikCube.print();
            System.out.println(RubikCubeScoring.getRubikCubeScore(rubikCube));
            if(rubikCube.isComplete()){
                found = true;
                break;
            }
        }
        Instant end = Instant.now();
        return new ExecutionSummary(found, found ? actionList : null, start, end);
    }

    record ScoreResult(Double score, List<RubikCubeAction> actionList, RubikCube rubikCube){
        public static ScoreResult empty(RubikCube cube){
            return new ScoreResult(0.0, new ArrayList<>(), cube);
        }
    }

    public static class ScoringTask extends RecursiveTask<ScoreResult> {

        ScoreResult scoreResult;
        int forkLimit;
        RubikCubeAction lastAction;

        public ScoringTask(ScoreResult scoreResult, int forkLimit){
            this.scoreResult = scoreResult;
            this.forkLimit = forkLimit;
        }

        public ScoringTask(ScoreResult scoreResult, int forkLimit, RubikCubeAction lastAction){
            this.scoreResult = scoreResult;
            this.forkLimit = forkLimit;
            this.lastAction = lastAction;
        }

        @Override
        protected ScoreResult compute() {
            List<ScoreResult> scoreResultList = getActionListWithHighestScore(scoreResult);
            if(scoreResultList.size() == 1) return scoreResultList.get(0);
            if(scoreResultList.size() > 0 && scoreResultList.get(0).actionList.size() == forkLimit) return scoreResultList.get(0);

            return ForkJoinTask.invokeAll(createSubTasks(scoreResultList))
                    .stream()
                    .map(ForkJoinTask::join)
                    .max(Comparator.comparingDouble(ScoreResult::score))
                    .orElseThrow();
        }

        private List<ScoringTask> createSubTasks(List<ScoreResult> scoreResultList){
            return scoreResultList.stream()
                    .map(result -> new ScoringTask(result, forkLimit, result.actionList.get(result.actionList.size() - 1)))
                    .collect(Collectors.toList());
        }

        public List<ScoreResult> getActionListWithHighestScore(ScoreResult result) {
            double highestScore = 0.0;
            List<ScoreResult> candidates = new ArrayList<>();

            for(RubikCubeAction action: result.rubikCube.getAllActions()){
                try{
                    if(lastAction != null && action.equals(lastAction.oppositeAction())) continue;
                    RubikCube clone = result.rubikCube.clone();
                    clone.performAction(action);
                    double score = RubikCubeScoring.getRubikCubeScore(clone);
                    List<RubikCubeAction> updatedList = new ArrayList<>(result.actionList);
                    updatedList.add(action);
                    if(clone.isComplete()) return List.of(new ScoreResult(score, updatedList, clone));
                    if(score > highestScore){
                        highestScore = score;
                        candidates = new ArrayList<>();
                    }
                    candidates.add(new ScoreResult(score, updatedList, clone));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            return candidates;
        }
    }

}
