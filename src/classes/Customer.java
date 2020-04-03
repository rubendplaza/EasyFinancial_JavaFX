package classes;

import bankGUI.LoginScreenController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author rubendplaza
 */
public class Customer extends User {

    private BankAccount bankAccount;
    private Level level;
    
    private static String currentCustomerFilePath;
    private static final String customerDataPath = "src/CustomerData/";

    public Customer(String username, String password, String role, double balance){
        super(username, password, role);
        currentCustomerFilePath = LoginScreenController.getCustomerFilePath();
        this.bankAccount = new BankAccount(balance);
        this.level = new SilverLevel();
        this.updateLevel();
    }
    
    public boolean deposit(double amount){
        
        if(bankAccount.deposit(amount)){
            updateBalance();
            updateLevel();
            return true;
        }
        else{
            return false;
        }
        
    }
    
    public boolean withdraw(double amount){
        
        if(bankAccount.withdraw(amount)){
            updateBalance();
            updateLevel();
            return true;
        }
        else{
            return false;
        }
        
    }
    
    public double getBalance(){
        return bankAccount.getBalance();
    }
    
    public boolean makeOnlinePurchase(double amount){
        
        if(bankAccount.makeOnlinePurchase(amount, level)){
            updateBalance();
            updateLevel();
            return true;
        }
        else{
            return false;
        }
        
    }
    
    public void updateBalance(){
        
        String tempFilePath = customerDataPath + "myTempFile.txt";
        String line;
        
        try{
            File inputFile = new File(currentCustomerFilePath);
            File tempFile = new File(tempFilePath);
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            int itr = 0;
            while((line = reader.readLine()) != null){
                
                if(itr == 0){
                    writer.write(line);
                    writer.newLine();
                }
                if(itr == 1){
                    writer.write(line);
                    writer.newLine();
                }
                if(itr == 2){
                    writer.write(Double.toString(this.getBalance()));
                }
                itr++;
                
            }
            
            writer.close();
            reader.close();
            boolean successfulDelete = tempFile.renameTo(inputFile);
            System.out.println("Customer File Update: " + successfulDelete);
            
        }catch(IOException ex){
            System.out.println("Error Updating Balance File.");
            ex.printStackTrace();
        }
        
    }

    public String getLevel() {
        return level.toString();
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void increaseLevel(){
        level.increaseLevel(this);
    }
    
    public void decreaseLevel(){
        level.decreaseLevel(this);
    }
    
    private void updateLevel(){
        this.increaseLevel();
        this.decreaseLevel();
    }
    
}

