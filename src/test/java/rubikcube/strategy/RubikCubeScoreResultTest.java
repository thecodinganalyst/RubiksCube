package rubikcube.strategy;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;
import rubikcube.action.ConsolidatedAction;
import solutioning.strategy.scoring.ScoreResult;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RubikCubeScoreResultTest {

    @Test
    void compareTo() {
        ScoreResult<RubikCube> a = new ScoreResult<>(1.2, null, new RubikCube(3));
        ScoreResult<RubikCube> b = new ScoreResult<>(4.0, null, new RubikCube(3));
        ScoreResult<RubikCube> c = new ScoreResult<>(2.4, null, new RubikCube(3));
        List<ScoreResult<RubikCube>> list = List.of(a, b, c);
        List<ScoreResult<RubikCube>> sorted = list.stream().sorted().toList();
        assertThat(sorted.get(0).getScore(), equalTo(4.0));
        assertThat(sorted.get(1).getScore(), equalTo(2.4));
        assertThat(sorted.get(2).getScore(), equalTo(1.2));
    }

    @Test
    void empty() {
        ScoreResult<RubikCube> empty = ScoreResult.empty(new RubikCube(3), 1.0);
        assertThat(empty.getScore(), equalTo(1.0));
        assertThat(empty.getActionScoreList(), hasSize(0));
        RubikCube cube = (RubikCube) empty.getSubject();
        assertThat(cube.getSize(), equalTo(3));
    }

    @Test
    void lastAction() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                1.2,
                List.of(
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3), 0.01),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 1, 3), 0.02),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 2, 3), 0.03),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3), 0.04),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 2, 3), 1.2)
                ),
                new RubikCube(3));

        ConsolidatedAction lastAction = (ConsolidatedAction) scoreResult.lastAction();
        assert lastAction != null;
        assertThat(lastAction.getFace(), equalTo(ConsolidatedAction.FACE.TOP_FACE));
        assertThat(lastAction.getDirection(), equalTo(ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE));
        assertThat(lastAction.getPosition(), equalTo(2));
        assertThat(lastAction.getSize(), equalTo(3));
    }
}