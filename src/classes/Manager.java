package classes;

import bankGUI.LoginScreenController;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rubendplaza
 */
public class Manager extends User {
    
    private final String customerUserNamesPath = "src/managerData/";
    private static final String customerUserNamesFile = "customerUserNames.txt";
    
    private ArrayList<String> usernames;
    private static Manager instance = null;
    
    private static String username = LoginScreenController.getUsername();
    private static String password = LoginScreenController.getPassword();
    private static String role = LoginScreenController.getRole();
    
    private Manager(String username, String password, String role) {
        super(username, password, role);
        usernames = new ArrayList<String>();
        initializeUserNames();
    }

    public void addCustomer(String username){
        usernames.add(username);
    }

    public void deleteCustomer(String username){
        usernames.remove(username);
    }
    
    public void initializeUserNames(){
        
        String userNamesFilePath = customerUserNamesPath + customerUserNamesFile;
        String line;
        
        try{
            FileReader fileReader = new FileReader(userNamesFilePath);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println("Initializing Username: " + line);
                usernames.add(line);
            }
            
            bufferedReader.close();
            
        }catch(IOException ex){
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        
    }
    
    public static Manager getInstance(){
        if(instance == null){
            instance = new Manager(username, password, role);
        }
        return instance;
    }
}
