package Model;

/**
 * This is the Customer Class.
 * This method has the customer class methods.
 */
public class Customer {
    private Integer customerId;
    private String customerName;
    private String address;
    private String postalCode;
    private String phone;

    private Integer divisionId;
    private String division;
    private String country;

    /**
     * This is the Customer Constructor.
     * This method is called when the Customer class is used.
     * @param inputCustomerId This is  the input customer id.
     * @param inputCustomerName This is the input customer name.
     * @param inputAddress This is the input address.
     * @param inputPostalCode This is the input postal code.
     * @param inputPhone This is the input phone number.
     * @param inputDivision This is input division.
     * @param inputDivisionId This is division id input.
     * @param inputCountry This input country input.
     */
    public Customer(Integer inputCustomerId, String inputCustomerName, String inputAddress, String inputPostalCode, String inputPhone, String inputDivision, Integer inputDivisionId, String inputCountry
    ){

        customerId = inputCustomerId;
        customerName = inputCustomerName;
        address = inputAddress;
        postalCode = inputPostalCode;
        phone = inputPhone;

        divisionId = inputDivisionId;
        division = inputDivision;
        country = inputCountry;

    }

    /**
     * This is the get Customer ID method.
     * This method gets the customer ID
     * @return - This returns an integer.
     */
    public Integer getId(){
        return customerId;
    }

    /**
     * This is the get Name Method.
     * This gets the name of the customer.
     * @return This returns a string.
     */
    public String getName(){
        return customerName;
    }

    /**
     * This is the get Address Method.
     * This gets the address of method.
     * @return - This returns a string.
     */
    public String getAddress(){
        return address;
    }

    /**
     * This get Postal code method.
     * This method gets the postal code.
     * @return - This returns a string.
     */
    public String getPostalCode(){
        return postalCode;
    }

    /**
     * This is the get phone number method.
     * This method gets the phone number.
     * @return - This returns a string.
     */
    public String getPhoneNumber(){
        return phone;
    }

    /**
     * This the get division method.
     * This method gets the division.
     * @return This returns a string.
     */
    public String getDivision(){
        return division;
    }

    /**
     * This is the get Country method.
     * This method gets the country
     * @return This method returns a string.
     */
    public String getCountry(){
        return country;
    }

    /**
     * This is the get division ID method.
     * This method gets the Division ID.
     * @return - This method returns an Intger.
     */
    public Integer getDivisionId(){
        return divisionId;
    }
}
