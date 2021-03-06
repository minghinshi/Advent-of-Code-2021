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

class BeaconScannerHandler {
    private final List<BeaconScanner> beaconScanners = new ArrayList<>();
    private final Map<Integer, Beacon> knownBeacons = new HashMap<>();

    public BeaconScannerHandler(List<String> input) {
        List<String> scannerInput = new ArrayList<>();
        int number = 0;
        for (int i = 1; i < input.size(); i++) {
            scannerInput.add(input.get(i));
            if (i == input.size() - 1 || input.get(i + 1).isBlank()) {
                beaconScanners.add(new BeaconScanner(this, scannerInput, number, number == 0));
                scannerInput.clear();
                number++;
                i += 2;
            }
        }
    }

    public int getNumberOfBeacons() {
        beaconScanners.get(0).checkAllScannersForOverLap();
        return knownBeacons.size();
    }

    public int getMaximumDistance() {
        int max = 0;
        for (int i = 0; i < beaconScanners.size(); i++) {
            for (int j = i + 1; j < beaconScanners.size(); j++) {
                int distance = beaconScanners.get(i).getPosition().subtract(beaconScanners.get(j).getPosition()).taxicabDistance();
                if (distance > max) max = distance;
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

class BeaconScanner {
    private final int number;
    private final List<Beacon> children = new ArrayList<>();
    private final BeaconScannerHandler handler;
    private Vector position;
    private boolean isPositionFound;

    public BeaconScanner(BeaconScannerHandler handler, List<String> input, int number, boolean useAbsoluteLocation) {
        this.handler = handler;
        this.number = number;
        for (String s : input) {
            int[] coordinates = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
            if (useAbsoluteLocation) {
                position = new Vector();
                isPositionFound = true;
            } else {
                isPositionFound = false;
            }
            children.add(new Beacon(handler, coordinates, useAbsoluteLocation));
        }
    }

    public void checkAllScannersForOverLap() {
        for (BeaconScanner scanner : handler.getBeaconScanners()) {
            if (!scanner.isPositionFound()) checkScannerForOverlap(scanner);
        }
    }

    public void checkScannerForOverlap(BeaconScanner otherScanner) {
        List<Beacon> beaconsFromThisScanner = children;
        List<Beacon> beaconsFromOtherScanner = otherScanner.getChildren();
        for (Beacon beacon1 : beaconsFromThisScanner) {
            int[] distancesFromBeacon1 = beaconsFromThisScanner.stream().mapToInt(e -> beacon1.getAbsoluteCoordinates().subtract(e.getAbsoluteCoordinates()).squareMagnitude()).toArray();
            for (Beacon beacon2 : beaconsFromOtherScanner) {
                int[] distancesFromBeacon2 = beaconsFromOtherScanner.stream().mapToInt(e -> beacon2.getRelativeCoordinates().subtract(e.getRelativeCoordinates()).squareMagnitude()).toArray();
                List<Beacon> overlapForThisScanner = new ArrayList<>(), overlapForOtherScanner = new ArrayList<>();
                for (int i = 0; i < distancesFromBeacon1.length; i++) {
                    for (int j = 0; j < distancesFromBeacon2.length; j++) {
                        if (distancesFromBeacon1[i] == distancesFromBeacon2[j]) {
                            overlapForThisScanner.add(beaconsFromThisScanner.get(i));
                            overlapForOtherScanner.add(beaconsFromOtherScanner.get(j));
                        }
                    }
                }
                if (overlapForThisScanner.size() >= 12) {
                    System.out.printf("Scanners regions %d and %d overlap.\n", number, otherScanner.getNumber());
                    otherScanner.updatePositions(overlapForThisScanner, overlapForOtherScanner);
                    otherScanner.checkAllScannersForOverLap();
                    return;
                }
            }
        }
    }

    public void updatePositions(List<Beacon> beacons1, List<Beacon> beacons2) {
        Vector vector1 = beacons1.get(1).getAbsoluteCoordinates().subtract(beacons1.get(0).getAbsoluteCoordinates());
        Vector vector2 = beacons2.get(1).getRelativeCoordinates().subtract(beacons2.get(0).getRelativeCoordinates());
        Matrix rotationMatrix = null;
        boolean isOrientationFound = false;

        //Search orientation
        // TODO: 19/12/2021 Reduce this to just 24 loops instead of 64
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    rotationMatrix = new Matrix(Math.PI / 2 * i, Math.PI / 2 * j, Math.PI / 2 * k);
                    if (vector1.equals(vector2.transform(rotationMatrix))) {
                        isOrientationFound = true;
                        break;
                    }
                }
                if (isOrientationFound) break;
            }
            if (isOrientationFound) break;
        }
        if (!isOrientationFound) throw new RuntimeException("Orientation not found");

        //Find positions
        position = beacons1.get(0).getAbsoluteCoordinates().subtract(beacons2.get(0).getRelativeCoordinates().transform(rotationMatrix));
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

    public Vector getPosition() {
        return position;
    }

    public boolean isPositionFound() {
        return isPositionFound;
    }
}

class Beacon {
    private final Vector relativeCoordinates;
    private final BeaconScannerHandler handler;
    private Vector absoluteCoordinates;
    private boolean found = false;

    public Beacon(BeaconScannerHandler handler, int[] coordinates, boolean useAbsolute) {
        this.handler = handler;
        relativeCoordinates = new Vector(coordinates);
        if (useAbsolute) {
            absoluteCoordinates = new Vector(coordinates);
            handler.getKnownBeacons().putIfAbsent(absoluteCoordinates.squareMagnitude(), this);
        }
    }

    public Vector getRelativeCoordinates() {
        return relativeCoordinates;
    }

    public Vector getAbsoluteCoordinates() {
        return absoluteCoordinates;
    }

    public void setAbsoluteCoordinates(Vector originPosition, Matrix rotation) {
        if (found) return;
        found = true;
        absoluteCoordinates = relativeCoordinates.transform(rotation).add(originPosition);
        handler.getKnownBeacons().putIfAbsent(absoluteCoordinates.squareMagnitude(), this);
    }
}