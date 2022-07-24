package rubikcube.scoring;

import rubikcube.RubikCube;
import solutioning.strategy.Subject;

public class RubikCubeScoringByLayerCompletedForMainSideOnly extends RubikCubeScoringByLayerCompleted{

    @Override
    public Double getRubikCubeScore(RubikCube cube) {

        if(!cube.getMain().isComplete()) return getRubikSideScore(cube.getMain());

        return getRubikCubeScoreForFace(cube, RubikCube.FACE.MAIN);
    }

    @Override
    public Double getScore(Subject<RubikCube> subject){
        return getRubikCubeScore((RubikCube) subject);
    }
}
