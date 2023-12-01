package main.org.example;


import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {


    }


    public static boolean checkIsPalindrome(String input) {
        return input != null && IntStream.range(0, input.length() / 2)
                .noneMatch(i -> input.charAt(i) != input.charAt(input.length() - 1 - i));
    }
}
