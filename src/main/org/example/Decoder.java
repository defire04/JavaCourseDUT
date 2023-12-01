package main.org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Decoder {


    public static String vowelSubstitutions(String input) {
        Map<String, String> vowels = new HashMap<>();

        vowels.put("1", "a");
        vowels.put("2", "e");
        vowels.put("3", "i");
        vowels.put("4", "o");
        vowels.put("5", "u");


        return Arrays.stream(input.split("")).map(letter -> vowels.getOrDefault(letter, letter)).collect(Collectors.joining());
    }

    public static String consonantSubstitution(String input) {
        List<String> consonant = List.of("bcdfghjklmnpqrstvwxyz".split(""));

        return Arrays.stream(input.split("")).map(letter -> {

            if (consonant.contains(letter)) {
                return consonant.get(consonant.indexOf(letter) - 1);
            }

            return letter;
        }).collect(Collectors.joining());
    }

    public static String findKindOfEncodingByWord(String input) {

        Pattern pattern = Pattern.compile("[^aeiou]+\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return "The word " + input + " is encrypted by vowel substitutions ";
        } else {
            return "The word " + input + " is encrypted by consonant substitution ";
        }
    }

    public static void findKindOfEncodingByMessage(String input) {
        Arrays.stream(input.split(" ")).forEach(Decoder::findKindOfEncodingByWord);
    }
}
