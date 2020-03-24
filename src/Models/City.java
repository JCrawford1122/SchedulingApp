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
public class City {
    private int cityId;
    private String city;
    private Country country;
    
    public City(){}
    
    public City(int cityId, String city, Country country){
        this.cityId = cityId;
        this.city = city;
        this.country = country;
    }
    
    public void setCityId(int cityId){
        this.cityId = cityId;
    }
    
    public int getCityId(){
        return this.cityId;
    }
    
    public void setCity(String city){
        this.city = city;
    }
    
    public String getCity(){
        return this.city;
    }
    
    public void setCountry(Country country){
        this.country = country;
    }
    
    public Country getCountry(){
        return this.country;
    }
}
