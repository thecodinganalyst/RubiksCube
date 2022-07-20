package rubikcube;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RubikCubeTest {

    @Test
    public void RubikCube_size2() {
        RubikCube cube = new RubikCube(2);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1}));
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2}));
        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3}));
        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4}));
        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5}));
        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6}));
        assertThat(cube.getAllActions().length, equalTo(12));
    }

    @Test
    public void RubikCube_TurnMidColUp() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColUp(1);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 6, 1}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 6, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 6, 1}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 5, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 5, 3}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 5, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 1, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 1, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 1, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 3, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 3, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 3, 6}));
    }

    @Test
    public void RubikCube_OppositeFace(){
        assertThat(RubikCube.getBackFaceOf(RubikCube.FACE.MAIN), is(RubikCube.FACE.BACK));
        assertThat(RubikCube.getBackFaceOf(RubikCube.FACE.BACK), is(RubikCube.FACE.MAIN));
        assertThat(RubikCube.getBackFaceOf(RubikCube.FACE.RIGHT), is(RubikCube.FACE.LEFT));
        assertThat(RubikCube.getBackFaceOf(RubikCube.FACE.LEFT), is(RubikCube.FACE.RIGHT));
        assertThat(RubikCube.getBackFaceOf(RubikCube.FACE.TOP), is(RubikCube.FACE.BOTTOM));
        assertThat(RubikCube.getBackFaceOf(RubikCube.FACE.BOTTOM), is(RubikCube.FACE.TOP));
    }

    @Test
    public void RubikCube_IsComplete() throws Exception {
        RubikCube cube = new RubikCube(3);
        assertThat(cube.isComplete(), equalTo(true));

        cube.turnColUp(0);
        assertThat(cube.isComplete(), equalTo(false));

        cube.turnColDown(0);
        assertThat(cube.isComplete(), equalTo(true));
    }

    @Test
    public void RubikCube_GetFace(){
        RubikCube cube = new RubikCube(3);

        RubikSide side = cube.getFace(RubikCube.FACE.MAIN);
        assertThat(side.getRow(0), equalTo(new int[]{1, 1, 1}));

        side = cube.getFace(RubikCube.FACE.RIGHT);
        assertThat(side.getRow(0), equalTo(new int[]{2, 2, 2}));

        side = cube.getFace(RubikCube.FACE.BACK);
        assertThat(side.getRow(0), equalTo(new int[]{3, 3, 3}));

        side = cube.getFace(RubikCube.FACE.LEFT);
        assertThat(side.getRow(0), equalTo(new int[]{4, 4, 4}));

        side = cube.getFace(RubikCube.FACE.TOP);
        assertThat(side.getRow(0), equalTo(new int[]{5, 5, 5}));

        side = cube.getFace(RubikCube.FACE.BOTTOM);
        assertThat(side.getRow(0), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_Check(){
        RubikCube cube = new RubikCube(3);
        assertThat(cube.check(), equalTo(true));
    }

    @Test
    public void RubikCube_FaceRight() throws Exception{
        RubikCube cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));

        cube.face(RubikCube.FACE.RIGHT);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{7, 4, 1}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{8, 5, 2}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{9, 6, 3}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{3, 6, 9}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{2, 5, 8}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{1, 4, 7}));
    }

    @Test
    public void RubikCube_FaceBack() throws Exception{
        RubikCube cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));

        cube.face(RubikCube.FACE.BACK);
        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{9, 8, 7}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{6, 5, 4}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{3, 2, 1}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{9, 8, 7}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 5, 4}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{3, 2, 1}));
    }

    @Test
    public void RubikCube_FaceLeft() throws Exception{
        RubikCube cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));

        cube.face(RubikCube.FACE.LEFT);
        assertThat(cube.getBack().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{3, 6, 9}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{2, 5, 8}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{1, 4, 7}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{7, 4, 1}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{8, 5, 2}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{9, 6, 3}));
    }

    @Test
    public void RubikCube_FaceTop() throws Exception{
        RubikCube cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
        cube.face(RubikCube.FACE.TOP);
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{3, 6, 9}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 5, 8}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{1, 4, 7}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{9, 8, 7}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{6, 5, 4}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{3, 2, 1}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{7, 4, 1}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{8, 5, 2}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{9, 6, 3}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{9, 8, 7}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{6, 5, 4}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 2, 1}));
    }

    @Test
    public void RubikCube_FaceBottom() throws Exception{
        RubikCube cube = new RubikCube(3,
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}),
                RubikSide.create(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
        cube.face(RubikCube.FACE.BOTTOM);
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{7, 4, 1}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{8, 5, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{9, 6, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{3, 6, 9}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{2, 5, 8}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{1, 4, 7}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{9, 8, 7}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 5, 4}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{3, 2, 1}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{7, 8, 9}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{9, 8, 7}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{6, 5, 4}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 2, 1}));

        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 2, 3}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{7, 8, 9}));
    }

    @Test
    public void RubikCube_TurnLeftColUp() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColUp(0);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{6, 1, 1}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{6, 1, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{6, 1, 1}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3, 5}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3, 5}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 3, 5}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{1, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{1, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{1, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{3, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{3, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{3, 6, 6}));
    }

    @Test
    public void RubikCube_TurnRightColUp() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColUp(2);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1, 6}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1, 6}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 1, 6}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{5, 3, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{5, 3, 3}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{5, 3, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 1}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 1}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 1}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 3}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 3}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 3}));
    }

    @Test
    public void RubikCube_TurnMidColDown() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColDown(1);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 5, 1}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 5, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 5, 1}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 6, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 6, 3}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 6, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 3, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 3, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 3, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 1, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 1, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 1, 6}));
    }

    @Test
    public void RubikCube_TurnLeftColDown() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColDown(0);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{5, 1, 1}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{5, 1, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{5, 1, 1}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3, 6}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3, 6}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 3, 6}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{3, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{3, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{3, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{1, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{1, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{1, 6, 6}));
    }

    @Test
    public void RubikCube_TurnRightColDown() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnColDown(2);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1, 5}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1, 5}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 1, 5}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{6, 3, 3}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{6, 3, 3}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{6, 3, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 3}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 3}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 3}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 1}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 1}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 1}));
    }

    @Test
    public void RubikCube_TurnTopRowToLeft() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToLeft(0);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnBottomRowToLeft() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToLeft(2);
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.getRight().getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnMiddleRowToLeft() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToLeft(1);
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.getRight().getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnTopRowToRight() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToRight(0);
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.getRight().getRow(0), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnBottomRowToRight() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToRight(2);
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.getRight().getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getRight().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getBack().getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    public void RubikCube_TurnMiddleRowToRight() throws Exception{
        RubikCube cube = new RubikCube(3);
        cube.turnRowToRight(1);
        assertThat(cube.getMain().getRow(1), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getMain().getRow(2), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getMain().getRow(0), equalTo(new int[]{1, 1, 1}));

        assertThat(cube.getRight().getRow(1), equalTo(new int[]{1, 1, 1}));
        assertThat(cube.getRight().getRow(2), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getRight().getRow(0), equalTo(new int[]{2, 2, 2}));

        assertThat(cube.getBack().getRow(1), equalTo(new int[]{2, 2, 2}));
        assertThat(cube.getBack().getRow(2), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getBack().getRow(0), equalTo(new int[]{3, 3, 3}));

        assertThat(cube.getLeft().getRow(1), equalTo(new int[]{3, 3, 3}));
        assertThat(cube.getLeft().getRow(2), equalTo(new int[]{4, 4, 4}));
        assertThat(cube.getLeft().getRow(0), equalTo(new int[]{4, 4, 4}));

        assertThat(cube.getTop().getRow(0), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(1), equalTo(new int[]{5, 5, 5}));
        assertThat(cube.getTop().getRow(2), equalTo(new int[]{5, 5, 5}));

        assertThat(cube.getBottom().getRow(0), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(1), equalTo(new int[]{6, 6, 6}));
        assertThat(cube.getBottom().getRow(2), equalTo(new int[]{6, 6, 6}));
    }

    @Test
    void testClone() throws CloneNotSupportedException {
        RubikCube cube = new RubikCube(3);

        RubikCube clone1 = cube.clone();
        cube.randomize();

        assertThat(clone1.getMain().getRow(0), equalTo(new int[]{ 1, 1, 1}));
        assertThat(clone1.getRight().getRow(1), equalTo(new int[]{ 2, 2, 2}));
        assertThat(clone1.getBack().getRow(2), equalTo(new int[]{ 3, 3, 3}));
        assertThat(clone1.getLeft().getCol(0), equalTo(new int[]{ 4, 4, 4}));
        assertThat(clone1.getTop().getCol(1), equalTo(new int[]{ 5, 5, 5}));
        assertThat(clone1.getBottom().getCol(2), equalTo(new int[]{ 6, 6, 6}));
    }

}
