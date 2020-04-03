package classes;

/**
 *
 * @author rubendplaza
 */
public class SilverLevel extends Level {
    
    private double upperBoundary = 10000;

    @Override
    public void increaseLevel(Customer customer) {
        double balance = customer.getBalance();
        if(balance >= upperBoundary){
            customer.setLevel(new GoldLevel());
            customer.increaseLevel();
        }  
    }

    @Override
    public void decreaseLevel(Customer customer) {
        double balance = customer.getBalance();
        if(balance < upperBoundary){
            customer.setLevel(new SilverLevel());
        }
    }
    
    @Override
    public String toString(){
        return "Silver";
    }
    
}
