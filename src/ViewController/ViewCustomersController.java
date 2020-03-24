/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.Customer;
import Models.CustomerDBManager;
import Models.CustomerView;
import Utils.DBConnection;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crawf
 */
public class ViewCustomersController implements Initializable {

        @FXML
    private TableColumn<?, ?> col_Id;

    @FXML
    private TableColumn<?, ?> col_Name;

    @FXML
    private TableColumn<?, ?> col_Address;

    @FXML
    private TableColumn<?, ?> col_Address2;

    @FXML
    private TableColumn<?, ?> col_City;

    @FXML
    private TableColumn<?, ?> col_Country;
    
    @FXML
    private TableColumn<?, ?> col_PostalCode;
    
    @FXML
    private TableColumn<?, ?> col_Phone;

    @FXML
    private Button btn_Main;
    @FXML
    private TableView tbl_Customers;
    
    private ArrayList<CustomerView> customerList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Instantiate db manager
        CustomerDBManager db = new CustomerDBManager();
        // add the customers from db to list
        customerList = db.viewCustomers();
        
        // Set the table column values
        col_Id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        col_Name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_PostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        
        
        col_Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        col_Address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        col_City.setCellValueFactory(new PropertyValueFactory<>("city"));
        col_Country.setCellValueFactory(new PropertyValueFactory<>("country"));
        // Set the table
        try{
            tbl_Customers.setItems(FXCollections.observableList(customerList));
            
        }catch(Exception e){System.out.println(e.getMessage());};
    }  
    
    // Action listener for the main menu button. Returns program to the main menu.
    public void mainMenu(ActionEvent event)throws IOException {
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Scene addCustomerScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addCustomerScene);
        stage.show();
    }
    
}


