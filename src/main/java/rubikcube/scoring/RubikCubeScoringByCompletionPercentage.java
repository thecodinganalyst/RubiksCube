package rubikcube.scoring;

import rubikcube.RubikCube;
import rubikcube.RubikSide;
import solutioning.strategy.Subject;
import solutioning.strategy.scoring.ScoringMechanism;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

public class RubikCubeScoringByCompletionPercentage implements ScoringMechanism<RubikCube> {

    public double getRubikSideScore(RubikSide rubikSide){
        return Math.max(
                getRubikSideScoreFor(rubikSide, rubikSide::getRow),
                getRubikSideScoreFor(rubikSide, rubikSide::getCol));
    }

    public double truncate(double score){
        return BigDecimal.valueOf(score)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public double getRubikSideScoreFor(RubikSide rubikSide, Function<Integer, int[]> getRowFn){
        Map<Integer, Double> scoreTable = new HashMap<>();
        Set<Integer> completedValues = new HashSet<>();
        for (int i = 0; i < rubikSide.getSize(); i++){
            int[] rowData = getRowFn.apply(i);
            Set<Integer> uniqueValues = Arrays.stream(rowData).boxed().collect(toSet());

            uniqueValues.forEach(value -> {
                Double prevScore = scoreTable.getOrDefault(value, 0.0);
                if(isComplete(rowData)) {
                    scoreTable.put(value, prevScore + 1.0);
                    completedValues.add(value);
                }else{
                    double prevFloatingScore = prevScore - prevScore.intValue();
                    double floatingScore = ((double) getOccurrence(rowData, value)) / rowData.length;
                    if(floatingScore > prevFloatingScore){
                        scoreTable.put(value, prevScore.intValue() + floatingScore);
                    }
                }
            });
        }
        Set<Integer> valuesWithIncompleteRows = scoreTable.keySet()
                .stream()
                .filter(i -> !completedValues.contains(i))
                .collect(toSet());
        valuesWithIncompleteRows.forEach(scoreTable::remove);

        double score = scoreTable.values().stream().max(Double::compareTo).orElse(0.0) / rubikSide.getSize();
        return truncate(score);
    }

    public int getOccurrence(int[] arr, Integer value){
        return (int) Arrays.stream(arr).filter(i -> Objects.equals(i, value)).count();
    }

    public boolean isComplete(int[] values){
        if(values.length <= 1) return true;
        return Arrays.stream(values).allMatch(i -> i == values[0]);
    }

    public Double getRubikCubeScore(RubikCube cube) {
        double score = cube.getAllSides()
                .values()
                .stream()
                .map(this::getRubikSideScore)
                .reduce(0.0, Double::sum) / 6.0;
        return BigDecimal.valueOf(score).setScale(5, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Double getScore(Subject<RubikCube> subject){
        return getRubikCubeScore((RubikCube) subject);
    }
}
