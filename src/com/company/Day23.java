package com.company;

import java.util.*;

public class Day23 {
    public static void main(String[] args) {
        AmphipodQuestion amphipod1 = new AmphipodQuestion(new int[][]{{0, 3, 0, 2}, {2, 3, 1, 1}});
        System.out.printf("The least energy required for 8 amphipods is %d.\n", amphipod1.getEnergyRequired());
        AmphipodQuestion amphipod2 = new AmphipodQuestion(new int[][]{{0, 3, 0, 2}, {3, 2, 1, 0}, {3, 1, 0, 2}, {2, 3, 1, 1}});
        System.out.printf("The least energy required for 16 amphipods is %d.\n", amphipod2.getEnergyRequired());
    }
}

//This is, again, just Dijkstra's algorithm.
class AmphipodQuestion {
    long nodeHash;
    long target;
    List<Long> frontier = new ArrayList<>();
    Set<Long> explored = new HashSet<>();
    HashMap<Long, AmphipodNode> nodeMap = new HashMap<>();

    public AmphipodQuestion(int[][] sideRooms) {
        AmphipodNode initial = new AmphipodNode(sideRooms);
        nodeHash = initial.getHash();
        int[][] goalState = new int[sideRooms.length][4];
        for (int[] row : goalState) {
            for (int i = 0; i < 4; i++) {
                row[i] = i;
            }
        }
        target = new AmphipodNode(goalState).getHash();
        frontier.add(nodeHash);
        nodeMap.put(nodeHash, initial);
    }

    public int getEnergyRequired() {
        System.out.print("\nSearching for solution...\n");
        long nanoTime = System.nanoTime();
        while (true) {
            if (frontier.size() == 0) throw new RuntimeException("Solution not found!");
            nodeHash = frontier.remove(0);
            AmphipodNode node = nodeMap.get(nodeHash);
            if (nodeHash == target) {
                System.out.printf("Solution found! Searched %d nodes in %.3f seconds.\n", explored.size(), (System.nanoTime() - nanoTime) * 1e-9);
                return node.getEnergyCost();
            }
            explored.add(nodeHash);
            if (explored.size() % 10000 == 0) System.out.printf("%d nodes searched.\n", explored.size());
            for (AmphipodNode neighbour : node.getNeighbours()) {
                nodeMap.putIfAbsent(neighbour.getHash(), neighbour);
                if (!explored.contains(neighbour.getHash()) && !frontier.contains(neighbour.getHash()))
                    frontier.add(neighbour.getHash());
                else if (frontier.contains(neighbour.getHash()))
                    if (nodeMap.get(neighbour.getHash()).getEnergyCost() > neighbour.getEnergyCost())
                        nodeMap.put(neighbour.getHash(), neighbour);
            }
        }
    }
}

class AmphipodNode {
    private static final int[] hallwayToPosition = new int[]{0, 1, 3, 5, 7, 9, 10};
    private static final int[] sideRoomToPosition = new int[]{2, 4, 6, 8};
    private static final int[] typeEnergy = new int[]{1, 10, 100, 1000};

    int[] hallway = new int[]{-1, -1, -1, -1, -1, -1, -1};
    int[][] sideRooms;
    int energyCost;
    AmphipodNode previousNode;

    public AmphipodNode(int[][] sideRooms) {
        this.sideRooms = sideRooms;
        energyCost = 0;
        previousNode = null;
    }

    public AmphipodNode(AmphipodNode previousNode, int[] hallway, int[][] sideRooms, int energyCost) {
        this.hallway = hallway;
        this.sideRooms = sideRooms;
        this.energyCost = energyCost;
        this.previousNode = previousNode;
    }

    public List<AmphipodNode> getNeighbours() {
        List<AmphipodNode> neighbours = new ArrayList<>();

        //Search paths from side rooms
        for (int col = 0; col < 4; col++) {
            int row = 0;
            while (row < sideRooms.length && sideRooms[row][col] == -1) row++;  //Finds the first non-vacant slot
            if (row == sideRooms.length) continue;

            int type = sideRooms[row][col];
            int left = col + 2, right = col + 1;
            while (left != 0 && hallway[left - 1] == -1) left--;
            while (right != hallway.length - 1 && hallway[right + 1] == -1) right++;
            for (int slot = left; slot <= right; slot++) {
                int[] newHallway = hallway.clone();
                newHallway[slot] = type;
                //Copy array
                int[][] newSideRooms = new int[sideRooms.length][];
                for (int i = 0; i < sideRooms.length; i++)
                    newSideRooms[i] = sideRooms[i].clone();
                newSideRooms[row][col] = -1;
                int energyCost = typeEnergy[type] * (Math.abs(hallwayToPosition[slot] - sideRoomToPosition[col]) + row + 1) + this.energyCost;
                neighbours.add(new AmphipodNode(this, newHallway, newSideRooms, energyCost));
            }
        }

        //Search paths from hallway
        for (int slot = 0; slot < hallway.length; slot++) {
            int type = hallway[slot];
            if (type != -1) {
                int left = slot, right = slot;
                while (left != 0 && hallway[left - 1] == -1) left--;
                while (right != hallway.length - 1 && hallway[right + 1] == -1) right++;
                if (left - 2 <= type && type <= right - 1) {
                    for (int row = sideRooms.length - 1; row >= 0; row--) {
                        if (sideRooms[row][type] == -1) {
                            int[] newHallway = hallway.clone();
                            newHallway[slot] = -1;
                            //Copy array
                            int[][] newSideRooms = new int[sideRooms.length][];
                            for (int i = 0; i < sideRooms.length; i++)
                                newSideRooms[i] = sideRooms[i].clone();
                            newSideRooms[row][type] = type;
                            int energyCost = typeEnergy[type] * (Math.abs(hallwayToPosition[slot] - sideRoomToPosition[type]) + row + 1) + this.energyCost;
                            neighbours.add(new AmphipodNode(this, newHallway, newSideRooms, energyCost));
                            break;
                        } else if (sideRooms[row][type] != type) break;
                    }
                }
            }
        }

        return neighbours;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < hallway.length; i++) {
            stringBuilder.append(hallway[i] == -1 ? "." : hallway[i]);
            if (i >= 1 && i <= 4) stringBuilder.append('.');
        }

        for (int[] sideRoom : sideRooms) {
            stringBuilder.append("\n##");
            for (int i : sideRoom) {
                stringBuilder.append(i == -1 ? "." : i).append("#");
            }
            stringBuilder.append("#");
        }

        stringBuilder.append("\n").append(energyCost);
        return stringBuilder.toString();
    }

    public long getHash() {
        long hash = 0;
        for (int i : hallway)
            hash = hash * 5 + (i + 1);
        for (int[] array : sideRooms)
            for (int i : array)
                hash = hash * 5 + (i + 1);
        return hash;
    }
}