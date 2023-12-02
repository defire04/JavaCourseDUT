package main.org.example;


import main.org.example.exception.AccountNotFoundException;
import main.org.example.exception.InsufficientFundsException;
import main.org.example.exception.NegativeAmountException;

public class Main {
    public static void main(String[] args) {


        Bank privatBank = new Bank();

        BankAccount johnDoe = null;
        BankAccount janeSmith = null;

        try {
            johnDoe = privatBank.createAccount("John Doe", 1);
            janeSmith = privatBank.createAccount("Jane Smith", 10);

            BankAccount badUser = privatBank.createAccount("Andrew", -10);
        } catch (NegativeAmountException e) {
            System.out.println("Cant create user because: " + e.getMessage());
        }
        assert johnDoe != null;
        assert janeSmith != null;

        try {
            privatBank.transferMoney(3, janeSmith.getAccountNumber(), 5);
        } catch (InsufficientFundsException | AccountNotFoundException e ) {
            System.out.println("Cant transfer money because: " + e.getMessage());
        }

        try {
            privatBank.transferMoney(johnDoe.getAccountNumber(), janeSmith.getAccountNumber(), 5);
        } catch (InsufficientFundsException | AccountNotFoundException e ) {
            System.out.println("Cant transfer money because: " + e.getMessage());
        }

        johnDoe.getAccountSummary();
        janeSmith.getAccountSummary();
    }
}
