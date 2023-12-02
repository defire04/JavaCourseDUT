# Laboratory work 5

## Program Functionality

With this laboratory robot, I implemented a reliable and rack-mounted program that simulates the system of a bank
system. This system will include account management, financial transactions and settlement functions.

## Phase 1: Create class BankAccount

```java

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class BankAccount {

    @NonNull
    private Long accountNumber;

    @NonNull
    private String accountName;

    @NonNull
    private Double balance;

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void getAccountSummary() {

        System.out.println("=======================" +
                "\nAccount Number: " + accountNumber +
                "\nAccount Name: " + accountName +
                "\nBalance: " + balance);
    }
}

```

## Phase 2: Create exceptions

```java
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
        super("Account not found!");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Insufficient funds!");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}

public class NegativeAmountException extends Exception {
    public NegativeAmountException() {
        super("Negative amount!");
    }

    public NegativeAmountException(String message) {
        super(message);
    }
}

```

## Phase 3: Create class Bank

```java

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

```

## Phase 4: Created test classes where you simulated different scenarios to test exception handling.

```java
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
        } catch (InsufficientFundsException | AccountNotFoundException e) {
            System.out.println("Cant transfer money because: " + e.getMessage());
        }

        try {
            privatBank.transferMoney(johnDoe.getAccountNumber(), janeSmith.getAccountNumber(), 5);
        } catch (InsufficientFundsException | AccountNotFoundException e) {
            System.out.println("Cant transfer money because: " + e.getMessage());
        }

        johnDoe.getAccountSummary();
        janeSmith.getAccountSummary();
    }

}

```


# Conclusion

Exception handling in Java includes the use of try, catch constructs to efficiently detect, handle, and recover from exceptions. Creating specialized exception classes allows you to precisely identify and handle specific error scenarios. Exception propagation is important for passing exceptions up the call, allowing for centralized and efficient exception management in the application.