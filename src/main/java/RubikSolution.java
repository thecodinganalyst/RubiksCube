import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RubikSolution {
    public static void main(String[] args) throws Exception {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();
        solution.randomActions(cube, 20)
                .forEach(action -> System.out.println(action.name()));
        cube.print();
        System.out.println(cube.check());
    }

    public List<RubikCube.ACTION> randomActions(RubikCube rubikCube, int count){
        Random random = new Random();
        int actionCount = RubikCube.ACTION.values().length;
        return IntStream.range(0, count).boxed().map(i -> {
          RubikCube.ACTION action = RubikCube.ACTION.values()[random.nextInt(actionCount)];
          rubikCube.doAction(action);
          return action;
        }).toList();
    }
}
