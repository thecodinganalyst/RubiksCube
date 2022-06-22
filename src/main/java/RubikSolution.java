import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RubikSolution {
    public static void main(String[] args) {
        RubikCube cube = new RubikCube(3);
        RubikSolution solution = new RubikSolution();
        List<RubikCubeAction> randomActions = solution.randomActions(cube, 20);
        randomActions.forEach(action -> System.out.println(action.getName()));
        cube.print();
        System.out.println(cube.check());

        List<RubikCubeAction> reverseActions = solution.reverseActions(randomActions);
        reverseActions.forEach(action -> System.out.println(action.getName()));
        reverseActions.forEach(cube::performAction);
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

    public List<RubikCubeAction> reverseActions(List<RubikCubeAction> originalActions){
        return IntStream.rangeClosed(1, originalActions.size())
                .boxed()
                .map(i -> originalActions.get(originalActions.size() - i))
                .map(RubikCubeAction::oppositeAction)
                .toList();
    }
}
