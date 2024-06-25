package com.example.kannammajannakka;

public class Model {

    String id, mail, name, profilepic, role, mechanicCity, mechanicState, mechanicNumber, mechanicPrice, vehicleName, mechanicName, searchKey;

    public Model() {}

    public Model(String id, String mail, String name, String profilepic, String role, String mechanicCity, String mechanicState, String mechanicNumber, String mechanicPrice, String vehicleName, String mechanicName, String searchKey) {
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.profilepic = profilepic;
        this.role = role;
        this.mechanicCity = mechanicCity;
        this.mechanicState = mechanicState;
        this.mechanicNumber = mechanicNumber;
        this.mechanicPrice = mechanicPrice;
        this.vehicleName = vehicleName;
        this.mechanicName = mechanicName;
        this.searchKey = searchKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMechanicCity() {
        return mechanicCity;
    }

    public void setMechanicCity(String mechanicCity) {
        this.mechanicCity = mechanicCity;
    }

    public String getMechanicState() {
        return mechanicState;
    }

    public void setMechanicState(String mechanicState) {
        this.mechanicState = mechanicState;
    }

    public String getMechanicNumber() {
        return mechanicNumber;
    }

    public void setMechanicNumber(String mechanicNumber) {
        this.mechanicNumber = mechanicNumber;
    }

    public String getMechanicPrice() {
        return mechanicPrice;
    }

    public void setMechanicPrice(String mechanicPrice) {
        this.mechanicPrice = mechanicPrice;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
