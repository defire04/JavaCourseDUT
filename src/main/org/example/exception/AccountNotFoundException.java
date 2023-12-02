package main.org.example.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(){
        super("Account not found!");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}