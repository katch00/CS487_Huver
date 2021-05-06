package myapp;

public class Customer {

    String firstName;
    String lastName;
    String username;
    int birthyear;
    int birthmonth;
    int birthday;
    String number;
    String address;
    String password;
    int cardNo;
    
    String type = "customer";

    public Customer(String firstName, String lastName, String username, int birthday, int birthmonth, int birthyear, String number,
           String address, String password, int cardNo)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthyear = birthyear;
        this.birthday = birthday;
        this.birthmonth = birthmonth;
        this.number = number;
        this.address = address;
        this.password = password;
        this.cardNo = cardNo;

    }

}
