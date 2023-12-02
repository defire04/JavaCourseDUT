package main.org.example.exception;

public class NegativeAmountException extends Exception {
    public NegativeAmountException() {
        super("Negative amount!");
    }

    public NegativeAmountException(String message) {
        super(message);
    }
}
