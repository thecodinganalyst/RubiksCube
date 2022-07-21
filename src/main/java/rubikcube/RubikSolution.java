package rubikcube;

import rubikcube.scoring.RubikCubeScoringByLayerCompleted;
import solutioning.strategy.Action;
import solutioning.strategy.ExecutionSummary;
import solutioning.strategy.multiple.MultipleForesightScoringStrategy;
import solutioning.strategy.random.RandomActionStrategy;
import solutioning.strategy.scoring.ForesightScoringStrategy;
import solutioning.strategy.scoring.ScoringMechanism;
import solutioning.strategy.scoring.ScoringStrategy;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RubikSolution {
    public static void main(String[] args) throws CloneNotSupportedException {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();
        ScoringMechanism<RubikCube> scoring = new RubikCubeScoringByLayerCompleted();

//        solution.runActionListAndReverse(cube.clone(), 20);

        cube.randomize();
        cube.print();
        System.out.println(scoring.getScore(cube));
//        solution.runRandomActionStrategy(cube.clone(), 100, 100, 3);
//        solution.runScoringStrategy(cube.clone(), 1000);
        solution.runForesightScoringStrategy(cube.clone(), 100, 4, 6, 0);

//        solution.runMultipleForesightScoringStrategy(cube.clone(), 200);

    }

    public void runMultipleForesightScoringStrategy(RubikCube cube, int limit){
        ScoringMechanism<RubikCube> scoring = new RubikCubeScoringByLayerCompleted();
        MultipleForesightScoringStrategy<RubikCube> multipleForesightScoringStrategy = new MultipleForesightScoringStrategy<>(limit, scoring);
        ExecutionSummary<RubikCube> executionSummary = multipleForesightScoringStrategy.execute(cube);

        executionSummary.print();
        cube.print();
        System.out.println(scoring.getScore(cube));
    }

    public void runForesightScoringStrategy(RubikCube cube, int limit, int foresightCount, int maxForesightCount, int skipLastScoreCount){
        ScoringMechanism<RubikCube> scoring = new RubikCubeScoringByLayerCompleted();
        ForesightScoringStrategy<RubikCube> foresightScoringStrategy = new ForesightScoringStrategy<>(limit, foresightCount, maxForesightCount, skipLastScoreCount, scoring);
        ExecutionSummary<RubikCube> executionSummary = foresightScoringStrategy.execute(cube);

        executionSummary.print();
        cube.print();
        System.out.println(scoring.getScore(cube));
    }

    public void runScoringStrategy(RubikCube cube, int limit){
        ScoringMechanism<RubikCube> scoring = new RubikCubeScoringByLayerCompleted();
        ScoringStrategy<RubikCube> scoringStrategy = new ScoringStrategy<>(limit, scoring);
        double score = scoring.getScore(cube);
        System.out.println(score);
        ExecutionSummary<RubikCube> executionSummary = scoringStrategy.execute(cube);
        executionSummary.print();
        cube.print();
        score = scoring.getScore(cube);
        System.out.println(score);
    }

    public void runRandomActionStrategy(RubikCube cube, int limit, int maxSteps, int successCount){
        cube.print();
        ScoringMechanism<RubikCube> scoring = new RubikCubeScoringByLayerCompleted();
        System.out.println(scoring.getScore(cube));
        RandomActionStrategy<RubikCube> randomActionStrategy = new RandomActionStrategy<>(limit, maxSteps, successCount);
        ExecutionSummary<RubikCube> executionSummary = randomActionStrategy.execute(cube);
        executionSummary.print();
    }

    public void runActionListAndReverse(RubikCube cube, int count){
        rubikcube.RubikSolution solution = new rubikcube.RubikSolution();
        cube.print();
        System.out.println("Check: " + cube.check());
        System.out.println("Perfect: " + cube.isComplete());

        List<Action<RubikCube>> randomActions = solution.randomActions(cube, count);
        randomActions.forEach(action -> System.out.println(action.getName()));
        cube.print();
        System.out.println("Check: " + cube.check());

        List<Action<RubikCube>> reverseActions = solution.reverseActions(randomActions);
        reverseActions.forEach(action -> System.out.println(action.getName()));
        reverseActions.forEach(cube::performAction);
        cube.print();
        System.out.println("Check: " + cube.check());
        System.out.println("Perfect: " + cube.isComplete());
    }

    public List<Action<RubikCube>> randomActions(RubikCube rubikCube, int count){
        Random random = new Random();
        int actionCount = rubikCube.getAllActions().length;
        return IntStream.range(0, count).boxed().map(i -> {
          Action<RubikCube> action = rubikCube.getAllActions()[random.nextInt(actionCount)];
          rubikCube.performAction(action);
          return action;
        }).toList();
    }

    public List<Action<RubikCube>> reverseActions(List<Action<RubikCube>> originalActions){
        return IntStream.rangeClosed(1, originalActions.size())
                .boxed()
                .map(i -> originalActions.get(originalActions.size() - i))
                .map(Action::oppositeAction)
                .toList();
    }
}
