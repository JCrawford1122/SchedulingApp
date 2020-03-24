/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.ConsultantReport;
import Models.CustomerDBManager;
import Models.MonthTypeReport;
import Models.User;
import Models.UserDBManager;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crawf
 */
public class ConsultantReportController implements Initializable {
    @FXML
    private ComboBox<String> cbox_Consultant;

    @FXML
    private TableView<ConsultantReport> tbl_Report;

    @FXML
    private TableColumn<?, ?> col_Customer;

    @FXML
    private TableColumn<?, ?> col_Type;

    @FXML
    private TableColumn<?, ?> col_Time;

    @FXML
    private Button btn_MainMenu;
    
    ArrayList<ConsultantReport> reportList = new ArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // connect to the db
        UserDBManager udb = new UserDBManager();
        // Add consultants to the list
        ArrayList<User> consultantList = udb.allUsers();
        // This lambda populates the combobox from a list of consultants.
        // It requires less code to setup and is a very efficient implementation of
        // a for each loop.
        consultantList.forEach((c) -> {cbox_Consultant.getItems().add(c.getUserName());});
        // set the values for table columns
        col_Customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_Time.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        try{
            //set the table
            tbl_Report.setItems(FXCollections.observableList(reportList));
        // Catch an Exception if there is no data for the table. No action
        // neccessary. The table will remain empty
        }catch(Exception e){}
    }    
    
    // event handler to update the table upon selection of consultant
    public void selectConsultant(ActionEvent e)throws IOException{
        // connect to the db
        CustomerDBManager db = new CustomerDBManager();
        // get the value from the combo box
        String consultant = cbox_Consultant.getValue();
        // populate the report list
        reportList = db.dbConsultantReport(consultant);
        // update the table
        tbl_Report.setItems(FXCollections.observableList(reportList));
        
    }
    
    // event handler for the main menu button. Returns user to the main menu
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
