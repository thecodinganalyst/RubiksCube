import java.util.Arrays;

public class FaceAction implements RubikCubeAction{

    private final RubikCube.FACE face;

    private FaceAction(RubikCube.FACE face){
        this.face = face;
    }

    public static FaceAction[] allActions(){
        return Arrays.stream(RubikCube.FACE.values())
                .filter(face -> face != RubikCube.FACE.MAIN)
                .map(FaceAction::new)
                .toArray(FaceAction[]::new);
    }

    @Override
    public String getName() {
        return "FACE_" + face.name();
    }

    @Override
    public void performAction(RubikCube rubikCube){
        rubikCube.face(face);
    }
}
