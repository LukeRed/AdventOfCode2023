package eu.lukered.aoc2023;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8 {
    public static void main(String[] args) {
        final Day8 day8 = new Day8();

        final Map<String, Node> collect =
                Input.DAY8_INPUT.stream().skip(2).map(Node::of).collect(Collectors.toMap(Node::id, n -> n));
        day8.step1(Input.DAY8_INPUT.get(0), collect);
        day8.step2(Input.DAY8_INPUT.get(0), collect);
    }

    private void step1(final String nav, final Map<String, Node> routes) {
        String currentNode = "AAA";
        long count = getCount(nav, routes, currentNode, s -> Objects.equals(s, "ZZZ"));

        System.out.println("Step 1: " + count);
    }

    private static long getCount(String nav, Map<String, Node> routes, final String startNode, Predicate<String> checker) {
        String currentNode = startNode;
        int navPointer = 0;
        long count = 0;
        while (!checker.test(currentNode)) {
            char instruction = nav.charAt(navPointer);
            navPointer = navPointer + 1 >= nav.length()? 0: navPointer + 1;
            if (instruction == 'L') {
                currentNode = routes.get(currentNode).left;
            }  else {
                currentNode = routes.get(currentNode).right;
            }
            count ++;
        }
        System.out.println(startNode + " -> " + currentNode + ": " + count);
        return count;
    }

    private void step2(final String nav, final Map<String, Node> routes) {
        List<Long> loopLength = routes.keySet().stream()
                .filter(s -> s.endsWith("A"))
                .map(s -> getCount(nav, routes, s, n -> n.endsWith("Z")))
                .toList();
        final long lcm = loopLength.stream()
                .reduce(Util::lcm)
                .orElseThrow();


        System.out.println("Step 2: " + lcm);
    }


    public record Node(String id, String left, String right) {
        private static final Pattern pattern = Pattern.compile("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)");

        public static Node of(final String raw) {
            Matcher matcher = pattern.matcher(raw);
            if (!matcher.find()) {
                throw new RuntimeException("Invalid data: " + raw);
            }
            return new Node(matcher.group(1), matcher.group(2), matcher.group(3));
        }
    }
}
