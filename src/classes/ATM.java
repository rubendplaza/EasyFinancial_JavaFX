package classes;

/**
 *
 * @author rubendplaza
 */
public interface ATM {

    public abstract boolean deposit(double amount);

    public abstract boolean withdraw(double amount);

    public abstract double getBalance();

    public abstract boolean makeOnlinePurchase(double amount, Level level);

}
