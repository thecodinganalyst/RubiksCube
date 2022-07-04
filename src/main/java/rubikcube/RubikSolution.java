package rubikcube;

import rubikcube.action.RubikCubeAction;
import rubikcube.strategy.*;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RubikSolution {
    public static void main(String[] args) throws CloneNotSupportedException {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();

        solution.runActionListAndReverse(cube.clone(), 20);

        cube.randomize();
//        solution.runRandomActionStrategy(cube.clone(), 10000, 100, 3);
//        solution.runScoringStrategy(cube.clone(), 1000);
        solution.runEnhancedScoringStrategy(cube.clone(), 100, 5);

    }

    public void runEnhancedScoringStrategy(RubikCube cube, int limit, int forkLimit){
        EnhancedScoringStrategy enhancedScoringStrategy = new EnhancedScoringStrategy(limit, forkLimit);
        ExecutionSummary executionSummary = enhancedScoringStrategy.execute(cube);
        cube.print();
        System.out.println(RubikCubeScoring.getRubikCubeScore(cube));
        executionSummary.print();
    }

    public void runScoringStrategy(RubikCube cube, int limit){
        ScoringStrategy scoringStrategy = new ScoringStrategy(limit);
        double score = RubikCubeScoring.getRubikCubeScore(cube);
        System.out.println(score);
        ExecutionSummary executionSummary = scoringStrategy.execute(cube);
        executionSummary.print();
        cube.print();
        score = RubikCubeScoring.getRubikCubeScore(cube);
        System.out.println(score);
    }

    public void runRandomActionStrategy(RubikCube cube, int limit, int maxSteps, int successCount){
        cube.print();
        System.out.println(RubikCubeScoring.getRubikCubeScore(cube));
        RandomActionStrategy randomActionStrategy = new rubikcube.strategy.RandomActionStrategy(limit, maxSteps, successCount);
        ExecutionSummary executionSummary = randomActionStrategy.execute(cube);
        executionSummary.print();
    }

    public void runActionListAndReverse(RubikCube cube, int count){
        rubikcube.RubikSolution solution = new rubikcube.RubikSolution();
        cube.print();
        System.out.println("Check: " + cube.check());
        System.out.println("Perfect: " + cube.isComplete());

        List<rubikcube.action.RubikCubeAction> randomActions = solution.randomActions(cube, count);
        randomActions.forEach(action -> System.out.println(action.getName()));
        cube.print();
        System.out.println("Check: " + cube.check());

        List<rubikcube.action.RubikCubeAction> reverseActions = solution.reverseActions(randomActions);
        reverseActions.forEach(action -> System.out.println(action.getName()));
        reverseActions.forEach(cube::performAction);
        cube.print();
        System.out.println("Check: " + cube.check());
        System.out.println("Perfect: " + cube.isComplete());
    }

    public List<RubikCubeAction> randomActions(RubikCube rubikCube, int count){
        Random random = new Random();
        int actionCount = rubikCube.getAllActions().length;
        return IntStream.range(0, count).boxed().map(i -> {
          RubikCubeAction action = rubikCube.getAllActions()[random.nextInt(actionCount)];
          rubikCube.performAction(action);
          return action;
        }).toList();
    }

    public List<RubikCubeAction> reverseActions(List<RubikCubeAction> originalActions){
        return IntStream.rangeClosed(1, originalActions.size())
                .boxed()
                .map(i -> originalActions.get(originalActions.size() - i))
                .map(RubikCubeAction::oppositeAction)
                .toList();
    }
}
