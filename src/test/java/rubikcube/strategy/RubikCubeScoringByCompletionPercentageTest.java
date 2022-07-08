package rubikcube.strategy;

import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;
import rubikcube.RubikSide;
import rubikcube.action.ConsolidatedAction;
import rubikcube.scoring.RubikCubeScoringByCompletionPercentage;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RubikCubeScoringByCompletionPercentageTest {

    RubikCubeScoringByCompletionPercentage scoring = new RubikCubeScoringByCompletionPercentage();

    @Test
    void getScoreFromValues() {

        assertThat(scoring.getScoreFromValues(new int[]{2, 2, 2}), equalTo(1.0));
        assertThat(scoring.getScoreFromValues(new int[]{1, 1, 2}), equalTo((double)2 / (double)3));
        assertThat(scoring.getScoreFromValues(new int[]{1, 2, 3}), equalTo((double)1/ (double)3));
    }

    @Test
    void getRubikSideScore() throws Exception {
        RubikSide side = new RubikSide(3, 1);
        assertThat(scoring.getRubikSideScore(side), equalTo(1.0));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1});
        double expectedRow = (1.0/3.0 + 1.0 + 1.0) / 3.0 * 2.0/3.0;
        double expectedCol = (1.0 + (2.0 / 3.0) + (2.0 / 3.0)) / 3.0 / 3.0;
        double expected = BigDecimal.valueOf(Math.max(expectedRow, expectedCol))
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 1, 3});
        side.setRow(1, new int[]{1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1});
        expectedRow = (2.0/3.0 + 1.0 + 1.0) / 3.0 * 2.0/3.0;
        expectedCol = (1.0 + 1.0 + (2.0 / 3.0)) / 3.0 * 2.0 / 3.0;
        expected = BigDecimal.valueOf(Math.max(expectedRow, expectedCol))
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 1, 4});
        side.setRow(2, new int[]{1, 1, 1});
        expectedRow = (1.0/3.0 + 2.0/3.0 + 1.0) / 3.0 / 3.0;
        expectedCol = (1.0 + 2.0/3.0 + 1.0/3.0) / 3.0 / 3.0;
        expected = BigDecimal.valueOf(Math.max(expectedRow, expectedCol))
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 1, 4});
        side.setRow(1, new int[]{1, 1, 4});
        side.setRow(2, new int[]{1, 1, 4});
        expectedRow = 0.0;
        expectedCol = (1.0 + 1.0 + 1.0) / 3.0 * 2.0 / 3.0;
        expected = BigDecimal.valueOf(Math.max(expectedRow, expectedCol))
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));
    }

    @Test
    void getRubikCubeScore() {
        RubikCube cube = new RubikCube(3);
        assertThat(scoring.getRubikCubeScore(cube), equalTo(1.0));

        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3));
        cube.print();
        double expected = BigDecimal.valueOf((1.0 + 1.0 + (2.0 / 3.0) + (2.0 / 3.0) + (2.0 / 3.0) + (2.0 / 3.0)) / 6.0)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikCubeScore(cube), equalTo(expected));
    }

    @Test
    void getRubikSideCompletionScore() throws Exception {
        RubikSide side = new RubikSide(3, 1);
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(1.0));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1});
        double expected = 2.0 / 3.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 2, 3});
        side.setRow(2, new int[]{1, 1, 1});
        expected = 1.0 / 3.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 2, 3});
        side.setRow(2, new int[]{1, 2, 3});
        expected = 0.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side = new RubikSide(3, 1);
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(1.0));

        side.setCol(0, new int[]{1, 2, 3});
        side.setCol(1, new int[]{1, 1, 1});
        side.setCol(2, new int[]{1, 1, 1});
        expected = 2.0 / 3.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{1, 2, 3});
        side.setCol(1, new int[]{1, 2, 3});
        side.setCol(2, new int[]{1, 1, 1});
        expected = 1.0 / 3.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{1, 2, 3});
        side.setCol(1, new int[]{1, 2, 3});
        side.setCol(2, new int[]{1, 2, 3});
        expected = 0.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setRow(0, new int[]{1, 1, 1});
        side.setRow(1, new int[]{1, 1, 1});
        side.setRow(2, new int[]{3, 3, 3});
        expected = 2.0 / 3.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{1, 1, 1});
        side.setRow(1, new int[]{2, 2, 2});
        side.setRow(2, new int[]{3, 3, 3});
        expected = 1.0 / 3.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));
    }

    @Test
    void getRubikSideCompletionScore_for4x4Cube() throws Exception {
        RubikSide side = new RubikSide(4, 1);
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(1.0));

        side.setRow(0, new int[]{2, 2, 2, 2});
        side.setRow(1, new int[]{1, 1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1, 1});
        side.setRow(3, new int[]{1, 1, 1, 1});
        double expected = 3.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2});
        side.setRow(1, new int[]{3, 3, 3, 3});
        side.setRow(2, new int[]{1, 1, 1, 1});
        side.setRow(3, new int[]{1, 1, 1, 1});
        expected = 2.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2});
        side.setRow(1, new int[]{3, 3, 3, 3});
        side.setRow(2, new int[]{4, 4, 4, 4});
        side.setRow(3, new int[]{1, 1, 1, 1});
        expected = 1.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{3, 2, 2, 2});
        side.setRow(1, new int[]{4, 3, 3, 3});
        side.setRow(2, new int[]{1, 4, 4, 4});
        side.setRow(3, new int[]{2, 1, 1, 1});
        expected = 0.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2});
        side.setRow(1, new int[]{2, 2, 2, 2});
        side.setRow(2, new int[]{4, 4, 4, 4});
        side.setRow(3, new int[]{4, 4, 4, 4});
        expected = 2.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        // test columns
        side.setCol(0, new int[]{2, 2, 2, 2});
        side.setCol(1, new int[]{1, 1, 1, 1});
        side.setCol(2, new int[]{1, 1, 1, 1});
        side.setCol(3, new int[]{1, 1, 1, 1});
        expected = 3.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{2, 2, 2, 2});
        side.setCol(1, new int[]{3, 3, 3, 3});
        side.setCol(2, new int[]{1, 1, 1, 1});
        side.setCol(3, new int[]{1, 1, 1, 1});
        expected = 2.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{2, 2, 2, 2});
        side.setCol(1, new int[]{3, 3, 3, 3});
        side.setCol(2, new int[]{4, 4, 4, 4});
        side.setCol(3, new int[]{1, 1, 1, 1});
        expected = 1.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{3, 2, 2, 2});
        side.setCol(1, new int[]{4, 3, 3, 3});
        side.setCol(2, new int[]{1, 4, 4, 4});
        side.setCol(3, new int[]{2, 1, 1, 1});
        expected = 0.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{2, 2, 2, 2});
        side.setCol(1, new int[]{2, 2, 2, 2});
        side.setCol(2, new int[]{4, 4, 4, 4});
        side.setCol(3, new int[]{4, 4, 4, 4});
        expected = 2.0 / 4.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getCol), equalTo(expected));
    }

    @Test
    void getRubikSideCompletionScore_for5x5Cube() throws Exception {
        RubikSide side = new RubikSide(5, 1);
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(1.0));

        side.setRow(0, new int[]{2, 2, 2, 2, 2});
        side.setRow(1, new int[]{1, 1, 1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1, 1, 1});
        side.setRow(3, new int[]{1, 1, 1, 1, 1});
        side.setRow(4, new int[]{1, 1, 1, 1, 1});
        double expected = 4.0 / 5.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2, 2});
        side.setRow(1, new int[]{3, 3, 3, 3, 3});
        side.setRow(2, new int[]{1, 1, 1, 1, 1});
        side.setRow(3, new int[]{1, 1, 1, 1, 1});
        side.setRow(4, new int[]{1, 1, 1, 1, 1});
        expected = 3.0 / 5.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2, 2});
        side.setRow(1, new int[]{3, 3, 3, 3, 3});
        side.setRow(2, new int[]{4, 4, 4, 4, 4});
        side.setRow(3, new int[]{1, 1, 1, 1, 1});
        side.setRow(4, new int[]{1, 1, 1, 1, 1});
        expected = 2.0 / 5.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2, 2});
        side.setRow(1, new int[]{3, 3, 3, 3, 3});
        side.setRow(2, new int[]{4, 4, 4, 4, 4});
        side.setRow(3, new int[]{5, 5, 5, 5, 5});
        side.setRow(4, new int[]{1, 1, 1, 1, 1});
        expected = 1.0 / 5.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{3, 2, 2, 2, 2});
        side.setRow(1, new int[]{4, 3, 3, 3, 3});
        side.setRow(2, new int[]{1, 4, 4, 4, 4});
        side.setRow(3, new int[]{2, 1, 1, 1, 1});
        side.setRow(4, new int[]{6, 2, 1, 1, 1});
        expected = 0.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2, 2});
        side.setRow(1, new int[]{2, 2, 2, 2, 2});
        side.setRow(2, new int[]{4, 4, 4, 4, 4});
        side.setRow(3, new int[]{4, 4, 4, 4, 4});
        side.setRow(4, new int[]{5, 5, 5, 5, 5});
        expected = 2.0 / 5.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 2, 2, 2});
        side.setRow(1, new int[]{2, 2, 2, 2, 2});
        side.setRow(2, new int[]{4, 4, 4, 4, 4});
        side.setRow(3, new int[]{4, 4, 4, 4, 4});
        side.setRow(4, new int[]{4, 4, 4, 4, 4});
        expected = 3.0 / 5.0;
        assertThat(scoring.getRubikSideCompletionScore(side, side::getRow), equalTo(expected));
    }

    @Test
    void getRubikSideAverageScore() throws Exception {
        RubikSide side = new RubikSide(3, 1);
        double expected = 1.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{1, 2, 3});
        expected = ((1.0 / 3.0) + 1.0 + 1.0) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side.setRow(1, new int[]{1, 2, 3});
        expected = ((1.0 / 3.0) + (1.0 / 3.0) + 1.0) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side.setRow(2, new int[]{1, 2, 3});
        expected = ((1.0 / 3.0) + (1.0 / 3.0) + (1.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side.setRow(2, new int[]{2, 2, 3});
        expected = ((1.0 / 3.0) + (1.0 / 3.0) + (2.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side.setRow(1, new int[]{2, 2, 3});
        expected = ((1.0 / 3.0) + (2.0 / 3.0) + (2.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side.setRow(0, new int[]{2, 2, 3});
        expected = ((2.0 / 3.0) + (2.0 / 3.0) + (2.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getRow), equalTo(expected));

        side = new RubikSide(3, 1);
        expected = 1.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{1, 2, 3});
        expected = ((1.0 / 3.0) + 1.0 + 1.0) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));

        side.setCol(1, new int[]{1, 2, 3});
        expected = ((1.0 / 3.0) + (1.0 / 3.0) + 1.0) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));

        side.setCol(2, new int[]{1, 2, 3});
        expected = ((1.0 / 3.0) + (1.0 / 3.0) + (1.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));

        side.setCol(2, new int[]{2, 2, 3});
        expected = ((1.0 / 3.0) + (1.0 / 3.0) + (2.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));

        side.setCol(1, new int[]{2, 2, 3});
        expected = ((1.0 / 3.0) + (2.0 / 3.0) + (2.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));

        side.setCol(0, new int[]{2, 2, 3});
        expected = ((2.0 / 3.0) + (2.0 / 3.0) + (2.0 / 3.0)) / 3.0;
        assertThat(scoring.getRubikSideAverageScore(side, side::getCol), equalTo(expected));
    }

}
