package rubikcube.strategy;

import rubikcube.RubikCube;
import rubikcube.action.RubikCubeAction;
import java.time.Instant;
import java.util.*;

public class ScoringStrategy implements RubikCubeStrategy {
    int limit;

    public ScoringStrategy(int limit){
        this.limit = limit;
    }

    public RubikCubeAction getActionWithHighestScore(RubikCube cube){
        double highestScore = 0;
        RubikCubeAction bestAction = null;
        for(RubikCubeAction action: cube.getAllActions()){
            cube.performAction(action);
            double score = RubikCubeScoring.getRubikCubeScore(cube);
            if(score > highestScore){
                highestScore = score;
                bestAction = action;
            }
            cube.performAction(action.oppositeAction());
        }
        return bestAction;
    }

    public ExecutionSummary performBestScoreTrial(RubikCube rubikCube){
        Instant start = Instant.now();
        List<RubikCubeAction> actionList = new ArrayList<>();
        boolean found = false;
        for(int i = 0; i < limit; i++){
            RubikCubeAction bestAction = getActionWithHighestScore(rubikCube);
            rubikCube.performAction(bestAction);
            actionList.add(bestAction);
            if(rubikCube.isComplete()){
                found = true;
                break;
            }
        }
        Instant end = Instant.now();
        return new ExecutionSummary(found, found ? actionList : null, start, end);
    }

    @Override
    public ExecutionSummary execute(RubikCube rubikCube) {
        ExecutionSummary executionSummary = null;
        try{
            executionSummary = performBestScoreTrial(rubikCube);
        } catch (Exception e){
            e.printStackTrace();
        }
        return executionSummary;
    }
}
