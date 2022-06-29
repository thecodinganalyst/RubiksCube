package rubikcube.strategy;

import rubikcube.RubikCube;
import rubikcube.action.RubikCubeAction;

import java.time.Instant;
import java.util.*;

public class RandomActionStrategy implements RubikCubeStrategy {

    Random random = new Random();
    int limit;
    int maxSteps;
    int successCount;

    public RandomActionStrategy(int limit, int maxSteps, int successCount){
        this.limit = limit;
        this.maxSteps = maxSteps;
        this.successCount = successCount;
    }

    @Override
    public ExecutionSummary execute(RubikCube rubikCube){
        Instant start = Instant.now();
        int success = 0;
        Collection<ExecutionSummary> successList = new ArrayList<>();

        for(int i = 0; i < limit; i++){
            try{
                RubikCube cube = rubikCube.clone();
                ExecutionSummary executionSummary = performRandomActionsUntilComplete(cube, maxSteps);

                if(executionSummary.successful){
                    success++;
                    successList.add(executionSummary);
                    System.out.println(i + ": " + cube.check());
                    cube.print();
                    if(success == successCount) break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ExecutionSummary fastest = successList.stream()
                                                .min(Comparator.comparingInt(a -> a.successSteps.size()))
                                                .orElse(null);

        Instant end = Instant.now();

        return new ExecutionSummary(
                fastest != null,
                fastest != null ? fastest.successSteps : null,
                start,
                end);
    }

    public ExecutionSummary performRandomActionsUntilComplete(RubikCube rubikCube, int withinSteps){
        Instant start = Instant.now();

        List<RubikCubeAction> actions = new ArrayList<>();
        for (int i = 0; i < withinSteps; i ++){
            RubikCubeAction action = performRandomAction(rubikCube);
            actions.add(action);
            if(rubikCube.isComplete()) break;
        }

        Instant end = Instant.now();
        return new ExecutionSummary(rubikCube.isComplete(), actions, start, end);
    }

    private RubikCubeAction getRandomAction(RubikCubeAction[] allPossibleActions){
        int rand = random.nextInt(allPossibleActions.length);
        return allPossibleActions[rand];
    }

    public RubikCubeAction performRandomAction(RubikCube rubikCube){
        RubikCubeAction action = getRandomAction(rubikCube.getAllActions());
        action.performAction(rubikCube);
        return action;
    }
}
