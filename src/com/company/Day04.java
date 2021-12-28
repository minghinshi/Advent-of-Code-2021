package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day04 {

    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day04.txt"));
        List<BingoBoard> bingoBoards = new ArrayList<>();
        List<Integer> remainingNumbers = new ArrayList<>();

        //Prepare data
        for (String numberString : content.get(0).split(",")) {
            remainingNumbers.add(Integer.parseInt(numberString));
        }
        content.remove(0);
        content.remove(0);
        GenerateBoards(bingoBoards, content);

        int numberOfCalls = remainingNumbers.size();
        int numberOfCards = bingoBoards.size();
        int numberOfBingo = 0;

        //Play the game!
        System.out.printf("\nSimulating a game of Bingo with %d cards...\n\n", numberOfCards);
        for (int i = 0; i < numberOfCalls; i++) {
            int number = remainingNumbers.get(0);
            remainingNumbers.remove(0);
            System.out.printf("The %s number is %d.\n\n", ordinal(i + 1), number);
            for (BingoBoard board : bingoBoards) {
                board.markNumber(number);
                if (board.checkForVictory()) {
                    numberOfBingo++;
                    System.out.printf("""
                            Bingo!
                            %s
                            Score: %d
                                                      
                            """, board.toString(), board.totalOfUnmarkedNumbers() * number);
                    if (numberOfBingo == bingoBoards.size()) {
                        System.out.println("Game over!");
                        return;
                    }
                }
            }
        }

    }

    public static void GenerateBoards(List<BingoBoard> boards, List<String> allRows) {
        String[] rowsOfNumbers = new String[5];
        int index = 0;
        while (allRows.size() != 0) {
            if (!allRows.get(0).isBlank()) {
                rowsOfNumbers[index] = allRows.get(0);
                index++;
                if (index == 5) {
                    boards.add(new BingoBoard(rowsOfNumbers));
                    rowsOfNumbers = new String[5];
                    index = 0;
                }
            }
            allRows.remove(0);
        }
    }

    public static String ordinal(int i) {
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        return switch (i % 100) {
            case 11, 12, 13 -> i + "th";
            default -> i + suffixes[i % 10];
        };
    }
}

class BingoBoard {
    int[][] numbers = new int[5][5];
    boolean[][] isNumberMarked = new boolean[5][5];
    boolean hasWon = false;

    public BingoBoard(String[] strings) {
        for (int i = 0; i < 5; i++) {
            String[] numberStrings = strings[i].trim().split(" +");
            for (int j = 0; j < 5; j++) {
                numbers[i][j] = Integer.parseInt(numberStrings[j]);
            }
        }
    }

    public void markNumber(int number) {
        boolean found = false;
        for (int i = 0; (i < 5 && !found); i++) {
            for (int j = 0; (j < 5 && !found); j++) {
                if (numbers[i][j] == number) {
                    found = true;
                    isNumberMarked[i][j] = true;
                }
            }
        }
    }

    public boolean checkForVictory() {
        if (hasWon) return false;
        for (int i = 0; i < 5; i++) {
            if (checkOneLine(i, false) || checkOneLine(i, true)) {
                hasWon = true;
                return true;
            }
        }
        return false;
    }

    boolean checkOneLine(int order, boolean checkVertical) {
        for (int i = 0; i < 5; i++) {
            if (!isNumberMarked[(checkVertical ? i : order)][(checkVertical ? order : i)]) return false;
        }
        return true;
    }

    public int totalOfUnmarkedNumbers() {
        int output = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                output += (isNumberMarked[i][j] ? 0 : numbers[i][j]);
            }
        }
        return output;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int number = numbers[i][j];
                boolean isMarked = isNumberMarked[i][j];
                output.append(isMarked ? "[" : " ").append(number < 10 ? " " : "").append(number).append(isMarked ? "]" : " ").append(" ");
            }
            output.append(i != 4 ? "\n" : "");
        }
        return output.toString();
    }
}