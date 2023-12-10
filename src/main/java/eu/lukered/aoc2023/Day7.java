package eu.lukered.aoc2023;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 {

    private static boolean step2 = false;

    public static void main(String[] args) {
        final Day7 day7 = new Day7();
        final List<Hand> list = Input.DAY7_INPUT.stream().map(s -> {
                    String[] split = s.split(" ");
                    return new Hand(split[0], Integer.parseInt(split[1]));
                }).toList();
        day7.step(list);
        step2 = true;
        day7.step(list);
    }

    public void step(final List<Hand> list) {
        List<Hand> list1 = list.stream().sorted()
                .sorted(Comparator.comparingInt(s -> s.getHandType().value))
                .peek(System.out::println)
                .toList();
        int sum = IntStream.range(0, list1.size())
                .map(i -> list1.get(i).bet * (i+1))
                .sum();
        System.out.println("Step " + (step2 ? '2':'1') + ": " + sum);
    }


    public record Hand(String hand, int bet) implements Comparable<Hand> {
        public HandType getHandType() {
            List<Integer> collect = hand.chars()
                    .mapToObj(c -> (char) c)
                    .filter(c -> ! (step2 && c.equals('J')))
                    .collect(Collectors.groupingBy(p -> p, Collectors.counting()))
                    .values().stream()
                    .map(Long::intValue)
                    .sorted(Comparator.reverseOrder())
                    .toList();
            final int jokers;
            if (step2) {
                jokers = (int) hand.chars().filter(c -> c == 'J').count();
            } else {
                jokers = 0;
            }
            return switch ((!collect.isEmpty() ? collect.get(0) : 0) + jokers) {
                case 5 -> HandType.FIVE_OF_A_KIND;
                case 4 -> HandType.FOUR_OF_A_KIND;
                case 3 -> collect.size() > 1 && collect.get(1) == 2 ? HandType.FULL_HOUSE : HandType.THREE_OF_A_KIND;
                case 2 -> collect.size() > 1 && collect.get(1) == 2 ? HandType.TWO_PAIR : HandType.ONE_PAIR;
                default -> HandType.HIGH_CARD;
            };
        }

        public int getCardValue(final int position) {
            return getIntValue(hand.charAt(position));
        }

        public int getIntValue(final char character) {
            if (Character.isDigit(character)) {
                return (int) character - '0';
            }
            return switch (character) {
                case 'A' -> 14;
                case 'K' -> 13;
                case 'Q' -> 12;
                case 'J' -> Day7.step2 ? 0 : 11;
                case 'T' -> 10;
                default -> throw new RuntimeException("Unknown card :" + character);
            };
        }

        @Override
        public int compareTo(final Hand o) {
            for (int i = 0; i < hand.length(); i++) {
                int compare = Integer.compare(this.getCardValue(i), o.getCardValue(i));
                if (compare != 0) {
                    return compare;
                }
            }
            return 0;
        }

        @Override
        public String toString() {
            return hand + " -> " + getHandType() + " - BET: " + bet;
        }
    }
    public enum HandType {
        FIVE_OF_A_KIND(7),
        FOUR_OF_A_KIND(6),
        FULL_HOUSE(5),
        THREE_OF_A_KIND(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1);

        public final int value;

        HandType(int value) {
            this.value = value;
        }
    }
}
