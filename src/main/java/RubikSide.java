import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public class RubikSide{
    private int size;
    private int[][] values;

    public RubikSide(int size, int value){
        this.size = size;
        int[] dimension = IntStream.generate(() -> value).limit(size).toArray();
        values = IntStream.range(0, size)
                .boxed()
                .map(i -> dimension.clone())
                .toArray(i -> new int[i][]);
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

    public void rotateClockwise() throws Exception{
         values = IntStream.range(0, size)
                                    .boxed()
                                    .map(i -> Utils.reverseArray(getCol(i)))
                                    .toArray(int[][]::new);
    }

    public void rotateAntiClockwise() throws Exception{
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
        IntStream.range(0, size).forEach(i -> System.out.println(Arrays.toString(values[i])));
    }

}