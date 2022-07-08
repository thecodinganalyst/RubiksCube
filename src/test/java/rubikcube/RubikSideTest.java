package rubikcube;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RubikSideTest {

    @Test
    public void Create2dRubikSide(){
        RubikSide rubikSide = new RubikSide(2, 1);
        int[] expected = {1, 1};
        assertThat(rubikSide.getRow(0), equalTo(expected));
        assertThat(rubikSide.getRow(1), equalTo(expected));
    }

    @Test
    public void Create3dRubikSide(){
        RubikSide rubikSide = new RubikSide(3, 2);
        int[] expected = {2, 2, 2};
        assertThat(rubikSide.getRow(0), equalTo(expected));
        assertThat(rubikSide.getRow(1), equalTo(expected));
        assertThat(rubikSide.getRow(2), equalTo(expected));
        assertThat(rubikSide.getCol(0), equalTo(expected));
        assertThat(rubikSide.getCol(1), equalTo(expected));
        assertThat(rubikSide.getCol(2), equalTo(expected));
    }

    @Test
    public void Create() throws Exception{
        int[][] values = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RubikSide rubikSide = RubikSide.create(values);
        assertThat(rubikSide.getCol(0), equalTo(new int[]{1, 4, 7}));
        assertThat(rubikSide.getCol(1), equalTo(new int[]{2, 5, 8}));
        assertThat(rubikSide.getCol(2), equalTo(new int[]{3, 6, 9}));
    }

    @Test
    public void Create_NotSquare(){
        int[][] values = {{1, 2, 3}, {4, 5, 6}, {7, 8}};
        assertThrows(Exception.class, () -> RubikSide.create(values));
    }

    @Test
    public void RubikSide_SetRows() throws Exception{
        RubikSide rubikSide = new RubikSide(3, 1);
        int[] row0 = {1, 2, 3};
        int[] row1 = {4, 5, 6};
        int[] row2 = {7, 8, 9};
        rubikSide.setRow(0, row0);
        rubikSide.setRow(1, row1);
        rubikSide.setRow(2, row2);

        assertThat(rubikSide.getRow(0), equalTo(row0));
        assertThat(rubikSide.getRow(1), equalTo(row1));
        assertThat(rubikSide.getRow(2), equalTo(row2));

        int[] col0 = {1, 4, 7};
        int[] col1 = {2, 5, 8};
        int[] col2 = {3, 6, 9};

        assertThat(rubikSide.getCol(0), equalTo(col0));
        assertThat(rubikSide.getCol(1), equalTo(col1));
        assertThat(rubikSide.getCol(2), equalTo(col2));
    }

    @Test
    public void RubikSide_Clone() throws Exception{
        RubikSide side = new RubikSide(3, 1);
        RubikSide clone = side.clone();
        side.setRow(0, new int[]{ 2, 2, 2 });
        assertThat(side.getRow(0), equalTo(new int[]{2, 2, 2}));
        assertThat(clone.getRow(0), equalTo(new int[]{1, 1, 1}));
    }

    @Test
    public void RubikSide_SetCols() throws Exception{
        RubikSide rubikSide = new RubikSide(3, 1);
        int[] col0 = {1, 2, 3};
        int[] col1 = {4, 5, 6};
        int[] col2 = {7, 8, 9};
        rubikSide.setCol(0, col0);
        rubikSide.setCol(1, col1);
        rubikSide.setCol(2, col2);

        assertThat(rubikSide.getCol(0), equalTo(col0));
        assertThat(rubikSide.getCol(1), equalTo(col1));
        assertThat(rubikSide.getCol(2), equalTo(col2));

        int[] row0 = {1, 4, 7};
        int[] row1 = {2, 5, 8};
        int[] row2 = {3, 6, 9};

        assertThat(rubikSide.getRow(0), equalTo(row0));
        assertThat(rubikSide.getRow(1), equalTo(row1));
        assertThat(rubikSide.getRow(2), equalTo(row2));
    }

    @Test
    public void RubikSide_SetRowRange() throws Exception{
        RubikSide rubikSide = new RubikSide(4, 1);
        rubikSide.setRowRange(1, 1, 3, new int[]{2, 2});
        assertThat(rubikSide.getRow(1), equalTo(new int[]{1, 2, 2, 1}));
        assertThat(rubikSide.getRowRange(1, 1, 3), equalTo(new int[]{2, 2}));
        assertThat(rubikSide.getRow(0), equalTo(new int[]{1, 1, 1, 1}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{1, 1, 1, 1}));
        assertThat(rubikSide.getRow(3), equalTo(new int[]{1, 1, 1, 1}));
    }

    @Test
    public void RubikSide_SetRowRangeFull() throws Exception{
        RubikSide rubikSide = new RubikSide(3, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3});
        rubikSide.setRow(1, new int[]{4, 5, 6});
        rubikSide.setRow(2, new int[]{7, 8, 9});

        rubikSide.setRowRange(0, 0, 3, new int[]{10, 11, 12});
        assertThat(rubikSide.getRow(0), equalTo(new int[]{10, 11, 12}));
        assertThat(rubikSide.getRowRange(0, 0, 3), equalTo(new int[]{10, 11, 12}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{4, 5, 6}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{7, 8, 9}));
    }

    @Test
    public void RubikSide_SetColRangeFull() throws Exception{
        RubikSide rubikSide = new RubikSide(3, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3});
        rubikSide.setRow(1, new int[]{4, 5, 6});
        rubikSide.setRow(2, new int[]{7, 8, 9});

        rubikSide.setColRange(0, 0, 3, new int[]{10, 11, 12});
        assertThat(rubikSide.getCol(0), equalTo(new int[]{10, 11, 12}));
        assertThat(rubikSide.getColRange(0, 0, 3), equalTo(new int[]{10, 11, 12}));
        assertThat(rubikSide.getCol(1), equalTo(new int[]{2, 5, 8}));
        assertThat(rubikSide.getCol(2), equalTo(new int[]{3, 6, 9}));
    }

    @Test
    public void RubikSide_SetColRange() throws Exception{
        RubikSide rubikSide = new RubikSide(5, 1);
        rubikSide.setRowRange(2, 1, 4, new int[]{2, 2, 2});
        assertThat(rubikSide.getRow(2), equalTo(new int[]{1, 2, 2, 2, 1}));
        assertThat(rubikSide.getRowRange(2, 1, 4), equalTo(new int[]{2, 2, 2}));
        assertThat(rubikSide.getRow(0), equalTo(new int[]{1, 1, 1, 1, 1}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{1, 1, 1, 1, 1}));
        assertThat(rubikSide.getRow(3), equalTo(new int[]{1, 1, 1, 1, 1}));
        assertThat(rubikSide.getRow(4), equalTo(new int[]{1, 1, 1, 1, 1}));
    }

    @Test
    public void RubikSide_RotateClockwise_size3() throws Exception{
        RubikSide rubikSide = new RubikSide(3, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3});
        rubikSide.setRow(1, new int[]{4, 5, 6});
        rubikSide.setRow(2, new int[]{7, 8, 9});

        rubikSide.rotateClockwise();

        assertThat(rubikSide.getRow(0), equalTo(new int[]{ 7, 4, 1}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{ 8, 5, 2}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{ 9, 6, 3}));
    }

    @Test
    public void RubikSide_RotateClockwise_size4() throws Exception{
        RubikSide rubikSide = new RubikSide(4, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3, 4});
        rubikSide.setRow(1, new int[]{5, 6, 7, 8});
        rubikSide.setRow(2, new int[]{9, 10, 11, 12});
        rubikSide.setRow(3, new int[]{13, 14, 15, 16});

        rubikSide.rotateClockwise();

        assertThat(rubikSide.getRow(0), equalTo(new int[]{ 13, 9, 5, 1}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{ 14, 10, 6, 2}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{ 15, 11, 7, 3}));
        assertThat(rubikSide.getRow(3), equalTo(new int[]{ 16, 12, 8, 4}));
    }

    @Test
    public void RubikSide_RotateClockwise_size5() throws Exception{
        RubikSide rubikSide = new RubikSide(5, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3, 4, 5});
        rubikSide.setRow(1, new int[]{6, 7, 8, 9, 10});
        rubikSide.setRow(2, new int[]{11, 12, 13, 14, 15});
        rubikSide.setRow(3, new int[]{16, 17, 18, 19, 20});
        rubikSide.setRow(4, new int[]{21, 22, 23, 24, 25});

        rubikSide.rotateClockwise();

        assertThat(rubikSide.getRow(0), equalTo(new int[]{ 21, 16, 11, 6, 1}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{ 22, 17, 12, 7, 2}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{ 23, 18, 13, 8, 3}));
        assertThat(rubikSide.getRow(3), equalTo(new int[]{ 24, 19, 14, 9, 4}));
        assertThat(rubikSide.getRow(4), equalTo(new int[]{ 25, 20, 15, 10, 5}));
    }

    @Test
    public void RubikSide_RotateAntiClockwise_size3() throws Exception{
        RubikSide rubikSide = new RubikSide(3, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3});
        rubikSide.setRow(1, new int[]{4, 5, 6});
        rubikSide.setRow(2, new int[]{7, 8, 9});

        rubikSide.rotateAntiClockwise();

        assertThat(rubikSide.getRow(0), equalTo(new int[]{ 3, 6, 9}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{ 2, 5, 8}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{ 1, 4, 7}));
    }

    @Test
    public void RubikSide_RotateAntiClockwise_size4() throws Exception{
        RubikSide rubikSide = new RubikSide(4, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3, 4});
        rubikSide.setRow(1, new int[]{5, 6, 7, 8});
        rubikSide.setRow(2, new int[]{9, 10, 11, 12});
        rubikSide.setRow(3, new int[]{13, 14, 15, 16});

        rubikSide.rotateAntiClockwise();

        assertThat(rubikSide.getRow(0), equalTo(new int[]{ 4, 8, 12, 16}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{ 3, 7, 11, 15}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{ 2, 6, 10, 14}));
        assertThat(rubikSide.getRow(3), equalTo(new int[]{ 1, 5, 9, 13}));
    }

    @Test
    public void RubikSide_RotateAntiClockwise_size5() throws Exception{
        RubikSide rubikSide = new RubikSide(5, 1);
        rubikSide.setRow(0, new int[]{1, 2, 3, 4, 5});
        rubikSide.setRow(1, new int[]{6, 7, 8, 9, 10});
        rubikSide.setRow(2, new int[]{11, 12, 13, 14, 15});
        rubikSide.setRow(3, new int[]{16, 17, 18, 19, 20});
        rubikSide.setRow(4, new int[]{21, 22, 23, 24, 25});

        rubikSide.rotateAntiClockwise();

        assertThat(rubikSide.getRow(0), equalTo(new int[]{ 5, 10, 15, 20, 25}));
        assertThat(rubikSide.getRow(1), equalTo(new int[]{ 4, 9, 14, 19, 24}));
        assertThat(rubikSide.getRow(2), equalTo(new int[]{ 3, 8, 13, 18, 23}));
        assertThat(rubikSide.getRow(3), equalTo(new int[]{ 2, 7, 12, 17, 22}));
        assertThat(rubikSide.getRow(4), equalTo(new int[]{ 1, 6, 11, 16, 21}));
    }

    @Test
    public void RubikSide_IsComplete() throws Exception{
        RubikSide rubikSide = new RubikSide(4, 1);
        assertThat(rubikSide.isComplete(), is(true));

        RubikSide rubikSide2 = new RubikSide(3, 1);
        rubikSide2.setRow(0, new int[]{1, 1, 3});
        assertThat(rubikSide2.isComplete(), is(false));
    }
}