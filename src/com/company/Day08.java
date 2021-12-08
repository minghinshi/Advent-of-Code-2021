package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/*
Which segments are on?
    a   b   c   d   e   f   g
0   1   1   1       1   1   1
1           1           1
2   1       1   1   1       1
3   1       1   1       1   1
4       1   1   1       1
5   1   1       1       1   1
6   1   1       1   1   1   1
7   1       1           1
8   1   1   1   1   1   1   1
9   1   1   1   1       1   1
 */
public class Day08 {
    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day08.txt"));
        int easyDigits = 0;
        int total = 0;
        for (String entry : content) {
            String[] entries = entry.split(" \\| ");
            String[] inputDigits = entries[0].split(" ");
            String[] outputDigits = entries[1].split(" ");
            HashMap<Character, Character> segmentMapping = new HashMap<>();
            HashMap<Integer, String> digitMapping = new HashMap<>();
            List<String> digitsWithFiveSegments = new ArrayList<>();
            List<String> digitsWithSixSegments = new ArrayList<>();
            List<Character> cde = new ArrayList<>();
            List<Character> bfg = new ArrayList<>();

            //Step 1
            for (String inputDigit : inputDigits) {
                switch (inputDigit.length()) {
                    case 2 -> digitMapping.put(1, inputDigit);
                    case 3 -> digitMapping.put(7, inputDigit);
                    case 4 -> digitMapping.put(4, inputDigit);
                    case 5 -> digitsWithFiveSegments.add(inputDigit);
                    case 6 -> digitsWithSixSegments.add(inputDigit);
                    case 7 -> digitMapping.put(8, inputDigit);
                }
            }

            //Step 2
            segmentMapping.put('a', subtractStrings(digitMapping.get(7), digitMapping.get(1)).get(0));

            //Step 3
            for (String digit : digitsWithSixSegments) {
                cde.add(subtractStrings("abcdefg", digit).get(0));
            }

            //Step 4
            for (char c = 'a'; c <= 'g'; c++)
                if(!cde.contains(c) && c != segmentMapping.get('a'))
                    bfg.add(c);
            for (String digit : digitsWithFiveSegments) {
                char[] segments = digit.toCharArray();
                boolean foundDigit = true;
                for (char c1 : bfg) {
                    boolean foundSegment = false;
                    for (char c2 : segments) {
                        if (c1 == c2) {
                            foundSegment = true;
                            break;
                        }
                    }
                    if (!foundSegment) {
                        foundDigit = false;
                        break;
                    }
                }
                if(foundDigit) {
                    digitMapping.put(5, digit);
                    digitsWithFiveSegments.remove(digit);
                    break;
                }
            }

            //Step 5
            for (String digit : digitsWithFiveSegments) {
                switch (subtractStrings(digitMapping.get(5), digit).size()){
                    case 1 -> digitMapping.put(3, digit);
                    case 2 -> digitMapping.put(2, digit);
                }
            }

            //Step 6
            segmentMapping.put('b', subtractStrings(digitMapping.get(5), digitMapping.get(3)).get(0));
            segmentMapping.put('c', subtractStrings(digitMapping.get(3), digitMapping.get(5)).get(0));
            segmentMapping.put('e', subtractStrings(digitMapping.get(2), digitMapping.get(3)).get(0));
            segmentMapping.put('f', subtractStrings(digitMapping.get(3), digitMapping.get(2)).get(0));

            //Step 7
            cde.remove(segmentMapping.get('c'));
            cde.remove(segmentMapping.get('e'));
            segmentMapping.put('d', cde.get(0));

            bfg.remove(segmentMapping.get('b'));
            bfg.remove(segmentMapping.get('f'));
            segmentMapping.put('g', bfg.get(0));

            //Step 8
            HashMap<Character, Character> signalMapping = new HashMap<>();
            for (char i : segmentMapping.keySet())
                signalMapping.put(segmentMapping.get(i), i);
            int outputValue = 0;
            for (int i = 0; i < 4; i++) {
                String digit = outputDigits[i];
                int number = 0;
                char[] signals = digit.toCharArray();
                switch (signals.length) {
                    case 2 -> {
                        number = 1;
                        easyDigits++;
                    }
                    case 3 -> {
                        number = 7;
                        easyDigits++;
                    }
                    case 4 -> {
                        number = 4;
                        easyDigits++;
                    }
                    case 5 -> {
                        for (char signal : signals) {
                            if (signalMapping.get(signal) == 'b') number = 5;
                            if (signalMapping.get(signal) == 'e') number = 2;
                        }
                        if (number == 0) number = 3;
                    }
                    case 6 -> {
                        List<Integer> possibleNumbers = new ArrayList<>(Arrays.asList(0, 6, 9));
                        for (char signal : signals) {
                            if (signalMapping.get(signal) == 'd') possibleNumbers.remove(Integer.valueOf(0));
                            if (signalMapping.get(signal) == 'c') possibleNumbers.remove(Integer.valueOf(6));
                            if (signalMapping.get(signal) == 'e') possibleNumbers.remove(Integer.valueOf(9));
                        }
                        number = possibleNumbers.get(0);
                    }
                    case 7 -> {
                        number = 8;
                        easyDigits++;
                    }
                }
                outputValue += Math.pow(10, 3-i) * number;
            }
            total += outputValue;
        }
        System.out.println();
        System.out.printf("Digits 1, 4, 7 and 8 appear %d times.\n",easyDigits);
        System.out.printf("The sum of output values is %d.\n", total);
    }

    public static List<Character> subtractStrings(String stringToSubtractFrom, String stringToSubtract){
        List<Character> characterList = new ArrayList<>();
        char[] arrayToSubtractFrom = stringToSubtractFrom.toCharArray(), arrayToSubtract = stringToSubtract.toCharArray();
        for (char c1 : arrayToSubtractFrom) {
            boolean found = false;
            for (char c2 : arrayToSubtract) {
                if (c1 == c2) {
                    found = true;
                    break;
                }
            }
            if (!found)
                characterList.add(c1);
        }
        return characterList;
    }
}