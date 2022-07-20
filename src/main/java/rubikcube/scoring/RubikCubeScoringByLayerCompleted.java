package rubikcube.scoring;

import rubikcube.RubikCube;
import rubikcube.RubikCube.FACE;
import solutioning.strategy.Subject;

import java.util.*;
import java.util.stream.IntStream;

public class RubikCubeScoringByLayerCompleted extends RubikCubeScoringByCompletionPercentage{

    public Double getScoreOfSideWithHighestScore(RubikCube cube){
        return cube.getAllSides().values().stream().map(this::getRubikSideScore).max(Double::compareTo).orElseThrow();
    }

    public List<FACE> getCompletedFaces(RubikCube cube){
        return cube.getAllSides()
                .keySet()
                .stream()
                .filter(face -> cube.getFace(face).isComplete())
                .toList();
    }

    public List<FACE> getAdjacentFaces(FACE face){
        if(face == FACE.MAIN || face == FACE.BACK) return List.of(FACE.TOP, FACE.RIGHT, FACE.BOTTOM, FACE.LEFT);
        if(face == FACE.RIGHT || face == FACE.LEFT) return List.of(FACE.TOP, FACE.BACK, FACE.BOTTOM, FACE.MAIN);
        if(face == FACE.TOP || face == FACE.BOTTOM) return List.of(FACE.MAIN, FACE.RIGHT, FACE.BACK, FACE.LEFT);
        return null;
    }

    public List<int[]> getAdjacentRows(RubikCube cube, FACE face, int row){
        int lastRow = cube.getSize() - row - 1;
        if (face == FACE.MAIN)
            return List.of(
                    cube.getFace(FACE.TOP).getRow(lastRow),
                    cube.getFace(FACE.BOTTOM).getRow(row),
                    cube.getFace(FACE.RIGHT).getCol(row),
                    cube.getFace(FACE.LEFT).getCol(lastRow)
            );
        else if(face == FACE.BACK)
            return List.of(
                    cube.getFace(FACE.TOP).getRow(row),
                    cube.getFace(FACE.BOTTOM).getRow(lastRow),
                    cube.getFace(FACE.RIGHT).getCol(lastRow),
                    cube.getFace(FACE.LEFT).getCol(row)
            );
        else if(face == FACE.TOP)
            return List.of(
                    cube.getFace(FACE.MAIN).getRow(row),
                    cube.getFace(FACE.RIGHT).getRow(row),
                    cube.getFace(FACE.BACK).getRow(row),
                    cube.getFace(FACE.LEFT).getRow(row)
            );
        else if(face == FACE.BOTTOM)
            return List.of(
                    cube.getFace(FACE.MAIN).getRow(lastRow),
                    cube.getFace(FACE.RIGHT).getRow(lastRow),
                    cube.getFace(FACE.BACK).getRow(lastRow),
                    cube.getFace(FACE.LEFT).getRow(lastRow)
            );
        else if (face == FACE.RIGHT)
            return List.of(
                    cube.getFace(FACE.MAIN).getCol(lastRow),
                    cube.getFace(FACE.TOP).getCol(lastRow),
                    cube.getFace(FACE.BACK).getCol(row),
                    cube.getFace(FACE.BOTTOM).getCol(lastRow)
            );
        else if(face == FACE.LEFT)
            return List.of(
                    cube.getFace(FACE.MAIN).getCol(row),
                    cube.getFace(FACE.TOP).getCol(row),
                    cube.getFace(FACE.BACK).getCol(lastRow),
                    cube.getFace(FACE.BOTTOM).getCol(row)
            );
        return null;
    }

    public List<int[]> getAdjacentRows(RubikCube cube, FACE face){
        int lastRow = cube.getSize() - 1;
        if (face == FACE.MAIN)
            return List.of(
                cube.getFace(FACE.TOP).getRow(lastRow),
                cube.getFace(FACE.BOTTOM).getRow(0),
                cube.getFace(FACE.RIGHT).getCol(0),
                cube.getFace(FACE.LEFT).getCol(lastRow)
            );
        else if(face == FACE.BACK)
            return List.of(
                cube.getFace(FACE.TOP).getRow(0),
                cube.getFace(FACE.BOTTOM).getRow(lastRow),
                cube.getFace(FACE.RIGHT).getCol(lastRow),
                cube.getFace(FACE.LEFT).getCol(0)
            );
        else if(face == FACE.TOP)
            return List.of(
                cube.getFace(FACE.MAIN).getRow(0),
                cube.getFace(FACE.RIGHT).getRow(0),
                cube.getFace(FACE.BACK).getRow(0),
                cube.getFace(FACE.LEFT).getRow(0)
            );
        else if(face == FACE.BOTTOM)
            return List.of(
                cube.getFace(FACE.MAIN).getRow(lastRow),
                cube.getFace(FACE.RIGHT).getRow(lastRow),
                cube.getFace(FACE.BACK).getRow(lastRow),
                cube.getFace(FACE.LEFT).getRow(lastRow)
            );
        else if (face == FACE.RIGHT)
            return List.of(
                cube.getFace(FACE.MAIN).getCol(lastRow),
                cube.getFace(FACE.TOP).getCol(lastRow),
                cube.getFace(FACE.BACK).getCol(0),
                cube.getFace(FACE.BOTTOM).getCol(lastRow)
            );
        else if(face == FACE.LEFT)
            return List.of(
                cube.getFace(FACE.MAIN).getCol(0),
                cube.getFace(FACE.TOP).getCol(0),
                cube.getFace(FACE.BACK).getCol(lastRow),
                cube.getFace(FACE.BOTTOM).getCol(0)
            );
        return null;
    }

    public boolean allSame(int[] input){
        return Arrays.stream(input).allMatch(i -> i == input[0]);
    }

    public Double getRubikCubeScoreForFace(RubikCube cube, FACE face){
        double result = 0.0;

        if(!cube.getFace(face).isComplete()) return result;
        result += 1.0;

        for (int i : midLevelsRequired(cube.getSize())){
            double score = getMidLevelScore(cube, face, i);
            result += score;
            if(score < 1.0) return result;
        }

        double lastScore = cube.isComplete() ? 1.0 : 0.0;
        result += lastScore;

        return result;
    }

    public int[] midLevelsRequired(int size){
        return IntStream.range(0, size - 1).toArray();
    }

    public Double getMidLevelScore(RubikCube cube, FACE face, int level){
        List<int[]> adjacentRows = getAdjacentRows(cube, face, level);
        return adjacentRows.stream()
                .filter(this::allSame)
                .count() / 4.0;
    }

    @Override
    public Double getRubikCubeScore(RubikCube cube) {

        List<FACE> completedFaces = getCompletedFaces(cube);
        if(completedFaces.size() == 0) return getScoreOfSideWithHighestScore(cube);

        return completedFaces.stream()
                .map(face -> getRubikCubeScoreForFace(cube, face))
                .max(Double::compareTo)
                .orElseThrow();
    }

    @Override
    public Double getScore(Subject<RubikCube> subject){
        return getRubikCubeScore((RubikCube) subject);
    }

}
