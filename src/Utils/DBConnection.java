/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author crawf
 */
public class DBConnection {
    // Database connection protocol
    private static final String protocol = "jdbc";
    // Datase vendor
    private static final String vendorName = ":mysql:";
    // Location of Database
    private static final String ipAddress ="//3.227.166.251/U05mZB";
    // Database connection string
    private static final String jdbcUrl = protocol+vendorName+ipAddress;
    // Driver interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    // Connection reference
    private static Connection connection = null;
    // User name
    private static final String userName ="U05mZB";
    // password
    private static final String password ="53688549132";
    
    public static Connection startConnection()
    {
        try{ 
            Class.forName(MYSQLJDBCDriver);
            connection = (Connection)DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("connection successful");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return connection;
    }
    
    public static void closeConnection()
    {
        try 
        {
            connection.close();
            System.out.println("connection closed");
        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static Connection getConnection(){
        return connection;
    }
}
