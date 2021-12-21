package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Day20 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day20.txt"));
        ImageEnhancement automaton = new ImageEnhancement(input);
        for (int i = 0; i < 2; i++)
            automaton.stepAutomaton();
        System.out.printf("\nAfter 2 steps, %d pixels are lit.\n", automaton.countCells());
        for (int i = 0; i < 48; i++)
            automaton.stepAutomaton();
        System.out.printf("After 50 steps, %d pixels are lit.\n", automaton.countCells());
    }
}

class ImageEnhancement{
    boolean[] rules = new boolean[512];
    boolean[][] cells;
    boolean areCellsOutsideBorderOn;

    public ImageEnhancement(List<String> input){
        char[] ruleChars = input.get(0).toCharArray();
        for (int i = 0; i < ruleChars.length; i++) {
            rules[i] = ruleChars[i] == '#';
        }

        cells = new boolean[input.size() - 2][input.get(2).length()];
        for (int i = 2; i < input.size(); i++) {
            char[] cellChars = input.get(i).toCharArray();
            for (int j = 0; j < cellChars.length; j++) {
                cells[i-2][j] = cellChars[j] == '#';
            }
        }
    }

    public void stepAutomaton(){
        boolean[][] newCells = new boolean[cells.length + 2][cells[0].length + 2];
        for (int i = 0; i < newCells.length; i++) {
            for (int j = 0; j < newCells.length; j++) {
                int rule = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        int bitValue = (int) Math.pow(2, 8 - k * 3 - l);
                        try{
                            rule += cells[i+k-2][j+l-2] ? bitValue : 0;
                        } catch (Exception e) {
                            rule += areCellsOutsideBorderOn ? bitValue : 0;
                        }
                    }
                }
                newCells[i][j] = rules[rule];
            }
        }
        cells = newCells;
        areCellsOutsideBorderOn = rules[0] != areCellsOutsideBorderOn;
    }

    public int countCells(){
        int count = 0;
        for (boolean[] row : cells) {
            for (boolean cell : row) {
                count += cell ? 1 : 0;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (boolean[] row : cells) {
            for (boolean cell : row) {
                stringBuilder.append(cell ? '#' : '.');
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}