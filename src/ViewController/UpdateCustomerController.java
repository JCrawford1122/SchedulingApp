/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.Customer;
import Models.CustomerDBManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crawf
 */
public class UpdateCustomerController implements Initializable {
    @FXML
    private TableView<Customer> tbl_Customers;

    @FXML
    private TextField txt_Name;

    @FXML
    private TextField txt_Address;

    @FXML
    private TextField txt_Address2;

    @FXML
    private MenuButton menu_City;

    @FXML
    private TextField txt_Country;

    @FXML
    private TextField txt_Zipcode;

    @FXML
    private TextField txt_Phone;
     @FXML
    private TableColumn<String, String> col_Name;

    @FXML
    private TableColumn<Integer, Integer> col_customerId;
    
    ArrayList<Customer> customers;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Instantiate db manager
        CustomerDBManager db = new CustomerDBManager();
        // Add all customers from the db to a list
        customers = db.dbAllCustomers();
        // Set the values for the table columns
        col_Name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        // add the column values to the table
        try{
            tbl_Customers.setItems(FXCollections.observableList(customers));
            
        }catch(Exception e){System.out.println(e.getMessage());};
    }    
    // action listener for the modify customer button
    public void modifyCustomer(ActionEvent event)throws IOException{
        // make a new customer object
        Customer customer;
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // When there is a customer selected
        if(!(tbl_Customers.getSelectionModel().getSelectedItem() == null)){
            // get the select customer
            customer = tbl_Customers.getSelectionModel().getSelectedItem();
            System.out.println(customer.getAddress().getAddressId());
            // get the controller for adding customers
            AddCustomerController controller = loader.getController();
            // set the form fields to the values of the customer
            controller.setFields(customer);
            // pass the customer to the addcustomer controller
            controller.setCustomer(customer);
            
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    
    // Deletes a customer from the db
    public void deleteCustomer(ActionEvent event)throws IOException{
        Customer customer;
        // When a customer is selected from the table
        if(!(tbl_Customers.getSelectionModel().getSelectedItem() == null)){
            // get the selected customer
            customer = tbl_Customers.getSelectionModel().getSelectedItem();
            // Instantiate db manager
            CustomerDBManager db = new CustomerDBManager();
            // Delete the customer
            db.deleteCustomer(customer);
            // update the list of customers
            customers = db.dbAllCustomers();
            // update the table
            tbl_Customers.setItems(FXCollections.observableList(customers));
        }
    }
    
    // Action listener for cancel button. Returns to the main menu.
    public void btnCancel(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
    
    
    

