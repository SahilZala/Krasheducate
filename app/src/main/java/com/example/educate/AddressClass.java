package com.example.educate;

public class AddressClass {
    String address,city;

    public AddressClass(String address, String city) {
        this.address = address;
        this.city = city;
    }

    public AddressClass() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
