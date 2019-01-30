package edu.javacourse.studentorder.domain;

public class Address {
    private String postCode;    // индекс
    private String street;      // улица
    private String building;    // здание
    private String extention;   // корпус
    private String apartment;   // квартира

    public Address() {
    }

    public Address(String postCode, String street, String building, String extention, String apartment) {
        this.postCode = postCode;
        this.street = street;
        this.building = building;
        this.extention = extention;
        this.apartment = apartment;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
}
