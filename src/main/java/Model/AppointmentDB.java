package Model;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is the Appointment DB
 * This class handles  the Appointment Data Base Methods.
 */
public class AppointmentDB {


    /**
     * This is the get Date Filtered Appointments Method.
     * This method gets all the filtered dates from appointments.
     * @param startRange - starting range.
     * @param endRange end range.
     * @return returns and observable list of appointments.
     * @throws SQLException this is the exception that can occur based on an error.
     */
    public static ObservableList<Appointment> getDateFilteredAppointments(ZonedDateTime startRange, ZonedDateTime endRange) throws SQLException {
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Connection connection = JDBC.connection;
        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start between ? AND ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        String startRangeString = startRange.format(formatter);
        String endRangeString = endRange.format(formatter);

        ps.setString(1, startRangeString);
        ps.setString(2, endRangeString);

        ResultSet results = ps.executeQuery();

        while ( results.next()) {

            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");


            Appointment newAppointment = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );
            filteredAppts.add(newAppointment);

        }
        ps.close();
        return filteredAppts;

    }

    /**
     * This is the reports total by type months.
     * This  gets all the reports and sort them by type and month.
     * @return Returns an observable list of strings.
     * @throws SQLException This is the SQL exception can occur if error.
     */
    public static ObservableList<String> reportTotalsByTypeAndMonth() throws SQLException {
        ObservableList<String> reportStrings = FXCollections.observableArrayList();
        Connection connection = JDBC.connection;
        reportStrings.add("Total Number of Appointment by Type and Month:\n");



        // Prepare SQL
        PreparedStatement typeSqlCommand = connection.prepareStatement(
                "SELECT Type, COUNT(Type) as \"Total\" FROM appointments GROUP BY Type");

        PreparedStatement monthSqlCommand = connection.prepareStatement(
                "SELECT MONTHNAME(Start) as \"Month\", COUNT(MONTH(Start)) as \"Total\" from appointments GROUP BY Month");

        ResultSet typeResults = typeSqlCommand.executeQuery();
        ResultSet monthResults = monthSqlCommand.executeQuery();

        while (typeResults.next()) {
            String typeStr = "Type: " + typeResults.getString("Type") + " Count: " +
                    typeResults.getString("Total") + "\n";
            reportStrings.add(typeStr);

        }

        while (monthResults.next()) {
            String monthStr = "Month: " + monthResults.getString("Month") + " Count: " +
                    monthResults.getString("Total") + "\n";
            reportStrings.add(monthStr);

        }

        monthSqlCommand.close();
        typeSqlCommand.close();

        return reportStrings;


    }

    /**
     * This is get Customer filtered appointments.
     * This method gets all the customers and filters the appointments.
     * @param apptDate - appointment date.
     * @param inputCustomerID customer id.
     * @return returns and observable list of appointments.
     * @throws SQLException this the SQL Exception that occurs if error.
     */
    public static ObservableList<Appointment> getCustomerFilteredAppointments(
            LocalDate apptDate, Integer inputCustomerID) throws SQLException {
        // Prepare SQL statement
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        Connection connection = JDBC.connection;
        PreparedStatement sqlCommand = connection.prepareStatement(
                "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c " +
                        "ON a.Contact_ID = c.Contact_ID WHERE datediff(a.Start, ?) = 0 AND Customer_ID = ?;"
        );

        sqlCommand.setInt(2, inputCustomerID);

        sqlCommand.setString(1, apptDate.toString());

        ResultSet results = sqlCommand.executeQuery();

        while( results.next() ) {
            // get data from the returned rows
            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");

            // populate into an appt object
            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );
            filteredAppts.add(newAppt);
        }

        sqlCommand.close();
        return filteredAppts;

    }

    /**
     * This is the update appointment.
     * This updates the appointment
     * @param inputApptID -  appointment id.
     * @param inputTitle title.
     * @param inputDescription description.
     * @param inputLocation location.
     * @param inputType type.
     * @param inputStart  Start.
     * @param inputEnd end.
     * @param inputLastUpdateBy last update by.
     * @param inputCustomerID customer id.
     * @param inputUserID user id.
     * @param inputContactID contact id.
     * @return returns a boolean.
     * @throws SQLException this is the error that can come from SQL error.
     */
    public static Boolean updateAppointment(Integer inputApptID, String inputTitle, String inputDescription,
                                            String inputLocation, String inputType, ZonedDateTime inputStart,
                                            ZonedDateTime inputEnd, String inputLastUpdateBy, Integer inputCustomerID,
                                            Integer inputUserID, Integer inputContactID) throws SQLException {
        Connection connection = JDBC.connection;
        PreparedStatement sqlCommand = connection.prepareStatement("UPDATE appointments "
                + "SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Last_Update=?,Last_Updated_By=?, " +
                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?");

        // Format inputStart and inputEnd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStartString = inputStart.format(formatter).toString();
        String inputEndString = inputEnd.format(formatter).toString();

        sqlCommand.setString(1,inputTitle);
        sqlCommand.setString(2, inputDescription);
        sqlCommand.setString(3, inputLocation);
        sqlCommand.setString(4, inputType);
        sqlCommand.setString(5, inputStartString);
        sqlCommand.setString(6, inputEndString);
        sqlCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        sqlCommand.setString(8, inputLastUpdateBy);
        sqlCommand.setInt(9, inputCustomerID);
        sqlCommand.setInt(10, inputUserID);
        sqlCommand.setInt(11, inputContactID);
        sqlCommand.setInt(12, inputApptID);

        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            sqlCommand.close();
            return false;
        }

    }

    /**
     * This is the add Appointment Method.
     * This adds the appointment to the database.
     * @param inputTitle title.
     * @param inputDescription description.
     * @param inputLocation location.
     * @param inputType type.
     * @param inputStart start.
     * @param inputEnd end.
     * @param inputCreatedBy createdBy.
     * @param inputLastUpdateBy last updated by.
     * @param inputCustomerID customer ID.
     * @param inputUserID user id.
     * @param inputContactID contact id.
     * @return
     * @throws SQLException
     */
    public static Boolean addAppointment(String inputTitle, String inputDescription,
                                         String inputLocation, String inputType, ZonedDateTime inputStart,
                                         ZonedDateTime inputEnd, String inputCreatedBy,
                                         String inputLastUpdateBy, Integer inputCustomerID,
                                         Integer inputUserID, Integer inputContactID) throws SQLException {

        Connection connection = JDBC.connection;
        PreparedStatement sqlCommand = connection.prepareStatement("INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Create_date, \n" +
                "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Format inputStart and inputEnd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String inputStartString = inputStart.format(formatter).toString();
        String inputEndString = inputEnd.format(formatter).toString();

        // Set Params
        sqlCommand.setString(1, inputTitle);
        sqlCommand.setString(2, inputDescription);
        sqlCommand.setString(3, inputLocation);
        sqlCommand.setString(4, inputType);
        sqlCommand.setString(5, inputStartString);
        sqlCommand.setString(6, inputEndString);
        sqlCommand.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        sqlCommand.setString(8, inputCreatedBy);
        sqlCommand.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(formatter).toString());
        sqlCommand.setString(10, inputLastUpdateBy);
        sqlCommand.setInt(11, inputCustomerID);
        sqlCommand.setInt(12, inputUserID);
        sqlCommand.setInt(13, inputContactID);

        // Execute query
        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            return false;
        }

    }


    /**
     * This is the delete appointment method.
     * This method deletes the selected appointment.
     * @param inputApptID - This is the appointment ID.
     * @return returns a boolean
     * @throws SQLException Ths is the SQL exception that can occur if there is an issue.
     */
    public static Boolean deleteAppointment(Integer inputApptID) throws SQLException {

        Connection connection = JDBC.connection;
        PreparedStatement sqlCommand = connection.prepareStatement("DELETE FROM appointments " +
                "WHERE Appointment_ID = ?");

        sqlCommand.setInt(1, inputApptID);

        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            return false;
        }

    }

    /**
     * This is the delete customers appointments.
     * This deletes the appointment based off customer id.
     * @param customerID - customer id.
     * @return return boolean.
     * @throws SQLException This  is the sql exception based off a potential error.
     */
    public static Boolean deleteCustomersAppointments(Integer customerID) throws SQLException {

        Connection connection = JDBC.connection;
        PreparedStatement sqlCommand = connection.prepareStatement("DELETE FROM appointments " +
                "WHERE Customer_ID = ?");

        sqlCommand.setInt(1, customerID);

        try {
            sqlCommand.executeUpdate();
            sqlCommand.close();
            return true;
        }
        catch (SQLException e) {
            //TODO- log error
            e.printStackTrace();
            return false;
        }

    }

    /**
     * This is the get all appointments method.
     * This method gets all appointments.
     *
     * @return this returns an observable list of appointments
     * @throws SQLException this is the error that can occur based off of wrong sql.
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        // Prepare SQL and execute query
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        Connection connection = JDBC.connection;

        PreparedStatement sqlCommand = connection.prepareStatement("SELECT * FROM appointments as a\n" +
                "LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID;");
        ResultSet results = sqlCommand.executeQuery();


        while( results.next() ) {
            // get data from the returned rows
            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp  lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");

            // populate into an appt object
            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );

            // Add to the observablelist
            allAppointments.add(newAppt);

        }
        sqlCommand.close();
        return allAppointments;

    }

    /**
     * This is the get appointment in 15 minutes method.
     * This method gets all the appointments in 15 minutes.
     *
     * @return the return is an observable list of appointment.
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentsIn15Mins() throws SQLException{

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // prepare times and convert to UTC
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime userTZnow = now.atZone(LogIn.getUserTimeZone());
        ZonedDateTime nowUTC = userTZnow.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime utcPlus15 = nowUTC.plusMinutes(15);

        // create input strings
        String rangeStart = nowUTC.format(formatter).toString();
        String rangeEnd = utcPlus15.format(formatter).toString();
        Integer logonUserID = LogIn.getLoggedOnUser().getUserId();

        Connection connection = JDBC.connection;
        PreparedStatement sqlCommand = connection.prepareStatement("SELECT * FROM appointments as a " +
                "LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE " +
                "Start BETWEEN ? AND ? AND User_ID = ? ");

        sqlCommand.setString(1, rangeStart);
        sqlCommand.setString(2, rangeEnd);
        sqlCommand.setInt(3, logonUserID);

        ResultSet results = sqlCommand.executeQuery();

        while( results.next() ) {
            // get data from the returned rows
            Integer appointmentID = results.getInt("Appointment_ID");
            String title = results.getString("Title");
            String description = results.getString("Description");
            String location = results.getString("Location");
            String type = results.getString("Type");
            Timestamp startDateTime = results.getTimestamp("Start");
            Timestamp endDateTime = results.getTimestamp("End");
            Timestamp createdDate = results.getTimestamp("Create_Date");
            String createdBy = results.getString("Created_by");
            Timestamp lastUpdateDateTime = results.getTimestamp("Last_Update");
            String lastUpdatedBy = results.getString("Last_Updated_By");
            Integer customerID = results.getInt("Customer_ID");
            Integer userID = results.getInt("User_ID");
            Integer contactID = results.getInt("Contact_ID");
            String contactName = results.getString("Contact_Name");

            Appointment newAppt = new Appointment(
                    appointmentID, title, description, location, type, startDateTime, endDateTime, createdDate,
                    createdBy, lastUpdateDateTime, lastUpdatedBy, customerID, userID, contactID, contactName
            );

            // Add to the observablelist
            allAppointments.add(newAppt);

        }
        return allAppointments;

    }










}
