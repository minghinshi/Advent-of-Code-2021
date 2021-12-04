/*
---Part 1---

"Let's see what's next..." the elf said. "Right, we need to pilot the ship. We've devised a course to find the sleigh keys. Can you help us double check it?"
"Sure, but how?" Kris answered.
"I fed some instructions to the submarine. There should be a copy of the instructions somewhere... Where is it..."
"Is it this file?" Noelle pointed at a text file on the screen. "It wasn't there last time I checked."
"It should be called 'Day02.txt'. Yeah, there you go. Open it."
"Okay..." Kris opened the file. There were instructions like "forward 2", "down 9" and "up 6". "...that's a whole lot of instructions."
"The path is indeed quite complex. Your task is to help us confirm where the submarine will end up."
"You mean..." Noelle asked.
"I mean, how far the submarine will travel across, and how deep it will be. Then we can make sure the submarine will go where it should. Got it?"
"Alright..." Noelle took a quick look at the screen. "...shouldn't be too difficult."
"You should be able to figure out the rest by yourself. I'll see you later." The elf walked out of the computer room.

"Okay, let's see... so 'forward' means the submarine will move... forward, and I guess the number after it is how far it'll move?"
"I mean, that's pretty obvious," Kris commented. "I'll just need to add up these numbers."
"And 'up' means an increase in depth..."
"Decrease."
"Oh yeah, decrease. Can't forget we're underwater! That means 'down' will be an increase instead."
"Yep. I think that's all we need to know."
"Let's solve the puzzle, then!"

--- Part 2 ---

"So, we found that the submarine will move 2,010 forward and 1,030 down," Noelle reported.
"1,030 down?" The elf asked, surprised. "Oh, right... I totally forgot to mention this."
"What?"
"The distance the submarine travels across should be in meters, but for the distance the submarine travels down..."
"Huh? They have different units?"
"Huh? They HAVE units?" asked Kris.
"Yeah. It's in centimeters."
"That's... I thought they used the same units."
"It's really weird," Kris added. "I've never seen something like that before."
"I mean, yes, it's a little odd. So if you said it will travel 1,030 down, that's like 10 metres..."
"That makes no sense!" Noelle laughed. "Kris, you're sure your code is correct, right?"
"But then the instructions don't make sense, either! Why is it moving up and down just a few centimeters?"
"You mean the 'up' and 'down' commands?"
"Yeah."
"Ah... I see what's the problem now. Wait a second..."

The elf searched the drawers until he took out a booklet. "This is the submarine manual. Read the first page and you'll know what I mean."
"Use commands to move the submarine," Kris said as they read the book. "Use 'forward' to move the submarine forward... use 'up' and 'down' to vertically ro- what?"
"Yeah, they're used to rotate the submarine. Did you think they were used for moving vertically?"
"Of course! Why are they labelled 'up' and 'down'?"
"Well... I don't know. Blame it on the programmer for bad naming skills, I guess."
"Well... I mean... y... you're not wrong. I call my integers 'i' and my strings 's'. Guess every programmer does that."
"Anyways, let's move on," Noelle said. "How do we find the vertical position?"
"Here's how 'up' and 'down' really work. The submarine keeps track of a number which I'll call 'aim'. It controls the vertical rotation of the submarine. Say, if the aim is 10, then for every meter the submarine moves forward, it also moves down 10 centimeters."
Kris and Noelle paused and thought for a moment.
"Is that clear enough?" The elf asked.
"Yeah, I think I understand now," Kris replied. "I'll go and modify my program."
"We'll get back to you later! Thanks for your help! Bye!" Noelle waved and followed Kris back to the computer room.
*/
package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day02 {
    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day02.txt"));
        List<Movement> movements = new ArrayList<>();
        int horizontalTotal = 0;
        int verticalTotal = 0;
        int aim = 0;

        for (String string : content) {
            Movement movement = new Movement(string);
            movements.add(movement);
        }

        //Part 1
        for (Movement movement : movements) {
            horizontalTotal += movement.getHorizontal();
            verticalTotal += movement.getVertical();
        }

        System.out.printf("""
                
                Treating "up" and "down" as vertical movement...
                The submarine will go forward %d metres and down %.0f metres.
                (Part 1 solution: %d)          
                """, horizontalTotal, verticalTotal * 0.01, horizontalTotal * verticalTotal);

        horizontalTotal = 0;
        verticalTotal = 0;

        //Part 2
        for (Movement movement : movements){
            horizontalTotal += movement.getHorizontal();
            verticalTotal += movement.getHorizontal() * aim;
            aim += movement.getVertical();
        }

        System.out.printf("""
                
                Treating "up" and "down" as rotations...
                The submarine will go forward %d metres and down %.0f metres.
                (Part 2 solution: %d)
                """, horizontalTotal, verticalTotal * 0.01, horizontalTotal * verticalTotal);
    }
}

class Movement{
    int horizontal = 0;
    int vertical = 0;

    public Movement(String input){
        String[] strings = input.split(" ");
        int magnitude = Integer.parseInt(strings[1]);
        switch (strings[0]) {
            case "forward" -> horizontal = magnitude;
            case "up" -> vertical = -magnitude;
            case "down" -> vertical = magnitude;
            default -> System.out.println("There is an error in your code!");
        }
    }

    public int getHorizontal() {
        return horizontal;
    }

    public int getVertical() {
        return vertical;
    }
}
/*
"Welcome back!" The elf greeted Kris and Noelle. "What do you get this time?"
"2,010 down and 1,034,321 down. That's... about 10 kilometers underwater... are we going that deep?"
"Let me see..." The elf grabbed a handwritten note. "That is where we're going! So our route is correct."
"Oh, look! The antenna is now at 4. Nice job, Kris!" Noelle smiled, trying to hide her thoughts. "10 kilometers... I must have messed up horribly..."
*/