package rubikcube;

import org.junit.jupiter.api.Test;
import solutioning.strategy.Action;

import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RubikSolutionTest {

    @Test
    void randomActions_shouldGenerateActionsAsSpecified() {
        RubikSolution solution = new RubikSolution();
        RubikCube cube = new RubikCube(3);
        List<Action<RubikCube>> actions = solution.randomActions(cube, 20);
        assertThat(actions.size(), equalTo(20));
        int variations = new HashSet<>(actions).size();
        assertThat(variations, greaterThan(1));
    }

    @Test
    void reverseActions() {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();
        List<Action<RubikCube>> randomActions = solution.randomActions(cube, 30);
        randomActions.forEach(action -> System.out.println(action.getName()));
        cube.print();
        System.out.println(cube.check());

        List<Action<RubikCube>> reverseActions = solution.reverseActions(randomActions);
        reverseActions.forEach(action -> System.out.println(action.getName()));
        reverseActions.forEach(cube::performAction);
        cube.print();
        System.out.println(cube.check());
        assertThat(cube.check(), equalTo(true));
        assertThat(cube.isComplete(), equalTo(true));
    }
}