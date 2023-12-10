package eu.lukered.aoc2023;

public class Util {

    public static void main(String[] args) {
        System.out.println("OUT: " + lcm(lcm(3,4), 5));
        System.out.println("OUT: " + lcm(lcm(lcm(lcm(lcm(21251, 15871), 16409), 14257), 18023), 11567));
    }

    public static long lcm(long number1, long number2) {
        return (Math.abs(number1 * number2) / gcd(number1, number2));
    }

    public static long gcd(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return number1 + number2;
        } else {
            long absNumber1 = Math.abs(number1);
            long absNumber2 = Math.abs(number2);
            long biggerValue = Math.max(absNumber1, absNumber2);
            long smallerValue = Math.min(absNumber1, absNumber2);
            return gcd(biggerValue % smallerValue, smallerValue);
        }
    }


}
