package bankGUI;

import classes.Manager;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rubendplaza
 */
public class ManagerScreenController implements Initializable {
    
    private final String loginScreenFXML = "LoginScreen.fxml";
    private final String loginTitle = LoginScreen.loginTitle;
    private final String txtExtension = ".txt";
    
    private final String customerUserNamesPath = "src/managerData/";
    private static final String customerDataPath = "src/CustomerData/";
    private static final String managerDataPath = "src/ManagerData/";
    private static final String customerUserNamesFile = "customerUserNames.txt";
    
    private String tempUsername;
    private String tempPassword;
    private static double initialBalance = 100.00;
    
    private Manager manager;
    
    @FXML
    private TextField newUsernameField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField deleteUsernameField;
    @FXML
    private Label addCustomerErrorMessage;
    @FXML
    private Label deleteCustomerErrorMessage;
    @FXML
    private TextArea listOfCustomersText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        manager = Manager.getInstance();
        showCustomerInfo();
        
    }
    
    private void showCustomerInfo(){
        
        String userNamesFilePath = customerUserNamesPath + customerUserNamesFile;
        String currentCustomerFilePath = "";
        String line;
        String customerInfo = "";
        
        try{
            FileReader fileReader = new FileReader(userNamesFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                
                currentCustomerFilePath = customerDataPath + line + txtExtension;
                
                FileReader customerFileReader = new FileReader(currentCustomerFilePath);
                BufferedReader customerBufferedReader = new BufferedReader(customerFileReader);
                int itr = 0;
                String customerLine;
                while((customerLine = customerBufferedReader.readLine()) != null){
                    if(itr == 0){
                        customerInfo += ("User: " + customerLine + "   ");
                    }
                    if(itr == 1){
                        customerInfo += ("Pass: " + customerLine + "   ");
                    }
                    if(itr == 2){
                        customerInfo += ("Bal: " + customerLine + "   \n\n");
                    }
                    itr++;
                }
                //System.out.println(currentCustomerFilePath);
            }
            
            listOfCustomersText.setText(customerInfo);
            
            bufferedReader.close();
            
        }catch(IOException ex){
            setAddCustomerErrorMessage("Error Opening customerUserNames File");
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        
    }

    @FXML
    private void deleteCustomerButton(ActionEvent event) {
        
        tempUsername = deleteUsernameField.getText();
        
        clearDeleteCustomerErrorMessage();
        
        if(tempUsername.isEmpty()){
            setDeleteCustomerErrorMessage("Username Empty");
            return;
        }
        
        String customerFilePath = customerDataPath + tempUsername + txtExtension;
        
        if(customerDoesNotExist(tempUsername)){
            setDeleteCustomerErrorMessage("Customer Does Not Exist");
            showCustomerInfo();
        }
        else{
            deleteCustomerFile(customerFilePath);
            deleteCustomerFromList(tempUsername);
            manager.deleteCustomer(tempUsername);
            showCustomerInfo();
        }
        
    }
    
    private void deleteCustomerFile(String customerFilePath){
        
        File file = new File(customerFilePath);
        
        if(file.delete()){
            System.out.println("File Deleted Successfully");
        }
        else{
            System.out.println("Failed To Delete File");
        }
        
    }
    
    private void deleteCustomerFromList(String username){
        
        String userNamesFilePath = customerUserNamesPath + customerUserNamesFile;
        String tempFilePath = customerUserNamesPath + "myTempFile.txt";
        String line;
        
        try{
            File inputFile = new File(userNamesFilePath);
            File tempFile = new File(tempFilePath);
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            while((line = reader.readLine()) != null){
                String trimmedLine = line.trim();
                if(trimmedLine.equals(username)) continue;
                writer.write(line);
                writer.newLine();
            }
            
            writer.close();
            reader.close();
            boolean successfulDelete = tempFile.renameTo(inputFile);
            System.out.println("File Deleted: " + successfulDelete);
            
        }catch(IOException ex){
            setDeleteCustomerErrorMessage("Error Opening customerUserNames File");
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        
    }

    @FXML
    private void addCustomerButton(ActionEvent event) {
        
        tempUsername = newUsernameField.getText();
        tempPassword = newPasswordField.getText();
        
        clearAddCustomerErrorMessage();
        
        if(tempUsername.isEmpty()){
            setAddCustomerErrorMessage("Username Empty");
            return;
        }
        
        if(tempPassword.isEmpty()){
            setAddCustomerErrorMessage("Password Empty");
            return;
        }
        
        String customerFilePath = customerDataPath + tempUsername + txtExtension;
        
        if(customerDoesNotExist(tempUsername)){
            createCustomerFile(customerFilePath);
            addCustomerToList(tempUsername);
            manager.addCustomer(tempUsername);
            showCustomerInfo();
        }
        else{
            setAddCustomerErrorMessage("Customer Already Exists");
            showCustomerInfo();
        }
        
    }
    
    private boolean createCustomerFile(String customerFilePath){
        
        try {
            
            File newCustomerFile = new File(customerFilePath);
            
            if(newCustomerFile.createNewFile()){
                
                System.out.println("File created: " + newCustomerFile.getName());
                
                FileWriter fileWriter = new FileWriter(customerFilePath, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                
                bufferedWriter.write(tempUsername);
                bufferedWriter.newLine();
                bufferedWriter.write(tempPassword);
                bufferedWriter.newLine();
                bufferedWriter.write(Double.toString(initialBalance));
            
                bufferedWriter.close();
                
                return true;
                
            }
            else{
                System.out.println("File already exists.");
                return false;
            }
            
        } catch (IOException ex) {
            setAddCustomerErrorMessage("Error Adding File");
            System.out.println("Error occured adding customer file.");
            ex.printStackTrace();
        }
        return false;
    }
    
    private boolean customerDoesNotExist(String username){
        
        String userNamesFilePath = customerUserNamesPath + customerUserNamesFile;
        String line;
        
        try{
            FileReader fileReader = new FileReader(userNamesFilePath);
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                if(line.equals(username)){
                    return false;
                }
            }
            
            bufferedReader.close();
            return true;
            
        }catch(IOException ex){
            setAddCustomerErrorMessage("Error Opening customerUserNames File");
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        return false;
    }
    
    private void addCustomerToList(String username){
        
        String userNamesFilePath = customerUserNamesPath + customerUserNamesFile;
        
        try{
            FileWriter fileWriter = new FileWriter(userNamesFilePath, true);
            
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            
            bufferedWriter.close();
            
        }catch(IOException ex){
            setAddCustomerErrorMessage("Error Opening customerUserNames File");
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        
    }

    @FXML
    private void managerLogOutButton(ActionEvent event) {
        
        LoginScreenController.setUsername("");
        LoginScreenController.setPassword("");
        
        loadScreen(loginScreenFXML, loginTitle, event);
        
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
    
    public void setAddCustomerErrorMessage(String errorType){
        addCustomerErrorMessage.setText("Error: " + errorType);
    }
    
    public void clearAddCustomerErrorMessage(){
        addCustomerErrorMessage.setText("");
    }
    
    public void setDeleteCustomerErrorMessage(String errorType){
        deleteCustomerErrorMessage.setText("Error: " + errorType);
    }
    
    public void clearDeleteCustomerErrorMessage(){
        deleteCustomerErrorMessage.setText("");
    }
    
}
