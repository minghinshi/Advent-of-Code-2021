/*

*/

package com.company;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Day01 {
    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day01.txt"));
        List<Integer> depths = new ArrayList<>();

        for (String string : content) {
            depths.add(Integer.parseInt(string));
        }

        System.out.println("The depth measurement increases " + countIncreases(depths,1) + " times, without using averages.");
        System.out.println("The depth measurement increases " + countIncreases(depths,3) + " times, using averages of 3 depths.");
    }

    public static int countIncreases(List<Integer> integerList, int slidingWindow){
        int count = 0;
        for (int i = slidingWindow; i < integerList.size(); i++) {
            if(integerList.get(i) > integerList.get((i-slidingWindow)))
                ++count;
        }
        return count;
    }
}