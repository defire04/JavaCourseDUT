# Laboratory work 4.1 

## Program Functionality

Implement a function that checks whether a given word is a palindrome (reads the same both backwards and forwards) using Java strings.

## Phase 1: Create method
```java
    public static boolean checkIsPalindrome(String input) {
        return input != null && IntStream.range(0, input.length() / 2)
                .noneMatch(i -> input.charAt(i) != input.charAt(input.length() - 1 - i));
    }
```

## Phase 2: Create test

```java
    @Test
public void palindrome() {
        assertFalse(Main.checkIsPalindrome(null));
        assertTrue(Main.checkIsPalindrome("radar"));
        assertTrue(Main.checkIsPalindrome("step on no pets"));
        assertFalse(Main.checkIsPalindrome("Step on no pets"));
        assertFalse(Main.checkIsPalindrome("A man a plan a canal Panama"));
        }

```

# Conclusion

We learned how to create a function in Java to check whether a given word is a palindrome.