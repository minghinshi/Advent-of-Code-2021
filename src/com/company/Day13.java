package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day13 {
    public static void main(String[] args) throws IOException {
        FoldingPaper foldingPaper = new FoldingPaper(Files.readAllLines(Paths.get("puzzleInputs/Day13.txt")));
        foldingPaper.fold();
        System.out.printf("\nAfter the first fold, there are %d dots.\n", foldingPaper.getNumberOfDots());
        foldingPaper.foldAll();
        System.out.printf("Activation code:\n%s", foldingPaper);
    }

    public static int getHash(int[] dot) {
        return dot[1] * 65536 + dot[0];
    }
}

class FoldingPaper {
    HashMap<Integer, int[]> dotHashMap = new HashMap<>();
    List<FoldAction> foldActions = new ArrayList<>();
    int xSize, ySize;

    public FoldingPaper(List<String> input) {
        boolean importedAllDots = false;
        HashMap<Integer, int[]> dotHashMap = new HashMap<>();
        for (String string : input) {
            if (string.isBlank()) importedAllDots = true;
            else if (importedAllDots) foldActions.add(new FoldAction(string));
            else {
                int[] dot = Arrays.stream(string.split(",")).mapToInt(Integer::parseInt).toArray();
                dotHashMap.put(Day13.getHash(dot), dot);
            }
        }
        setDotHashMap(dotHashMap);
    }

    public void fold() {
        HashMap<Integer, int[]> newDotHashMap = new HashMap<>();
        FoldAction foldAction = foldActions.get(0);
        for (int[] dot : dotHashMap.values()) {
            if (foldAction.getFoldingAxis() == 'x')
                dot[0] = foldAction.coordinate - Math.abs(foldAction.coordinate - dot[0]);
            else if (foldAction.getFoldingAxis() == 'y')
                dot[1] = foldAction.coordinate - Math.abs(foldAction.coordinate - dot[1]);
            else throw new RuntimeException("Cannot fold paper");
            newDotHashMap.putIfAbsent(Day13.getHash(dot), dot);
        }
        setDotHashMap(newDotHashMap);
        foldActions.remove(0);
    }

    public void foldAll() {
        int numberOfFolds = foldActions.size();
        for (int i = 0; i < numberOfFolds; i++) {
            fold();
        }
    }

    public void setDotHashMap(HashMap<Integer, int[]> dotHashMap) {
        this.dotHashMap = dotHashMap;
        xSize = 0;
        ySize = 0;
        for (int[] dot : dotHashMap.values()) {
            if (dot[0] + 1 > xSize) xSize = dot[0] + 1;
            if (dot[1] + 1 > ySize) ySize = dot[1] + 1;
        }
    }

    public int getNumberOfDots() {
        return dotHashMap.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ySize; i++) {
            for (int j = 0; j < xSize; j++) {
                if (dotHashMap.containsKey(i * 65536 + j)) stringBuilder.append("██");
                else stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

class FoldAction {
    char foldingAxis;
    int coordinate;

    public FoldAction(String string) {
        String[] split = string.substring(11).split("=");
        switch (split[0]) {
            case "x" -> foldingAxis = 'x';
            case "y" -> foldingAxis = 'y';
            default -> throw new RuntimeException("Axis not found");
        }
        coordinate = Integer.parseInt(split[1]);
    }

    public char getFoldingAxis() {
        return foldingAxis;
    }
}