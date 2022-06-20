import java.util.Arrays;
import java.util.stream.IntStream;

public class Utils {

    public static int[] reverseArray(int[] arr){
        return IntStream.rangeClosed(1, arr.length)
                        .map(i -> arr[arr.length - i])
                        .toArray();
    }

    public static int[][] reverse2dArray(int[][] arr){
        int[][] interim =  IntStream.range(0, arr.length)
                                    .boxed()
                                    .map(i -> reverseArray(arr[i]))
                                    .toArray(int[][]::new);

        return IntStream.rangeClosed(1, interim.length)
                        .boxed()
                        .map(i -> interim[interim.length - i])
                        .toArray(int[][]::new);
    }
}
