package rubikcube.action;

import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class ConsolidatedActionTest {

    @Test
    void allActions() {
        ConsolidatedAction[] allActions = ConsolidatedAction.allActions(3);
        assertThat(allActions.length, equalTo(18));
    }

    @Test
    void performAction() {
        RubikCube cube = new RubikCube(3);
        cube.performAction(new ConsolidatedAction(ConsolidatedAction.FACE.MAIN_FACE, ConsolidatedAction.DIRECTION.CLOCKWISE, 0, 3));
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{ 1, 1, 1}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{ 1, 1, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{ 1, 1, 1}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{ 5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{ 5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{ 4, 4, 4}));

        assertThat(cube.getRight().getCol(0), equalTo(new int[]{ 5, 5, 5}));
        assertThat(cube.getRight().getCol(1), equalTo(new int[]{ 2, 2, 2}));
        assertThat(cube.getRight().getCol(2), equalTo(new int[]{ 2, 2, 2}));

        assertThat(cube.getBack().getCol(0), equalTo(new int[]{ 3, 3, 3}));
        assertThat(cube.getBack().getCol(1), equalTo(new int[]{ 3, 3, 3}));
        assertThat(cube.getBack().getCol(2), equalTo(new int[]{ 3, 3, 3}));

        assertThat(cube.getLeft().getCol(0), equalTo(new int[]{ 4, 4, 4}));
        assertThat(cube.getLeft().getCol(1), equalTo(new int[]{ 4, 4, 4}));
        assertThat(cube.getLeft().getCol(2), equalTo(new int[]{ 6, 6, 6}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{ 2, 2, 2}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{ 6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{ 6, 6, 6}));
    }

}