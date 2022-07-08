package rubikcube.scoring;

import rubikcube.RubikCube;
import rubikcube.RubikSide;
import solutioning.strategy.Subject;
import solutioning.strategy.scoring.ScoringMechanism;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RubikCubeScoringByCompletionPercentage implements ScoringMechanism<RubikCube> {

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
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
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
