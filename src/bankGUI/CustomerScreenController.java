package bankGUI;

import classes.Customer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rubendplaza
 */
public class CustomerScreenController implements Initializable {
    
    private final String loginScreenFXML = "LoginScreen.fxml";
    private final String loginTitle = LoginScreen.loginTitle;
    private final String txtExtension = ".txt";
    
    private final String customerUserNamesPath = "src/managerData/";
    private static final String customerDataPath = "src/CustomerData/";
    private static final String managerDataPath = "src/ManagerData/";
    private static final String customerUserNamesFile = "customerUserNames.txt";
    
    private static String currentCustomerFilePath;
    
    private static String currRole = LoginScreenController.getRole();
    private Customer customer;
    private String currUsername;
    private String currPassword;
    private String currBalance;
    
    private String tempAmount;
    private double amount;

    private Random random;
    
    @FXML
    private TextField depositAmountField;
    @FXML
    private TextField withdrawAmountField;
    @FXML
    private TextField purchaseAmountField;
    @FXML
    private Label depositErrorMessage;
    @FXML
    private Label withdrawErrorMessage;
    @FXML
    private Label purchaseErrorMessage;
    @FXML
    private Label usernameDisplayLabel;
    @FXML
    private Label balanceDisplayLabel;
    @FXML
    private Label levelDisplayLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        random = new Random();
        currentCustomerFilePath = LoginScreenController.getCustomerFilePath();
        getCustomerInfo();
        customer = new Customer(currUsername, currPassword, currRole, Double.parseDouble(currBalance));
        showCustomerInfo();
        
    }
    
    private void getCustomerInfo(){
        
        String line;
        
        try{
            FileReader fileReader = new FileReader(currentCustomerFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int itr = 0;
            while((line = bufferedReader.readLine()) != null) {
                if(itr == 0){
                    currUsername = line;
                    System.out.println("Username: " + currUsername);
                }
                if(itr == 1){
                    currPassword = line;
                    System.out.println("Password: " + currPassword);
                }
                if(itr == 2){
                    currBalance = line;
                    System.out.println("Balance: " + currBalance);
                }
                itr++;
            }
            
            bufferedReader.close();
            
        }catch(IOException ex){
            
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        
    }
    
    private void showCustomerInfo(){
        
        String line;
        
        try{
            FileReader fileReader = new FileReader(currentCustomerFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int itr = 0;
            while((line = bufferedReader.readLine()) != null) {
                if(itr == 0){
                    currUsername = line;
                    usernameDisplayLabel.setText(currUsername);
                    System.out.println("Username: " + currUsername);
                }
                if(itr == 1){
                    currPassword = line;
                    System.out.println("Password: " + currPassword);
                }
                if(itr == 2){
                    currBalance = line;
                    balanceDisplayLabel.setText(currBalance);
                    System.out.println("Balance: " + currBalance);
                }
                itr++;
            }
            
            levelDisplayLabel.setText(customer.getLevel());
            
            bufferedReader.close();
            
        }catch(IOException ex){
            
            System.out.println("Error opening usernames file.");
            ex.printStackTrace();
        }
        
    }
    
    @FXML
    private void depositButton(ActionEvent event) {
        
        tempAmount = depositAmountField.getText();
        clearDepositErrorMessage();
        
        if(isNumeric(tempAmount)){
            amount = Double.parseDouble(tempAmount);
            if(customer.deposit(amount)){
                System.out.println("Successful Deposit");
            }
            else{
                setDepositErrorMessage("Invalid Amount");
            }
        }
        else{
            setDepositErrorMessage("Not An Amount");
        }
        
        showCustomerInfo();
        
    }

    @FXML
    private void withdrawButton(ActionEvent event) {
        
        tempAmount = withdrawAmountField.getText();
        clearWithDrawErrorMessage();
        
        if(isNumeric(tempAmount)){
            amount = Double.parseDouble(tempAmount);
            if(customer.withdraw(amount)){
                System.out.println("Successful Withdraw");
            }
            else{
                setWithDrawErrorMessage("Invalid Amount");
            }
        }
        else{
            setWithDrawErrorMessage("Not An Amount");
        }
        
        showCustomerInfo();
        
    }

    @FXML
    private void purchaseButton(ActionEvent event) {
        
        tempAmount = purchaseAmountField.getText();
        clearPurchaseErrorMessage();
        
        if(isNumeric(tempAmount)){
            amount = Double.parseDouble(tempAmount);
            if(customer.makeOnlinePurchase(amount)){
                System.out.println("Successful Purchase");
            }
            else{
                setPurchaseErrorMessage("Invalid Amount");
            }
        }
        else{
            setPurchaseErrorMessage("Not An Amount");
        }
        
        showCustomerInfo();
        
    }

    @FXML
    private void getBalanceButton(ActionEvent event) {
        
        int randomInt = random.nextInt(3);
        switch (randomInt) {
            case 0:
                balanceDisplayLabel.setTextFill(Color.web("#FF0000", 0.8));
                break;
            case 1:
                balanceDisplayLabel.setTextFill(Color.web("#00FF2D", 0.8));
                break;
            case 2:
                balanceDisplayLabel.setTextFill(Color.web("#0000FF", 0.8));
                break;
            default:  
                balanceDisplayLabel.setTextFill(Color.web("#028DF0", 0.8));
                break;
        }
        
    }

    @FXML
    private void logOutButton(ActionEvent event) {
        
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
    
    private boolean isNumeric(String string){
        boolean numeric = true;
        
        try{
            Double num = Double.parseDouble(string);
        }catch(NumberFormatException ex){
            ex.printStackTrace();
            numeric = false;
        }
        return numeric;
        
    }
    
    public void setDepositErrorMessage(String errorType){
        depositErrorMessage.setText("Error: " + errorType);
    }
    
    public void clearDepositErrorMessage(){
        depositErrorMessage.setText("");
    }
    
    public void setWithDrawErrorMessage(String errorType){
        withdrawErrorMessage.setText("Error: " + errorType);
    }
    
    public void clearWithDrawErrorMessage(){
        withdrawErrorMessage.setText("");
    }
    
    public void setPurchaseErrorMessage(String errorType){
        purchaseErrorMessage.setText("Error: " + errorType);
    }
    
    public void clearPurchaseErrorMessage(){
        purchaseErrorMessage.setText("");
    }
    
}
