package eu.lukered.aoc2023;


import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {
    public static final Map<String, String> digitReplacement = Map.of("one", "1", "two", "2", "three", "3", "four", "4", "five", "5", "six", "6", "seven", "7", "eight", "8", "nine", "9");
    public static final Pattern filterRegex = Pattern.compile("(?=(one|two|three|four|five|six|seven|eight|nine|\\d))");
    public static void main(String[] args) {
        final var sum = Util.DAY1_INPUT
                .stream()
                .map(Day1::replaceWithRegex)
                .map(s -> String.format("%s%s", s.charAt(0), s.charAt(s.length() - 1)))
                .mapToInt(Integer::valueOf)
                .sum();
        System.out.printf("Sum is: " + sum);
    }
    private static String replaceWithRegex(final String input) {
        final StringBuilder output = new StringBuilder();
        final Matcher matcher = filterRegex.matcher(input);
        while (matcher.find()) {
            final String current = matcher.group(1);
            output.append(digitReplacement.getOrDefault(current, current));
        }
        return output.toString();
    }
}