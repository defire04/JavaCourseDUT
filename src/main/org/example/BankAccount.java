package main.org.example;


import lombok.*;



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
