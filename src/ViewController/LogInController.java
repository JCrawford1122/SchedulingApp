package ViewController;

import Models.User;
import Models.UserDBManager;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController implements Initializable{
    
    public static final String USERNAME = "username";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String CANCEL = "cancel";
    public static final String INVALID ="invalid";
   
    @FXML
    private Label lbl_UserName;

    @FXML
    private TextField txt_UserName;

    @FXML
    private Label lbl_Password;

    @FXML
    private TextField txt_Password;

    @FXML
    private Button btn_Cancel;
    
    @FXML
    private Button btn_Login;
    
    private String invalid;

    @FXML
    // Cancels login, closes the program
    void btnCancel(ActionEvent event) throws IOException {
        Stage stage = (Stage) btn_Cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    // Action when the user logs in
    public void btnLogin(ActionEvent event) throws IOException {
        Stage stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Parent root = loader.load();
        Scene mainMenuScene = new Scene(root);
        // Instantiate db manager
        UserDBManager manager = new UserDBManager();
        // create a new user object
        User user = new User();
        // get the username and password entered by the user
        String username = txt_UserName.getText();
        String password = txt_Password.getText();
        Logger logger = Logger.getLogger("log.txt");
        // Checks the fields for a value
        if(username.length() != 0 && password.length() != 0)
        { 
            // login and validate the user 
            // EXCEPTION CONTROL FOR INVALID LOGIN CREDENTIALS
            if(manager.login(username, password))
            {
                // creates the log file or appends to it if the file already exists
                try{
                    // create a file handler for the text file
                    FileHandler fh = new FileHandler("log.txt", true);
                    SimpleFormatter sf = new SimpleFormatter();
                    fh.setFormatter(sf);
                    // add the file handler to the logger
                    logger.addHandler(fh);
                    // set the string to log
                    logger.log(Level.INFO, (username + " logged in at " + LocalDateTime.now().toString()));
                }catch(Exception e){};
                // get the local time of the user at login
                LocalDateTime ldt = LocalDateTime.now();
                // convert the time to a zonedDateTime
                ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
                // convert the zonedDateTime to UTC time
                ZonedDateTime udt = zdt.withZoneSameInstant(ZoneOffset.UTC);
                // create a time 15 minutes from login
                ZonedDateTime udtPlusMinutes = udt.plusMinutes(15);
                // Formatter for the time
                DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                // convert the times to strings
                String time1 = udt.format(customFormatter);
                String time2 = udtPlusMinutes.format(customFormatter);
                // Check the db for appointments for the user within the time frame
                if(manager.appointmentAlert(username, time1, time2)){
                    // Alert the user of an upcoming appointment
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have an appointment within 15 minutes");
                    alert.showAndWait();
                }
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(mainMenuScene);
            stage.show();
            }

        }
        // Alert the user invalid login
                    else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, invalid);
            alert.showAndWait();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // get the users locale
        Locale locale = Locale.getDefault();
        // get the resource bundle for the users locale
        resources = ResourceBundle.getBundle("Resources/language", locale);
        
        // set ui text to the values from the resource bundle
        btn_Login.setText(resources.getString(LOGIN));
        btn_Cancel.setText(resources.getString(CANCEL));
        lbl_Password.setText(resources.getString(PASSWORD));
        lbl_UserName.setText(resources.getString(USERNAME));
        invalid = resources.getString(INVALID);
    }

}
