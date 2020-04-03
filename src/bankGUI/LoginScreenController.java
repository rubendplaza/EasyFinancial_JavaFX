package bankGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; 
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 *
 * @author rubendplaza
 */
public class LoginScreenController implements Initializable {
    
    private static final String managerRoleText = "Manager";
    private static final String customerRoleText = "Customer";
    private final String currentManagerFile = "manager.txt";
    
    private final String managerScreenFXML = "ManagerScreen.fxml";
    private final String customerScreenFXML = "CustomerScreen.fxml";
    
    private final String customerUserNamesPath = "src/managerData/";
    private static final String customerDataPath = "src/CustomerData/";
    private static final String managerDataPath = "src/managerData/";
    private static final String customerUserNamesFile = "customerUserNames.txt";
    
    private static String currentCustomerFile;
    
    private static String currentRole = "Manager";
    private static String username;
    private static String password;
    
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginErrorMessage;
    @FXML
    private ToggleGroup roleToggleGroup;
    @FXML
    private Button loginButton;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        currentRole = managerRoleText;
        
    }
   
    public static String getRole(){
        return currentRole;
    }
    
    public static void setRole(String role){
        if(role.equals(managerRoleText)){
            currentRole = managerRoleText;
        }
        if(role.equals(customerRoleText)){
            currentRole = customerRoleText;
        }
    }
    
    public static String getUsername(){
        return username;
    }
    
    public static void setUsername(String newUsername){
        username = newUsername;
    }
    
    public static String getPassword(){
        return password;
    }
    
    public static void setPassword(String newPassword){
        password = newPassword;
    }

    @FXML
    private void loginButton(ActionEvent event) {
        
        username = usernameField.getText();
        password = passwordField.getText();
        String filePath = null;
        clearLoginErrorMessage();
        
        //MANAGER SIGN IN PROCESS
        if(currentRole.equals(managerRoleText)){
            
            filePath = managerDataPath + currentManagerFile;
            
            if(fileExists(managerDataPath, currentManagerFile)){
                
                if(validateCredentials(username, password, filePath)){
                    
                    loadScreen(managerScreenFXML, managerRoleText, event);
                    
                }
                else{
                    printLoginErrorMessage("Invalid Credentials");
                    return;
                }
                
            }
            else{
                printLoginErrorMessage("User Does Not Exist");
                return;
            }
              
        }
        
        //CUSTOMER SIGN IN PROCESS
        else if(currentRole.equals(customerRoleText)){
            
            currentCustomerFile = username + ".txt";
            filePath = customerDataPath + currentCustomerFile;
            
            if(fileExists(customerDataPath, currentCustomerFile)){
                
                if(userExists(username)){
                    
                    if(validateCredentials(username, password, filePath)){
                        
                        loadScreen(customerScreenFXML, customerRoleText, event);
                        
                    }
                    else{
                        printLoginErrorMessage("Invalid Credentials");
                        return;
                    }
                }
                else{
                    printLoginErrorMessage("User Does Not Exist");
                }
                
            }
            else{
                printLoginErrorMessage("User Does Not Exist");
                return;
            }
            
        }
        
        else{
            printLoginErrorMessage("Error With Roles");
            return;
        }
        
    }

    @FXML
    private void managerRoleButton(ActionEvent event) {
        currentRole = managerRoleText;
    }

    @FXML
    private void customerRoleButton(ActionEvent event) {
        currentRole = customerRoleText;
    }
    
    private BufferedReader getBufferedReader(String filePath){
        
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        
        try{
            
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("getBufferedReader IOException");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("getBufferedReader Exception");
        }
        
        return bufferedReader;
        
    }
    
    private boolean validateCredentials(String username, String password, String userFilePath){
        
        String line = null;
        String actualUsername = null;
        String actualPassword = null;
        
        try{
            
            BufferedReader fileReader = getBufferedReader(userFilePath);
            
            int itr = 0;
            while((line = fileReader.readLine()) != null) {
                if(itr == 0){
                    actualUsername = line;
                }
                if(itr == 1){
                    actualPassword = line;
                }
                itr++;
            }
            
            fileReader.close();
            
        }catch(IOException e){
            e.printStackTrace();
            printLoginErrorMessage("Validating Credentials");
        }
        
        if(username.equals(actualUsername) && password.equals(actualPassword)){
            clearLoginErrorMessage();
            return true;
        }
        
        return false;
        
    }
    
    private void printLoginErrorMessage(String errorType){
        loginErrorMessage.setText("Login Error: " + errorType);
    }
    
    private void clearLoginErrorMessage(){
        loginErrorMessage.setText("");
    }
    
    private boolean fileExists(String directory, String file){
        
        boolean exists = false;
        
        File dir = new File(directory);
        File[] dir_contents = dir.listFiles();
        exists = new File(directory, file).exists();
        
        System.out.println("File Exists: " + new File(directory, file).exists());
        
        return exists;
        
    }
    
    private boolean userExists(String username){
        
        boolean exists = false;
        
        if(fileExists(customerUserNamesPath, customerUserNamesFile)){
            
            String filePath = customerUserNamesPath+customerUserNamesFile;
            String line = null;
            
            try{
                
                BufferedReader bufferedReader = getBufferedReader(filePath);
            
                while((line = bufferedReader.readLine()) != null) {
                    if(username.equals(line)){
                        exists = true;
                        return exists;
                    }
                }
            
                bufferedReader.close();
            
            }catch(IOException e){
                System.out.println("An error ocurred.");
                e.printStackTrace();
            }
                   
        }
        
        return exists;
        
    }
    
    private void loadScreen(String location, String title, ActionEvent event){
        
        try {
            Parent screenParent = FXMLLoader.load(getClass().getResource(location));
            Scene screenScene = new Scene(screenParent);
            
            //Gets the stage information
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setTitle(title);
            window.setScene(screenScene);
            window.show();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error Opening Up Screen");
        }
        
    }
    
    public static String getCustomerFilePath(){
        return customerDataPath+currentCustomerFile;
    }
    
    public static String getUserNamesFilePath(){
        return managerDataPath+customerUserNamesFile;
    }
    
}
