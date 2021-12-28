package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day24 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day24.txt"));
        MONADSolver monadSolver = new MONADSolver(input);
        System.out.printf("\nThe greatest model number is %d.\n", monadSolver.solve(false));
        System.out.printf("The smallest model number is %d.\n", monadSolver.solve(true));
    }
}

class MONADSolver {
    ALUModel aluModel;

    public MONADSolver(List<String> input) {
        aluModel = new ALUModel(input);
    }

    public long solve(boolean lowest) {
        return solve(new int[0], lowest);
    }

    public long solve(int[] currentNumber, boolean lowest) {
        int currentIndex = currentNumber.length;
        if (currentIndex == 14) {
            int[] aluOutput = aluModel.execute(currentNumber);
            if (aluOutput[3] == 0) {
                long solution = 0;
                for (int i : currentNumber) {
                    solution = solution * 10 + i;
                }
                return solution;
            }
        }
        if (aluModel.requireMatching[currentIndex]) {
            int[] input = new int[currentIndex + 1];
            System.arraycopy(currentNumber, 0, input, 0, currentIndex);
            int[] aluOutput = aluModel.execute(input, currentIndex * 18 + 6);
            if (aluOutput[1] >= 1 && aluOutput[1] <= 9) {
                input[currentIndex] = aluOutput[1];
                return solve(input, lowest);
            }
        } else {
            for (int i = 1; i <= 9; i++) {
                int[] newNumber = new int[currentIndex + 1];
                System.arraycopy(currentNumber, 0, newNumber, 0, currentIndex);
                newNumber[currentIndex] = (lowest ? i : 10 - i);
                long solution = solve(newNumber, lowest);
                if (solution != -1) return solution;
            }
        }
        return -1;
    }
}

class ALUModel {
    boolean[] requireMatching = new boolean[14];
    List<ALUInstruction> instructions;

    public ALUModel(List<String> puzzleInput) {
        for (int i = 0; i < 14; i++) {
            for (int j = i * 18; j < (i + 1) * 18; j++) {
                if (puzzleInput.get(j).contains("-")) {
                    requireMatching[i] = true;
                    break;
                }
            }
        }
        instructions = puzzleInput.stream().map(ALUInstruction::new).collect(Collectors.toList());
    }

    public int[] execute(int[] inputs) {
        return execute(inputs, instructions.size());
    }

    //ALU interpreter
    public int[] execute(int[] inputs, int haltAt) {
        int[] registers = new int[4];
        int inputPointer = 0;
        for (int i = 0; i < haltAt; i++) {
            ALUInstruction instruction = instructions.get(i);
            if (instruction.type == 0) {
                registers[instruction.operand1] = inputs[inputPointer];
                inputPointer++;
            } else {
                int otherOperand = (instruction.isLiteralValue ? instruction.operand2 : registers[instruction.operand2]);
                switch (instruction.type) {
                    case 1 -> registers[instruction.operand1] += otherOperand;
                    case 2 -> registers[instruction.operand1] *= otherOperand;
                    case 3 -> registers[instruction.operand1] /= otherOperand;
                    case 4 -> registers[instruction.operand1] %= otherOperand;
                    case 5 -> registers[instruction.operand1] = registers[instruction.operand1] == otherOperand ? 1 : 0;
                }
            }
        }
        return registers;
    }
}

class ALUInstruction {
    int type, operand1, operand2;
    boolean isLiteralValue;

    public ALUInstruction(String instruction) {
        String[] strings = instruction.split(" ");
        switch (strings[0]) {
            case "inp" -> type = 0;
            case "add" -> type = 1;
            case "mul" -> type = 2;
            case "div" -> type = 3;
            case "mod" -> type = 4;
            case "eql" -> type = 5;
        }
        operand1 = getRegister(strings[1]);
        if (type != 0) {
            try {
                operand2 = Integer.parseInt(strings[2]);
                isLiteralValue = true;
            } catch (NumberFormatException e) {
                operand2 = getRegister(strings[2]);
                isLiteralValue = false;
            }
        }
    }

    public int getRegister(String s) {
        return switch (s) {
            case "w" -> 0;
            case "x" -> 1;
            case "y" -> 2;
            case "z" -> 3;
            default -> throw new RuntimeException("Invalid register name!");
        };
    }
}