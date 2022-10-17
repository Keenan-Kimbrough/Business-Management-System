package Model;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the Customer DB Class.
 * This handles the Customer DataBase Functionality.
 */
public class CustomerDB {


    /**
     * This is the update Customer method.
     * This SQL Command that handles updating the customer in Database.
     * @param division - This is the Division.
     * @param name - This is the name.
     * @param address - This is the address.
     * @param postalCode - This is the postal Code.
     * @param phoneNum - This is the phone Number
     * @param customerId - This is the customer Id.
     * @return - The return type is Boolean, true or false.
     * @throws SQLException - This is the SQL exception that can happen from method.
     */
    public static Boolean updateCustomer (String division, String name, String address, String postalCode, String phoneNum, Integer customerId) throws SQLException {

        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Connection connection = JDBC.connection;
        String sql = "UPDATE customers SET Customer_Name=?, Address=?, postal_Code=?, Phone=?, " +
                "Last_Update=?,Last_Updated_By=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        ps.setString(6, LogIn.getLoggedOnUser().getUserName());
        ps.setInt(7, CustomerDB.getSpecificDivisionId(division));
        ps.setInt(8, customerId);
        // Execute Query
        try {
            ps.executeUpdate();
            ps.close();
            System.out.println("Update Customer Query Successful");
            return true;
        }
        catch (SQLException e){
            e.printStackTrace();
            ps.close();
            System.out.println("Update Customer Query Failed");
            return false;
        }
    }

    /**
     * This is the delete customer method.
     * This method deletes using an SQL command to delete the selected customer from database.
     * @param customerId - This customer ID.
     * @return This returns a boolean of true of false, that means if it was successful.
     * @throws SQLException This is possible exception that can occur.
     */
    public static Boolean deleteCustomer(Integer customerId) throws SQLException {
        Connection connection = JDBC.connection;
        String sql = "DELETE FROM customers WHERE Customer_ID =?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        try {
            ps.executeUpdate();
            ps.close();
            System.out.println("Delete Customer Query successful");

            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Delete Customer Query Failed");
            return false;
        }
    }

    /**
     * This is the add Customer method.
     * This methods uses an SQL syntax to add a customer to the customer database.
     * @param country The country.
     * @param division The division.
     * @param name The name.
     * @param address The address.
     * @param postalCode The postal code.
     * @param phoneNum The phone number.
     * @param divisionId The Division.
     * @return Returns a boolean based on if successful or not.
     * @throws SQLException This exception that can come from it.
     */
    public static Boolean addCustomer(String country, String division, String name, String address, String postalCode, String phoneNum, Integer divisionId) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Connection connection = JDBC.connection;

        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone,Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) Values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);


        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phoneNum);
        ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        ps.setString(6,LogIn.getLoggedOnUser().getUserName());
        ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        ps.setString(8,LogIn.getLoggedOnUser().getUserName());
        ps.setInt(9, getSpecificDivisionId(division));

        // execute query
        try {
            ps.executeUpdate();
            ps.close();
            return true;

        }
        catch(SQLException e){
            e.printStackTrace();
            ps.close();
            return false;

        }

}


    /**
     * This is the get Specific Division Id Method.
     * This returns the specific division.
     * @param division - This is the division.
     * @return This returns an Integer.
     * @throws SQLException This is a SQL exception that can potentially occur in method.
     */
    public static Integer getSpecificDivisionId(String division) throws SQLException {
        Integer divID = 0;
        Connection connection = JDBC.connection;
        String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, division);
        ResultSet result = ps.executeQuery();

        while(result.next()) {
            divID = result.getInt("Division_ID");
        }
        ps.close();
        return divID;
    }


    /**
     * This is the get customer ID method.
     * This method returns all the customer ID
     * @return Returns an Observable list of Integers.
     * @throws SQLException This is the SQL exception that can occur in method potentially.
     */
    public static ObservableList<Integer> getAllCustomerId() throws SQLException {
        ObservableList<Integer> allCustomerId = FXCollections.observableArrayList();
        Connection connection = JDBC.connection;
        String sql = "SELECT DISTINCT Customer_ID FROM customers";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet results = ps.executeQuery();

        while( results.next()) {
            allCustomerId.add(results.getInt("Customer_ID"));
        }

        ps.close();
        return allCustomerId;
    }

    /**
     * This is the get all Countries method.
     * This method returns all the countries.
     * @return The returns is a observable list of strings.
     * @throws SQLException This is the potential SQL exception that could happen.
     */
    public static ObservableList<String> getAllCountries() throws SQLException {
        ObservableList<String> allCountries = FXCollections.observableArrayList();
        Connection connection = JDBC.connection;
        String sql = "SELECT DISTINCT Country FROM countries";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet results = ps.executeQuery();

        while(results.next()){
            allCountries.add(results.getString("Country"));
        }
        ps.close();
        return allCountries;
    }


    /**
     * This is the get Filtered Division Method.
     * This method gets the specific filtered divisions.
     * @param inputCountry - This is the specific country
     * @return This returns an Observable List of strings.
     * @throws SQLException  This is the SQL Exeception that can occur.
     */
    public static ObservableList<String> getFilteredDivision(String inputCountry) throws SQLException {
        ObservableList<String> filteredDivs = FXCollections.observableArrayList();
try{
    System.out.println("This is the input country");
        Connection connection = JDBC.connection;
        JDBC.openConnection();
        String sql = "SELECT c.Country, c.Country_ID,  d.Division_ID, d.Division FROM countries as \n" +
                "c  INNer JOIN first_level_divisions AS d ON c.Country_ID = d.Country_ID\n" +
                "AND c.Country = ?";


        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, inputCountry);
        ResultSet results = ps.executeQuery();
        System.out.println(inputCountry);




        while(results.next()){
            filteredDivs.add(results.getString("Division"));
        }
        ps.close();
        return filteredDivs;

    }catch(SQLException e){
    System.out.println("SQL Exception:" + e );
}
    return filteredDivs ;}

    /**
     * This is the get all customers method.
     * This gets all the customers.
     * @return This returns an Observable list of Customer objects.
     * @throws SQLException This is the SQL Exception that can occur in the method.
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        Connection connection = JDBC.connection;
        ObservableList <Customer> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT cx.Customer_ID, cx.Customer_Name, cx.Address, cx.Postal_Code, cx.Phone, cx.Division_ID, " +
                "f.Division, f.COUNTRY_ID, co.Country FROM customers as cx INNER JOIN first_level_divisions " +
                "as f on cx.Division_ID = f.Division_ID INNER JOIN countries as co ON f.COUNTRY_ID = co.Country_ID";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet results = ps.executeQuery();

        while(results.next()){

            Integer customerId = results.getInt("Customer_ID");
            String customerName = results.getString("Customer_Name");
            String customerAddress = results.getString("Address");
            String customerPostalCode = results.getString("Postal_Code");
            String customerPhoneNumber = results.getString("Phone");
            String customerDivision = results.getString("Division");
            Integer divId = results.getInt("Division_ID");
            String customerCountry = results.getString("Country");

            Customer newCustomer = new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivision, divId, customerCountry);

            allCustomers.add(newCustomer);
        }
        ps.close();
        return allCustomers;
    }

}
