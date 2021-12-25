package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day25 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day25.txt"));
        CucumberAutomata automata = new CucumberAutomata(input);
        System.out.printf("\nThe sea cucumbers stop moving after %d steps.\n", automata.stepAll());
    }
}

class CucumberAutomata{
    private int[][] cells;
    private final int height;
    private final int length;

    public CucumberAutomata(List<String> input){
        height = input.size();
        length = input.get(0).length();
        cells = new int[height][length];
        for (int i = 0; i < height; i++) {
            String s = input.get(i);
            for (int j = 0; j < length; j++) {
                char c  = s.charAt(j);
                cells[i][j] = c == '.' ? 0 : (c == '>' ? 1 : 2);
            }
        }
    }

    public int stepAll(){
        int numberOfSteps = 0;
        do {
            numberOfSteps++;
        } while (step());
        return numberOfSteps;
    }

    public boolean step(){
        boolean moved = false;

        //Horizontal
        int[][] newCells = new int[height][length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                int prevX = (j+length-1)%length;
                int nextX = (j+1)%length;
                if(cells[i][j] == 1 && cells[i][nextX] == 0){
                    newCells[i][j] = 0;
                    moved = true;
                }else if(cells[i][j] == 0 && cells[i][prevX] == 1){
                    newCells[i][j] = 1;
                    moved = true;
                }else{
                    newCells[i][j] = cells[i][j];
                }
            }
        }
        cells = newCells;

        //Vertical
        newCells = new int[height][length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                int prevY = (i+height-1)%height;
                int nextY = (i+1)%height;
                if(cells[i][j] == 2 && cells[nextY][j] == 0){
                    newCells[i][j] = 0;
                    moved = true;
                }else if(cells[i][j] == 0 && cells[prevY][j] == 2){
                    newCells[i][j] = 2;
                    moved = true;
                }else{
                    newCells[i][j] = cells[i][j];
                }
            }
        }
        cells = newCells;

        //Return a flag
        return moved;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                stringBuilder.append(cells[i][j] == 0 ? '.' : (cells[i][j] == 1 ? '>' : 'v'));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
