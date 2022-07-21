package rubikcube.scoring;

import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;
import rubikcube.RubikSide;
import rubikcube.action.ConsolidatedAction;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RubikCubeScoringByCompletionPercentageTest {

    RubikCubeScoringByCompletionPercentage scoring = new RubikCubeScoringByCompletionPercentage();

    @Test
    void getRubikSideScore() throws Exception {
        RubikSide side = new RubikSide(3, 1);
        assertThat(scoring.getRubikSideScore(side), equalTo(1.0));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1});
        double expected = BigDecimal.valueOf(7.0 / 9.0)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 1, 3});
        side.setRow(1, new int[]{1, 1, 1});
        side.setRow(2, new int[]{1, 1, 1});
        expected = BigDecimal.valueOf(8.0 / 9.0)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 2, 3});
        side.setRow(1, new int[]{1, 1, 4});
        side.setRow(2, new int[]{1, 1, 1});
        expected = BigDecimal.valueOf(5.0 / 9.0)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 1, 4});
        side.setRow(1, new int[]{1, 1, 4});
        side.setRow(2, new int[]{1, 1, 4});
        expected = BigDecimal.valueOf(2.0 / 3.0)
                .setScale(5, RoundingMode.HALF_UP)
                .doubleValue();
        assertThat(scoring.getRubikSideScore(side), equalTo(expected));

        side.setRow(0, new int[]{1, 2, 4});
        side.setRow(1, new int[]{1, 3, 4});
        side.setRow(2, new int[]{1, 1, 1});
        expected = BigDecimal.valueOf(4.0 / 9.0)
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
    void isComplete() {
        assertThat(scoring.isComplete(new int[]{1}), equalTo(true));

        assertThat(scoring.isComplete(new int[]{1, 1}), equalTo(true));
        assertThat(scoring.isComplete(new int[]{1, 2}), equalTo(false));

        assertThat(scoring.isComplete(new int[]{1, 1, 1}), equalTo(true));
        assertThat(scoring.isComplete(new int[]{1, 1, 2}), equalTo(false));
        assertThat(scoring.isComplete(new int[]{1, 2, 3}), equalTo(false));
    }

    @Test
    void getOccurrence() {
        assertThat(scoring.getOccurrence(new int[]{1, 2, 3}, 1), equalTo(1));
        assertThat(scoring.getOccurrence(new int[]{1, 1, 3}, 1), equalTo(2));
        assertThat(scoring.getOccurrence(new int[]{1, 1, 1}, 1), equalTo(3));
        assertThat(scoring.getOccurrence(new int[]{1, 2, 3}, 4), equalTo(0));
    }

}
