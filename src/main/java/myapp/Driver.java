package myapp;

public class Driver {

    String firstName;
    String lastName;
    String username;
    int birthyear;
    int birthmonth;
    int birthday;
    String number;
    String address;
    String city;
    String zipCode;
    String state;
    String password;
    String license;
    String plateNum;
    int numPassengers;
    int overallRating;
    String isOnline;
    String isAvailable;

    String type = "driver";

    public Driver(String firstName, String lastName, String username, int birthday, int birthmonth, int birthyear, String number,
           String address, String city, String zipCode, String state, String password, String license, String plateNum, int numPassengers)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthyear = birthyear;
        this.birthday = birthday;
        this.birthmonth = birthmonth;
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
        this.overallRating = 0;
        this.isOnline = "false";
        this.isAvailable = "false";
    }

}
