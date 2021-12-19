package com.company;

import java.util.Arrays;

class LinAlg {
    public static int[] transformVector(int[] vector, int[][] transformationMatrix){
        int[] resultant = new int[vector.length];
        for (int i = 0; i < resultant.length; i++) {
            for (int j = 0; j < resultant.length; j++) {
                resultant[i] += vector[j] * transformationMatrix[j][i];
            }
        }
        return resultant;
    }

    public static int[][] multiplyMatrix(int[][] a, int[][] b){
        if(a[0].length != b.length)
            throw new RuntimeException("Matrices cannot be multiplied");
        int[][] resultant = new int[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                int sum = 0;
                for (int k = 0; k < a[0].length; k++) {
                    sum += a[i][k] * b[k][j];
                }
                resultant[i][j] = sum;
            }
        }
        return resultant;
    }

    public static int[][] multiplyMatrix(int[][]... matrices){
        int[][] resultant = matrices[0];
        for (int i = 1; i < matrices.length; i++) {
            resultant = multiplyMatrix(resultant,matrices[i]);
        }
        return resultant;
    }

    public static int[] addVectors(int[] a, int[] b){
        if(a.length != b.length)
            throw new RuntimeException("Vectors cannot be added");
        int[] resultant = new int[a.length];
        for (int i = 0; i < a.length; i++)
            resultant[i] = a[i] + b[i];
        return resultant;
    }

    public static int[] subtractVectors(int[] a, int[] b){
        if(a.length != b.length)
            throw new RuntimeException("Vectors cannot be subtracted");
        int[] resultant = new int[a.length];
        for (int i = 0; i < a.length; i++)
            resultant[i] = a[i] - b[i];
        return resultant;
    }

    public static int getSquaredMagnitude(int[] vector){
        return Arrays.stream(vector).map(x -> x*x).sum();
    }

    public static int getTaxicabDistance(int[] vector){ return Arrays.stream(vector).map(Math::abs).sum(); }

    public static boolean areVectorsEqual(int[] a, int[] b){
        if(a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if(a[i] != b[i]) return false;
        }
        return true;
    }

    public static int[][] getRotationMatrix(double x, double y, double z){
        int[][] xRotation = new int[][]{{1,0,0},{0, (int) Math.round(Math.cos(x)), (int) Math.round(-Math.sin(x))}, {0, (int) Math.round(Math.sin(x)), (int) Math.round(Math.cos(x))}};
        int[][] yRotation = new int[][]{{(int) Math.round(Math.cos(y)), 0,  (int) Math.round(Math.sin(y))},{0,1,0},{(int) Math.round(-Math.sin(y)), 0,  (int) Math.round(Math.cos(y))}};
        int[][] zRotation = new int[][]{{(int) Math.round(Math.cos(z)), (int) Math.round(-Math.sin(z)), 0},{(int) Math.round(Math.sin(z)), (int) Math.round(Math.cos(z)), 0},{0,0,1}};
        return multiplyMatrix(xRotation, yRotation, zRotation);
    }
}