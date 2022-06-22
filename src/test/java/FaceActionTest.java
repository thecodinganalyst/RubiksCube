import org.junit.jupiter.api.Test;

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


}