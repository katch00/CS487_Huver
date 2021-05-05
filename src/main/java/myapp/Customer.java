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
    String city;
    String zipCode;
    String state;
    String password;
    String cardNo;
    String securityNo;
    
    String type = "customer";

    public Customer(String firstName, String lastName, String username, int birthday, int birthmonth, int birthyear, String number,
           String address, String city, String zipCode, String state, String password, String cardNo, String securityNo)
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
        this.password = password;
        this.cardNo = cardNo;
        this.securityNo = securityNo;

    }

}
