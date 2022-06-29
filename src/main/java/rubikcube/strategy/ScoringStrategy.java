package rubikcube.strategy;

import rubikcube.RubikCube;
import rubikcube.action.RubikCubeAction;
import rubikcube.RubikSide;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ScoringStrategy implements RubikCubeStrategy {
    int limit;

    public ScoringStrategy(int limit){
        this.limit = limit;
    }

    public double getScoreFromValues(int[] values){
        Map<Integer, List<Integer>> groups = Arrays.stream(values).boxed().collect(Collectors.groupingBy(value -> value));
        int numerator = groups.keySet().size();
        if (numerator == 1) return 1;
        if (numerator == values.length) return 1.0 / values.length;
        return (double) numerator / (double) values.length;
    }

    public double getRubikSideCompletionScore(RubikSide rubikSide, Function<Integer, int[]> rubikSideGetValueFunction){
        List<int[]> qualifiedValuesList = IntStream.range(0, rubikSide.getSize())
                .boxed()
                .map(rubikSideGetValueFunction)
                .filter(values -> getScoreFromValues(values) == 1.0)
                .toList();

        if(qualifiedValuesList.size() == 0) return 0;

        Map<Integer, List<Integer>> groupedValues = qualifiedValuesList
                .stream()
                .map(values -> Arrays.stream(values).boxed().findFirst().orElseThrow())
                .collect(Collectors.groupingBy(i -> i));

        Stream<Integer> groupSizes = groupedValues.values()
                        .stream()
                        .map(List::size);

        int maxGroupSize = groupSizes.max(Comparator.comparingInt(i -> i)).orElseThrow();

        return ((double) maxGroupSize) / rubikSide.getSize();
    }

    public double getRubikSideAverageScore(RubikSide rubikSide, Function<Integer, int[]> rubikSideGetValueFunction){
        return IntStream.range(0, rubikSide.getSize())
                        .boxed()
                        .map(rubikSideGetValueFunction)
                        .mapToDouble(this::getScoreFromValues)
                        .average()
                        .orElse(0.0);
    }

    public double getRubikSideScore(RubikSide rubikSide){
        double averageRowScore = getRubikSideAverageScore(rubikSide, rubikSide::getRow);
        double rowSimilarityScore = getRubikSideCompletionScore(rubikSide, rubikSide::getRow);
        double rubikSideRowScore = averageRowScore * rowSimilarityScore;

        double averageColScore = getRubikSideAverageScore(rubikSide, rubikSide::getCol);
        double colSimilarityScore = getRubikSideCompletionScore(rubikSide, rubikSide::getCol);
        double rubikSideColScore = averageColScore * colSimilarityScore;

        return BigDecimal.valueOf(Math.max(rubikSideRowScore, rubikSideColScore))
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public double getRubikCubeScore(RubikCube cube){
        return cube.getAllSides()
                    .values()
                    .stream()
                    .map(this::getRubikSideScore)
                    .reduce(0.0, Double::sum) / 6.0;
    }

    public RubikCubeAction getActionWithHighestScore(RubikCube cube){
        double highestScore = 0;
        RubikCubeAction bestAction = null;
        for(RubikCubeAction action: cube.getAllActions()){
            cube.performAction(action);
            double score = getRubikCubeScore(cube);
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
