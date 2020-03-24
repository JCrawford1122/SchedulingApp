package ViewController;

import Models.Appointment;
import Models.CustomerDBManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_AddCustomer;

    @FXML
    private Button btn_UpdateCustomer;

 

    @FXML
    private Button btn_ViewCustomer;

    @FXML
    private Button btn_newApt;

    @FXML
    private Button btn_updateApt;

    @FXML
    private Button btn_Weekly;

    @FXML
    private Button btn_Monthly;

    @FXML
    private Button btn_TypeByMonth;

    @FXML
    private Button btn_ConsultantSched;

    @FXML
    private Button btn_aptLocations;

    @FXML
    void initialize() 
    {
        

    }
    // Loads the screen for adding new customers
    public void addCustomer(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
        Parent root = loader.load();
        Scene addCustomerScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addCustomerScene);
        stage.show();
        
    }
    // Loads the screen for Modifying customers
    public void updateCustomer(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateCustomer.fxml"));
        Parent root = loader.load();
        Scene updateCustomerScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(updateCustomerScene);
        stage.show();
    }
    // Loads the screen for viewing all customers
    public void viewCustomers(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewCustomers.fxml"));
        Parent root = loader.load();
        Scene viewCustomersScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(viewCustomersScene);
        stage.show();
    }
    // Loads the screen for creating a new appointment
    public void newAppointment(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));
        Parent root = loader.load();
        Scene addAppointmentScene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addAppointmentScene);
        stage.show();
    }
    
        // Loads the screen for creating a new appointment
    public void weeklyView(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApptWeeklyView.fxml"));
        Parent root = loader.load();
        Scene addAppointmentScene = new Scene(root);
        ApptWeeklyViewController aptWeeklyController = loader.getController();
        CustomerDBManager db = new CustomerDBManager();
        ArrayList<Appointment> appointments = db.dbWeeklyAppointments();
        aptWeeklyController.setAppointmentList(appointments);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addAppointmentScene);
        stage.show();
    }
    
    // loads the screen for monthly view of appointments
    public void monthlyView(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApptWeeklyView.fxml"));
        Parent root = loader.load();
        Scene addAppointmentScene = new Scene(root);
        ApptWeeklyViewController aptWeeklyController = loader.getController();
        CustomerDBManager db = new CustomerDBManager();
        ArrayList<Appointment> appointments = db.dbMonthlyAppointments();
        aptWeeklyController.setAppointmentList(appointments);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addAppointmentScene);
        stage.show();
    }
    
    // Event handler for update appointment button
    public void btnUpdate(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApptWeeklyView.fxml"));
        Parent root = loader.load();
        Scene addAppointmentScene = new Scene(root);
        ApptWeeklyViewController aptWeeklyController = loader.getController();
        CustomerDBManager db = new CustomerDBManager();
        ArrayList<Appointment> appointments = db.dbAllAppointments();
        aptWeeklyController.setAppointmentList(appointments);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(addAppointmentScene);
        stage.show();
    }
    
    // Event handler for the appointments by month button
    public void btnTypeByMonth(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TypeReport.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
    }
    
    // event handler for consultant report button
    public void btnConsultantReport(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConsultantReport.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    
    // event handler for location report button
    public void btnLocationReport(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LocationReport.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}