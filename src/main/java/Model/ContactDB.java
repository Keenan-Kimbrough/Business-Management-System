package Model;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * This is the  Contact DB Class.
 * This is the methods for manipulating the Contact DataBase Connections.
 */
public class ContactDB {


    /**
     * This the get Minute Scheduled Method.
     * This method gets  time  based off schedule.
     * @param contactId - This is the contact ID.
     * @return This returns and integer.
     * @throws SQLException This the is SQL exception that can potentially occur.
     */
  public static  Integer getMinuteScheduled(String contactId) throws SQLException {
      Integer totalMinutes = 0;
      Connection connection = JDBC.connection;
      String sql = "SELECT * FROM appointments WHERE Contact_ID = ?";
      PreparedStatement ps = connection.prepareStatement(sql);

      ps.setString(1, contactId);

      ResultSet results = ps.executeQuery();

      while(results.next()){
          LocalDateTime startDateTime = results.getTimestamp("Start").toLocalDateTime();
          LocalDateTime endDateTime = results.getTimestamp("End").toLocalDateTime();
          totalMinutes += (int) Duration.between(startDateTime, endDateTime).toMinutes();
      }

      ps.close();
      return totalMinutes;
  }


    /**
     * This is the get Contact Appointment Method.
     * This method gets the contact appointments.
     * @param contactId - This is the required contact ID.
     * @return This is an observable string.
     * @throws SQLException This is the SQL exception that occurs when error may happen.
     */
  public static ObservableList<String> getContactAppointments(String contactId) throws SQLException {
      ObservableList<String> appointmentString = FXCollections.observableArrayList();
      Connection connection = JDBC.connection;
      String sql = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ?";
      PreparedStatement ps  = connection.prepareStatement(sql);
      ps.setString(1, contactId);
      ResultSet results = ps.executeQuery();

      while(results.next()) {
          String appointmentId = results.getString("Appointment_ID");
          String title = results.getString("Title");
          String type = results.getString("Type");
          String start = results.getString("Start");
          String end = results.getString("End");
          String customerID = results.getString("Customer_ID");

          String newLine = "  AppointmentID: " + appointmentId + "\n";
          newLine += "        Title: " + title + "\n";
          newLine += "        Type: " + type + "\n";
          newLine += "        Start date/time: " + start + " UTC\n";
          newLine += "        End date/time: " + end + " UTC\n";
          newLine += "        CustomerID: " + customerID + "\n";

          appointmentString.add(newLine);
      }
      ps.close();
      return appointmentString;

  }

    /**
     * This is the get all contact name method.
     * This method gets all the contact names.
     * @return The return type is an observable list of strings.
     * @throws SQLException This exception is based off an SQL error that might happen.
     */
  public static ObservableList <String> getAllContactName() throws SQLException {
      ObservableList <String> allContactName = FXCollections.observableArrayList();
      String sql = "SELECT DISTINCT Contact_Name FROM contacts";
      PreparedStatement ps = JDBC.connection.prepareStatement(sql);

      ResultSet results = ps.executeQuery();
      while(results.next()){
          allContactName.add(results.getString("Contact_Name"));
      }
      ps.close();
      return allContactName;
  }

    /**
     * This is the find contact ID method.
     * @param contactName - This The contact name parameter.
     * @return This returns an Integer.
     * @throws SQLException This SQL Exception that may occur.
     */
  public static Integer findContactId(String contactName) throws SQLException {
      Integer contactId = -1;
      String sql = "SELECT DISTINCT Contact_ID FROM contacts WHERE Contact_Name = ?";
      Connection connection = JDBC.connection;
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, contactName);
      ResultSet results = ps.executeQuery();

      while(results.next()){
          contactId = results.getInt("Contact_ID");

      }
      ps.close();
      return contactId;

  }

}
