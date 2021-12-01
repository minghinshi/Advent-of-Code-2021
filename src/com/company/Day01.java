package com.company;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Day01 {

    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day01.txt"));
        List<Integer> depths = new ArrayList<>();
        List<Integer> depthsSmooth = new ArrayList<>();

        for (String string : content) {
            depths.add(Integer.parseInt(string));
        }

        for (int i = 0; i < depths.size() - 2; i++) {
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += depths.get(i+j);
            }
            depthsSmooth.add(sum);
        }

        System.out.println("The depth measurement increases " + countIncreases(depths) + " times, without using averages.");
        System.out.println("The depth measurement increases " + countIncreases(depthsSmooth) + " times, using averages of 3 depths.");
    }

    public static int countIncreases(List<Integer> integerList){
        int count = 0;
        for (int i = 1; i < integerList.size(); i++) {
            if(integerList.get(i) > integerList.get((i-1)))
                ++count;
        }
        return count;
    }
}