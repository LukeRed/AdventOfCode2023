package eu.lukered.aoc2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class Day3 {
    public static void main(String[] args) {
        final Day3 day3 = new Day3();
        final Grid grid = new Grid(Input.DAY3_INPUT);
        day3.step1(grid);
        day3.step2(grid);
    }

    private void step1(final Grid grid) {
        final List<Integer> partNumbers = new ArrayList<>();
        for (int y = 0; y < grid.data.size(); y++) {
            final String line = grid.data.get(y);
            for (int x = 0; x < line.length(); x++) {
                final char curChar = grid.getCharAt(x, y);
                if (Character.isDigit(curChar) || curChar == '.') {
                    continue;
                }
                partNumbers.addAll(grid.getNumbersAround(x,y));
            }
        }
        final int sum = partNumbers.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Step 1: " + sum);
    }

    private void step2(final Grid grid) {
        final List<Integer> gearRatios = new ArrayList<>();
        for (int y = 0; y < grid.data.size(); y++) {
            final String line = grid.data.get(y);
            for (int x = 0; x < line.length(); x++) {
                final char curChar = grid.getCharAt(x, y);
                if (curChar != '*') {
                    continue;
                }
                final List<Integer> numbersAround = grid.getNumbersAround(x, y);
                if (numbersAround.size() != 2) {
                    continue;
                }
                gearRatios.add(numbersAround.get(0) * numbersAround.get(1));
            }
        }
        final int sum = gearRatios.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Step 2: " + sum);
    }

    private record Grid(List<String> data) {
        public char getCharAt(final int x, final int y) {
            return data.get(y).charAt(x);
        }

        public List<Integer> getNumbersAround(final int x, final int y) {
            return IntStream.range(y-1, y+2)
                    .filter(v -> v >= 0 && v < data.size())
                    .boxed()
                    .flatMap(curY -> getNumbersInLine(x, curY).stream())
                    .toList();
        }

        public List<Integer> getNumbersInLine(final int x, final int y) {
            final String line = data.get(y);
            int left = (x == 0) ? 0 : x-1;
            int right = (x+1 == line.length())? x : x+1;
            LinkedList<Character> current = new LinkedList<>(line.substring(left, right+1)
                    .chars().mapToObj(c -> (char) c).toList());

            while (left > 0 && current.getFirst() != '.') {
                left--;
                current.addFirst(line.charAt(left));
            }

            while (right < line.length() - 1 && current.getLast() != '.') {
                right++;
                current.addLast(line.charAt(right));

            }
            final StringBuilder sb = new StringBuilder();
            current.forEach(sb::append);
            return Arrays.stream(sb.toString().split("[^0-9]"))
                    .filter(s -> !s.isBlank()).map(Integer::valueOf).toList();
        }
    }
}
