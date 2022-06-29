package rubikcube.action;

import rubikcube.RubikCube;

public interface RubikCubeAction {
    String getName();
    void performAction(RubikCube rubikCube);
    RubikCubeAction oppositeAction();
}
