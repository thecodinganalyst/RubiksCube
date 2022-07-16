package solutioning.strategy.scoring;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rubikcube.RubikCube;
import rubikcube.action.ConsolidatedAction;
import solutioning.strategy.Action;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class ScoreResultTest {

    @Test
    void ScoreResult(){
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>((List<Pair<Action<RubikCube>, Double>>) null, new RubikCube(3));
        assertThat(scoreResult.getScore(), equalTo(0.0));
        assertThat(scoreResult.getActionCount(), equalTo(0));

        scoreResult = new ScoreResult<>(
                List.of(
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3), 0.01)
                ),
                new RubikCube(3));
        assertThat(scoreResult.getScore(), equalTo(0.01));
        assertThat(scoreResult.getActionCount(), equalTo(1));

        scoreResult = new ScoreResult<>(
                List.of(
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3), 0.01),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 1, 3), 0.02),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.RIGHT_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 2, 3), 0.03),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3), 0.04),
                        Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.TOP_FACE, ConsolidatedAction.DIRECTION.ANTI_CLOCKWISE, 2, 3), 1.2)
                ),
                new RubikCube(3));

        assertThat(scoreResult.getScore(), equalTo(1.2));
        assertThat(scoreResult.getActionCount(), equalTo(5));
    }

    @Test
    void compareTo() {
        ScoreResult<RubikCube> a = new ScoreResult<>(1.2, new RubikCube(3));
        ScoreResult<RubikCube> b = new ScoreResult<>(4.0, new RubikCube(3));
        ScoreResult<RubikCube> c = new ScoreResult<>(2.4, new RubikCube(3));
        List<ScoreResult<RubikCube>> list = List.of(a, b, c);
        List<ScoreResult<RubikCube>> sorted = list.stream().sorted().toList();
        assertThat(sorted.get(0).getScore(), equalTo(4.0));
        assertThat(sorted.get(1).getScore(), equalTo(2.4));
        assertThat(sorted.get(2).getScore(), equalTo(1.2));
    }

    @Test
    void empty() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(1.0, new RubikCube(3));
        ScoreResult<RubikCube> empty = scoreResult.empty();
        assertThat(empty.getScore(), equalTo(1.0));

        Action<RubikCube> action = Mockito.mock(ConsolidatedAction.class);
        scoreResult = new ScoreResult<>(List.of(Pair.with(action, 0.1), Pair.with(action, 0.2)), new RubikCube(3));
        empty = scoreResult.empty();
        assertThat(empty.getScore(), equalTo(0.2));
        assertThat(empty.getActionCount(), equalTo(0));
    }

    @Test
    void lastAction() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(
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

    @Test
    void addActionScore() {
        ScoreResult<RubikCube> scoreResult = new ScoreResult<>(0.6, new RubikCube(3));
        assertThat(scoreResult.getScore(), equalTo(0.6));
        scoreResult.addActionScore(Pair.with(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3), 0.4));
        assertThat(scoreResult.getScore(), equalTo(0.4));
    }

}