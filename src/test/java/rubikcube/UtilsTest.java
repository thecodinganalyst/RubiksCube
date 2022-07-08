package rubikcube;

import org.junit.jupiter.api.Test;
import rubikcube.Utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UtilsTest {

    @Test
    void reverseArray() {
        int[] arr = {1, 2, 3, 4, 5};
        assertThat(Utils.reverseArray(arr), equalTo(new int[]{5, 4, 3, 2, 1}));
    }

    @Test
    void reverse2dArray(){
        int[][] arr = {{1, 2}, {3, 4}};
        int[][] expected = {{4, 3}, {2, 1}};
        assertThat(Utils.reverse2dArray(arr), equalTo(expected));
    }

    @Test
    void getRandom() {
        assertThat(Utils.getRandom(1, 3), lessThanOrEqualTo(3));
        assertThat(Utils.getRandom(1, 3), greaterThanOrEqualTo(1));

        assertThat(Utils.getRandom(3, 3), equalTo(3));
    }
}