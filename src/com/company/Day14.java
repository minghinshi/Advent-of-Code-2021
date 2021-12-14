package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class Day14 {
    public static void main(String[] args) throws IOException {
        Polymer polymer = new Polymer(Files.readAllLines(Paths.get("puzzleInputs/Day14.txt")));
        polymer.repeatExpandPolymer(10);
        polymer.analyse(1);
        polymer.repeatExpandPolymer(30);
        polymer.analyse(2);
    }

    public static int getHash(char c1, char c2){
        return c1 * 256 + c2;
    }

    public static int getHash(char[] chars){
        return getHash(chars[0], chars[1]);
    }
}

class Polymer{
    HashMap<Integer, Long> pairAmounts = new HashMap<>();
    HashMap<Integer, Character> pairInsertionRules = new HashMap<>();
    HashMap<Integer, char[]> pairs = new HashMap<>();
    char firstAtom, lastAtom;
    int expansions;

    public Polymer(List<String> strings){
        char[] polymer = strings.get(0).toCharArray();
        firstAtom = polymer[0];
        lastAtom = polymer[polymer.length - 1];
        for (int i = 0; i < polymer.length - 1; i++) {
            int hash = Day14.getHash(polymer[i], polymer[i+1]);
            pairAmounts.put(hash, pairAmounts.getOrDefault(hash, 0L) + 1);
        }
        for (int i = 2; i < strings.size(); i++) {
            String[] split = strings.get(i).split(" -> ");
            int hash = Day14.getHash(split[0].toCharArray());
            pairs.put(hash, split[0].toCharArray());
            pairInsertionRules.put(hash, split[1].charAt(0));
        }
    }

    public void repeatExpandPolymer(int expansions){
        for (int i = 0; i < expansions; i++) {
            expandPolymer();
        }
    }

    public void expandPolymer(){
        ++expansions;
        HashMap<Integer, Long> newPairAmounts = new HashMap<>();
        for (int hash : pairAmounts.keySet()) {
            char[] pair = pairs.get(hash);
            char newElement = pairInsertionRules.get(hash);
            int newHash = Day14.getHash(pair[0], newElement);
            newPairAmounts.put(newHash, newPairAmounts.getOrDefault(newHash, 0L) + pairAmounts.getOrDefault(hash, 0L));
            newHash = Day14.getHash(newElement, pair[1]);
            newPairAmounts.put(newHash, newPairAmounts.getOrDefault(newHash, 0L) + pairAmounts.getOrDefault(hash, 0L));
        }
        pairAmounts = newPairAmounts;
    }

    HashMap<Character, Long> countAtoms(){
        HashMap<Character, Long> atoms = new HashMap<>();
        atoms.put(firstAtom, 1L);
        atoms.put(lastAtom, 1L);
        for (int hash : pairInsertionRules.keySet()) {
            char[] pair = pairs.get(hash);
            for (char element : pair) {
                atoms.put(element, atoms.getOrDefault(element, 0L) + pairAmounts.getOrDefault(hash, 0L));
            }
        }
        atoms.replaceAll((e, v) -> atoms.get(e) / 2);
        return atoms;
    }

    public void analyse(int part){
        HashMap<Character, Long> atoms = countAtoms();
        char rareAtom = 0, commonAtom = 0;
        long smallest = 0, largest = 0;
        boolean initialized = false;
        for (char element : atoms.keySet()) {
            long amount = atoms.get(element);
            if(!initialized || amount < smallest){
                smallest = amount;
                rareAtom = element;
            }
            if(!initialized || amount > largest){
                largest = amount;
                commonAtom = element;
            }
            if(!initialized) initialized = true;
        }

        System.out.printf("\nAfter %d expansions,\nthe most common element is %s, occurring %d times;\nThe least common element is %s, occurring %d times.\n(Part %d solution: %d)\n",
                expansions, commonAtom, largest, rareAtom, smallest, part, largest - smallest);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int hash : pairAmounts.keySet()) {
            stringBuilder.append(pairs.get(hash)).append("=").append(pairAmounts.get(hash)).append(", ");
        }
        return stringBuilder.toString();
    }
}