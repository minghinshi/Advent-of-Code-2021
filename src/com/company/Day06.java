package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day06 {
    public static void main(String[] args) throws IOException {
        String content = Files.readAllLines(Paths.get("puzzleInputs/Day06.txt")).get(0);
        List<Integer> internalTimers = Arrays.stream(content.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        long[] numberOfFish = new long[9];
        for (int i : internalTimers) {
            numberOfFish[i]++;
        }

        System.out.println();
        //Both Part 1 and 2
        for (int i = 0; i < 256; i++) {
            long[] updatedNumberOfFish = new long[9];
            System.arraycopy(numberOfFish, 1, updatedNumberOfFish, 0, 8);
            updatedNumberOfFish[6] += numberOfFish[0];
            updatedNumberOfFish[8] += numberOfFish[0];
            numberOfFish = updatedNumberOfFish;

            if(i == 79 || i == 255){
                long total = 0;
                for (long l : numberOfFish) {
                    total += l;
                }
                System.out.printf("After %d days, there would be %s lanternfish.\n", i+1, total);
            }
        }
    }
}
