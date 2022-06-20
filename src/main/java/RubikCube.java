import java.util.function.Consumer;

public class RubikCube{
    public RubikSide main;
    public RubikSide right;
    public RubikSide left;
    public RubikSide back;
    public RubikSide top;
    public RubikSide bottom;
    public int size;

    public Consumer<Integer> actions;

    public RubikCube(int size){
        this.size = size;
        main = new RubikSide(size, 1);
        right = new RubikSide(size, 2);
        back = new RubikSide(size, 3);
        left = new RubikSide(size, 4);
        top = new RubikSide(size, 5);
        bottom = new RubikSide(size, 6);
    }

    public void turnColUp(int col) throws Exception{
        int[] mainCol = main.getCol(col);
        RubikSide reversedBack = back.cloneReversed();
        main.setCol(col, bottom.getCol(col));
        bottom.setCol(col, reversedBack.getCol(col));
        reversedBack.setCol(col, top.getCol(col));
        top.setCol(col, mainCol);
        back = reversedBack.cloneReversed();
        if(col == 0){
            left.rotateAntiClockwise();
        }else if(col == (size - 1)){
            right.rotateClockwise();
        }
    }

    public void turnRowToRight(int row) throws Exception{
        int[] mainTopRow = main.getRow(row);
        main.setRow(row, left.getRow(row));
        left.setRow(row, back.getRow(row));
        back.setRow(row, right.getRow(row));
        right.setRow(row, mainTopRow);
        if(row == 0){
            top.rotateAntiClockwise();
        }else if(row == (size - 1)){
            bottom.rotateClockwise();
        }
    }

    public void turnRowToLeft(int row) throws Exception{
        int[] mainTopRow = main.getRow(row);
        main.setRow(row, right.getRow(row));
        right.setRow(row, back.getRow(row));
        back.setRow(row, left.getRow(row));
        left.setRow(row, mainTopRow);
        if(row == 0){
            top.rotateClockwise();
        }else if(row == (size - 1)){
            bottom.rotateAntiClockwise();
        }
    }
}
