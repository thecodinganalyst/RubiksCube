import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RubikSolution {
    public static void main(String[] args) {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();
        solution.randomActions(cube, 20)
                .forEach(action -> System.out.println(action.getName()));
        cube.print();
        System.out.println(cube.check());
    }

    public List<RubikCubeAction> randomActions(RubikCube rubikCube, int count){
        Random random = new Random();
        int actionCount = rubikCube.getAllActions().length;
        return IntStream.range(0, count).boxed().map(i -> {
          RubikCubeAction action = rubikCube.getAllActions()[random.nextInt(actionCount)];
          rubikCube.performAction(action);
          return action;
        }).toList();
    }
}
