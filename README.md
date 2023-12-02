# Laboratory work 6

## Program Functionality

This system, implemented in the Java programming language, provides functionality for creating accounts, carrying out financial transactions and reconciling accounts. The program handles different types of errors and uses specialized exception classes to handle specific error scenarios, providing robustness against unforeseen situations. The implementation also uses the principle of exception propagation to efficiently manage exceptions in the program.

## Phase 1: Array Initialization

```java

public static final int[][][] CINEMA = new int[5][10][20];

```

## Phase 2: Seat Reservation

```java

    public static void bookSeats(int hallNumber, int row, int[] seats) {
        System.out.println("Booking seats:");

        if (hallNumber >= 1 && hallNumber <= CINEMA.length) {
            int hallIndex = hallNumber - 1;

            if (row >= 1 && row <= CINEMA[hallIndex].length) {
                int rowIndex = row - 1;

                for (int seat : seats) {
                    if (seat >= 1 && seat <= CINEMA[hallIndex][rowIndex].length) {
                        int seatIndex = seat - 1;

                        if (CINEMA[hallIndex][rowIndex][seatIndex] == 0) {
                            CINEMA[hallIndex][rowIndex][seatIndex] = 1;
                            System.out.println("Seat " + hallNumber + "-" + row + "-" + seat + " booked");
                        } else {
                            System.out.println("Seat " + hallNumber + "-" + row + "-" + seat + " already booked");
                        }
                    } else {
                        System.out.println("Invalid seat " + hallNumber + "-" + row + "-" + seat);
                    }
                }
            } else {
                System.out.println("Invalid row " + hallNumber + "-" + row);
            }
        } else {
            System.out.println("Invalid hall " + hallNumber);
        }
    }

```

## Phase 3: Canceling a Reservation

```java


    public static void cancelBooking(int hallNumber, int row, int[] seats) {
        System.out.println("Canceling booked seats:");

        if (hallNumber >= 1 && hallNumber <= CINEMA.length) {
            int hallIndex = hallNumber - 1;

            if (row >= 1 && row <= CINEMA[hallIndex].length) {
                int rowIndex = row - 1;

                for (int seat : seats) {
                    if (seat >= 1 && seat <= CINEMA[hallIndex][rowIndex].length) {
                        int seatIndex = seat - 1;

                        if (CINEMA[hallIndex][rowIndex][seatIndex] == 1) {
                            CINEMA[hallIndex][rowIndex][seatIndex] = 0;
                            System.out.println("Booking for seat " + hallNumber + "-" + row + "-" + seat + " canceled");
                        } else {
                            System.out.println("Seat " + hallNumber + "-" + row + "-" + seat + " was not booked");
                        }
                    } else {
                        System.out.println("Invalid seat " + hallNumber + "-" + row + "-" + seat);
                    }
                }
            } else {
                System.out.println("Invalid row " + hallNumber + "-" + row);
            }
        } else {
            System.out.println("Invalid hall " + hallNumber);
        }
    }


```

## Phase 4: Availability Check

```java

    public static void checkAvailability(int screen, int numSeats) {
        System.out.println("Checking seat availability for Screen " + screen + ":");

        if (screen >= 1 && screen <= CINEMA.length) {
            int hallIndex = screen - 1;

            for (int row = 0; row < CINEMA[hallIndex].length; row++) {
                int availableSeats = 0;

                for (int seat = 0; seat < CINEMA[hallIndex][row].length; seat++) {
                    if (CINEMA[hallIndex][row][seat] == 0) {
                        availableSeats++;

                        if (availableSeats == numSeats) {
                            System.out.println("Available seats: Row " + (row + 1) + ", Seats " + (seat - numSeats + 2) + " to " + (seat + 1));
                            return;
                        }
                    } else {
                        availableSeats = 0; // Reset the count if a booked seat is encountered
                    }
                }
            }

            System.out.println("Sorry, not enough consecutive available seats found.");
        } else {
            System.out.println("Invalid screen number " + screen);
        }
    }


```

## Phase 5: Printing a Seating Arrangement

```java


    
    private static void printCinema() {
        System.out.println("Seats in the cinema:");

        for (int hall = 0; hall < CINEMA.length; hall++) {
            System.out.println("Hall " + (hall + 1) + ":");

            for (int row = 0; row < CINEMA[hall].length; row++) {
                System.out.print("Row " + (row + 1) + ": ");

                for (int seat = 0; seat < CINEMA[hall][row].length; seat++) {
                    System.out.print(CINEMA[hall][row][seat] + " ");
                }

                System.out.println();
            }

            System.out.println();
        }
    }
    private static void printSeatingArrangement(int hallNumber) {
        System.out.println("Seating arrangement for Hall " + hallNumber + ":");

        if (hallNumber >= 1 && hallNumber <= CINEMA.length) {
            int hallIndex = hallNumber - 1;

            for (int row = 0; row < CINEMA[hallIndex].length; row++) {
                System.out.print("Row " + (row + 1) + ": ");

                for (int seat = 0; seat < CINEMA[hallIndex][row].length; seat++) {
                    char status = (CINEMA[hallIndex][row][seat] == 0) ? 'O' : 'X';
                    System.out.print(status + " ");
                }

                System.out.println();
            }
        } else {
            System.out.println("Invalid hall number " + hallNumber);
        }
    }



```

## Phase 6: Find best available

```java
    private static List<Integer> findBestAvailable(int hallNumber, int numSeats) {
        if (hallNumber >= 1 && hallNumber <= CINEMA.length) {
            int hallIndex = hallNumber - 1;

            for (int row = 0; row < CINEMA[hallIndex].length; row++) {
                int availableSeats = 0;
                int startSeat = -1;

                for (int seat = 0; seat < CINEMA[hallIndex][row].length; seat++) {
                    if (CINEMA[hallIndex][row][seat] == 0) {
                        if (startSeat == -1) {
                            startSeat = seat;
                        }

                        availableSeats++;

                        if (availableSeats == numSeats) {
                            return List.of(row + 1, startSeat + 1);
                        }
                    } else {
                        availableSeats = 0;
                        startSeat = -1;
                    }
                }
            }
        }
        return null;
    }
    

```

## Phase 7: Auto book
```java



    public static void autoBook(int hallNumber, int numSeats) {
        System.out.println("Auto-booking " + numSeats + " best available seats:");

        List<Integer> bestAvailable = findBestAvailable(hallNumber, numSeats);

        if (bestAvailable != null) {
            int rowToBook = bestAvailable.get(0);
            int startSeatToBook = bestAvailable.get(1);

            bookSeats(hallNumber, rowToBook, generateSeatArray(startSeatToBook, numSeats));

            System.out.println("Auto-booked seats: Hall " + hallNumber + ", Row " + rowToBook + ", Seats " + startSeatToBook + " to " + (startSeatToBook + numSeats - 1));
        } else {
            System.out.println("Not enough consecutive available seats to auto-book.");
        }
    }

    private static int[] generateSeatArray(int startSeat, int numSeats) {
        int[] seats = new int[numSeats];
        for (int i = 0; i < numSeats; i++) {
            seats[i] = startSeat + i;
        }
        return seats;
    }
```


# Conclusion

In this lab, we developed a Java program using multidimensional arrays and complex algorithms to solve a real-world problem. We have mastered working with arrays, performed efficient operations such as sorting and filtering, and successfully applied algorithms to optimize the processing of large volumes of data