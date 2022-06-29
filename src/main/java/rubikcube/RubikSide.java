package rubikcube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class RubikSide implements Cloneable{
    private final int size;
    private int[][] values;

    public RubikSide(int size, int value){
        this.size = size;
        int[] dimension = IntStream.generate(() -> value).limit(size).toArray();
        values = IntStream.range(0, size)
                .boxed()
                .map(i -> dimension.clone())
                .toArray(int[][]::new);
    }

    public static RubikSide create(int[][] values) throws Exception{
        int rowCount = values.length;
        for(int[] col: values){
            if(col.length != rowCount){
                throw new Exception("rubikcube.RubikSide: values must be a square");
            }
        }
        return new RubikSide(values);
    }

    private RubikSide(int[][] values){
        size = values.length;
        this.values = values;
    }

    @Override
    public RubikSide clone() throws CloneNotSupportedException {
        super.clone();
        int[][] newValues = IntStream.range(0, size).boxed()
                .map(i -> Arrays.copyOf(values[i], values[i].length))
                .toArray(int[][]::new);
        return new RubikSide(newValues);
    }

    public List<Integer> getValueList(){
        List<Integer> answer = new ArrayList<>();

        for (int[] i: values){
            for(int j: i){
                answer.add(j);
            }
        }
        return answer;
    }

    public RubikSide cloneReversed() throws Exception {
        int[][] reversed = Utils.reverse2dArray(values);
        return RubikSide.create(reversed);
    }

    public int getSize(){
        return size;
    }

    public int[] getRow(int row){
        return values[row];
    }

    public int[] getCol(int col){
        return IntStream.range(0, size)
                .map(i -> values[i][col])
                .toArray();
    }

    public void setRow(int row, int[] newValues) throws Exception{
        if(newValues.length != size) throw new Exception("setRow: size of newValues is invalid");
        if(row < 0 || row >= size) throw new Exception("setRow: invalid row - " + row);
        values[row] = newValues;
    }

    public void setCol(int col, int[] newValues) throws Exception{
        if(newValues.length != size) throw new Exception("setCol: size of newValues is invalid");
        if(col < 0 || col >= size) throw new Exception("setCol: invalid col - " + col);
        IntStream.range(0, size).forEach(i -> values[i][col] = newValues[i]);
    }

    public int[] getRowRange(int row, int colStartInclusive, int colEndExclusive) throws Exception{
        if(colStartInclusive < 0 || colEndExclusive > size || row < 0 || row >= size)
            throw new Exception("getRowRange: invalid parameters - " + row + " - " + colStartInclusive + " - " + colEndExclusive);
        return Arrays.copyOfRange(values[row], colStartInclusive, colEndExclusive);
    }

    public int[] getColRange(int col, int rowStartInclusive, int rowEndExclusive) throws Exception{
        if(rowStartInclusive < 0 || rowEndExclusive > size || col < 0 || col >= size)
            throw new Exception("getColRange: invalid parameters - " + col + " - " + rowStartInclusive + " - " + rowEndExclusive);
        return Arrays.copyOfRange(getCol(col), rowStartInclusive, rowEndExclusive);
    }

    public void setRowRange(int row, int colStartInclusive, int colEndExclusive, int[] newValues) throws Exception{
        if(colStartInclusive < 0 || colEndExclusive > size || row < 0 || row > size)
            throw new Exception("setRowRange: invalid parameters - " + row + " - " + colStartInclusive + " - " + colEndExclusive);
        if(newValues.length > (colEndExclusive - colStartInclusive)) throw new Exception("setRowRange: invalid newValues size");

        for(int i = colStartInclusive, j = 0; i < colEndExclusive; i ++, j++){
            values[row][i] = newValues[j];
        }
    }

    public void setColRange(int col, int rowStartInclusive, int rowEndExclusive, int[] newValues) throws Exception{
        if(rowStartInclusive < 0 || rowEndExclusive > size || col < 0 || col > size)
            throw new Exception("setColRange: invalid parameters - " + col + " - " + rowStartInclusive + " - " + rowEndExclusive);
        if(newValues.length > (rowEndExclusive - rowStartInclusive)) throw new Exception("setColRange: invalid newValues size");

        for(int i = rowStartInclusive, j = 0; i < rowEndExclusive; i++, j++){
            values[i][col] = newValues[j];
        }
    }

    public void rotateClockwise(){
         values = IntStream.range(0, size)
                                    .boxed()
                                    .map(i -> Utils.reverseArray(getCol(i)))
                                    .toArray(int[][]::new);
    }

    public void rotateAntiClockwise(){
        values = IntStream.rangeClosed(1, size)
                            .boxed()
                            .map(i -> getCol(size - i))
                            .toArray(int[][]::new);
    }

    public boolean isComplete(){
        int sample = values[0][0];
        return Arrays.stream(values).flatMapToInt(IntStream::of).allMatch(i -> i == sample);
    }

    public void print(){
        IntStream.range(0, size)
                    .forEach(i ->
                            System.out.println(Arrays.toString(values[i]))
                    );
    }

    public static String[] getEmptyString(int size){
        return IntStream.range(0, size).boxed().map(i -> "         ").toArray(String[]::new);
    }

    public String[] getString(){
        return IntStream.range(0, size)
                .boxed()
                .map(i -> Arrays.toString(values[i]))
                .toArray(String[]::new);
    }

}