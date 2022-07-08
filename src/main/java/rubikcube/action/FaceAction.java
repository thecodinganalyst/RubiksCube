package rubikcube.action;

import rubikcube.RubikCube;
import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.Arrays;

public class FaceAction implements Action<RubikCube> {

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
    public void performAction(Subject<RubikCube> rubikCube){
        ((RubikCube)rubikCube).face(face);
    }

    @Override
    public Action<RubikCube> oppositeAction(){
        if(face == RubikCube.FACE.BACK) return this;
        if(face == RubikCube.FACE.RIGHT) return new FaceAction(RubikCube.FACE.LEFT);
        if(face == RubikCube.FACE.LEFT) return new FaceAction(RubikCube.FACE.RIGHT);
        if(face == RubikCube.FACE.TOP) return new FaceAction(RubikCube.FACE.BOTTOM);
        if(face == RubikCube.FACE.BOTTOM) return new FaceAction(RubikCube.FACE.TOP);
        return null;
    }
}
