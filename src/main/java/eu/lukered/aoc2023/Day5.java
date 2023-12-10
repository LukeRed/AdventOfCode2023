package eu.lukered.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * UGLY!! This took too long to beautify it.
 */
public class Day5 {
    private static final GroupMatcher seedsPattern = new GroupMatcher(Pattern.compile("seeds: ([0-9 ]+)"), 1);
    private static final GroupMatcher mapPattern = new GroupMatcher(Pattern.compile("([a-z\\-]+) map:"), 1);
    private static final GroupMatcher fromPattern = new GroupMatcher(Pattern.compile("([a-z]+)-to-[a-z]+"), 1);
    private static final GroupMatcher toPattern = new GroupMatcher(Pattern.compile("[a-z]+-to-([a-z]+)"), 1);

    public static void main(String[] args) {
        final Day5 day5 = new Day5();
        day5.step1(Input.DAY5_INPUT);
        day5.step2(Input.DAY5_INPUT);
    }

    private void step1(final List<String> input) {
        final List<Long> seeds = Arrays.stream(seedsPattern.get(input.get(0)).split(" ")).map(Long::parseLong).toList();
        System.out.println("Seeds: " + seeds);

        final Map<String, DualMap> maps = parse(input);
        Map<String, LinkedList<Long>> paths = new HashMap<>();
        seeds.forEach(s -> paths.put(s.toString(), new LinkedList<>(List.of(s))));
        String selected = "seed";
        while (selected != null) {
            final String current = selected;
            final Optional<String> mapName = maps.keySet().stream().filter(s -> s.startsWith(current)).findAny();
            if (mapName.isEmpty()) {
                break;
            }
            selected = toPattern.get(mapName.get());
            System.out.println("Map: " + current + " - " + mapName.get());
            final Map<Long, Long> currentMap = maps.get(mapName.get());
            paths.values().forEach(v -> v.addLast(currentMap.get(v.getLast())));
        }
        List<LinkedList<Long>> list = paths.values().stream().sorted(Comparator.comparing(LinkedList::getLast)).toList();
        System.out.println("Step 1: " + list.get(0).getLast());
    }

    private void step2(final List<String> input) {
        AtomicInteger counter = new AtomicInteger(0);
        final List<Range> seeds = Arrays.stream(seedsPattern.get(input.get(0)).split(" "))
                .map(Long::parseLong)
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 2))
                .values()
                .stream()                        //stream the pairs
                .map(l -> new Range(l.get(0), l.get(1), null)).toList();
        System.out.println("Seeds: " + seeds);

        final Map<String, DualMap> maps = parse(input);
        List<Range> currentRanges = seeds;
        String selected = "seed";
        while (selected != null) {
            final String current = selected;
            final Optional<String> mapName = maps.keySet().stream().filter(s -> s.startsWith(current)).findAny();
            if (mapName.isEmpty()) {
                break;
            }
            selected = toPattern.get(mapName.get());
            System.out.println("Map: " + current + " - " + mapName.get());
            final DualMap currentMap = maps.get(mapName.get());
            currentRanges = currentRanges.stream().flatMap(r -> currentMap.getRange(r).stream()).toList();
        }
        Range winningRange = currentRanges.stream().min(Comparator.comparing(Range::start)).orElseThrow();
        System.out.println("Step 1: " + winningRange);
    }

    private Map<String, DualMap> parse(final List<String> input) {

        final Map<String, DualMap> maps = new HashMap<>();

        for (int i = 2; i < input.size(); i++) {
            final DualMap curMap = new DualMap();
            maps.put(mapPattern.get(input.get(i)), curMap);
            System.out.println(" -> new Map " + mapPattern.get(input.get(i)));
            i++;
            while (i < input.size() && ! input.get(i).isBlank()) {
                System.out.println("    + " + input.get(i));
                curMap.putElements(input.get(i));
                i++;
            }
        }
        return maps;
    }


    private static class DualMap extends HashMap<Long, Long> implements Map<Long, Long> {
        private List<Entry> entries = new ArrayList<>();
        public void putElements(final String raw) {
            List<Long> s = Arrays.stream(raw.split(" ")).map(Long::parseLong).toList();
            if (s.size() != 3) {
                throw new RuntimeException("Element lengths do not match: " + s);
            }
            entries.add(new Entry(s.get(1), s.get(0), s.get(2)));
        }


        @Override
        public boolean containsKey(Object key) {
            return true;
        }

        public List<Range> getRange(final Range parent) {
            long nextStart = parent.start;
            long nextLenght = parent.length;
            List<Range> ranges = new LinkedList<>();

            while (nextLenght > 0) {
                final long curStart = nextStart;
                Optional<Entry> entry = entries.stream().filter(e -> e.source <= curStart && e.sourceEnd() > curStart).findFirst();
                if (entry.isPresent()) {
                    final Entry currentEntry = entry.get();
                    long offset = curStart - currentEntry.source;
                    long count = Math.min(nextLenght, currentEntry.length - offset);
                    ranges.add(new Range(currentEntry.target + offset, count, parent));
                    nextStart = curStart + count;
                    nextLenght = nextLenght - count;
                } else {
                    Entry next = entries.stream().filter(e -> e.source > curStart).min(Comparator.comparingLong(c -> c.source))
                            .orElse(new Entry(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE));
                    long count = Math.min(nextLenght, next.source - curStart);
                    ranges.add(new Range(curStart, count, parent));
                    nextStart = curStart + count;
                    nextLenght = nextLenght - count;
                }
            }
            return ranges;
        }
        @Override
        public Long get(Object key) {
            final long element = (Long) key;
            return entries.stream().filter(e -> e.source <= element && e.source + e.length > element).findFirst().map(e -> (element - e.source() + e.target())).orElse(element);
        }

        @Override
        public String toString() {
            return entries.stream()
                    .sorted(Comparator.comparingLong(c -> c.source))
                    .map(c -> (new Range(c.source, c.length, null)) + " -> " + (c.target - c.source))
                    .collect(Collectors.joining("\n"));
        }

        private record Entry(long source, long target, long length) {
            public long sourceEnd() {
                return source + length;
            }
        }
    }
    private record Range(long start, long length, Range parent) {
        @Override
        public String toString() {
            return "|-" +
                    start +
                    "--" +
                    (start + length) +
                    "-|";
        }
    }
}
