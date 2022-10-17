package Model;

import DAO.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This is the User DB Class.
 * This Class that lays out the User Database methods.
 */
public class UserDB {

    /**
     * This is the get all user's ID method.
     * This method returns all the users in the user database in an Observable List.
     * @return - The return is the Integer based ObservableList.
     * @throws SQLException This is the SQL exception that can happen with this method.
     */
    public static ObservableList<Integer> getAllUserId() throws SQLException {
        ObservableList<Integer> allUserId = FXCollections.observableArrayList();
        Connection connection = JDBC.connection;
        String sql = "SELECT DISTINCT User_ID FROM users";
        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet results = ps.executeQuery();

        while ( results.next()){
            allUserId.add(results.getInt("User_ID"));

        }
        ps.close();
        return allUserId;

    }


}
