package com.company;

import java.util.Arrays;
import java.util.Objects;

class Vector {
    private final int[] components;
    private final int dimensions;

    public Vector() {
        components = new int[]{0, 0, 0};
        dimensions = 3;
    }

    public Vector(int... components) {
        this.components = components;
        dimensions = components.length;
    }

    public Vector add(Vector other) {
        if (dimensions != other.getDimensions())
            throw new RuntimeException("Vectors of different dimensions cannot be added");
        int[] otherComponents = other.getComponents();
        int[] newComponents = new int[dimensions];
        for (int i = 0; i < dimensions; i++)
            newComponents[i] = components[i] + otherComponents[i];
        return new Vector(newComponents);
    }

    public Vector subtract(Vector other) {
        if (dimensions != other.getDimensions())
            throw new RuntimeException("Vectors of different dimensions cannot be added");
        int[] otherComponents = other.getComponents();
        int[] newComponents = new int[dimensions];
        for (int i = 0; i < dimensions; i++)
            newComponents[i] = components[i] - otherComponents[i];
        return new Vector(newComponents);
    }

    public Vector transform(Matrix t) {
        int[][] table = t.getTable();
        int[] newComponents = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                newComponents[i] += components[j] * table[j][i];
            }
        }
        return new Vector(newComponents);
    }

    public int squareMagnitude() {
        return Arrays.stream(components).map(x -> x * x).sum();
    }

    public int taxicabDistance() {
        return Arrays.stream(components).map(Math::abs).sum();
    }


    public int[] getComponents() {
        return components;
    }

    public int getDimensions() {
        return dimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return dimensions == vector.dimensions && Arrays.equals(components, vector.components);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions);
        result = 31 * result + Arrays.hashCode(components);
        return result;
    }
}

class Matrix {
    private final int[][] table;
    private final int rows;
    private final int columns;

    public Matrix(int[][] table) {
        this.table = table;
        columns = table.length;
        rows = table[0].length;
    }

    public Matrix(double x, double y, double z) {
        Matrix xRotation = new Matrix(new int[][]{{1, 0, 0}, {0, (int) Math.round(Math.cos(x)), (int) Math.round(-Math.sin(x))}, {0, (int) Math.round(Math.sin(x)), (int) Math.round(Math.cos(x))}});
        Matrix yRotation = new Matrix(new int[][]{{(int) Math.round(Math.cos(y)), 0, (int) Math.round(Math.sin(y))}, {0, 1, 0}, {(int) Math.round(-Math.sin(y)), 0, (int) Math.round(Math.cos(y))}});
        Matrix zRotation = new Matrix(new int[][]{{(int) Math.round(Math.cos(z)), (int) Math.round(-Math.sin(z)), 0}, {(int) Math.round(Math.sin(z)), (int) Math.round(Math.cos(z)), 0}, {0, 0, 1}});
        Matrix result = xRotation.multiply(yRotation).multiply(zRotation);
        table = result.table;
        rows = result.rows;
        columns = result.columns;
    }

    public Matrix multiply(Matrix other) {
        if (rows != other.getColumns()) throw new RuntimeException("Matrices cannot be multiplied");
        int[][] otherTable = other.getTable();
        int[][] newTable = new int[columns][other.getRows()];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < other.getRows(); j++) {
                int sum = 0;
                for (int k = 0; k < rows; k++) {
                    sum += table[i][k] * otherTable[k][j];
                }
                newTable[i][j] = sum;
            }
        }
        return new Matrix(newTable);
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int[][] getTable() {
        return table;
    }
}