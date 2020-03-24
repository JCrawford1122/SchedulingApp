/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Utils.DBConnection;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author crawf
 * 
 * UserDBManager is a class for methods that access the user table in the database
 */
public class UserDBManager {
    public UserDBManager(){}
    // The user of the program
    private User user;
    
    private void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return user;
    }
    // Verifies the login credetials
    public Boolean login(String username, String password){
        try
        {
            // Connect to the db
           Connection connection = DBConnection.getConnection();
           Statement statement = connection.createStatement();
           // sql query
           String sqlStatement = "SELECT * FROM user";
           // results
           ResultSet result = statement.executeQuery(sqlStatement);
           // Coompare the username and password to each user in the table
           while(result.next()){
               // EXCEPTION CONTROL FOR INVALID LOGIN
               // If thers no match false
               if(!result.getString("userName").equals(username) && 
                  !result.getString("password").equals(password)){
                   return false;
               }
           }
        }
        // catch sql exception. querry was unsuccessful so return false
        catch(SQLException e)
        {
            return false;
        }
        // A matching username and password was found
        return true;
    }
    
    // Returns an array list of all users/consultants
    public ArrayList<User> allUsers(){
        // make the list
        ArrayList<User> users = new ArrayList();
        try{
            // connect to the db
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            // sql query
            String sql = "SELECT * from user";
            // results
            ResultSet results = statement.executeQuery(sql);
            // Create a user object for each row returned
            while(results.next()){
                User user = new User();
                user.setUserName(results.getString("userName"));
                user.setUserId(results.getInt("userId"));
                // add the user to the list
                users.add(user);
            }
        // catch sql exception. No action neccessary here.    
        }catch(SQLException e){}

        return users;
    }
    
    // Returns boolean value for checking if the user has an appointment
    // between two times
    public boolean appointmentAlert(String userName, String time, String time2){
        // return value. gets set to true if conditions are met
        boolean alertUser = false;
        try{
            // connect to the db
            Connection connection = DBConnection.getConnection();
            // sql query
            String sql ="SELECT * FROM appointment INNER JOIN user USING(userId) "
                + "WHERE (userName = ?) AND (start BETWEEN ? AND ? ) ";
            PreparedStatement statement = connection.prepareStatement(sql);
            // set the placeholder values for the sql string
            statement.setString(1, userName);
            statement.setString(2, time);
            statement.setString(3, time2);
            // results
            ResultSet results = statement.executeQuery();
            // if a row is returned the user has an upcoming appointment
            if(results.next()){
                alertUser = true;
            }
        // Catch sql exception. No action neccessary here.    
        }catch(SQLException e){}
        return alertUser;
    }
}
