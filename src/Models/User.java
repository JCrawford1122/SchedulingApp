/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author crawf
 * 
 * The User is the the person operating the program. Users are also consultants.
 */
public class User {
    private int userId;
    private String userName;
    private String password;
    
    public User(){}
    
    public User(int userId, String userName, String password){
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }
    
    public void setUserId(int id){
        this.userId = id;
    }
    
    public int getUserId(){
        return this.userId;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
    public String getUserName(){
        return this.userName;
    }
    
    public void setPassword(String password){
        this.password = password;
        
    }
}
