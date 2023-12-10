package eu.lukered.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day4 {
    public static void main(String[] args) {
        final Day4 day4 = new Day4();
        final List<ScratchCard> scratchCards = Input.DAY4_INPUT.stream().map(ScratchCard::of).toList();
        day4.step1(scratchCards);
        day4.step2(scratchCards);
    }

    private void step1(final List<ScratchCard> scratchCards) {
        final int sum = scratchCards.stream()
                .mapToInt(sc -> getPoints(sc.actualNumbers.stream()
                            .filter(sc.winningNumbers::contains)
                            .count()))
                .sum();
        System.out.println("Step 1: " + sum);
    }

    private void step2(final List<ScratchCard> scratchCards) {
        List<Integer> copies = new ArrayList<>(IntStream.range(0, scratchCards.size()).mapToObj(i -> 1).toList());
        for (ScratchCard sc : scratchCards) {
            long count = sc.actualNumbers.stream()
                    .filter(sc.winningNumbers::contains)
                    .count();
            for (int i = sc.number; i < sc.number + count; i++) {
                copies.set(i, copies.get(i) + copies.get(sc.number - 1));
            }
        }

        final int sum = copies.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Step 2: " + sum);
    }


    private static int getPoints(long numbersRight) {
        numbersRight--;
        if (numbersRight < 0) {
            return 0;
        }
        return (int) Math.pow(2, numbersRight);
    }

    public record ScratchCard (int number, List<Integer> winningNumbers, List<Integer> actualNumbers) {
        private static final Pattern parserPattern = Pattern.compile("^Card +(\\d+): ([0-9 ]+)\\|([0-9 ]+)$");
        public static ScratchCard of(final String input) {
            Matcher matcher = parserPattern.matcher(input);
            matcher.find();
            return new ScratchCard(Integer.parseInt(matcher.group(1)),
                    Arrays.stream(matcher.group(2).split(" ")).filter(s -> !s.isBlank()).map(Integer::parseInt).toList(),
                    Arrays.stream(matcher.group(3).split(" ")).filter(s -> !s.isBlank()).map(Integer::parseInt).toList());

        }
    }
}
