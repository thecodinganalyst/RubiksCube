package rubikcube.strategy;

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
        assertThat(sorted, contains(b, c, a));
    }

    @Test
    void empty() {
        ScoreResult<RubikCube> empty = ScoreResult.empty(new RubikCube(3));
        assertThat(empty.score(), equalTo(0.0));
        assertThat(empty.actionList(), hasSize(0));
        RubikCube cube = (RubikCube) empty.subject();
        assertThat(cube.getSize(), equalTo(3));
    }

    @Test
    void lastAction() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
                1.2,
                List.of(
                        new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3),
                        new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 1, 3),
                        new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 2, 3),
                        new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3),
                        new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 2, 3)
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