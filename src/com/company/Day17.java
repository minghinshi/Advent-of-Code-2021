package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day17 {
    public static void main(String[] args) throws IOException {
        ProbeLauncher probeLauncher = new ProbeLauncher(Files.readAllLines(Paths.get("puzzleInputs/Day17.txt")).get(0));
        System.out.printf("\nThe highest possible height is %.2f metres.\nThere are %d possible trajectories.\n", probeLauncher.getHighestYPosition() * 0.01, probeLauncher.getNumberOfTrajectories());
    }
}

class ProbeLauncher {
    // Notice the lack of maximum X velocity and minimum Y velocity.
    // This is because they are bounded by the target area, so it is redundant to use variables to store them.
    int targetMinX, targetMaxX, targetMinY, targetMaxY, minXVel, maxYVel;

    public ProbeLauncher(String input) {
        String[] strings = input.substring(13).split(", ");
        String[] stringsX = strings[0].substring(2).split("\\.\\.");
        targetMinX = Integer.parseInt(stringsX[0]);
        targetMaxX = Integer.parseInt(stringsX[1]);
        String[] stringsY = strings[1].substring(2).split("\\.\\.");
        targetMinY = Integer.parseInt(stringsY[0]);
        targetMaxY = Integer.parseInt(stringsY[1]);
        minXVel = (int) Math.ceil((Math.sqrt(1 + 8 * targetMinX) - 1) / 2);    //Quadratic equation and arithmetic series
        maxYVel = -targetMinY - 1;
    }

    int getHighestYPosition() {
        return (1 + maxYVel) * maxYVel / 2; //Arithmetic series
    }

    int getNumberOfTrajectories() {
        int count = 0;
        for (int XVel = minXVel; XVel <= targetMaxX; XVel++)
            for (int YVel = targetMinY; YVel <= maxYVel; YVel++)
                count += (new Probe(XVel, YVel).canHitTarget(targetMinX, targetMaxX, targetMinY, targetMaxY) ? 1 : 0);
        return count;
    }
}

class Probe {
    int XPos = 0, YPos = 0, XVel, YVel;

    public Probe(int XVel, int YVel) {
        this.XVel = XVel;
        this.YVel = YVel;
    }

    public boolean canHitTarget(int targetMinX, int targetMaxX, int targetMinY, int targetMaxY) {
        while (XPos <= targetMaxX && YPos >= targetMinY) {
            XPos += XVel;
            YPos += YVel;
            XVel -= (XVel == 0 ? 0 : 1);
            YVel--;
            if (XPos >= targetMinX && XPos <= targetMaxX && YPos >= targetMinY && YPos <= targetMaxY) return true;
        }
        return false;
    }
}