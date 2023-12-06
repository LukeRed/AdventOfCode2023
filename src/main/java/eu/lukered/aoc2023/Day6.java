package eu.lukered.aoc2023;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {
    private static final Pattern numberPattern = Pattern.compile("(\\d+)");


    public static void main(String[] args) {
        final Day6 day6 = new Day6();
        day6.step(1, parseGames(Util.DAY6_INPUT));
        day6.step(2, parseBigGames(Util.DAY6_INPUT));
    }

    private void step(final int step, final List<Game> games) {
        long result = games.stream().mapToLong(g -> {
            final double quatratic = Math.sqrt(Math.pow(g.time, 2) - 4 * g.distance);
            if (quatratic < 0) return 0;
            final double start = Math.floor((g.time - quatratic) / 2) + 1;
            final double end = Math.ceil((g.time + quatratic) / 2) - 1;
            System.out.println("Starting at " + start + " ending at " + end + " - total: " + (end - start + 1) + " [" + g.time + " - " + g.distance + " - " + quatratic + "]");
            return (long) (end - start + 1);
        }).reduce(1, (a, b) -> a * b);
        System.out.println("Step " + step + ": " + result);
    }

    private static List<Game> parseBigGames(final List<String> input) {
        return parseGames(input.stream().map(i -> i.replace(" ", "")).toList());
    }
    private static List<Game> parseGames(final List<String> input) {
        final Matcher timeMatcher = numberPattern.matcher(input.get(0));
        final Matcher distanceMatcher = numberPattern.matcher(input.get(1));
        final List<Game> list = new LinkedList<>();
        while (timeMatcher.find() && distanceMatcher.find()) {
            list.add(new Game(Long.parseLong(timeMatcher.group(1)), Long.parseLong(distanceMatcher.group(1))));
        }
        return list;
    }

    private record Game(long time, long distance) {}
}
