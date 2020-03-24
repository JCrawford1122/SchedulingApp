/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Models.Appointment;
import Models.Customer;
import Models.CustomerDBManager;
import Models.CustomerView;
import Models.User;
import Models.UserDBManager;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
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
public class AddAppointmentController implements Initializable {
     @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Customer> tbl_Customers;

    @FXML
    private TableColumn<?, ?> col_CustName;

    @FXML
    private TableColumn<?, ?> col_CustID;

    @FXML
    private TextField txt_CustName;

    @FXML
    private Button btn_Save;

    @FXML
    private Button btn_Back;

    @FXML
    private MenuButton menu_Consultant;

    @FXML
    private DatePicker dp_Date;



    @FXML
    private TextField txt_Type;
    
    @FXML 
    private ComboBox<String> cbox_Hour;
    
    @FXML
    private ComboBox<String> cbox_Minutes;
    
    @FXML
    private ComboBox<String> cbox_Type;
    // The local hours each location operates
    private final String[] businessHours = new String[]
        {"8", "9", "10", "11", "12", "13", "14", "15", "16"};
    // minute intervals for appointment times
    private final String[] businessMinutes = new String[]{"00", "15", "30", "45"};
    // types of appointments to select from
    private final String[] appointmentTypes = new String[]{"Initial Consultation", 
        "Planning Session", "Follow Up"};
    private ArrayList<Customer> customerList;
    private ArrayList<Appointment> appointmentList;
    private Appointment appointmentUpdate;
    
    private ArrayList<User> consultantList;
    // observable list for appointment types
    private ObservableList<String> apptTypes = FXCollections.observableArrayList();
    // observable list for appointment hours
    private ObservableList<String> hours = FXCollections.observableArrayList();
    //observable list for appointment minutes
    private ObservableList<String> minutes = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Create db managers
        CustomerDBManager db = new CustomerDBManager();
        UserDBManager dbUsers = new UserDBManager();
        hours.addAll(businessHours);
        minutes.addAll(businessMinutes);
        apptTypes.addAll(appointmentTypes);
        DateTimeFormatter tFormat = DateTimeFormatter.ofPattern("HH:mm");
        // populate list of customers
        customerList = db.dbAllCustomers();
        // populate list of consultants
        consultantList = dbUsers.allUsers();
        // Set the values for table columns
        col_CustID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        col_CustName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        // Autofill customer name when selected from the table
        // One example of using a lambda expression. 
        tbl_Customers.getSelectionModel().selectedItemProperty().addListener(( newValue)-> {
            if(newValue != null){
               txt_CustName.setText(tbl_Customers.getSelectionModel().getSelectedItem().getCustomerName());
            }
        });
        // Populate the table
        try{
            tbl_Customers.setItems(FXCollections.observableList(customerList));
            
        }catch(Exception e){System.out.println(e.getMessage());};
        
