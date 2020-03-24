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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import static java.time.ZoneOffset.UTC;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author crawf
 * 
 * CustomerDBManager is class containing methods that fetch and manipulate data
 * from the database. All customer related database access must use this class.
 */
public class CustomerDBManager {
    // dbAddCustomer  adds a new customer to the database
    public void dbAddCustomer(String name, String address1, String address2,
                 String city, String country, String zip, String phone)
    {
        try{
            // create the connection to the database
            Connection connection = DBConnection.getConnection();
            try{
            // Insert the address first to satisfy foreign key constraints
                String sql = "Insert into address set address = ?, address2 =?,"
                    + "cityId = (Select cityId from city WHERE city = ?), "
                    + "postalCode = ?, phone = ?, createDate = now(), createdBy = ''"
                    + ",lastUpdateBy = ''"; 
                PreparedStatement prepStatement = connection.prepareStatement(sql);
                // set the placeholders in the sql string 
                prepStatement.setString(1, address1);
                prepStatement.setString(2, address2);
                prepStatement.setString(3, city);
                prepStatement.setString(4, zip);
                prepStatement.setString(5, phone);
                // Execute the sql statement
                prepStatement.executeUpdate();
                prepStatement.close();
            // Catch any exception and notify the user that the insert failed
            }catch(SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "address table insert failed");
                alert.showAndWait();
            }

            // Attempt to insert into the customer table
            try {
                // Value for active column. A new customer starts as active.
                int isActive = 1;
               
                String sql = "INSERT INTO customer SET customerName = ?"
                        + ",addressId = (SELECT addressId FROM address WHERE "
                        + "address = ? AND postalCode = ?), active = ?"
                        + ", createDate = now(), createdBy = '', lastUpdateBy = ''";
                PreparedStatement prepStatement = connection.prepareStatement(sql);
                // set values for the place holders in the sql string
                prepStatement.setString(1, name);
                prepStatement.setString(2, address1 );
                prepStatement.setString(3, zip);
                prepStatement.setInt(4, isActive);
                // execute the the sql statement
                prepStatement.executeUpdate();
                prepStatement.close();
                // Catch any exception and notify the user of the failure
            }catch(SQLException e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR, "customer table inser failed");
                alert.showAndWait();
            }

            
        
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to connect to the database");
            alert.showAndWait();
        }
    }
    
    // Querries the database and returns an array list of all customers in the db
    public ArrayList<Customer> dbAllCustomers(){
        // Arraylist to put customers
        ArrayList<Customer> customerList = new ArrayList();
        try{
            // Connect to the db
            Connection connection = DBConnection.getConnection();
            // statement for the sql string
            Statement statement = connection.createStatement();
            // sql to be executed
            String sql = "SELECT customerId, customerName,addressId, address, address2, city,"
                + "cityId, country, postalCode, phone FROM country INNER JOIN city USING (countryId) "
                + "INNER JOIN address USING (cityId) INNER JOIN customer USING (addressId) ORDER BY customerId";
            // results from the dabase
            ResultSet results = statement.executeQuery(sql);
            // create objects for each row returned
            while(results.next()){
                Country country = new Country();
                City city = new City();
                Address address = new Address();
                Customer customer = new Customer();
                // Set the fields for each object
                country.setCountry(results.getString("country"));
                city.setCityId(results.getInt("cityId"));
                city.setCity(results.getString("city"));
                city.setCountry(country);
                address.setAddressId(results.getInt("addressId"));
                address.setAddress(results.getString("address"));
                address.setAddress2(results.getString("address2"));
                address.setPhoneNumber(results.getString("phone"));
                address.setPostalCode(results.getString("postalCode"));
                address.setCity(city);
                customer.setCustomerId(results.getInt("customerId"));
                customer.setCustomerName(results.getString("customerName"));
                customer.setAddress(address);
                // Add the customer to the array list of customers
                customerList.add(customer);
            }
        // Catch any exception and notify the user that the query was not executed
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The sql query failed to execute");
            alert.showAndWait();
        }
        return customerList;
    }
    
    // Modifies a customer to the given values returns true if successful
    public boolean modifyCustomer(Customer customer, String name, String address1, String address2,
                 String city, String country, String zip, String phone ){
        //return value
        boolean success;
        try{
            // connect to the database    
            Connection connection = DBConnection.getConnection();
            // sql query 
            String sql = "UPDATE address set address = ?, address2 = ?,"
                + " cityId = (Select cityId from city WHERE city = ?), "
                + "postalCode = ?, phone = ? WHERE addressId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            // set the sql string placeholder values
            statement.setString(1, address1);
            statement.setString(2, address2);
            statement.setString(3, city);
            statement.setString(4, zip);
            statement.setString(5, phone);
            statement.setInt(6, customer.getAddress().getAddressId());
            // execute the update
            statement.executeUpdate();
            statement.close();
            success = true;
        // Catch an sql exception and alert the user the update has failed
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Update Failed");
            alert.showAndWait();
            // set the return value to false
            success = false;
        }
        try{
            // connect to the database
            Connection connection = DBConnection.getConnection();
            // set the sql string
            String sql = "UPDATE customer SET customerName = ?"
                        + ",addressId = (SELECT addressId FROM address WHERE "
                        + "address = ? AND postalCode = ?) WHERE customerId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            // set the sql string placeholder values
            statement.setString(1, name);
            statement.setString(2, address1);
            statement.setString(3, zip);
            statement.setInt(4, customer.getCustomerId());
            // execute the update
            statement.executeUpdate();
            statement.close();
            // set the return value to true;
            success = true;
        // Catch an sql exception and alert the user that the query failed    
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The update failed");
            alert.showAndWait();
            // set the return value to false
            success = false;
        }
        return success;
    }
    
    // Removes a customer from the database
    public boolean deleteCustomer(Customer customer){
        boolean success = false;
        
        try{
            Connection connection = DBConnection.getConnection();
            String apptSql = "DELETE FROM appointment WHERE customerId = ?";
            PreparedStatement apptStatement = connection.prepareStatement(apptSql);
            apptStatement.setInt(1, customer.getCustomerId());
            apptStatement.executeUpdate();
            apptStatement.close();
            
        }catch(Exception e){System.out.println(e.getMessage());}
        try{
            // connect to the database    
            Connection connection = DBConnection.getConnection();
            // sql statement
            String sql = "DELETE FROM customer Where customerId = ?";
            
           
            PreparedStatement statement = connection.prepareStatement(sql);
         
            // set the sql string placeholder values
            statement.setInt(1, customer.getCustomerId());
            
            //execute the statement

            statement.executeUpdate();
            statement.close();

            
            // set the return value to true
            success = true;
            // catch an sql exception and alert the user that the deletion failed
        }catch(SQLException e){
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Customer delete failed");
            alert.showAndWait();
        }
        // delete the address that is linked to the customer
        try {
            // connect to the database
            Connection connection = DBConnection.getConnection();
            // sql to delete by id
            String sql = "DELETE FROM address WHERE addressId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            // set the sql string placeholder values
            statement.setInt(1, customer.getAddress().getAddressId());
            //execute the delete
            statement.executeUpdate();
            statement.close();
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Address delete failded");
            alert.showAndWait();
        }
        return success;
    }
    
    // Creates an array list of CustomerView.
    public ArrayList<CustomerView> viewCustomers() {
        // make the list
        ArrayList<CustomerView> customers = new ArrayList();
        try{
            // connect to the database
            Connection connection = DBConnection.getConnection();
            // query for the db
            String sql =  "SELECT customerId, customerName,addressId, address, address2, city,"
                + "cityId, country, postalCode, phone FROM country INNER JOIN city USING (countryId) "
                + "INNER JOIN address USING (cityId) INNER JOIN customer USING (addressId) ORDER BY customerId";
            Statement statement = connection.createStatement();
            //query results
            ResultSet results = statement.executeQuery(sql);
            // create customerView objects from table data and add to the list
            while(results.next()){
                CustomerView customer = new CustomerView();
                customer.setCustomerId(results.getInt("customerId"));
                customer.setCustomerName(results.getString("customerName"));
                customer.setAddress(results.getString("address"));
                customer.setAddress2(results.getString("address2"));
                customer.setCity(results.getString("city"));
                customer.setPostalCode(results.getString("postalCode"));
                customer.setPhone(results.getString("phone"));
                customer.setCountry(results.getString("country"));
                // add the customerView to the list
                customers.add(customer);
            }
        // catch an sql exception and notify the user that there was an error
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The sql Failed");
            alert.showAndWait();
        }      
        return customers;
    }
    
    // Creates an array list of appointments within 7 days of method being called
    public ArrayList<Appointment> dbWeeklyAppointments() {
        // Make the list
        ArrayList<Appointment> appointments = new ArrayList();
        try{
            // connect to the database
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            // sql query
            String sql = "Select appointmentId, customerId, customerName, userId, type, start, end, contact, location"
                + " FROM appointment INNER JOIN customer Using (customerId) "
                + "WHERE start BETWEEN now() AND date_add(now(), interval 1 week) ORDER BY start";
            // sql results
            ResultSet results = statement.executeQuery(sql);
            // create appointment object for each row and add to the list
            while(results.next()){
                Appointment appointment = new Appointment();
                appointment.setCustomerName(results.getString("customerName"));
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setType(results.getString("type"));
                String timeString = results.getString("start");
                
                String convertedTime = appointmentZonedTimeFromUtc(timeString);
                System.out.println(timeString + " in the sql statement");
                appointment.setStart(convertedTime);
                System.out.println(convertedTime + " converted time");
                String endString = results.getString("end");
                String formattedEndString = appointmentZonedTimeFromUtc(endString);
                appointment.setEnd(formattedEndString);
                appointment.setContact(results.getString("contact"));
                appointment.setLocation(results.getString("location"));
                System.out.print("appointment time added as " + appointment.getStart());
                // add the appointment to the list
                appointments.add(appointment);       
            }
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error has occured");
            alert.showAndWait();
        }
        return appointments;
    }
    
    // Creates an array list of appointments with 1 month of the method being called
    public ArrayList<Appointment> dbMonthlyAppointments() {
        // make the list
        ArrayList<Appointment> appointments = new ArrayList();
    
        try{
            //connect to the database
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            // query to execute
            String sql = "Select appointmentId, customerId, customerName, userId, type, start, end, contact, location"
                + " FROM appointment INNER JOIN customer Using (customerId) "
                + "WHERE start BETWEEN now() AND LAST_DAY(now()) ORDER BY start";
            // query results
            ResultSet results = statement.executeQuery(sql);
            // Create appointment for each row returned and add to the list
            while(results.next()){
                Appointment appointment = new Appointment();
                appointment.setCustomerName(results.getString("customerName"));
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setType(results.getString("type"));
                String start = results.getString("start");
                String localStart = appointmentZonedTimeFromUtc(start);
                appointment.setStart(localStart);
                String end = results.getString("end");
                String localEnd = appointmentZonedTimeFromUtc(end);
                appointment.setEnd(localEnd);
                appointment.setContact(results.getString("contact"));
                appointment.setLocation(results.getString("location"));
                // add the appointment to the list
                appointments.add(appointment);       
        }
        // Catch an sql exception and alert the user there was an error
        }catch(SQLException e){ 
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error fetching monthly report");
            alert.showAndWait();
        }
    return appointments;
    }
    
    // Returns an arrayList of all appointments
    public ArrayList<Appointment> dbAllAppointments() {
        // create the list
        ArrayList<Appointment> appointments = new ArrayList();
    
        try{
            // connect to the database
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            // sql string to execute
            String sql = "Select appointmentId, customerId, customerName, userId, type, start, end, contact, location"
                + " FROM appointment INNER JOIN customer Using (customerId) ORDER BY start";
            // query results
            ResultSet results = statement.executeQuery(sql);
            // create appointment objects and add to the list
            while(results.next()){
                
                Appointment appointment = new Appointment();
                appointment.setCustomerName(results.getString("customerName"));
                appointment.setAppointmentId(results.getInt("appointmentId"));
                appointment.setCustomerId(results.getInt("customerId"));
                appointment.setUserId(results.getInt("userId"));
                appointment.setType(results.getString("type"));
                String convertedTime = appointmentZonedTimeFromUtc(results.getString("start"));
                appointment.setLocation(results.getString("location"));
                appointment.setStart(convertedTime);
                String convertedEndTime = appointmentZonedTimeFromUtc(results.getString("end"));
                appointment.setEnd(convertedEndTime);
                appointment.setContact(results.getString("contact"));
                // add the appointment to the list
                appointments.add(appointment);       
            }
        // catch sql exception and alert the user of an error    
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error retrieving appoinments");
            alert.showAndWait();
        }
    return appointments;
    }
    
    // Removes a row from the appointment table and returns a boolean result
    // All delete buttons call this method. No new appointments or rows are created anywhere
    public boolean deleteAppointment(Appointment appointment){
        // return value
        boolean success = false;
        try{
            // connect to the database
            Connection connection = DBConnection.getConnection();
            // delete query
            String sql = "DELETE FROM appointment Where appointmentId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Set the placeholder values
            statement.setInt(1, appointment.getAppointmentId());
            // execute the delete
            statement.executeUpdate();
            statement.close();
            // set the return value 
            success = true;
        // catch sql exception and alert the user that the delete failed    
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error deleting appointment");
            alert.showAndWait();
        }
        return success;
    }
    
    // Adds a row to the appointment table of the db    
    public void dbAddAppointment(Customer customer, String consultant, String type,
                                 String start, String end)
    {
        try{
            // connect to the db
            Connection connection = DBConnection.getConnection();
            try{
                // sql query to get a row from the table where the time of
                // the new appointment overlaps with an existing appointment and
                // customer or contact
                String sqlTime = "SELECT * FROM appointment WHERE (customerId = ? OR"
                        + " contact = ?) AND ((? BETWEEN start AND end) OR ? BETWEEN start AND end)";
                PreparedStatement ps = connection.prepareStatement(sqlTime);
                // set the placeholders in the sql string
                ps.setInt(1, customer.getCustomerId());
                ps.setString(2, consultant);
                ps.setString(3, start);
                ps.setString(4, end);
                // results of the sql query
                ResultSet results = ps.executeQuery();
                // if a row is found the appointment time is unavailable
                // alert the user to pick a new time
                if(results.next()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Unavailable Appointment Time");
                    alert.showAndWait();
                // the time is a valid time. insert the appointment    
                }else{
                    try{
                        // insert statement
                        String sql = "INSERT INTO appointment SET customerId = ?, "
                        + "userId = (Select userId from user Where userName = ?), "
                        + "contact = ?, type = ?, start = ?, end = ?, createDate = now(), "
                        + "createdBy = 'test', lastUpdate = '0000-00-00 00:00:00', lastUpdateBy = '', "
                        + "url = '', location = ?, description = '', title = '' " ; 
                        PreparedStatement prepStatement = connection.prepareStatement(sql);
                        // set the place holder values for the sql string          
                        prepStatement.setInt(1, customer.getCustomerId());
                        prepStatement.setString(2, consultant );
                        prepStatement.setString(3, consultant );
                        prepStatement.setString(4, type);
                        prepStatement.setString(5, start);
                        prepStatement.setString(6, end); 
                        prepStatement.setString(7, customer.getAddress().getCity().getCity());
                        // execute the query
                        prepStatement.executeUpdate();
                        prepStatement.close();
                    // catch sql exception and alert the user of an error              
                    }catch(SQLException e){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Error inserting appointment");
                        alert.showAndWait();
                    }
                }
                // close the prepared statement 
                ps.close();
            // catch an sql exception and alert the user    
            }catch(SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error inserting appointment");
                alert.showAndWait();
            } 
        // Catch any possible exception and alert the user        
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error connecting to the db");
            alert.showAndWait();
        }
    }
   
    // Updates an existing row in the appointment table
    public void dbUpdateAppointment(Customer customer, String consultant, String type, 
                                    String start, String end, int appointmentId){
        try{
            // connect to the database
            Connection connection = DBConnection.getConnection();
            try{
                System.out.println("attempting to update appointment");
                // query to get rows that have overlapping times, customers, or consultants
                String sqlTime = "SELECT * FROM appointment WHERE ((customerId = ? OR"
                        + " contact = ?) AND ((? BETWEEN start and end) OR (? BETWEEN start AND end) )) AND "
                        + "(appointmentId <> ?)";
                PreparedStatement ps = connection.prepareStatement(sqlTime);
                // set the placeholder values of the sql string
                ps.setInt(1, customer.getCustomerId());
                ps.setString(2, consultant);
                ps.setString(3, start);
                ps.setInt(5, appointmentId);
                ps.setString(4, end);
                // results of the sql query
                ResultSet results = ps.executeQuery();
                // if a row is returned there is an overlapping appointment
                // alert the user of unavailabe time
                if(results.next()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Unavailable Appointment Time");
                    alert.showAndWait();
                // the time is valid, update the row    
                }else{
                    try{
                        // sql update query
                        String sql = "Update appointment SET customerId = ?, "
                                + "userId = (Select userId from user Where userName = ?), "
                                + "contact = ?, type = ?, start = ?, end = ? WHERE "
                                + "appointmentId = ?" ; 
                        PreparedStatement prepStatement = connection.prepareStatement(sql);
                        // set the placeholder values for the sql string
                        prepStatement.setInt(1, customer.getCustomerId());
                        prepStatement.setString(2, consultant);
                        prepStatement.setString(3, consultant);
                        prepStatement.setString(4, type);
                        prepStatement.setString(5, start);
                        prepStatement.setString(6, end);
                        prepStatement.setInt(7, appointmentId);
                        // execute the sql query
                        prepStatement.executeUpdate();
                        prepStatement.close();
                    // catch sql exception and alert the user of failure
                    }catch(SQLException e){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Update failed");
                        alert.showAndWait();
                    }
                }
                ps.close();
            // catch sql exception and alert the user of failure
            }catch(SQLException e){
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error updating appointment");
                alert.showAndWait();
            } 
        // catch any exception and and alert the user of failure    
        }catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error connecting to the db");
            alert.showAndWait();
        }
    }
    
    // Returns an array list of MonthlyReport objects from the db
    public ArrayList<MonthTypeReport> dbMonthTypeReport(){
        // create the list
        ArrayList<MonthTypeReport> reportList = new ArrayList();
        try{
            // connect to the db
            Connection connection = DBConnection.getConnection();
            Statement statement = connection.createStatement();
            // query the db
            String sql = "SELECT MONTHNAME(start) as 'Month', COUNT(DISTINCT type) AS 'Apointment Types' "
                + " FROM appointment group by Month";
            // query results
            ResultSet results = statement.executeQuery(sql);
            // Create a MonthlyReport object for each row in results
            while(results.next()){
                MonthTypeReport report = new MonthTypeReport();
                report.setMonth(results.getString("Month"));
                report.setCount(results.getInt("Apointment Types"));
                // add the MonthlyReport to the list
                reportList.add(report);
            }
        // catch sql exception and alert the user of failure
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to retrieve report");
            alert.showAndWait();
        }
        return reportList;
    }
    
    // Returns an array list of ConsultantReport objects from the db
    public ArrayList<ConsultantReport> dbConsultantReport(String contact){
        // make the list
        ArrayList<ConsultantReport> reportList = new ArrayList();
        try{
            // connect to the db
            Connection connection = DBConnection.getConnection();
            // Query the db
            String sql = "SELECT customerName, type, start FROM appointment INNER JOIN customer "
                + " USING (customerId) WHERE contact = ? ORDER BY start";
            PreparedStatement statement = connection.prepareStatement(sql);
            // set the placeholder values for the sql string
            statement.setString(1, contact);
            // query result
            ResultSet results = statement.executeQuery();
            // Create Consultant report objects for each row returned
            while(results.next()){
                ConsultantReport report = new ConsultantReport();
                // set the fields
                report.setCustomerName(results.getString("customerName"));
                report.setType(results.getString("type"));
                
                report.setTime(appointmentZonedTimeFromUtc(results.getString("start")));
                // add to the list
                reportList.add(report);
            }
        // catch sql exception and alert user of failed report;
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to generate consultant report");
            alert.showAndWait();
        }
        return reportList;
    }
    
    // Returns an array list of cities the company operates in
    public ArrayList<City> dbGetCities(){
        // make the list
        ArrayList<City> cities = new ArrayList();
        try{
            //connect to the database
            Connection connection = DBConnection.getConnection();
            // sql query
            String sql = "SELECT city from city";
            Statement statement = connection.createStatement();
            // query results
            ResultSet results = statement.executeQuery(sql);
            // add each city to the list
            while(results.next()){
                City city = new City();
                city.setCity(results.getString("city"));
                cities.add(city);
            }
            
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to retrieve cities");
            alert.showAndWait();
        }
        return cities;
    }
    
    // Returns an array list of LocationReport objects
    public ArrayList<LocationReport> dbLocationReport(String city){
        // create the list
        ArrayList<LocationReport> reportList = new ArrayList();
        try{
        // connect to the db    
        Connection connection = DBConnection.getConnection();
        // sql query
        String sql = "SELECT customerName, type, contact, start FROM appointment INNER JOIN customer "
                + " USING (customerId) WHERE location = ? ORDER BY start";
        PreparedStatement statement = connection.prepareStatement(sql);
        // set the placeholder values for the sql string
        statement.setString(1, city);
        // query results
        ResultSet results = statement.executeQuery();
        // create a LocationReport object for each row returned
        while(results.next()){
            LocationReport report = new LocationReport();
            report.setCustomerName(results.getString("customerName"));
            report.setType(results.getString("type"));

            report.setTime(appointmentZonedTimeFromUtc(results.getString("start")));
            report.setConsultant(results.getString("contact"));
            // add to the list
            reportList.add(report);
        }
        }catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error retrieving location report");
            alert.showAndWait();
        }

        return reportList;
    }
    
        // Method for converting entered appointment from UTC to local zoned time
    public String appointmentZonedTimeFromUtc(String date){
              LocalDate ld = LocalDate.parse(date.substring(0, 10));
              LocalTime utcTime = LocalTime.parse(date.substring(11, 16));
              ZonedDateTime zdtUTC = ZonedDateTime.of(ld, utcTime, UTC);
              ZonedDateTime myTime = zdtUTC.withZoneSameInstant(ZoneId.systemDefault());
              System.out.println("myTime " + myTime + " should be 20:30");
              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
              String time = myTime.format(formatter);
              return time;

    } 
}


