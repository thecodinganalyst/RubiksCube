import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RubikCubeTest {

    @Test
    public void RubikCube_size2() {
        RubikCube cube = new RubikCube(2);
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 1}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 1}));
        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2}));
        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 3}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 3}));
        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4}));
        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5}));
        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6}));
    }

    @Test
    public void RubikCube_TurnMidColUp() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColUp(1);
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 6, 1}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 6, 1}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{1, 6, 1}));

        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 5, 3}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 5, 3}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{3, 5, 3}));

        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 1, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 1, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 1, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 3, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 3, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 3, 6}));
    }

    @Test
    public void RubikCube_TurnLeftColUp() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColUp(0);
        assertThat(cube.main.getRow(0), equalTo(new int[]{6, 1, 1}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{6, 1, 1}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{6, 1, 1}));

        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 3, 5}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 3, 5}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{3, 3, 5}));

        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{1, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{1, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{1, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{3, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{3, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{3, 6, 6}));
    }

    @Test
    public void RubikCube_TurnRightColUp() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColUp(2);
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 1, 6}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 1, 6}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{1, 1, 6}));

        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(0), equalTo(new int[]{5, 3, 3}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{5, 3, 3}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{5, 3, 3}));

        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 1}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 1}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 1}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 3}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 3}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 3}));
    }

    @Test
    public void RubikCube_TurnTopRowToLeft() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToLeft(0);
        assertThat(cube.main.getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.right.getRow(0), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.left.getRow(0), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnBottomRowToLeft() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToLeft(2);
        assertThat(cube.main.getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.right.getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.left.getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnMiddleRowToLeft() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToLeft(1);
        assertThat(cube.main.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.right.getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.left.getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnTopRowToRight() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToRight(0);
        assertThat(cube.main.getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.right.getRow(0), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.left.getRow(0), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnBottomRowToRight() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToRight(2);
        assertThat(cube.main.getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.main.getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.right.getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.right.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.back.getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.left.getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.left.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnMiddleRowToRight() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToRight(1);
        assertThat(cube.main.getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.main.getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.main.getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.right.getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.right.getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.right.getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.back.getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.back.getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.back.getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.left.getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.left.getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.left.getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.top.getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.top.getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.bottom.getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.bottom.getRow(2), equalTo(new int[]{6, 6, 6}));
    }
}
