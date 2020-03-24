/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingapp;

import Utils.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author crawf
 */
public class SchedulingApp extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ViewController/LogIn.fxml"));
        Parent mainRoot = loader.load();
        Scene loginScene = new Scene(mainRoot);
        
        stage.setScene(loginScene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
    
}
