/*
"What's going on?" Kris looked around as the overboard alarm went off. They saw Noelle lying on the floor, seemingly hurt. They rushed to help her.

"Kris! What do I do now? I... I ruined it!"
"Ruined what?"
"The sleigh keys! I tripped and... they're in the ocean now! They're gone!"
"Huh..." Kris walked towards the edge of the ship and looked down. Indeed, there were sleigh keys slowly drifting down into the ocean floor, becoming less and less visible.
"What do I do... what do I do..." Noelle looked increasingly worried. "I'm always clumsy like this..."

Kris knew that this is another occasion where they had to comfort her. But this time, they were not so sure what to say.
"Noelle... You wait here. I'll ask them what to do."

"Someone dumped the sleigh keys into the ocean? Who is it?" asked an elf.
"No... not dumped! It was an accident, she tripped-"
"I don't care! Who did that? They're going to ruin our Christmas!"
"My friend."
"What's your friend's name?"
"Noelle."
"Is she an elf?"
"No, she's a reindeer. Rudolph is her dad."
The elf hesitated for a moment. "Look... it doesn't matter if she's your friend or Rudolph's daughter. Reindeers should only be responsible for gift delivery."
"Then why does she have the sleigh keys?"
"YOU should be the one answering this question!"
"I... I have no idea!"
The elf sighed. "Bring her here. Quick."

As Kris went to find Noelle, there was an announcement. "This is the captain, may I have your attention. An accident has just happened which resulted in sleigh keys being scattered all over the ocean. A submarine will be deployed soon. Please listen to the Elves' order. Thank you."
"A submarine... is it really that serious of a problem..." Kris wondered.

"So you're Noelle, right? Come on in, let's discuss this in the submarine." The elf stepped through the doors, and the two followed. The door closed behind them, and soon they were underwater.
"Y... yes..." Noelle answered, regretting what she has just done.
"So I heard from your friend, Kris, that you dropped the sleigh keys accidentally into the ocean. Is that correct?"
"Yeah."
"Well, according to rule 1728, only elves should handle important materials like sleigh keys, so you shouldn't have them."
"I- I'm sorry... Someone told me to help deliver them."
"Exactly what I thought had happened. Is that an elf who asked you?"
"Yes..."
"I see. Looks like you're innocent, then."
"Thank you. I can go now... right... never mind. Wh- why am I brought underwater?"

Kris and Noelle could do nothing but admire the festive lights, which were installed in the submarine for obvious reasons.
"Hey, you two..." the elf started. "Although you didn't mean to lose the sleigh keys, you did it anyways, so I would like you to assist us in getting them back."
"You're just finding excuses, aren't you..." Kris replied.
"Yeah," Noelle added. "You said I should only deliver gifts."
"Well, um... uh... yes."
"What?" Noelle exclaimed.
"L- listen- we... we can't deal with this situation alone. I actually wanted you two to help us get the sleigh keys back. It's not an easy task.
"How?"
"You see this thing here? The thing that says 0 right now."
"Yeah..."
"That's showing the strength of our submarine's antenna."
"The antenna? Let me guess... it could help us find the sleigh keys?"
"Well... I'm not sure. Mind you, this thing is still in experimental phase. We haven't fully tested it. But it's our only hope. Though right now, it's unpowered so it does nothing."
"Which is why it shows 0..."
"Exactly. And you two will help me power it up."
"And how?" Kris asked, getting somewhat impatient. "You still haven't told us what we should do."
"You'll be solving puzzles!"
"Puzzles? Kris really likes puzzles."
"Wait, Noelle, no-"
"That's great! I'm sure you'll like helping us. Each puzzle you solve will get us a star, which will power up the antenna."
"How many stars will it need?"
"Again, we don't have an exact number, but I think around 50."
"50 puzzles? No way I'm doing this!"
"Don't worry. Each puzzle should be pretty small. Now, I want you to help me with this... Come over here."

---Part 1---

"See this big list of numbers?" The elf pointed at the small screen.
"Yes..." Kris and Noelle glanced at the screen. There's a number shown in each row of it.
"This is the sonar sweep report."
"What's a sonar sweep?" Kris asked.
"It's like... you know, the submarine uses... sound waves to detect how deep the sea floor is." Noelle replied.
"Correct," the elf said. "Each number here is in metres, and it shows the sea floor depth as the sweep looks further and further away from the submarine. See the first number? It says 134. This means the sea floor here is 134 metres deep."
"I see. What am I gonna do?" said Kris.
"You'll help us figure out how quickly the depth is increasing. Count the number of times that a measurement increased from the previous one."
"What do you mean?"
"Well, for example, the first number is 134, right? The second is 138, which is larger than 134, so that's 1 increase. The third number is 142, so that's 2 increases. Count until you reach the end of the list."
"T... This is impossible! There's... so many numbers! How am I-"
"I knew you're going to ask this. Follow me."

Kris and Noelle followed the elf to a room with a single computer. "We have a computer here, " the elf explained. "The sonar sweep report is saved to this computer already. I've also got IntelliJ installed in the computer."
"IntelliJ? Oh, I know what it is! So I'm doing programming puzzles?" Kris asked excitedly.
"Yeah, programming puzzles. You're not meant to calculate by hand."
"...You really should have said this earlier!" Noelle cheerfully said. "Kris is an expert in programming!"
"That's nice. You two will certainly be helpful then. Sorry if I caused much confusion."
"It's fine. Everything's okay now! Just leave it to us!"
"Alright. Get back to us soon!"

---Part 2---

"So how many increases did you two find?" said the elf.
"1,766." Noelle said.
"Hmm, 1,766..."
"Is anything wrong?"
"Not really... but I expected it to be a bit higher. The sea floor appears to be quite steep."
"Huh? But that's a lot already!"
"Well, provided that there's 2,000 measurements... it's... quite high. But I think the noise in the data is decreasing the number."
"Noise?" Kris wondered.
"As in variation in the data. What if... hmm... how about this. We can use a three-measurement sliding window."
Kris and Noelle stood in silence, in a vain attempt to understand what the elf said.
"...by that, I mean we add up 3 numbers in a row, and then compare them to see if there's an increase."
"So, for example... I add up 134, 138 and 142..." Noelle explained, trying to clear things up.
"Yes."
"Then I add up 138, 142 and 143..."
"Yes."
"Then I see which one is larger?"
"There you go! Now you just need to count the number of increases again."
"No problem! Kris and I will handle this!"

Kris and Noelle went back to the computer room, where Kris modified their code. Noelle sat aside them and spectated.
"Hey, Kris... you actually don't need to add up the numbers."
"Huh? Why?" Kris kept typing the code.
"You can ignore the middle digits!"
"...I'm confused. What are 'middle digits'?"
"Say you're comparing 1, 2 and 3 with 2, 3 and 4..."
"Oh, I see! How haven't I thought of that?"
"Got it?"
"Yeah! I just need to compare 1 and 4! That's really clever, Noelle!"
"Thanks, Kris!"
*/
package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day01 {
    public static void main(String[] args) throws IOException {
        List<String> content = Files.readAllLines(Paths.get("puzzleInputs/Day01.txt"));
        List<Integer> depths = new ArrayList<>();

        for (String string : content) {
            depths.add(Integer.parseInt(string));
        }

        System.out.println();
        System.out.println("The depth measurement increases " + countIncreases(depths, 1) + " times, without using averages.");
        System.out.println("The depth measurement increases " + countIncreases(depths, 3) + " times, using averages of 3 depths.");
    }

    public static int countIncreases(List<Integer> integerList, int slidingWindow) {
        int count = 0;
        for (int i = slidingWindow; i < integerList.size(); i++) {
            if (integerList.get(i) > integerList.get((i - slidingWindow))) ++count;
        }
        return count;
    }
}
/*
"How many increases did you count this time?" The elf asked.
"1,797. It's just a bit larger..." It was still Noelle who gave the figures.
"It's fine. Now the number's about right. Oh look, the antenna's powered up."
Kris and Noelle looked at the display, which showed 2 instead of 0.
"We... did it? We solved the puzzles?" Noelle asked, slightly doubtful but excited."
"Yeah! We can move on to the next task."
*/