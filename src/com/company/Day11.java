package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 {
    public static void main(String[] args) throws IOException {
        Grid grid = new Grid(Files.readAllLines(Paths.get("puzzleInputs/Day11.txt")));
        grid.Simulate();
    }
}

class Grid{
    Octopus[][] gridOfOctopus;

    public Grid(List<String> input){
        //Fill grid with octopus
        gridOfOctopus = new Octopus[10][10];
        for (int i = 0; i < 10; i++) {
            char[] chars = input.get(i).toCharArray();
            for (int j = 0; j < 10; j++) {
                gridOfOctopus[i][j] = new Octopus(chars[j]-48);
            }
        }

        //Add neighbours
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Octopus octopus = gridOfOctopus[i][j];
                for (int k = i - 1; k <= i + 1 ; k++) {
                    if (k < 0 || k >= 10) continue;
                    for (int l = j - 1; l <= j + 1 ; l++) {
                        if(l < 0 || l >= 10) continue;
                        if(k == i && l == j) continue;
                        octopus.addNeighbour(gridOfOctopus[k][l]);
                    }
                }
            }
        }
    }

    public void Simulate(){
        int flashes = 0;
        int step = 0;

        while (true) {
            step++;
            boolean isAllOctopusesFlashing = true;

            //Increase all octopus energy levels by 1, and handle flashing
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    gridOfOctopus[j][k].increaseEnergy();
                }
            }

            //Tally octopuses that have flashed
            for (int j = 0; j < 10; j++) {
                for (int k = 0; k < 10; k++) {
                    if (gridOfOctopus[j][k].isFlashing()) {
                        flashes++;
                        gridOfOctopus[j][k].stopFlashing();
                    }else{
                        isAllOctopusesFlashing = false;
                    }
                }
            }

            //Display solutions
            if(step == 100)
                System.out.printf("\nAfter 100 steps, the octopuses would have flashed %d times.\n", flashes);
            if(isAllOctopusesFlashing){
                System.out.printf("The octopuses synchronize after %d steps.\n", step);
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                stringBuilder.append(gridOfOctopus[i][j]);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

class Octopus{
    List<Octopus> neighbours = new ArrayList<>();
    int energy;
    boolean isFlashing;

    public Octopus(int energy){
        this.energy = energy;
        isFlashing = false;
    }

    public void addNeighbour(Octopus octopus){
        neighbours.add(octopus);
    }

    public void increaseEnergy(){
        if(isFlashing) return;
        energy++;
        if(energy > 9){
            isFlashing = true;
            for (Octopus neighbour : neighbours) {
                neighbour.increaseEnergy();
            }
        }
    }

    public boolean isFlashing() {
        return isFlashing;
    }

    public void stopFlashing(){
        energy = 0;
        isFlashing = false;
    }

    @Override
    public String toString() {
        return Integer.toString(energy);
    }
}