package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day09 {
    public static void main(String[] args) throws IOException {
        HeightMap heightMap = new HeightMap(Files.readAllLines(Paths.get("puzzleInputs/Day09.txt")));
        System.out.printf("\nThe total risk level of all low points is %d.\n", heightMap.getTotalRiskLevel());
        List<Integer> basinSizes = heightMap.getBasinSizes();
        System.out.printf("\nThe 3 largest basins have sizes %d, %d and %d.\n(Part 2 solution: %d)\n", basinSizes.get(0), basinSizes.get(1), basinSizes.get(2), basinSizes.get(0) * basinSizes.get(1) * basinSizes.get(2));
    }
}


class HeightMap {
    private final List<Location> lowPoints;
    private final List<Basin> basins;

    public HeightMap(List<String> input) {
        int height = input.size();
        int length = input.get(0).length();
        Location[][] locationGrid = new Location[height][length];
        lowPoints = new ArrayList<>();
        basins = new ArrayList<>();

        //First round of initialization
        for (int i = 0; i < height; i++) {
            char[] chars = input.get(i).toCharArray();
            for (int j = 0; j < length; j++) {
                locationGrid[i][j] = new Location(chars[j] - 48);
            }
        }

        //Second round of initialization
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                Location location = locationGrid[i][j];
                for (int k = i - 1; k <= i + 1; k++) {
                    if (k == i || k < 0 || k >= height) continue;
                    location.addNeighbour(locationGrid[k][j]);
                }
                for (int l = j - 1; l <= j + 1; l++) {
                    if (l == j || l < 0 || l >= length) continue;
                    location.addNeighbour(locationGrid[i][l]);
                }
                location.setLowPoint();
                if (location.isLowPoint()) {
                    lowPoints.add(location);
                }
            }
        }

        //Initialize basins
        for (Location lowPoint : lowPoints) {
            basins.add(new Basin(lowPoint));
        }
    }

    public int getTotalRiskLevel() {
        int total = 0;
        for (Location location : lowPoints) {
            total += location.getRiskLevel();
        }
        return total;
    }

    public List<Integer> getBasinSizes() {
        return basins.stream().map(Basin::getSize).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }
}

class Location {
    private final int height;
    private final int riskLevel;
    private final List<Location> neighbours;
    private boolean isLowPoint = true;
    private Basin basin;

    public Location(int height) {
        this.height = height;
        riskLevel = height + 1;
        neighbours = new ArrayList<>();
    }

    public void setLowPoint() {
        for (Location neighbour : neighbours) {
            if (height >= neighbour.getHeight()) {
                isLowPoint = false;
                break;
            }
        }
    }

    public void setBasin(Basin basin) {
        if (this.basin != null || height == 9) return;
        this.basin = basin;
        basin.addLocation(this);
        for (Location neighbour : neighbours) {
            neighbour.setBasin(basin);
        }
    }

    public void addNeighbour(Location location) {
        neighbours.add(location);
    }

    public int getHeight() {
        return height;
    }

    public int getRiskLevel() {
        return riskLevel;
    }

    public boolean isLowPoint() {
        return isLowPoint;
    }
}

class Basin {
    private final List<Location> locations;

    public Basin(Location lowPoint) {
        locations = new ArrayList<>();
        lowPoint.setBasin(this);
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public int getSize() {
        return locations.size();
    }
}