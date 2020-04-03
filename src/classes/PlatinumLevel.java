package classes;

/**
 *
 * @author rubendplaza
 */
public class PlatinumLevel extends Level {

    private double lowerBoundary = 20000;
    
    @Override
    public void increaseLevel(Customer customer) {
        double balance = customer.getBalance();
        if(balance >= lowerBoundary){
            customer.setLevel(new PlatinumLevel());
        }
    }

    @Override
    public void decreaseLevel(Customer customer) {
        double balance = customer.getBalance();
        if(balance < lowerBoundary){
            customer.setLevel(new GoldLevel());
            customer.decreaseLevel();
        }
    }
    
    @Override
    public String toString(){
        return "Platinum";
    }
    
}
