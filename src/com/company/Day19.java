package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day19 {

    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day19.txt"));
        BeaconScannerHandler handler = new BeaconScannerHandler(input);
        System.out.print("\nSearching for overlapping scanner regions...\n\n");
        System.out.printf("\nThere are %d beacons.\n", handler.getNumberOfBeacons());
        System.out.printf("\nThe largest taxicab distance between any two scanners is %d metres.\n", handler.getMaximumDistance());
    }
}

class BeaconScannerHandler{
    private final List<BeaconScanner> beaconScanners = new ArrayList<>();
    private final Map<Integer, Beacon> knownBeacons = new HashMap<>();

    public BeaconScannerHandler(List<String> input){
        List<String> scannerInput = new ArrayList<>();
        int number = 0;
        for (int i = 1; i < input.size(); i++) {
            scannerInput.add(input.get(i));
            if(i == input.size() - 1 || input.get(i + 1).isBlank()){
                beaconScanners.add(new BeaconScanner(this, scannerInput, number, number == 0));
                scannerInput.clear();
                number++;
                i += 2;
            }
        }
    }

    public int getNumberOfBeacons(){
        beaconScanners.get(0).checkAllScannersForOverLap();
        return knownBeacons.size();
    }

    public int getMaximumDistance(){
        int max = 0;
        for (int i = 0; i < beaconScanners.size(); i++) {
            for (int j = i+1; j < beaconScanners.size(); j++) {
                int distance = LinAlg.getTaxicabDistance(LinAlg.subtractVectors(beaconScanners.get(i).getPosition(), beaconScanners.get(j).getPosition()));
                if(distance > max)
                    max = distance;
            }
        }
        return max;
    }

    public List<BeaconScanner> getBeaconScanners() {
        return beaconScanners;
    }

    public Map<Integer, Beacon> getKnownBeacons() {
        return knownBeacons;
    }
}

class BeaconScanner{
    private final int number;
    private final List<Beacon> children = new ArrayList<>();
    private int[] position;
    private boolean isPositionFound;
    BeaconScannerHandler handler;

    public BeaconScanner(BeaconScannerHandler handler, List<String> input, int number, boolean useAbsoluteLocation){
        this.handler = handler;
        this.number = number;
        for (String s : input) {
            int[] coordinates = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
            if(useAbsoluteLocation){
                position = new int[]{0,0,0};
                isPositionFound = true;
            }else{
                isPositionFound = false;
            }
            children.add(new Beacon(handler, coordinates, useAbsoluteLocation));
        }
    }

    public void checkAllScannersForOverLap(){
        for (BeaconScanner scanner : handler.getBeaconScanners()) {
            if(!scanner.isPositionFound()) checkScannerForOverlap(scanner);
        }
    }

    public void checkScannerForOverlap(BeaconScanner otherScanner){
        List<Beacon> beaconsFromThisScanner = children;
        List<Beacon> beaconsFromOtherScanner = otherScanner.getChildren();
        for (Beacon beacon1 : beaconsFromThisScanner) {
            int[] distancesFromBeacon1 = beaconsFromThisScanner.stream().mapToInt(e -> LinAlg.getSquaredMagnitude(LinAlg.subtractVectors(beacon1.getAbsoluteCoordinates(), e.getAbsoluteCoordinates()))).toArray();
            for (Beacon beacon2 : beaconsFromOtherScanner) {
                int[] distancesFromBeacon2 = beaconsFromOtherScanner.stream().mapToInt(e -> LinAlg.getSquaredMagnitude(LinAlg.subtractVectors(beacon2.getRelativeCoordinates(), e.getRelativeCoordinates()))).toArray();
                List<Beacon> overlapForThisScanner = new ArrayList<>(), overlapForOtherScanner = new ArrayList<>();
                for (int i = 0; i < distancesFromBeacon1.length; i++) {
                    for (int j = 0; j < distancesFromBeacon2.length; j++) {
                        if(distancesFromBeacon1[i] == distancesFromBeacon2[j]){
                            overlapForThisScanner.add(beaconsFromThisScanner.get(i));
                            overlapForOtherScanner.add(beaconsFromOtherScanner.get(j));
                        }
                    }
                }
                if(overlapForThisScanner.size() >= 12){
                    System.out.printf("Scanners regions %d and %d overlap.\n", number, otherScanner.getNumber());
                    otherScanner.updatePositions(overlapForThisScanner, overlapForOtherScanner);
                    otherScanner.checkAllScannersForOverLap();
                    return;
                }
            }
        }
    }

    public void updatePositions(List<Beacon> beacons1, List<Beacon> beacons2){
        int[] vector1 = LinAlg.subtractVectors(beacons1.get(1).getAbsoluteCoordinates(), beacons1.get(0).getAbsoluteCoordinates());
        int[] vector2 = LinAlg.subtractVectors(beacons2.get(1).getRelativeCoordinates(), beacons2.get(0).getRelativeCoordinates());
        int[][] rotationMatrix = null;
        boolean isOrientationFound = false;

        //Search orientation
        // TODO: 19/12/2021 Reduce this to just 24 loops instead of 64
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    rotationMatrix = LinAlg.getRotationMatrix(Math.PI / 2 * i, Math.PI / 2 * j, Math.PI / 2 * k);
                    if(LinAlg.areVectorsEqual(LinAlg.transformVector(vector2, rotationMatrix), vector1)) {
                        isOrientationFound = true;
                        break;
                    }
                }
                if(isOrientationFound) break;
            }
            if(isOrientationFound) break;
        }
        if(!isOrientationFound) throw new RuntimeException("Orientation not found");

        //Find positions
        position = LinAlg.subtractVectors(beacons1.get(0).getAbsoluteCoordinates(), LinAlg.transformVector(beacons2.get(0).getRelativeCoordinates(), rotationMatrix));
        isPositionFound = true;
        for (Beacon beacon : children) {
            beacon.setAbsoluteCoordinates(position, rotationMatrix);
        }
    }

    public List<Beacon> getChildren() {
        return children;
    }

    public int getNumber() {
        return number;
    }

    public int[] getPosition() {
        return position;
    }

    public boolean isPositionFound() {
        return isPositionFound;
    }
}

class Beacon{
    private final int[] relativeCoordinates = new int[3];
    private int[] absoluteCoordinates = new int[3];
    private boolean found = false;
    BeaconScannerHandler handler;

    public Beacon(BeaconScannerHandler handler, int[] coordinates, boolean useAbsolute){
        this.handler = handler;
        System.arraycopy(coordinates, 0, relativeCoordinates, 0, 3);
        if(useAbsolute){
            System.arraycopy(coordinates, 0, absoluteCoordinates, 0, 3);
            handler.getKnownBeacons().putIfAbsent(LinAlg.getSquaredMagnitude(absoluteCoordinates), this);
        }
    }

    public int[] getRelativeCoordinates(){
        return relativeCoordinates;
    }

    public int[] getAbsoluteCoordinates() {
        return absoluteCoordinates;
    }

    public void setAbsoluteCoordinates(int[] originPosition, int[][] rotation) {
        if(found) return;
        found = true;
        absoluteCoordinates = LinAlg.addVectors(LinAlg.transformVector(relativeCoordinates, rotation), originPosition);
        handler.getKnownBeacons().putIfAbsent(LinAlg.getSquaredMagnitude(absoluteCoordinates), this);
    }
}