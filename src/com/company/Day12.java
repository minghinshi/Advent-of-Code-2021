package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day12 {
    public static void main(String[] args) throws IOException {
        CaveMap caveMap = new CaveMap(Files.readAllLines(Paths.get("puzzleInputs/Day12.txt")));
        System.out.println("\nFinding paths that visit small caves at most once...");
        System.out.printf("Found %d paths.\n", caveMap.getNumberOfPaths(false));
        System.out.println("\nFinding paths that visit one small cave at most twice...");
        System.out.printf("Found %d paths.\n", caveMap.getNumberOfPaths(true));
    }
}

class CaveMap {
    HashMap<String, Cave> caveHashMap = new HashMap<>();

    public CaveMap(List<String> input) {
        for (String str : input) {
            String[] caveNames = str.split("-");
            caveHashMap.putIfAbsent(caveNames[0], new Cave(caveNames[0]));
            caveHashMap.putIfAbsent(caveNames[1], new Cave(caveNames[1]));
            caveHashMap.get(caveNames[0]).addConnection(caveHashMap.get(caveNames[1]));
            caveHashMap.get(caveNames[1]).addConnection(caveHashMap.get(caveNames[0]));
        }
    }

    public int getNumberOfPaths(boolean allowVisitingSmallCavesTwice) {
        Path path = new Path(caveHashMap.get("start"), allowVisitingSmallCavesTwice);
        return path.getNumberOfPathsFromHere();
    }
}

class Cave {
    String caveName;
    boolean isBig;
    List<Cave> connectedCaves = new ArrayList<>();

    public Cave(String caveName) {
        this.caveName = caveName;
        isBig = caveName.charAt(0) >= 'A' && caveName.charAt(0) <= 'Z';
    }

    public void addConnection(Cave cave) {
        connectedCaves.add(cave);
    }

    public List<Cave> getConnectedCaves() {
        return connectedCaves;
    }

    public boolean isBig() {
        return isBig;
    }

    public String getCaveName() {
        return caveName;
    }

    @Override
    public String toString() {
        return caveName;
    }
}

class Path {
    List<Cave> visitedCaves;
    List<Path> childPaths = new ArrayList<>();
    Cave previousCave;
    boolean isTerminal;
    boolean visitedSmallCaveTwice;

    public Path(Cave cave, boolean allowVisitingSmallCaveTwice) {
        visitedCaves = new ArrayList<>();
        visitedCaves.add(cave);
        previousCave = cave;
        isTerminal = false;
        visitedSmallCaveTwice = !allowVisitingSmallCaveTwice;
        findChildrenPaths();
    }

    public Path(List<Cave> caves, Cave previousCave, boolean visitedSmallCaveTwice) {
        visitedCaves = caves;
        if (!previousCave.isBig() && visitedCaves.contains(previousCave)) this.visitedSmallCaveTwice = true;
        else this.visitedSmallCaveTwice = visitedSmallCaveTwice;
        this.previousCave = previousCave;
        visitedCaves.add(previousCave);
        isTerminal = previousCave.getCaveName().equals("end");
        findChildrenPaths();
    }

    public void findChildrenPaths() {
        if (isTerminal) return;
        for (Cave connectedCave : previousCave.getConnectedCaves()) {
            if (connectedCave.getCaveName().equals("start")) continue;
            if (connectedCave.isBig() || !visitedCaves.contains(connectedCave) || !visitedSmallCaveTwice) {
                List<Cave> visitedCaves = new ArrayList<>(this.visitedCaves);
                Path childPath = new Path(visitedCaves, connectedCave, visitedSmallCaveTwice);
                childPaths.add(childPath);
            }
        }
    }

    public int getNumberOfPathsFromHere() {
        if (isTerminal) return 1;
        int count = 0;
        for (Path childPath : childPaths) {
            count += childPath.getNumberOfPathsFromHere();
        }
        return count;
    }
}