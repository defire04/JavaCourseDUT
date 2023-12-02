package main.org.example.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Insufficient funds!");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
