package Model;

import DAO.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;


/**
 * This is the LogIn Class.
 * This handles the Login Methods.
 */
public class LogIn {

    private static User loggedOnUser;
    private static Locale userLocale;
    private static ZoneId userTimeZone;

    /**
     * This is the LogIn Constructor.
     * This Constructor is called when the class is called.
     */
    public LogIn(){

    }

    /**
     * This is the LogIn Attempt method.
     * This Method executes SQL command that checks the log in paremeters.
     * @param userNameInput - Log in User name.
     * @param userPasswordInput Log in Password.
     * @return - Returns true or false depending on if the username and password is in the database.
     * @throws SQLException - This is the exception that can happen with the method.
     */
    public static boolean logInAttempt(String userNameInput, String userPasswordInput) throws SQLException {
       try {
           JDBC.openConnection();
           Connection connection = JDBC.connection;
           PreparedStatement sql = connection.prepareStatement("SELECT * FROM users WHERE User_Name = ? AND Password = ?");
           sql.setString(1, userNameInput);
           sql.setString(2, userPasswordInput);
           System.out.println("executing log in Query...");
           ResultSet result = sql.executeQuery();
           if (!result.next()) {
               sql.close();

               System.out.println("Query Failed");
               return false;
               //Log In Failed
           } else {
               loggedOnUser = new User(result.getString("User_Name"), result.getInt("User_ID"));
               userLocale = Locale.getDefault();
               userTimeZone = ZoneId.systemDefault();
               sql.close();
               System.out.println("Query Successful");

               return true;
           }
       }
       catch (SQLException e) {
           System.out.println(" This is the error : " + e);
           return false;
       }

    }

    /**
     * This is get user locale method.
     * This gets the user locale.
     * @return This return is type locale.
     */
    public static Locale getUserLocale(){
        return userLocale;
    }

    /**
     * This is the get Logged on User method.
     * This method gets logged on user.
     * @return - This method returns a User.
     */
    public static User getLoggedOnUser(){
        return loggedOnUser;
    }

    /**
     * This is the get user Time Zone Method.
     * This method returns the user Time zone.
     * @return - This method returns a zoneID.
     */
    public static ZoneId getUserTimeZone(){
        return userTimeZone;
    }


    public static void logOff(){
        JDBC.closeConnection();
        loggedOnUser = null;
        userLocale = null;
        userTimeZone = null;

    }




}
