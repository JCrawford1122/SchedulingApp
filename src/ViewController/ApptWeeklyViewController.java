/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.Appointment;
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
public class ApptWeeklyViewController implements Initializable {

    @FXML
    private TableView<Appointment> tbl_Weekly;

    @FXML
    private TableColumn<?, ?> col_Customer;

    @FXML
    private TableColumn<?, ?> col_Consultant;

    @FXML
    private TableColumn<?, ?> col_Start;

    @FXML
    private TableColumn<?, ?> col_End;

    @FXML
    private TableColumn<?, ?> col_Type;

    @FXML
    private Button btn_MainMenu;
    
    private ArrayList<Appointment> appointmentList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomerDBManager db = new CustomerDBManager();
        
        //appointmentList = db.dbWeeklyAppointments();
        // Set the table column values
        col_Customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        col_Consultant.setCellValueFactory(new PropertyValueFactory<>("contact"));
        col_Start.setCellValueFactory(new PropertyValueFactory<>("start"));
        col_End.setCellValueFactory(new PropertyValueFactory<>("end"));
        col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        
         try{
            tbl_Weekly.setItems(FXCollections.observableList(appointmentList));
            
        }catch(Exception e){System.out.println(e.getMessage());};
    }
    
    public void setAppointmentList(ArrayList<Appointment> appointments){
        this.appointmentList = appointments;
        tbl_Weekly.setItems(FXCollections.observableArrayList(appointments));
       // System.out.println("called setAppointmentList " + appointments.get(0).getStart());
    }
    
    public void deleteAppointment(ActionEvent event)throws IOException {
 
        Appointment appointment;
        if(!(tbl_Weekly.getSelectionModel().getSelectedItem() == null)){
            appointment = tbl_Weekly.getSelectionModel().getSelectedItem();
            CustomerDBManager db = new CustomerDBManager();
            db.deleteAppointment(appointment);
            appointmentList = db.dbWeeklyAppointments();
            tbl_Weekly.setItems(FXCollections.observableArrayList(appointmentList));
        }
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
    
    public void btnUpdate(ActionEvent event)throws IOException{
                // make a new customer object
        Appointment appointment;
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // When there is a customer selected
        if(!(tbl_Weekly.getSelectionModel().getSelectedItem() == null)){
            // get the select customer
            appointment = tbl_Weekly.getSelectionModel().getSelectedItem();
            
            // get the controller for adding customers
            AddAppointmentController controller = loader.getController();
            // set the form fields to the values of the customer
            
            controller.setFields(appointment);
            // pass the customer to the addcustomer controller
            
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        
        }
    }

}


