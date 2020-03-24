/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;
import java.util.regex.Pattern;
import Models.Address;
import Models.City;
import Models.Country;
import Models.Customer;
import Models.CustomerDBManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crawf
 */
public class AddCustomerController implements Initializable {
    // Location Constants
    private static final String PHOENIX = "Phoenix";
    private static final String NEW_YORK = "New York";
    private static final String LONDON = "London";
    private static final String UNITED_STATES = "United States";
    private static final String ENGLAND = "England";
@FXML
    private TextField txt_Name;

    @FXML
    private TextField txt_Address;

    @FXML
    private TextField txt_Address2;

    @FXML
    private MenuButton spn_City;
    
    @FXML 
    private MenuItem menuItem1;
    
   @FXML
    private MenuItem menuItem2;
    
    @FXML
    private MenuItem menuItem3;

    @FXML
    private TextField txt_Country;

    @FXML
    private TextField txt_ZipCode;

    @FXML
    private TextField txt_Phone;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_Address;

    @FXML
    private Label lbl_Address2;

    @FXML
    private Label lbl_City;

    @FXML
    private Label lbl_Country;

    @FXML
    private Label lbl_ZipCode;

    @FXML
    private Label lbl_Phone;

    @FXML
    private Button btn_Cancel;

    @FXML
    private Button btn_Add;
    
    private Customer customer;
    // List of office locations
    private ObservableList<String> cities;
    // Map cities to countries
    private Map<String, String> locationMap = new HashMap<>();
            
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // Add city and countries to the map
       locationMap.put(LONDON, ENGLAND);
       locationMap.put(PHOENIX, UNITED_STATES);
       locationMap.put(NEW_YORK, UNITED_STATES);
       // Create menu items
       menuItem1 = new MenuItem(LONDON);
       menuItem2 = new MenuItem(NEW_YORK);
       menuItem3 = new MenuItem(PHOENIX);
       // Add menu items to the menu
       spn_City.getItems().addAll(menuItem1, menuItem2, menuItem3);
       // Using Lamda to set an event handler for selecting each menu item. 
       menuItem1.setOnAction(event -> {
           // fills in the country field when the user selects a city
           txt_Country.setText(locationMap.get(menuItem1.getText()));
           // Sets the text of the menu to the selected city
           spn_City.setText(menuItem1.getText());
       });
        menuItem2.setOnAction( event -> {
           txt_Country.setText(locationMap.get(menuItem2.getText()));
           spn_City.setText(menuItem2.getText());
       });
        menuItem3.setOnAction(event -> {
        txt_Country.setText(locationMap.get(menuItem3.getText()));
        spn_City.setText(menuItem3.getText());
       });
    }
      

        
    
    public void addCustomer(ActionEvent event) throws IOException {
        // Get the values from the form fields    
        String customerName = txt_Name.getText();
        String address = txt_Address.getText();
        String address2 = txt_Address2.getText();
        String city = spn_City.getText();
        String country = txt_Country.getText();
        String zipcode = txt_ZipCode.getText();
        String phone = txt_Phone.getText();
        // Alert the user of invalid input
        if(!validateInput(customerName, address, address2, city, country, zipcode, phone)){
            Alert alert = new Alert(AlertType.INFORMATION, "Invalid Input");
            alert.showAndWait();
           
        }
        // Add a new customer to the DB
        else{
            // Check the button text
            if(btn_Add.getText().equals("Add")){
            // Instantiate db manager
            CustomerDBManager db = new CustomerDBManager();
            // Add the customer
            db.dbAddCustomer(customerName, address, address2, city, country, zipcode, phone);
            // verify that it worked. Can remove later.
            System.out.println("customer added");
            }
            // Check if we are modifying existing customer
            else if(btn_Add.getText().equals("Modify")){
                // Instantiate db manager
                CustomerDBManager db = new CustomerDBManager();
                // Modify the existing customer
                db.modifyCustomer(customer, customerName, address, address2, city, country, zipcode, phone);
            }
            // return to the main menu after add
            btnCancelOnAction(event);
        }
    }
    
    // This method runs validation on input fields. 1 form of exception control
    // against invalid input by the user.
    private boolean validateInput(String customerName, String address, String address2,
                    String city, String country, String zipcode, String phone){
        // return value for valid input
        boolean isValid = true;
        // Regular expression String
        String regex = "\\A\\d+\\z";
        // Customer name field contains a value
        if(customerName.length() == 0){
            isValid = false;
        }
        // Address field contains a value
        if(address.length() ==  0){
            isValid = false;
        }
        
        if(phone.length() == 0 || phone.length() >15 ){
            txt_Phone.setText("Invalid");
            isValid = false;
        } else if( !Pattern.matches(regex, phone)){
            txt_Phone.setText("Invalid");
            isValid = false;
        }
        // Address 2 is not required. if empty sets to an empty string
        if(address2.length() == 0){
            txt_Address2.setText("");
        }
        // check the user has selected a city
        if(city.equals("Select City")){
            isValid = false;
        }
        if(zipcode.length() == 0 || zipcode.length() >= 10){
            txt_ZipCode.setText("Invalid");
            isValid = false;
        }else if(!Pattern.matches(regex, zipcode)){
           txt_ZipCode.setText("Invalid");
           isValid = false;
        }
        // returns false if any field was invalid
        return isValid;
        
    }
    // Sets the fields to the values of a customer to be modified
    public void setFields(Customer customer){
        txt_Name.setText(customer.getCustomerName());
        txt_Address.setText(customer.getAddress().getAddress());
        txt_Address2.setText(customer.getAddress().getAddress2());
        spn_City.setText(customer.getAddress().getCity().getCity());
        txt_Country.setText(customer.getAddress().getCity().getCountry().getCountry());
        txt_ZipCode.setText(customer.getAddress().getPostalCode());
        txt_Phone.setText(customer.getAddress().getPhoneNumber());
        btn_Add.setText("Modify");
        
    }
    
    // Method returns program to the main menu when the user cancels
    public void btnCancelOnAction(ActionEvent event)throws IOException {
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Scene addCustomerScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addCustomerScene);
        stage.show();
        
        
    }
    
    // Sets the current customer passed from another controller
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
}