        // Add consultants to the consultants menu
        // Lambda to populates the menu of consultants. Greatly increases efficiency by
        // reducing the amount of code needed and provides greater readability. 
        consultantList.forEach((c) -> {
            MenuItem menuItem = new MenuItem(c.getUserName());
            // Listener to display the selected menuItem on the menu
            // Using the lambda here to set the action listener for every menuItem 
            // in one line of code. It is much more efficient that writing an action 
            // listener method for functionality that is only used here. 
            menuItem.setOnAction(e -> menu_Consultant.setText(menuItem.getText()));
            // Add the consultant to the menu
            menu_Consultant.getItems().add(menuItem);
        });
        cbox_Hour.setItems(hours);
        cbox_Minutes.setItems(minutes);
        cbox_Type.setItems(apptTypes);
        // 2nd example for use of lamda expression. Used to disable dates on
        // the datepicker that are outside of operating hours.
        // Using the lamda is more efficient because it allows me to set the the 
        // DayCellFactory without having to create a Callback .
        dp_Date.setDayCellFactory(p -> new DateCell(){
           @Override
           // method to update the datepicker
           public void updateItem(LocalDate date, boolean empty){
               super.updateItem(date, empty);
               // disable weekends and dates earlier than current date
               setDisable(empty || (date.getDayOfWeek() == DayOfWeek.SATURDAY) ||
                       (date.getDayOfWeek() == DayOfWeek.SUNDAY) || (date.isBefore(LocalDate.now())));
           }
        });
    }    
    
    // method creates an appointment object and calls the db manager class to 
    // insert the appointment to the database
    public void makeAppointment(){
        // create new appointment object
        Appointment appointment = new Appointment();
        // create a customer object
        Customer customer = new Customer();
        // get the consultant
        String consultant = menu_Consultant.getText();
        // get the value from the datepicker
        LocalDate date = dp_Date.getValue();
        // get the hour of the appointment
        String hour = cbox_Hour.getValue();
        // get the minutes of the appointment
        String minutes = cbox_Minutes.getValue();
        // get the type of appointment
        String type = cbox_Type.getValue();
        // get the customer object from the table if one is selected
        if(!(tbl_Customers.getSelectionModel().getSelectedItem() == null)){
        customer = tbl_Customers.getSelectionModel().getSelectedItem();

        }
        // validate the fields. This a form of exception control. It prevents 
        // null pointer exceptions from empty fields in the form
        if(validateInput(customer.getCustomerName(), consultant, date, hour, minutes, type)){
            // get the customer db manager
            CustomerDBManager db = new CustomerDBManager();
            // formatter for the time
            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // convert the start time to UTC
            ZonedDateTime udtStart = appointmentZonedTimeUtc(customer.getAddress().getCity().getCity(),date, hour, minutes);
            // set the end time 1 hour after start time in UTC
            ZonedDateTime udtEnd = udtStart.plusHours(1);
            // format the time for insertion
            String startString = customFormatter.format(udtStart);
            String endString = customFormatter.format(udtEnd);
            // set the customer name for the appointment
            appointment.setCustomerName(customer.getCustomerName());
            // set the contact/consultant 
            appointment.setContact(consultant);
            // set the appointment time to the formatted time
            appointment.setStart(customFormatter.format(udtStart));
            appointment.setEnd(customFormatter.format(udtEnd));
            // set the appointment type
            
            appointment.setType(type);
            // If the button text is "save" insert a new appointment
            if(btn_Save.getText().equals("Save")){
                db.dbAddAppointment(customer, consultant, type, startString, endString);
            }
            // If the button text is "update", update an existing appointment
            if(btn_Save.getText().equals("Update")){
                db.dbUpdateAppointment(customer, consultant, type, startString, 
                    endString, appointmentUpdate.getAppointmentId());
            }
            btn_Back.fire();
            // Notify the user the appointment was not added
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error adding appointment");
            alert.showAndWait();
        }    
    }
    
    // Method for converting entered appointment time to UTC zoned time
    public ZonedDateTime appointmentZonedTimeUtc(String city, LocalDate date, String hour, String minutes){
        // The local time entered by the user      
        LocalDateTime ldt;
        ldt = LocalDateTime.of(date.getYear(), date.getMonthValue(), 
                date.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minutes));
        ZonedDateTime appointmentTime;
        // convert the time to a zonedDateTime of the users system
         switch(city){
             case "London" :
                 appointmentTime = ZonedDateTime.of(ldt, ZoneId.of("Europe/London"));
                 break;
             case "Phoenix":
                 appointmentTime = ZonedDateTime.of(ldt, ZoneId.of("America/Phoenix"));
                 break;
             case "New York":
                 appointmentTime = ZonedDateTime.of(ldt, ZoneId.of("America/New_York")); 
                 break;
             default :
                 appointmentTime = ZonedDateTime.of(ldt, ZoneId.of("Europe/London"));
         }
        
        //ZonedDateTime locZdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
        // convert the zonedDateTime to UTC
        ZonedDateTime appointmentToUTC = appointmentTime.withZoneSameInstant(ZoneOffset.UTC);        
        return appointmentToUTC;
    } 
    
    // Action Listener for the save button
    public void btnSave(ActionEvent event)throws IOException{
        
        makeAppointment();
    }
    
    // Validation user input. SECOND METHOD OF EXCEPTION HANDLING
    public boolean validateInput(String custName, String consultant, LocalDate date,
                                  String hour, String minutes, String type) {
        // the return value
        boolean isValid = true;
        // set return value false if a customer is not selected
        if(txt_CustName.getText().length() == 0){
            isValid = false;
        }
        // set the return value false if a consultant is not selected
        if(menu_Consultant.getText().equals("Selet Consultant")){
            isValid = false;
        }
        // set the return vvalue false if a date is not selected
        if(dp_Date.getValue() == null){
            isValid = false;
        }
        // set the return value false if an hour is not selected
        if(cbox_Hour.getValue().equals("Hour") || !hours.contains(cbox_Hour.getSelectionModel().getSelectedItem())){
            isValid = false;
        }
        // set the return value false if minutes are not selected
        if(cbox_Minutes.getValue().equals("Minutes")){
            isValid = false;
        }
        // set the return value false if a type is not selected
        if(cbox_Type.getValue().equals("Type")){
            isValid = false;
        }
        // returns true if every condition is met
        return isValid;
    }
    
    // action listner for the main menu button. Returns user to the main menu
    public void btnMainMenu(ActionEvent event)throws IOException{
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();  
    }
    // Method pre sets the fields in the form when a user is updating an 
    // existing appointment
    public void setFields(Appointment appointment){
        LocalDate ld = LocalDate.parse(appointment.getStart().substring(0, 10));
        LocalTime lt = LocalTime.parse(appointment.getStart().substring(11, 16));
        String hour;
        LocalDateTime userViewTime = LocalDateTime.of(ld, lt);
        ZonedDateTime userZoneTime = ZonedDateTime.of(userViewTime, ZoneId.systemDefault());
        System.out.println(userZoneTime);
        ZonedDateTime officeTime;
        // find the customer with the customer id
        for(Customer c: customerList){
           if(c.getCustomerId() == appointment.getCustomerId()){
               // select the customer to autofill customer textbox
               tbl_Customers.getSelectionModel().select(c);
               // the customer was found so stop looking
               break;
           }
        }
        // set the fields as the original appointment values
        menu_Consultant.setText(appointment.getContact());
        cbox_Type.getSelectionModel().select(appointment.getType());
        // set the appointment in the controller
        this.appointmentUpdate = appointment;
        // change the button text to "update"
        btn_Save.setText("Update");
        tbl_Customers.setVisible(false);
        // SET THE DATE AND TIME FOR UPDATING APPOINTMENT
        dp_Date.setValue(LocalDate.parse(appointment.getStart().substring(0,10)));
        switch(appointment.getLocation()){
            case "London":
                officeTime = userZoneTime.withZoneSameInstant(ZoneId.of("Europe/London"));
                break;
            case "Phoenix":
                officeTime = userZoneTime.withZoneSameInstant(ZoneId.of("America/Phoenix"));
                break;
            case "New York":
                officeTime = userZoneTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                break;
            default :
                officeTime = userZoneTime.withZoneSameInstant(ZoneId.of("Europe/London"));
                 break;
        }
        
        hour = Integer.toString(officeTime.getHour());
        System.out.println(hour);
        cbox_Hour.getSelectionModel().select(hour);
        cbox_Minutes.getSelectionModel().select(appointment.getStart().substring(14,16));
    }
}
