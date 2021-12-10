package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day10 {
    public static void main(String[] args) throws IOException {
        NavigationSubsystem navigationSubsystem = new NavigationSubsystem(Files.readAllLines(Paths.get("puzzleInputs/Day10.txt")));
        System.out.printf("\nThe total syntax error score is %d.\n", navigationSubsystem.getTotalSyntaxErrorScore());
        System.out.printf("The middle completion score is %d.\n", navigationSubsystem.getMiddleCompletionScore());
    }
}

class BrokenLine{
    private boolean isCorrupted;
    private int syntaxErrorScore;

    private final boolean isIncomplete;
    private long completionScore;

    public BrokenLine(String string){
        char[] chars = string.toCharArray();
        Stack<Character> stack = new Stack<>();

        //Check for corruption
        for (char c : chars) {
            if (isCorrupted) break;
            switch (c){
                case '(', '[', '{', '<':
                    stack.push(c);
                    break;
                case ')', ']', '}', '>':
                    if(stack.pop() != getCounterpart(c)){
                        isCorrupted = true;
                        syntaxErrorScore = getSyntaxErrorScore(c);
                    }
                    break;
            }
        }

        //Check for completeness
        isIncomplete = !isCorrupted;
        if(isIncomplete){
            while (!stack.isEmpty()){
                completionScore = completionScore * 5 + getCompletionScore(stack.pop());
            }
        }
    }

    private char getCounterpart(char c){
        return switch (c) {
            case ')' -> '(';
            case ']' -> '[';
            case '}' -> '{';
            case '>' -> '<';
            default -> throw new RuntimeException("Counterpart does not exist");
        };
    }

    private int getSyntaxErrorScore(char c){
        return switch (c) {
            case ')' -> 3;
            case ']' -> 57;
            case '}' -> 1197;
            case '>' -> 25137;
            default -> throw new RuntimeException("Syntax error score does not exist");
        };
    }

    private int getCompletionScore(char c){
        return switch (c) {
            case '(' -> 1;
            case '[' -> 2;
            case '{' -> 3;
            case '<' -> 4;
            default -> throw new RuntimeException("Completion score does not exist");
        };
    }

    public int getSyntaxErrorScore() {
        return syntaxErrorScore;
    }

    public long getCompletionScore() {
        return completionScore;
    }

    public boolean isCorrupted() {
        return isCorrupted;
    }

    public boolean isIncomplete() {
        return isIncomplete;
    }
}

class NavigationSubsystem{
    private final List<BrokenLine> corruptedLines;
    private final List<BrokenLine> incompleteLines;

    public NavigationSubsystem(List<String> puzzleInput){
        corruptedLines = new ArrayList<>();
        incompleteLines = new ArrayList<>();

        //Initialize broken lines
        for (String string : puzzleInput) {
            BrokenLine brokenLine = new BrokenLine(string);
            if(brokenLine.isCorrupted())
                corruptedLines.add(brokenLine);
            if(brokenLine.isIncomplete())
                incompleteLines.add(brokenLine);
        }
    }

    public int getTotalSyntaxErrorScore(){
        return corruptedLines.stream().mapToInt(BrokenLine::getSyntaxErrorScore).sum();
    }

    public Long getMiddleCompletionScore(){
        return incompleteLines.stream().map(BrokenLine::getCompletionScore).sorted().collect(Collectors.toList()).get(incompleteLines.size() / 2);
    }
}