package main.org.example;


import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        System.out.println(Decoder.vowelSubstitutions("t2st3ng"));
        System.out.println(Decoder.consonantSubstitution("vetviph"));
        System.out.println(Decoder.findKindOfEncodingByWord("vetviph"));
        Decoder.findKindOfEncodingByMessage("t2st3ng vetviph");

    }
}
