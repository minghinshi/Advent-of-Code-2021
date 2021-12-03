package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day03 {
    public static void main(String[] args) throws IOException {
        List<String> binaryStrings = Files.readAllLines(Paths.get("puzzleInputs/Day03.txt"));
        int binaryNumberLength = binaryStrings.get(0).length();

        //Part 1
        int gammaRate = 0;
        int epsilonRate = 0;

        for (int i = 0; i < binaryNumberLength; i++) {
            int increment = (int)Math.pow(2,binaryNumberLength-i-1);
            if (findMostCommonBit(binaryStrings, i))
                gammaRate += increment;
            else
                epsilonRate += increment;
        }

        System.out.printf("""
                
                The gamma rate is %d.
                The epsilon rate is %d.
                The submarine is consuming %d watts.
                """, gammaRate, epsilonRate, gammaRate * epsilonRate);

        //Part 2
        List<String> oxygenList = Files.readAllLines(Paths.get("puzzleInputs/Day03.txt"));
        List<String> carbonList = Files.readAllLines(Paths.get("puzzleInputs/Day03.txt"));

        for (int i = 0; i < binaryNumberLength; i++) {
            oxygenList = eliminateBinaryStrings(oxygenList, findMostCommonBit(oxygenList, i), i);
            carbonList = eliminateBinaryStrings(carbonList, !findMostCommonBit(carbonList, i), i);
        }

        int oxygenRating = Integer.parseInt(oxygenList.get(0), 2);
        int carbonRating = Integer.parseInt(carbonList.get(0), 2);
        System.out.printf("""
                
                The oxygen generator rating is %d.
                The CO2 scrubber rating is %d.
                The life support rating is %d.
                """, oxygenRating, carbonRating, oxygenRating * carbonRating);
    }

    public static boolean findMostCommonBit(List<String> binaryStrings, int position){
        int ones = 0;
        for (String binaryString : binaryStrings) {
            if(binaryString.charAt(position) == '1')
                ones++;
        }
        return ones * 2 >= binaryStrings.size();
    }

    public static List<String> eliminateBinaryStrings(List<String> binaryStrings, boolean requiredBit, int position){
        if (binaryStrings.size() == 1) return binaryStrings;
        int i = 0;
        while (i < binaryStrings.size()){
            if(binaryStrings.get(i).charAt(position) != (requiredBit ? '1' : '0'))
                binaryStrings.remove(i);
            else
                i++;
        }
        return binaryStrings;
    }
}
