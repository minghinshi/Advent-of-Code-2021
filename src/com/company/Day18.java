package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day18 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day18.txt"));
        MathHomework mathHomework = new MathHomework(input);
        SnailfishNumber sum = mathHomework.getSum();
        SnailfishNumber max = mathHomework.getMax();
        System.out.printf("\nThe sum of all the numbers in order is:\n%s\nIt has a magnitude of %d.\n", sum, sum.getMagnitude());
        System.out.printf("\nThe maximum of the sum of two numbers is:\n%s\nIt has a magnitude of %d.\n", max, max.getMagnitude());
    }
}

class MathHomework {
    private final List<String> input;

    public MathHomework(List<String> input) {
        this.input = input;
    }

    public SnailfishNumber getSum() {
        SnailfishNumber result = new SnailfishNumber(input.get(0));
        for (int i = 1; i < input.size(); i++) {
            result = result.add(new SnailfishNumber(input.get(i)));
        }
        return result;
    }

    public SnailfishNumber getMax() {
        int max = 0;
        SnailfishNumber result = null;
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (i == j) continue;
                SnailfishNumber number = new SnailfishNumber(input.get(i)).add(new SnailfishNumber(input.get(j)));
                int magnitude = number.getMagnitude();
                if (magnitude > max) {
                    result = number;
                    max = magnitude;
                }
            }
        }
        return result;
    }
}

//Binary tree
class SnailfishNumber {
    private SnailfishNumber parent, leftChild, rightChild;
    private boolean isLiteralValue;
    private int value, height;

    public SnailfishNumber(String string) {
        this(string, null);
    }

    public SnailfishNumber(int value, SnailfishNumber parent) {
        this(String.valueOf(value), parent);
    }

    public SnailfishNumber(String string, SnailfishNumber parent) {
        this.parent = parent;
        height = (parent == null ? 0 : parent.getHeight() + 1);
        try {
            value = Integer.parseInt(string);
            isLiteralValue = true;
        } catch (NumberFormatException e) {
            isLiteralValue = false;
        }

        char[] chars = string.toCharArray();
        int searchingLayer = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '[') searchingLayer++;
            if (chars[i] == ']') searchingLayer--;
            if (chars[i] == ',' && searchingLayer == 1) {
                leftChild = new SnailfishNumber(string.substring(1, i), this);
                rightChild = new SnailfishNumber(string.substring(i + 1, string.length() - 1), this);
            }
        }

    }

    public SnailfishNumber(SnailfishNumber leftChild, SnailfishNumber rightChild, SnailfishNumber parent) {
        if (leftChild.getHeight() != rightChild.getHeight())
            throw new RuntimeException("Cannot create parent of 2 numbers at different layers");
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
        isLiteralValue = false;
        height = leftChild.getHeight();
        leftChild.shiftUp();
        rightChild.shiftUp();
        leftChild.setParent(this);
        rightChild.setParent(this);
    }

    public void reduce() {
        while (tryReduce()) ;
    }

    public boolean tryReduce() {
        if (tryExplode()) return true;
        return trySplit();
    }

    public boolean tryExplode() {
        if (!isLiteralValue && height == 4) {
            explode();
            return true;
        }
        if (!isLiteralValue) {
            if (leftChild.tryExplode()) return true;
            return rightChild.tryExplode();
        }
        return false;
    }

    public boolean trySplit() {
        if (isLiteralValue && value >= 10) {
            split();
            return true;
        }
        if (!isLiteralValue) {
            if (leftChild.trySplit()) return true;
            return rightChild.trySplit();
        }
        return false;
    }

    private void split() {
        leftChild = new SnailfishNumber(value / 2, this);
        rightChild = new SnailfishNumber(value / 2 + (value % 2), this);
        isLiteralValue = false;
    }

    public void explode() {
        addToSide(false);
        addToSide(true);
        leftChild = null;
        rightChild = null;
        isLiteralValue = true;
        value = 0;
    }

    public void addToSide(boolean toRight) {
        SnailfishNumber targetNumber = this, parent = this.getParent();
        while (parent != null && (toRight ? parent.getRightChild() : parent.getLeftChild()) == targetNumber) {
            targetNumber = parent;
            parent = parent.getParent();
        }
        if (parent == null) return;
        targetNumber = (toRight ? parent.getRightChild() : parent.getLeftChild());
        while (!targetNumber.isLiteralValue())
            targetNumber = (toRight ? targetNumber.getLeftChild() : targetNumber.getRightChild());
        targetNumber.add(toRight ? rightChild.getValue() : leftChild.getValue());
    }

    public void shiftUp() {
        height++;
        if (leftChild != null) leftChild.shiftUp();
        if (rightChild != null) rightChild.shiftUp();
    }

    public SnailfishNumber add(SnailfishNumber term) {
        SnailfishNumber result = new SnailfishNumber(this, term, parent);
        result.reduce();
        return result;
    }

    public int add(int number) {
        if (!isLiteralValue) throw new RuntimeException("Attempt to add number to pair");
        value += number;
        return value;
    }

    public int getMagnitude() {
        return (isLiteralValue ? value : 3 * leftChild.getMagnitude() + 2 * rightChild.getMagnitude());
    }

    public int getHeight() {
        return height;
    }

    public int getValue() {
        return value;
    }

    public SnailfishNumber getParent() {
        return parent;
    }

    public void setParent(SnailfishNumber parent) {
        this.parent = parent;
    }

    public SnailfishNumber getLeftChild() {
        return leftChild;
    }

    public SnailfishNumber getRightChild() {
        return rightChild;
    }

    public boolean isLiteralValue() {
        return isLiteralValue;
    }

    @Override
    public String toString() {
        return (isLiteralValue ? String.valueOf(value) : "[" + leftChild.toString() + "," + rightChild.toString() + "]");
    }
}