/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.CustomerDBManager;
import Models.MonthTypeReport;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author crawf
 */
public class TypeReportController implements Initializable {
    @FXML
    private TableView<MonthTypeReport> tbl_Report;

    @FXML
    private TableColumn<?, ?> col_Month;

    @FXML
    private TableColumn<?, ?> col_Count;

    @FXML
    private Button btn_Back;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // connect to the db
        CustomerDBManager db = new CustomerDBManager();
        // create a list of MonthlyTypeReport objects
        ArrayList<MonthTypeReport> reportList = db.dbMonthTypeReport();
        // set the column values
        col_Month.setCellValueFactory(new PropertyValueFactory<>("month"));
        col_Count.setCellValueFactory(new PropertyValueFactory<>("count"));
        System.out.println("reportList count = " + reportList.size());
           try{
            // update the table   
            tbl_Report.setItems(FXCollections.observableList(reportList));
        // catch an exception for an empty table. Leave the table empty    
        }catch(Exception e){}
    }    
        
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
