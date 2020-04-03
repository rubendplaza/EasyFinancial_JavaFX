package classes;

/**
 *
 * @author rubendplaza
 */
public class BankAccount implements ATM {

    /**
     * OVERVIEW: This mutable class is responsible for handling all the
     * transactions involved with a customer's account and updating its balance.
     */
    /**
     * AF(c) = where (Balance = b.balance)
     */
    /**
     * RI(c) = true if c.balance >= 0 = false otherwise
     */
    private double balance;

    /**
     * MODIFIES: balance
     *
     * EFFECTS: sets the account balance
     */
    public BankAccount(double balance) {
        this.balance = balance;
    }

    /**
     * EFFECTS: If balance >= 0 returns true, otherwise returns false
     */
    private boolean repOK() {
        if (balance >= 0) {
            return true;
        }
        return false;
    }

    /**
     * MODIFIES: balance
     *
     * EFFECTS: If amount to deposit less than 0 then returns false otherwise
     * deposits the amount to the balance
     */
    @Override
    public boolean deposit(double amount) {

        if (amount <= 0) {
            return false;
        }

        balance += amount;
        return true;

    }

    /**
     * MODIFIES: balance
     *
     * EFFECTS: If amount to withdraw is not greater than the balance then the
     * amount is withdrawn and returns true otherwise returns false
     */
    @Override
    public boolean withdraw(double amount) {
        if (canWithdraw(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * EFFECTS: If amount to withdraw is greater than 0 and customer has enough
     * money in their account then returns true, otherwise returns false
     */
    public boolean canWithdraw(double amount) {
        if (amount > 0 && (balance - amount) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * EFFECTS: returns balance
     */
    @Override
    public double getBalance() {
        return balance;
    }

    /**
     * MODIFIES: balance
     *
     * EFFECTS: Withdraws the amount plus the purchase fee depending on the
     * given current level of the customer
     */
    @Override
    public boolean makeOnlinePurchase(double amount, Level level) {

        if (amount < 50) {
            return false;
        }

        if (level instanceof SilverLevel) {
            if (canWithdraw(amount + 20)) {
                balance -= (amount + 20);
                return true;
            } else {
                return false;
            }
        } else if (level instanceof GoldLevel) {
            if (canWithdraw(amount + 10)) {
                balance -= (amount + 10);
                return true;
            } else {
                return false;
            }
        } else if (level instanceof PlatinumLevel) {
            if (canWithdraw(amount)) {
                balance -= (amount);
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("Doesn't Have Level");
            return false;
        }

    }

    /**
     * EFFECTS: returns the Abstraction Function implementation of the
     * BankAccount class
     */
    @Override
    public String toString() {
        return "Balance: " + this.getBalance();
    }

}
