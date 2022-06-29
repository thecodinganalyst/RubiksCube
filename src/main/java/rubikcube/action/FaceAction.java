package rubikcube.action;

import rubikcube.RubikCube;

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

    public RubikCube.FACE getFace(){
        return this.face;
    }

    @Override
    public String getName() {
        return "FACE_" + face.name();
    }

    @Override
    public void performAction(RubikCube rubikCube){
        rubikCube.face(face);
    }

    @Override
    public RubikCubeAction oppositeAction(){
        if(face == RubikCube.FACE.BACK) return this;
        if(face == RubikCube.FACE.RIGHT) return new FaceAction(RubikCube.FACE.LEFT);
        if(face == RubikCube.FACE.LEFT) return new FaceAction(RubikCube.FACE.RIGHT);
        if(face == RubikCube.FACE.TOP) return new FaceAction(RubikCube.FACE.BOTTOM);
        if(face == RubikCube.FACE.BOTTOM) return new FaceAction(RubikCube.FACE.TOP);
        return null;
    }
}
