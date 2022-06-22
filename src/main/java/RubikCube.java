import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RubikCube{
    public RubikSide main;
    public RubikSide right;
    public RubikSide left;
    public RubikSide back;
    public RubikSide top;
    public RubikSide bottom;
    public int size;

    public enum FACE {
        MAIN, RIGHT, BACK, LEFT, TOP, BOTTOM
    }

    public enum ACTION {
        FACE_RIGHT, FACE_BACK, FACE_LEFT, FACE_TOP, FACE_BOTTOM,
        TURN_TOP_ROW_RIGHT, TURN_TOP_ROW_LEFT,
        TURN_MIDDLE_ROW_RIGHT, TURN_MIDDLE_ROW_LEFT,
        TURN_BOTTOM_ROW_RIGHT, TURN_BOTTOM_ROW_LEFT,
        TURN_LEFT_COL_UP, TURN_LEFT_COL_DOWN,
        TURN_MIDDLE_COL_UP, TURN_MIDDLE_COL_DOWN,
        TURN_RIGHT_COL_UP, TURN_RIGHT_COL_DOWN
    }

    public void doAction(ACTION action) {
        try{
            switch (action){
                case FACE_TOP -> face(FACE.TOP);
                case FACE_BACK -> face(FACE.BACK);
                case FACE_BOTTOM -> face(FACE.BOTTOM);
                case FACE_RIGHT -> face(FACE.RIGHT);
                case FACE_LEFT -> face(FACE.LEFT);
                case TURN_TOP_ROW_LEFT -> turnRowToLeft(0);
                case TURN_TOP_ROW_RIGHT -> turnRowToRight(0);
                case TURN_MIDDLE_ROW_LEFT -> turnRowToLeft(1);
                case TURN_MIDDLE_ROW_RIGHT -> turnRowToRight(1);
                case TURN_BOTTOM_ROW_LEFT -> turnRowToLeft(2);
                case TURN_BOTTOM_ROW_RIGHT -> turnRowToRight(2);
                case TURN_LEFT_COL_UP -> turnColUp(0);
                case TURN_LEFT_COL_DOWN -> turnColDown(0);
                case TURN_MIDDLE_COL_UP -> turnColUp(1);
                case TURN_MIDDLE_COL_DOWN -> turnColDown(1);
                case TURN_RIGHT_COL_UP -> turnColUp(2);
                case TURN_RIGHT_COL_DOWN -> turnColDown(2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public RubikCube(int size){
        this.size = size;
        main = new RubikSide(size, 1);
        right = new RubikSide(size, 2);
        back = new RubikSide(size, 3);
        left = new RubikSide(size, 4);
        top = new RubikSide(size, 5);
        bottom = new RubikSide(size, 6);
    }

    public RubikSide getFace(FACE face){
        if(face == FACE.MAIN) return main;
        if(face == FACE.BACK) return back;
        if(face == FACE.LEFT) return left;
        if(face == FACE.RIGHT) return right;
        if(face == FACE.TOP) return top;
        return bottom;
    }

    public boolean check(){
        List<Integer> valueList = main.getValueList();
        valueList.addAll(right.getValueList());
        valueList.addAll(back.getValueList());
        valueList.addAll(left.getValueList());
        valueList.addAll(top.getValueList());
        valueList.addAll(bottom.getValueList());
        Map<Integer, List<Integer>> result = valueList.stream().collect(Collectors.groupingBy(i -> i));
        if(result.keySet().size() != 6) return false;
        int expectedCount = size * size;
        if(result.get(1).size() != expectedCount) return false;
        if(result.get(2).size() != expectedCount) return false;
        if(result.get(3).size() != expectedCount) return false;
        if(result.get(4).size() != expectedCount) return false;
        if(result.get(5).size() != expectedCount) return false;
        return result.get(6).size() == expectedCount;
    }

    public void print(){
        String[] box = join(RubikSide.getEmptyString(size), top.getString());
        Arrays.stream(box).forEach(System.out::println);

        box = join(left.getString(), main.getString(), right.getString(), back.getString());
        Arrays.stream(box).forEach(System.out::println);

        box = join(RubikSide.getEmptyString(size), bottom.getString());
        Arrays.stream(box).forEach(System.out::println);

        System.out.println(" ");
    }

    public static FACE getBackFaceOf(FACE face){
        if(face == FACE.MAIN) return FACE.BACK;
        if(face == FACE.RIGHT) return FACE.LEFT;
        if(face == FACE.BACK) return FACE.MAIN;
        if(face == FACE.LEFT) return FACE.RIGHT;
        if(face == FACE.TOP) return FACE.BOTTOM;
        return FACE.TOP;
    }

    public static FACE getRightFaceOf(FACE face){
        if(face == FACE.MAIN) return FACE.RIGHT;
        if(face == FACE.RIGHT) return FACE.BACK;
        if(face == FACE.BACK) return FACE.LEFT;
        if(face == FACE.LEFT) return FACE.MAIN;
        return FACE.RIGHT;
    }

    public static FACE getLeftFaceOf(FACE face){
        if(face == FACE.MAIN) return FACE.LEFT;
        if(face == FACE.RIGHT) return FACE.MAIN;
        if(face == FACE.BACK) return FACE.RIGHT;
        if(face == FACE.LEFT) return FACE.BACK;
        return FACE.LEFT;
    }

    public static FACE getTopFaceOf(FACE face){
        if(face == FACE.TOP) return FACE.BACK;
        if(face == FACE.BOTTOM) return FACE.MAIN;
        return FACE.TOP;
    }

    public static FACE getBottomFaceOf(FACE face){
        if(face == FACE.TOP) return FACE.MAIN;
        if(face == FACE.BOTTOM) return FACE.BACK;
        return FACE.BOTTOM;
    }

    public void face(FACE newFace){
        Map<FACE, RubikSide> old = Map.of(
                FACE.MAIN, main.clone(),
                FACE.RIGHT, right.clone(),
                FACE.BACK, back.clone(),
                FACE.LEFT, left.clone(),
                FACE.TOP, top.clone(),
                FACE.BOTTOM, bottom.clone()
        );
        main = getFace(newFace);
        right = old.get(getRightFaceOf(newFace));
        back = old.get(getBackFaceOf(newFace));
        left = old.get(getLeftFaceOf(newFace));
        top = old.get(getTopFaceOf(newFace));
        bottom = old.get(getBottomFaceOf(newFace));
    }

    private String[] join(String[]... sides){
        BinaryOperator<String[]> reducer = (a, b) ->
                IntStream.range(0, a.length)
                            .boxed()
                            .map(i -> a[i].concat(" ").concat(b[i]))
                            .toArray(String[]::new);
        return Arrays.stream(sides).reduce(reducer).get();
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

    public void turnColDown(int col) throws Exception{
        int[] mainCol = main.getCol(col);
        RubikSide reversedBack = back.cloneReversed();
        main.setCol(col, top.getCol(col));
        top.setCol(col, reversedBack.getCol(col));
        reversedBack.setCol(col, bottom.getCol(col));
        bottom.setCol(col, mainCol);
        back = reversedBack.cloneReversed();
        if(col == 0){
            left.rotateClockwise();
        }else if(col == (size - 1)){
            right.rotateAntiClockwise();
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
