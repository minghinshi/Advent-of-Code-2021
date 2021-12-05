package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day05 {
    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day05.txt"));
        List<LineOfVents> linesOfVents = new ArrayList<>();
        int[][] gridOfVents = new int[1000][1000];
        int overlaps = 0;

        //Adding lines
        for (String string : content) {
            linesOfVents.add(new LineOfVents(string));
        }

        //Part 1
        for (LineOfVents line : linesOfVents) {
            gridOfVents = line.fillVents(gridOfVents, false);
        }

        for (int[] rowOfVent : gridOfVents)
            for (int vents : rowOfVent)
                overlaps += (vents >= 2 ? 1 : 0);
        System.out.printf("\nConsidering horizontal and vertical lines only...\nThere are %d dangerous areas.\n", overlaps);

        //Part 2
        overlaps = 0;

        for (LineOfVents line : linesOfVents) {
            gridOfVents = line.fillVents(gridOfVents, true);
        }

        for (int[] rowOfVent : gridOfVents)
            for (int vents : rowOfVent)
                overlaps += (vents >= 2 ? 1 : 0);
        System.out.printf("\nConsidering all of the lines...\nThere are %d dangerous areas.\n", overlaps);
    }
}

class LineOfVents{
    int xStart;
    int yStart;
    int xEnd;
    int yEnd;

    int slope = 0;
    int xIntercept;
    int yIntercept;

    boolean isVertical = false;

    public LineOfVents(String s){
        String[] strings = s.split(" -> ");
        String[] start = strings[0].split(",");
        xStart = Integer.parseInt(start[0]);
        yStart = Integer.parseInt(start[1]);
        String[] end = strings[1].split(",");
        xEnd = Integer.parseInt(end[0]);
        yEnd = Integer.parseInt(end[1]);

        if (xStart == xEnd) {
            isVertical = true;
            xIntercept = xStart;
        } else {
            slope = (yEnd - yStart) / (xEnd - xStart);
            yIntercept = yStart - slope * xStart;   //y = mx + c => c = y - mx
        }
    }

    public int[][] fillVents(int[][] grid, boolean calculateDiagonals){
        if(!calculateDiagonals) {
            if (isVertical)
                for (int y = Math.min(yStart, yEnd); y <= Math.max(yStart, yEnd); y++)
                    grid[xIntercept][y]++;
            else if (slope == 0)
                for (int x = Math.min(xStart, xEnd); x <= Math.max(xStart, xEnd); x++)
                    grid[x][yIntercept]++;
        } else if (slope != 0) {
            for (int x = Math.min(xStart, xEnd); x <= Math.max(xStart, xEnd); x++)
                grid[x][slope * x + yIntercept]++;
        }
        return grid;
    }
}

