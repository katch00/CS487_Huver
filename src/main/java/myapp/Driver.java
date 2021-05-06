package myapp;

public class Driver {

    String firstName;
    String lastName;
    String username;
    String birthdate;
    String number;
    String address;
    String city;
    String zipCode;
    String state;
    String password;
    String license;
    String plateNum;
    String numPassengers;

    String type = "driver";

    public Driver(String firstName, String lastName, String username, String birthdate, String number,
           String address, String city, String zipCode, String state, String password, String license, String plateNum, String numPassengers)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthdate = birthdate;
        this.number = number;
        this.address = address;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.type = "driver";
        this.password = password;
        this.license = license;
        this.plateNum = plateNum;
        this.numPassengers = numPassengers;

    }

}
