public class RubikSolution {
    public static void main(String[] args) throws Exception {
        RubikCube cube = new RubikCube(3);
        cube.print();
        cube.turnRowToRight(0);
        cube.print();
        cube.turnColUp(2);
        cube.print();
        System.out.println(cube.check());
    }
}
