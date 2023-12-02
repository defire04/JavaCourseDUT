package main.org.example;

import lombok.SneakyThrows;
import main.org.example.exception.AccountNotFoundException;
import main.org.example.exception.InsufficientFundsException;
import main.org.example.exception.NegativeAmountException;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private final List<BankAccount> bankAccountList = new ArrayList<>();

    private static long number = 1;

    public BankAccount createAccount(String accountName, double initialDeposit) throws NegativeAmountException {

        if (initialDeposit < 0) {
            throw new NegativeAmountException();
        }

        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(number++)
                .balance(initialDeposit)
                .accountName(accountName)
                .build();
        bankAccountList.add(bankAccount);

        return bankAccount;
    }


    public BankAccount findAccount(long accountNumber) throws AccountNotFoundException {
        return bankAccountList.stream()
                .filter(bankAccount -> bankAccount.getAccountNumber() == accountNumber)
                .findFirst()
                .orElseThrow(AccountNotFoundException::new);
    }


    public void transferMoney(long fromAccountNumber, long toAccountNumber, double amount) throws InsufficientFundsException, AccountNotFoundException {
        BankAccount fromBankAccount = findAccount(fromAccountNumber);
        BankAccount toBankAccount = findAccount(toAccountNumber);


        if (fromBankAccount.getBalance() < amount) {
            throw new InsufficientFundsException();
        }

        fromBankAccount.withdraw(amount);


        toBankAccount.deposit(amount);
    }


}
