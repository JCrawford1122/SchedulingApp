/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.City;
import Models.CustomerDBManager;
import Models.LocationReport;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crawf
 */
public class LocationReportController implements Initializable {
  @FXML
    private TableView<LocationReport> tbl_Report;

    @FXML
    private TableColumn<?, ?> col_Customer;

    @FXML
    private TableColumn<?, ?> col_Consultant;

    @FXML
    private TableColumn<?, ?> col_Type;

    @FXML
    private TableColumn<?, ?> col_Time;

    @FXML
    private ComboBox<String> cbox_Location;

    @FXML
    private Button btn_MainMenu;
    
    private ArrayList<City> cities= new ArrayList();
    private ArrayList<LocationReport> reportList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Connect to the db
        CustomerDBManager udb = new CustomerDBManager();
        // make a list for the report
        reportList = new ArrayList();
        // get the locations from the db
        cities = udb.dbGetCities();
           // Another example of Lamda expression. Populate the choice box of 
           // cities 
           cities.forEach((c) -> {
           cbox_Location.getItems().add(c.getCity());
        });
        // set the table column values
        col_Customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_Consultant.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_Time.setCellValueFactory(new PropertyValueFactory<>("time"));
        try{
            // update the table
            tbl_Report.setItems(FXCollections.observableList(reportList));
        // catch an exception caused by an empty list. Leave the table empty    
        }catch(Exception e){}
    }    
    
    // Action listener for the location combobox box
    public void selectLocation(ActionEvent e)throws IOException{
        // connect to the db
        CustomerDBManager db = new CustomerDBManager();
        // get the value out of the combobox
        String location = cbox_Location.getValue();
        // get the list from the db
        reportList = db.dbLocationReport(location);
        // update the table
        tbl_Report.setItems(FXCollections.observableList(reportList));
    }
        
    // Action listener for the main menu button. Returns the user to the main menu    
    public void btnMainMenu(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }   
    
}
