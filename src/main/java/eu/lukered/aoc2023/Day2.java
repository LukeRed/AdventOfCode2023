package eu.lukered.aoc2023;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Day2 {
    private static final GroupMatcher redPattern = new GroupMatcher(Pattern.compile("(\\d+) red"), 1);
    private static final GroupMatcher greenPattern = new GroupMatcher(Pattern.compile("(\\d+) green"), 1);
    private static final GroupMatcher bluePattern = new GroupMatcher(Pattern.compile("(\\d+) blue"), 1);

    public static void main(String[] args) {
        final Day2 day2 = new Day2();
        final List<DiceGame> parsedInput = parseInput(Input.DAY2_INPUT);
        day2.step1(parsedInput);
        day2.step2(parsedInput);
    }


    private void step1(final List<DiceGame> parsedInput) {
        final ResultChecker resultChecker = new ResultChecker(12, 13, 14);
        int result = parsedInput.stream()
                .filter(resultChecker::isValid)
                .mapToInt(DiceGame::id)
                .sum();
        System.out.println("Step 1: " + result);
    }

    private void step2(final List<DiceGame> parsedInput) {
        int result = parsedInput.stream()
                .mapToInt(Day2::getMinDiceCubed)
                .sum();
        System.out.println("Step 2: " + result);
    }

    private static List<DiceGame> parseInput(final List<String> input) {
        return input.stream().map(i -> {
            final String[] split = i.substring(5).split(": ");
            return new DiceGame(Integer.parseInt(split[0]), Arrays.stream(split[1].split(";"))
                    .map(s -> new GameResult(redPattern.getInt(s), greenPattern.getInt(s), bluePattern.getInt(s)))
                    .toList());
        }).toList();
    }


    private static int getMinDiceCubed(final DiceGame diceGame) {
        final int red = diceGame.results.stream().mapToInt(GameResult::red).max().orElse(0);
        final int green = diceGame.results.stream().mapToInt(GameResult::green).max().orElse(0);
        final int blue = diceGame.results.stream().mapToInt(GameResult::blue).max().orElse(0);
        return red * green * blue;
    }


    private record ResultChecker(int maxRed, int maxGreen, int maxBlue) {
        public boolean isValid(final DiceGame diceGame) {
            return diceGame.results.stream()
                    .allMatch(r -> r.red <= maxRed && r.green <= maxGreen && r.blue <= maxBlue);
        }
    }

    private record DiceGame (int id, List<GameResult> results){}
    private record GameResult(int red, int green, int  blue){}
}
