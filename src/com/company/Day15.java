package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day15 {
    public static void main(String[] args) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("puzzleInputs/Day15.txt"));
        ChitonMap chitonMap = new ChitonMap(input,1);
        System.out.print("\nSearching part of the map...\n");
        System.out.printf("The lowest total risk is %d.\n", chitonMap.getShortestDistance());
        chitonMap = new ChitonMap(input,5);
        System.out.print("\nSearching the whole map...\n");
        System.out.printf("The lowest total risk is %d.\n", chitonMap.getShortestDistance());

    }
}

//This is Dijkstra's algorithm for finding the shortest path.
class ChitonMap{
    private final ChitonNode[][] chitonNodes;
    private final List<ChitonNode> unvisitedNodes = new ArrayList<>();
    private ChitonNode currentNode;
    private final int height;
    private final int length;

    public ChitonMap(List<String> input, int scale){
        int inputHeight = input.size();
        int inputLength = input.get(0).length();
        height = inputHeight * scale;
        length = inputLength * scale;
        chitonNodes = new ChitonNode[height][length];

        //Initialize nodes
        for (int i = 0; i < height; i++) {
            char[] chars = input.get(i%inputHeight).toCharArray();
            for (int j = 0; j < length; j++) {
                boolean isStart = i == 0 && j == 0;
                ChitonNode node = new ChitonNode(i, j, (chars[j%inputLength] - 49 + i / inputHeight + j / inputLength) % 9 + 1, isStart);
                chitonNodes[i][j] = node;
                if(isStart){
                    currentNode = node;
                }
            }
        }

        //Set neighbours
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                ChitonNode chitonNode = chitonNodes[i][j];
                for (int k = i - 1; k <= i + 1 ; k++) {
                    if (k == i || k < 0 || k >= height) continue;
                    chitonNode.addNeighbour(chitonNodes[k][j]);
                }
                for (int l = j - 1; l <= j + 1 ; l++) {
                    if(l == j || l < 0 || l >= length) continue;
                    chitonNode.addNeighbour(chitonNodes[i][l]);
                }
            }
        }
    }

    public int getShortestDistance(){
        while (currentNode.getX() != length - 1 || currentNode.getY() != height - 1) {
            visitCurrentNode();
            selectNextNode();
        }
        return currentNode.getTentativeDistance();
    }

    public void visitCurrentNode(){
        currentNode.visit();
        unvisitedNodes.remove(currentNode);
        for (ChitonNode neighbour : currentNode.getNeighbours()) {
            if(!unvisitedNodes.contains(neighbour) && !neighbour.isVisited()){
                unvisitedNodes.add(neighbour);
            }
        }
    }

    public void selectNextNode(){
        int lowestDistance = Integer.MAX_VALUE;
        ChitonNode nextNode = null;
        for (ChitonNode node : unvisitedNodes) {
            int tentativeDistance = node.getTentativeDistance();
            if(tentativeDistance < lowestDistance){
                lowestDistance = tentativeDistance;
                nextNode = node;
            }
        }
        currentNode = nextNode;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                if(chitonNodes[i][j].isVisited())
                    stringBuilder.append("#");
                else
                    stringBuilder.append(".");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

class ChitonNode{
    private final int x;
    private final int y;
    private final int riskLevel;
    private int tentativeDistance;
    private boolean isVisited;
    private final List<ChitonNode> neighbours = new ArrayList<>();

    public ChitonNode(int y, int x, int riskLevel, boolean isStart){
        this.x = x;
        this.y = y;
        this.riskLevel = riskLevel;
        tentativeDistance = (isStart ? 0 : Integer.MAX_VALUE);
        isVisited = false;
    }
    
    public void visit(){
        for (ChitonNode neighbour : neighbours) {
            if(neighbour.isVisited()) continue;
            neighbour.findNewDistance(tentativeDistance);
        }
        isVisited = true;
    }

    public void findNewDistance(int previousDistance){
        int newTentativeDistance = previousDistance + riskLevel;
        if(newTentativeDistance < tentativeDistance){
            tentativeDistance = newTentativeDistance;
        }
    }

    public int getTentativeDistance() {
        return tentativeDistance;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public List<ChitonNode> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(ChitonNode neighbour) {
        neighbours.add(neighbour);
    }
}