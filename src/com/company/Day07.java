package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day07 {
    public static void main(String[] args) throws IOException {
        String content = Files.readAllLines(Paths.get("puzzleInputs/Day07.txt")).get(0);
        List<Integer> crabLocations = Arrays.stream(content.split(",")).map(Integer::parseInt).collect(Collectors.toList());

        findLowestFuel(crabLocations, false);   //Part 1
        findLowestFuel(crabLocations, true);    //Part 2
    }

    public static void findLowestFuel (List<Integer> crabLocations, boolean useQuadraticFueling) {
        System.out.printf("\nUsing %s fuel burning...\n", useQuadraticFueling ? "quadratic" : "linear");
        int lowestFuel = totalFuel(crabLocations, 0, useQuadraticFueling);
        int targetLocation = 0;
        while (true){
            int fuelAtNextLocation = totalFuel(crabLocations, targetLocation + 1, useQuadraticFueling);
            if (fuelAtNextLocation > lowestFuel) {
                System.out.printf("The crabs will require %d fuel.\n",lowestFuel);
                break;
            } else {
                lowestFuel = fuelAtNextLocation;
                targetLocation++;
            }
        }
    }

    public static int totalFuel(List<Integer> crabLocations, int targetLocation, boolean useQuadraticFueling){
        int totalFuel = 0;
        for (int crabLocation : crabLocations) {
            int fuel = Math.abs(crabLocation - targetLocation);
            if(useQuadraticFueling)
                fuel = ((fuel * fuel) + fuel) / 2;
            totalFuel += fuel;
        }
        return totalFuel;
    }
}
