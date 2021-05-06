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
    String password;
    String license;
    String plateNum;
    int numPassengers;

    String type = "driver";

    public Driver(String firstName, String lastName, String username, int birthday, int birthmonth, int birthyear, String number,
           String address, String password, String license, String plateNum, int numPassengers)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthyear = birthyear;
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.number = number;
        this.address = address;
        this.type = "driver";
        this.password = password;
        this.license = license;
        this.plateNum = plateNum;
        this.numPassengers = numPassengers;

    }

}
