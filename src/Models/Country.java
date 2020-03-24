/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author crawf
 */
public class Country {
    private int countryId;
    private String country;
    
    public Country(){}
    
    public Country(int countryId, String country){
        this.countryId = countryId;
        this.country = country;
    }
    
    public void setCountryId(int id){
        this.countryId = id;
    }
    
    public int getCountryId(){
        return this.countryId;
    }
    
    public void setCountry(String country){
        this.country = country;
    }
    
    public String getCountry(){
        return this.country;
    }
}
