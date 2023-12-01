# Laboratory work 4.2 (Hard)

## Program Functionality

In a galaxy far, far away there are messages encrypted in strange patterns that needed decoding. Men was tasked with writing a program that decodes these messages.

An encrypted message consists of words separated by delimiters, and each word can be encoded using different methods.

## Phase 1: Create first decoding method 
```java
 public static String vowelSubstitutions(String input) {
    Map<String, String> vowels = new HashMap<>();

    vowels.put("1", "a");
    vowels.put("2", "e");
    vowels.put("3", "i");
    vowels.put("4", "o");
    vowels.put("5", "u");


    return Arrays.stream(input.split("")).map(letter -> vowels.getOrDefault(letter, letter)).collect(Collectors.joining());
}
```

## Phase 2: Create second decoding method

```java
public static String consonantSubstitution(String input) {
    List<String> consonant = List.of("bcdfghjklmnpqrstvwxyz".split(""));

    return Arrays.stream(input.split("")).map(letter -> {

    if (consonant.contains(letter)) {
        return consonant.get(consonant.indexOf(letter) - 1);
    }

    return letter;
    }).collect(Collectors.joining());
}
```
## Phase 3: Method determining the type of decryption by word
```java
public static String findKindOfEncodingByWord(String input) {

    Pattern pattern = Pattern.compile("[^aeiou]+\\d+");
    Matcher matcher = pattern.matcher(input);

    if (matcher.find()) {
        return "The word " + input + " is encrypted by vowel substitutions ";
    } else {
        return "The word " + input + " is encrypted by consonant substitution ";
    }
}
```

## Phase 4: Method determining the type of decryption by message
```java
public static void findKindOfEncodingByMessage(String input) {
    Arrays.stream(input.split(" ")).forEach(Decoder::findKindOfEncodingByWord);
}
```


## Phase 5: Created a main function to demonstrate your decoder
```java
public class Main {
    public static void main(String[] args) {

        System.out.println(Decoder.vowelSubstitutions("t2st3ng"));
        System.out.println(Decoder.consonantSubstitution("vetviph"));
        Decoder.findKindOfEncodingByWord("vetviph");
        Decoder.findKindOfEncodingByMessage("t2st3ng vetviph");

    }
}
```

## Phase 6: Write test
```java
public class DecoderTest {

    @Test
    public void vowelSubstitutions(){
       assertEquals(Decoder.vowelSubstitutions("t2st3ng"), "testing");
    }

    @Test
    public void consonantSubstitution(){
        assertEquals(Decoder.consonantSubstitution("vetviph"), "testing");
    }

    @Test
    public void findKindOfEncodingByWord(){
        assertTrue( Decoder.findKindOfEncodingByWord("vetviph").contains("consonant"));
    }
}
```

# Conclusion

I decrypted the messages. Understood the immutable nature of String. I worked with row methods. Used regular expressions.