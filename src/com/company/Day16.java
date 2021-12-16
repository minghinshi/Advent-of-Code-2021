package com.company;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day16 {
    public static void main(String[] args) throws IOException {
        Packet packet = new Packet(hexToBin(Files.readAllLines(Paths.get("puzzleInputs/Day16.txt")).get(0)));
        System.out.printf("\nThe total of the version numbers is %d.\n", packet.getTotalVersionNumbers());
        System.out.printf("The value of the packet is %d.\n", packet.getValue());
    }

    static String hexToBin(String s) {
        StringBuilder bits = new StringBuilder(new BigInteger(s, 16).toString(2));
        while (bits.length() % 4 != 0)
            bits.insert(0, "0");
        for (int i = 0; i < bits.length(); i++) {
            if(s.charAt(i) != '0') break;
            bits.insert(0, "0000");
        }
        return bits.toString();
    }
}

class Packet{
    private final List<Packet> subPackets = new ArrayList<>();
    private final String unusedBits;
    private final int version;
    private final int typeID;
    private long value;

    public Packet(String input){
        version = Integer.parseInt(input.substring(0,3),2);
        typeID = Integer.parseInt(input.substring(3,6),2);

        if(typeID == 4){
            //Value packets
            StringBuilder stringBuilder = new StringBuilder(input.substring(6));
            int numberLength = 0;
            while (true){
                char c = stringBuilder.charAt(numberLength);
                stringBuilder.deleteCharAt(numberLength);
                numberLength += 4;
                if(c == '0') break;
            }
            value = Long.parseLong(stringBuilder.substring(0,numberLength),2);
            unusedBits = stringBuilder.substring(numberLength);
        } else {
            //Operator packets
            int lengthTypeID = Integer.parseInt(input.substring(6, 7));
            String subPacketString = null;

            if(lengthTypeID == 0) {
                //Length mode
                int subPacketLength = Integer.parseInt(input.substring(7, 22), 2);
                subPacketString = input.substring(22);
                int targetLength = subPacketString.length() - subPacketLength;
                while (subPacketString.length() != targetLength) {
                    Packet childPacket = new Packet(subPacketString);
                    subPackets.add(childPacket);
                    subPacketString = childPacket.getUnusedBits();
                }
            }else if(lengthTypeID == 1){
                //Count mode
                int subPacketCount = Integer.parseInt(input.substring(7, 18), 2);
                subPacketString = input.substring(18);
                int remainingPackets = subPacketCount;
                while (remainingPackets != 0) {
                    Packet childPacket = new Packet(subPacketString);
                    subPackets.add(childPacket);
                    subPacketString = childPacket.getUnusedBits();
                    remainingPackets--;
                }
            }
            unusedBits = subPacketString;
        }
    }

    public int getTotalVersionNumbers(){
        int total = version;
        for (Packet subPacket : subPackets) {
            total += subPacket.getTotalVersionNumbers();
        }
        return total;
    }

    public long getValue(){
        return switch (typeID) {
            case 0 -> subPackets.stream().mapToLong(Packet::getValue).sum();
            case 1 -> subPackets.stream().mapToLong(Packet::getValue).reduce(1L, (product, x) -> product * x);
            case 2 -> subPackets.stream().mapToLong(Packet::getValue).min().orElseThrow();
            case 3 -> subPackets.stream().mapToLong(Packet::getValue).max().orElseThrow();
            case 4 -> value;
            case 5 -> (subPackets.get(0).getValue() > subPackets.get(1).getValue() ? 1L : 0L);
            case 6 -> (subPackets.get(0).getValue() < subPackets.get(1).getValue() ? 1L : 0L);
            case 7 -> (subPackets.get(0).getValue() == subPackets.get(1).getValue() ? 1L : 0L);
            default -> throw new RuntimeException("Invalid Type ID!");
        };
    }

    public String getUnusedBits() {
        return unusedBits;
    }
}