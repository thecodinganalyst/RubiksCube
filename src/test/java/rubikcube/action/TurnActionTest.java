package rubikcube.action;

import org.junit.jupiter.api.Test;
import rubikcube.action.TurnAction;

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

    @Test
    void oppositeAction() {
        TurnAction[] turnActions = TurnAction.allActions(3);
        Arrays.stream(turnActions).forEach(turnAction -> {
            TurnAction.TURN_TYPE turnType = turnAction.turnType;
            TurnAction.DIRECTION direction = turnAction.direction;
            int turnPosition = turnAction.turnPosition;
            TurnAction oppAction = (TurnAction) turnAction.oppositeAction();
            assertThat(oppAction.turnPosition, equalTo(turnPosition));
            assertThat(oppAction.turnType, equalTo(turnType));
            if(direction == TurnAction.DIRECTION.UP) assertThat(oppAction.direction, equalTo(TurnAction.DIRECTION.DOWN));
            if(direction == TurnAction.DIRECTION.DOWN) assertThat(oppAction.direction, equalTo(TurnAction.DIRECTION.UP));
            if(direction == TurnAction.DIRECTION.LEFT) assertThat(oppAction.direction, equalTo(TurnAction.DIRECTION.RIGHT));
            if(direction == TurnAction.DIRECTION.RIGHT) assertThat(oppAction.direction, equalTo(TurnAction.DIRECTION.LEFT));
        });
    }
}