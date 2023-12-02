package test.org.example;

import main.org.example.Main;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MainTest {

    @Before
    public void setUp() {
        for (int[][] hall : Main.CINEMA) {
            for (int[] row : hall) {
                Arrays.fill(row, 0);
            }
        }
    }

    @Test
    public void bookAndCancelSeats() {

        Main.bookSeats(1, 1, new int[]{1, 2, 3});
        assertEquals(1, Main.CINEMA[0][0][0]);
        assertEquals(1, Main.CINEMA[0][0][1]);
        assertEquals(1, Main.CINEMA[0][0][2]);

        Main.cancelBooking(1, 1, new int[]{2});
        assertEquals(1, Main.CINEMA[0][0][0]);
        assertEquals(0, Main.CINEMA[0][0][1]);
        assertEquals(1, Main.CINEMA[0][0][2]);
    }

    @Test
    public void checkAvailability() {

        Main.bookSeats(1, 1, new int[]{1, 2, 3, 4, 5});

        Main.checkAvailability(1, 3);
        Main.checkAvailability(1, 6);

        Main.bookSeats(1, 2, new int[]{1, 2, 3, 4, 5});

        Main.checkAvailability(1, 6);
    }

    @Test
    public void autoBook() {

        Main.bookSeats(1, 1, new int[]{1, 2, 3, 4, 5});

        Main.autoBook(1, 3);

        assertEquals(1, Main.CINEMA[0][0][0]);
        assertEquals(1, Main.CINEMA[0][0][1]);
        assertEquals(1, Main.CINEMA[0][0][2]);
        assertEquals(1, Main.CINEMA[0][0][3]);
        assertEquals(1, Main.CINEMA[0][0][4]);
    }

    @Test
    public void invalidInput() {

        Main.bookSeats(1, 0, new int[]{1, 2, 3});
        assertEquals(0, Main.CINEMA[0][0][0]);

        Main.cancelBooking(0, 1, new int[]{1, 2, 3});
        assertEquals(0, Main.CINEMA[0][0][0]);
    }



}