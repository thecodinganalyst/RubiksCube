package rubikcube.action;

import org.junit.jupiter.api.Test;
import rubikcube.RubikCube;
import rubikcube.action.FaceAction;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FaceActionTest {

    FaceAction[] faceActions = FaceAction.allActions();

    @Test
    void allActions_containSpecificNames(){
        Set<String> used = Stream.of("FACE_RIGHT", "FACE_BACK", "FACE_LEFT", "FACE_TOP", "FACE_BOTTOM")
                                    .collect(Collectors.toSet());
        Arrays.stream(faceActions).forEach(faceAction -> {
            String faceActionName = faceAction.getName();
            assertThat(used, hasItem(faceActionName));
            used.remove(faceActionName);
        });
        assertThat(used.size(), equalTo(0));
    }


    @Test
    void oppositeAction() {
        Arrays.stream(faceActions).forEach(faceAction -> {
            FaceAction oppAction = (FaceAction) faceAction.oppositeAction();
            if(faceAction.getFace() == RubikCube.FACE.BACK) assertThat(oppAction.getFace(), equalTo(RubikCube.FACE.BACK));
            if(faceAction.getFace() == RubikCube.FACE.RIGHT) assertThat(oppAction.getFace(), equalTo(RubikCube.FACE.LEFT));
            if(faceAction.getFace() == RubikCube.FACE.LEFT) assertThat(oppAction.getFace(), equalTo(RubikCube.FACE.RIGHT));
            if(faceAction.getFace() == RubikCube.FACE.TOP) assertThat(oppAction.getFace(), equalTo(RubikCube.FACE.BOTTOM));
            if(faceAction.getFace() == RubikCube.FACE.BOTTOM) assertThat(oppAction.getFace(), equalTo(RubikCube.FACE.TOP));
        });
    }
}