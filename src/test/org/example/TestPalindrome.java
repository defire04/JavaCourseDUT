package test.org.example;


import main.org.example.Main;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestPalindrome {

    @Test
    public void palindrome() {
        assertFalse(Main.checkIsPalindrome(null));
        assertTrue(Main.checkIsPalindrome("radar"));
        assertTrue(Main.checkIsPalindrome("step on no pets"));
        assertFalse(Main.checkIsPalindrome("Step on no pets"));
        assertFalse(Main.checkIsPalindrome("A man a plan a canal Panama"));
    }
}
