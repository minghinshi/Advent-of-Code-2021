package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day22 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day22.txt"));
        Reactor reactor = new Reactor(input);
        System.out.printf("\nAfter initialization, %d cubes are on.\n", reactor.sumAll(20));
        System.out.printf("After rebooting, %d cubes are on.\n", reactor.sumAll());
    }
}

class Reactor{
    List<Region> regions = new ArrayList<>();
    List<long[]> lowerBounds = new ArrayList<>();
    List<long[]> upperBounds = new ArrayList<>();

    public Reactor(List<String> input){
        for (String string : input) {
            regions.add(new Region(string));
            lowerBounds = regions.stream().map(Region::getLowerBound).collect(Collectors.toList());
            upperBounds = regions.stream().map(Region::getUpperBound).collect(Collectors.toList());
        }
    }

    public long sumAll(){
        return sumAll(regions.size());
    }

    public long sumAll(int numberOfRegions){
        long total = 0;
        for (int i = 0; i < numberOfRegions; i++) {
            total += countNewLights(i);
        }
        return total;
    }

    public long countNewLights(int end){
        List<Integer> intersections = new ArrayList<>();
        intersections.add(end);
        return countNewLights(intersections);
    }

    public long countNewLights(List<Integer> intersections){
        long minX = intersections.stream().map(this.lowerBounds::get).mapToLong(x -> x[0]).max().orElseThrow();
        long maxX = intersections.stream().map(this.upperBounds::get).mapToLong(x -> x[0]).min().orElseThrow();
        long minY = intersections.stream().map(this.lowerBounds::get).mapToLong(x -> x[1]).max().orElseThrow();
        long maxY = intersections.stream().map(this.upperBounds::get).mapToLong(x -> x[1]).min().orElseThrow();
        long minZ = intersections.stream().map(this.lowerBounds::get).mapToLong(x -> x[2]).max().orElseThrow();
        long maxZ = intersections.stream().map(this.upperBounds::get).mapToLong(x -> x[2]).min().orElseThrow();

        if(minX > maxX || minY > maxY || minZ > maxZ)
            return 0L;
        else{
            long volume = regions.get(intersections.get(intersections.size()-1)).isTurnOn ? (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1) : 0;
            for (int i = intersections.get(intersections.size()-1)-1; i >= 0; i--) {
                List<Integer> newIntersections = new ArrayList<>(intersections);
                newIntersections.add(i);
                volume -= countNewLights(newIntersections);
            }
            return volume;
        }
    }
}

class Region{
    long[] lowerBound = new long[3], upperBound = new long[3];
    boolean isTurnOn;

    public Region(String input){
        String[] strings1 = input.split(" ");
        isTurnOn = strings1[0].equals("on");
        String[] strings2 = strings1[1].split(",");
        for (int i = 0; i < 3; i++) {
            String[] boundsStrings = strings2[i].split("\\.\\.");
            lowerBound[i] = Integer.parseInt(boundsStrings[0].substring(2));
            upperBound[i] = Integer.parseInt(boundsStrings[1]);
        }
    }

    public long getVolume(){
        long volume = 1L;
        for (int i = 0; i < 3; i++) {
            volume *= upperBound[i] - lowerBound[i] + 1;
        }
        return volume;
    }

    public long[] getLowerBound() {
        return lowerBound;
    }

    public long[] getUpperBound() {
        return upperBound;
    }
}