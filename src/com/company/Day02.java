package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day02 {
    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day02.txt"));
        List<Movement> movements = new ArrayList<>();
        int horizontalTotal = 0;
        int verticalTotal = 0;
        int aim = 0;

        for (String string : content) {
            Movement movement = new Movement(string);
            movements.add(movement);
        }

        //Part 1
        for (Movement movement : movements) {
            horizontalTotal += movement.getHorizontal();
            verticalTotal += movement.getVertical();
        }

        System.out.printf("""
                Treating "up" and "down" as vertical movement...
                The submarine will go forward %d metres and down %.0f metres.
                (Part 1 solution: %d)          
                """, horizontalTotal, verticalTotal * 0.01, horizontalTotal * verticalTotal);

        horizontalTotal = 0;
        verticalTotal = 0;

        //Part 2
        for (Movement movement : movements){
            horizontalTotal += movement.getHorizontal();
            verticalTotal += movement.getHorizontal() * aim;
            aim += movement.getVertical();
        }

        System.out.printf("""
                Treating "up" and "down" as rotations...
                The submarine will go forward %d metres and down %.0f metres.
                (Part 2 solution: %d)""", horizontalTotal, verticalTotal * 0.01, horizontalTotal * verticalTotal);
    }
}

class Movement{
    int horizontal = 0;
    int vertical = 0;

    public Movement(String input){
        String[] strings = input.split(" ");
        int magnitude = Integer.parseInt(strings[1]);
        switch (strings[0]) {
            case "forward" -> horizontal = magnitude;
            case "up" -> vertical = -magnitude;
            case "down" -> vertical = magnitude;
            default -> System.out.println("There is an error in your code!");
        }
    }

    public int getHorizontal() {
        return horizontal;
    }

    public int getVertical() {
        return vertical;
    }
}
