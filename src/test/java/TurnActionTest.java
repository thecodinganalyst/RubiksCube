import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TurnActionTest {

    @Test
    void allActions_for2dArrayOk() {
        TurnAction[] turnActions = TurnAction.allActions(2);

        Set<String> used = Stream.of(
                        "TURN_ROW_0_LEFT", "TURN_ROW_0_RIGHT", "TURN_COL_0_UP", "TURN_COL_0_DOWN",
                        "TURN_ROW_1_LEFT", "TURN_ROW_1_RIGHT", "TURN_COL_1_UP", "TURN_COL_1_DOWN"
                )
                .collect(Collectors.toSet());
        Arrays.stream(turnActions).forEach(turnAction -> {
            String turnActionName = turnAction.getName();
            assertThat(used, hasItem(turnActionName));
            used.remove(turnActionName);
        });
        assertThat(used.size(), equalTo(0));
    }

    @Test
    void allActions_for3dArrayOk() {
        TurnAction[] turnActions = TurnAction.allActions(3);

        Set<String> used = Stream.of(
                "TURN_ROW_0_LEFT", "TURN_ROW_0_RIGHT", "TURN_COL_0_UP", "TURN_COL_0_DOWN",
                        "TURN_ROW_1_LEFT", "TURN_ROW_1_RIGHT", "TURN_COL_1_UP", "TURN_COL_1_DOWN",
                        "TURN_ROW_2_LEFT", "TURN_ROW_2_RIGHT", "TURN_COL_2_UP", "TURN_COL_2_DOWN"
                )
                .collect(Collectors.toSet());
        Arrays.stream(turnActions).forEach(turnAction -> {
            String turnActionName = turnAction.getName();
            assertThat(used, hasItem(turnActionName));
            used.remove(turnActionName);
        });
        assertThat(used.size(), equalTo(0));
    }

    @Test
    void allActions_for4dArrayOk() {
        TurnAction[] turnActions = TurnAction.allActions(4);

        Set<String> used = Stream.of(
                        "TURN_ROW_0_LEFT", "TURN_ROW_0_RIGHT", "TURN_COL_0_UP", "TURN_COL_0_DOWN",
                        "TURN_ROW_1_LEFT", "TURN_ROW_1_RIGHT", "TURN_COL_1_UP", "TURN_COL_1_DOWN",
                        "TURN_ROW_2_LEFT", "TURN_ROW_2_RIGHT", "TURN_COL_2_UP", "TURN_COL_2_DOWN",
                        "TURN_ROW_3_LEFT", "TURN_ROW_3_RIGHT", "TURN_COL_3_UP", "TURN_COL_3_DOWN"
                )
                .collect(Collectors.toSet());
        Arrays.stream(turnActions).forEach(turnAction -> {
            String turnActionName = turnAction.getName();
            assertThat(used, hasItem(turnActionName));
            used.remove(turnActionName);
        });
        assertThat(used.size(), equalTo(0));
    }
}