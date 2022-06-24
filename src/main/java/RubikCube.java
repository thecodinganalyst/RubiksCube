import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RubikCube{
    private RubikSide main;
    private RubikSide right;
    private RubikSide left;
    private RubikSide back;
    private RubikSide top;
    private RubikSide bottom;
    private final int size;
    private final RubikCubeAction[] allActions;

    public RubikSide getMain() {
        return main;
    }

    public RubikSide getRight() {
        return right;
    }

    public RubikSide getLeft() {
        return left;
    }

    public RubikSide getBack() {
        return back;
    }

    public RubikSide getTop() {
        return top;
    }

    public RubikSide getBottom() {
        return bottom;
    }

    public int getSize() {
        return size;
    }

    public RubikCubeAction[] getAllActions() {
        return allActions;
    }

    public enum FACE {
        MAIN, RIGHT, BACK, LEFT, TOP, BOTTOM
    }

    public void performAction(RubikCubeAction action) {
        action.performAction(this);
    }

    public RubikCube(int size){
        this.size = size;
        main = new RubikSide(size, 1);
        right = new RubikSide(size, 2);
        back = new RubikSide(size, 3);
        left = new RubikSide(size, 4);
        top = new RubikSide(size, 5);
        bottom = new RubikSide(size, 6);
        allActions = Stream.concat(
                        Stream.of(FaceAction.allActions()),
                        Stream.of(TurnAction.allActions(size)))
                    .toArray(RubikCubeAction[]::new);
    }

    public RubikSide getFace(FACE face){
        if(face == FACE.MAIN) return getMain();
        if(face == FACE.BACK) return getBack();
        if(face == FACE.LEFT) return getLeft();
        if(face == FACE.RIGHT) return getRight();
        if(face == FACE.TOP) return getTop();
        return getBottom();
    }

    public boolean check(){
        List<Integer> valueList = getMain().getValueList();
        valueList.addAll(getRight().getValueList());
        valueList.addAll(getBack().getValueList());
        valueList.addAll(getLeft().getValueList());
        valueList.addAll(getTop().getValueList());
        valueList.addAll(getBottom().getValueList());
        Map<Integer, List<Integer>> result = valueList.stream().collect(Collectors.groupingBy(i -> i));
        if(result.keySet().size() != 6) return false;
        int expectedCount = getSize() * getSize();
        if(result.get(1).size() != expectedCount) return false;
        if(result.get(2).size() != expectedCount) return false;
        if(result.get(3).size() != expectedCount) return false;
        if(result.get(4).size() != expectedCount) return false;
        if(result.get(5).size() != expectedCount) return false;
        return result.get(6).size() == expectedCount;
    }

    public boolean isComplete(){
        return main.isComplete() &&
                right.isComplete() &&
                back.isComplete() &&
                left.isComplete() &&
                top.isComplete() &&
                bottom.isComplete();
    }

    public void print(){
        String[] box = join(RubikSide.getEmptyString(getSize()), getTop().getString());
        Arrays.stream(box).forEach(System.out::println);

        box = join(getLeft().getString(), getMain().getString(), getRight().getString(), getBack().getString());
        Arrays.stream(box).forEach(System.out::println);

        box = join(RubikSide.getEmptyString(getSize()), getBottom().getString());
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
                FACE.MAIN, getMain().clone(),
                FACE.RIGHT, getRight().clone(),
                FACE.BACK, getBack().clone(),
                FACE.LEFT, getLeft().clone(),
                FACE.TOP, getTop().clone(),
                FACE.BOTTOM, getBottom().clone()
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
        return Arrays.stream(sides).reduce(reducer).orElseThrow();
    }

    public void turnColUp(int col) throws Exception{
        int[] mainCol = getMain().getCol(col);
        RubikSide reversedBack = getBack().cloneReversed();
        getMain().setCol(col, getBottom().getCol(col));
        getBottom().setCol(col, reversedBack.getCol(col));
        reversedBack.setCol(col, getTop().getCol(col));
        getTop().setCol(col, mainCol);
        back = reversedBack.cloneReversed();
        if(col == 0){
            getLeft().rotateAntiClockwise();
        }else if(col == (getSize() - 1)){
            getRight().rotateClockwise();
        }
    }

    public void turnColDown(int col) throws Exception{
        int[] mainCol = getMain().getCol(col);
        RubikSide reversedBack = getBack().cloneReversed();
        getMain().setCol(col, getTop().getCol(col));
        getTop().setCol(col, reversedBack.getCol(col));
        reversedBack.setCol(col, getBottom().getCol(col));
        getBottom().setCol(col, mainCol);
        back = reversedBack.cloneReversed();
        if(col == 0){
            getLeft().rotateClockwise();
        }else if(col == (getSize() - 1)){
            getRight().rotateAntiClockwise();
        }
    }

    public void turnRowToRight(int row) throws Exception{
        int[] mainTopRow = getMain().getRow(row);
        getMain().setRow(row, getLeft().getRow(row));
        getLeft().setRow(row, getBack().getRow(row));
        getBack().setRow(row, getRight().getRow(row));
        getRight().setRow(row, mainTopRow);
        if(row == 0){
            getTop().rotateAntiClockwise();
        }else if(row == (getSize() - 1)){
            getBottom().rotateClockwise();
        }
    }

    public void turnRowToLeft(int row) throws Exception{
        int[] mainTopRow = getMain().getRow(row);
        getMain().setRow(row, getRight().getRow(row));
        getRight().setRow(row, getBack().getRow(row));
        getBack().setRow(row, getLeft().getRow(row));
        getLeft().setRow(row, mainTopRow);
        if(row == 0){
            getTop().rotateClockwise();
        }else if(row == (getSize() - 1)){
            getBottom().rotateAntiClockwise();
        }
    }
}
