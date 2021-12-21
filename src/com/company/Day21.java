package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day21 {
    public static void main(String[] args) {
        DeterministicDice regularDice = new DeterministicDice(1,6);
        regularDice.stepAll();
        System.out.printf("\nDeterministic dice:\nDice rolled %d times.\nP1's score is %d.\nP2's score is %d.\n(Part 1 solution: %d)\n",
                regularDice.rolls, regularDice.player1Score, regularDice.player2Score, regularDice.getAnswer());

        DiracDice diracDice = new DiracDice(1, 6, 21);
        diracDice.stepAll();
        System.out.printf("\nDirac dice:\nP1 wins %d times.\nP2 wins %d times.\n(Part 2 solution: %d)\n",
                diracDice.player1Wins, diracDice.player2Wins, diracDice.getAnswer());
    }
}

class DeterministicDice{
    int player1Pos, player2Pos, player1Score = 0, player2Score = 0, moves = 6, rolls = 0;
    boolean isPlayer1Turn = true;

    public DeterministicDice(int player1Pos, int player2Pos){
        this.player1Pos = player1Pos;
        this.player2Pos = player2Pos;
    }

    public void stepAll(){
        while(player1Score < 1000 && player2Score < 1000)
            step();
    }

    public void step(){
        rolls += 3;
        if(isPlayer1Turn){
            player1Pos = (player1Pos + moves - 1) % 10 + 1;
            player1Score += player1Pos;
        }else{
            player2Pos = (player2Pos + moves - 1) % 10 + 1;
            player2Score += player2Pos;
        }
        moves = (moves + 9) % 10;
        isPlayer1Turn = !isPlayer1Turn;
    }

     public int getAnswer(){
        return Math.min(player1Score, player2Score) * rolls;
     }
}

class DiracDice{
    private static final Map<Integer, Integer> movesToCases = Map.of(3,1,4,3,5,6,6,7,7,6,8,3,9,1);

    //States are stored as [PLayer1Pos, Player2Pos, Player1Point, Player2Point]
    HashMap<Integer, Long> numberOfStates = new HashMap<>();
    HashMap<Integer, int[]> stateArray = new HashMap<>();
    int winningScore;
    long player1Wins, player2Wins;
    boolean isPlayer1Turn = true;

    public DiracDice(int player1Pos, int player2Pos, int winningScore){
        this.winningScore = winningScore;
        int[] initialState = new int[]{player1Pos, player2Pos, 0, 0};
        int hash = getStateHash(initialState);
        stateArray.put(hash, initialState);
        numberOfStates.put(hash, 1L);
    }

    public void stepAll(){
        while(numberOfStates.size() != 0)
            step();
    }

    public void step(){
        HashMap<Integer, Long> newNumberOfStates = new HashMap<>();
        int positionPointer = (isPlayer1Turn ? 0 : 1), scorePointer = (isPlayer1Turn ? 2 : 3);
        for (int key : numberOfStates.keySet()) {
            for (int moves : movesToCases.keySet()) {
                int[] newState = stateArray.get(key).clone();
                newState[positionPointer] = (newState[positionPointer] + moves - 1) % 10 + 1;
                newState[scorePointer] += newState[positionPointer];
                if(newState[scorePointer] >= winningScore){
                    if(isPlayer1Turn)
                        player1Wins += numberOfStates.get(key) * movesToCases.get(moves);
                    else
                        player2Wins += numberOfStates.get(key) * movesToCases.get(moves);
                }else{
                    int hash = getStateHash(newState);
                    stateArray.putIfAbsent(hash, newState);
                    newNumberOfStates.put(hash, numberOfStates.get(key) * movesToCases.get(moves) + newNumberOfStates.getOrDefault(hash, 0L));
                }
            }
        }
        numberOfStates = newNumberOfStates;
        isPlayer1Turn = !isPlayer1Turn;
    }

    public long getAnswer(){
        return Math.max(player1Wins, player2Wins);
    }

    public int getStateHash(int[] state){
        int hash = 0;
        for (int i = 0; i < 4; i++) {
            hash = hash * 256 + state[i];
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int key : numberOfStates.keySet()) {
            stringBuilder.append(Arrays.toString(stateArray.get(key))).append(": ").append(numberOfStates.get(key)).append("\n");
        }
        return stringBuilder.toString();
    }
}