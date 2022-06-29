package rubikcube.strategy;

import rubikcube.RubikCube;

public interface RubikCubeStrategy {
    ExecutionSummary execute(RubikCube rubikCube);
}
