package classes;

/**
 *
 * @author rubendplaza
 */
public class GoldLevel extends Level {

    private double upperBoundary = 20000;
    private double lowerBoundary = 10000;
        
    @Override
    public void increaseLevel(Customer customer) {
        double balance = customer.getBalance();
        if(balance >= upperBoundary){
            customer.setLevel(new PlatinumLevel());
            customer.increaseLevel();
        }
    }

    @Override
    public void decreaseLevel(Customer customer) {
        double balance = customer.getBalance();
        if(balance < lowerBoundary){
            customer.setLevel(new SilverLevel());
            customer.decreaseLevel();
        }
    }
    
    @Override
    public String toString(){
        return "Gold";
    }
    
}
