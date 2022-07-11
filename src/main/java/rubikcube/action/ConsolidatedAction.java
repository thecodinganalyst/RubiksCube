package rubikcube.action;

import rubikcube.RubikCube;
import solutioning.strategy.Action;
import solutioning.strategy.Subject;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ConsolidatedAction implements Action<RubikCube> {
    
    public enum FACE { TOP_FACE, MAIN_FACE, RIGHT_FACE }
    public enum DIRECTION { CLOCKWISE, ANTI_CLOCKWISE}

    private final FACE face;
    private final DIRECTION direction;
    private final int position;
    private final int size;

    public ConsolidatedAction(FACE face, DIRECTION direction, int position, int size){
        this.face = face;
        this.direction = direction;
        this.position = position;
        this.size = size;
    }
    
    public static ConsolidatedAction[] allActions(int size){
        return IntStream.range(0, size)
                .boxed()
                .flatMap(i -> Stream.of(
                        new ConsolidatedAction(FACE.TOP_FACE, DIRECTION.CLOCKWISE, i, size),
                        new ConsolidatedAction(FACE.TOP_FACE, DIRECTION.ANTI_CLOCKWISE, i, size),
                        new ConsolidatedAction(FACE.RIGHT_FACE, DIRECTION.CLOCKWISE, i, size),
                        new ConsolidatedAction(FACE.RIGHT_FACE, DIRECTION.ANTI_CLOCKWISE, i, size),
                        new ConsolidatedAction(FACE.MAIN_FACE, DIRECTION.CLOCKWISE, i, size),
                        new ConsolidatedAction(FACE.MAIN_FACE, DIRECTION.ANTI_CLOCKWISE, i, size)
                ))
                .toArray(ConsolidatedAction[]::new);
    }

    @Override
    public String getName() {
        return "TURN_" + face.name() + "_" + position + "_" + direction.name();
    }

    @Override
    public void performAction(Subject<RubikCube> subject) {
        try {
            RubikCube rubikCube = (RubikCube) subject;
            if (face == FACE.MAIN_FACE){
                if(direction == DIRECTION.CLOCKWISE) {
                    rubikCube.face(RubikCube.FACE.RIGHT);
                    rubikCube.turnColDown(position);
                    rubikCube.face(RubikCube.FACE.LEFT);
                }else {
                    rubikCube.face(RubikCube.FACE.RIGHT);
                    rubikCube.turnColUp(position);
                    rubikCube.face(RubikCube.FACE.LEFT);
                }
            }
            if(face == FACE.RIGHT_FACE){
                if(direction == DIRECTION.CLOCKWISE){
                    rubikCube.turnColUp(size - position - 1);
                }else {
                    rubikCube.turnColDown(size - position - 1);
                }
            }
            if(face == FACE.TOP_FACE){
                if(direction == DIRECTION.CLOCKWISE){
                    rubikCube.turnRowToLeft(position);
                }else {
                    rubikCube.turnRowToRight(position);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Action<RubikCube> oppositeAction() {
        return new ConsolidatedAction(
                face,
                direction == DIRECTION.CLOCKWISE ? DIRECTION.ANTI_CLOCKWISE : DIRECTION.CLOCKWISE,
                position,
                size);
    }

    @Override
    public boolean equals(Object obj){
        if(!obj.getClass().equals(this.getClass())) return false;
        return this.face.equals(((ConsolidatedAction) obj).face) &&
                this.position == ((ConsolidatedAction) obj).position &&
                this.direction.equals(((ConsolidatedAction) obj).direction);
    }

    public FACE getFace() {
        return face;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public int getPosition() {
        return position;
    }

    public int getSize() {
        return size;
    }
}
