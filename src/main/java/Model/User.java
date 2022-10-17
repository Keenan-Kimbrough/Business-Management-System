package Model;

/**
 * This is the User Class.
 * This User provides the methods for the User Class as well instantiates.
 */
public class User {
    private String userName;
    private Integer userID;

    /**
     * This is the User Constructor.
     * This method is used when user is called.
     * @param inputUserName - Input name if user.
     * @param inputUserId - Input user id of user.
     */
    public User(String inputUserName, Integer inputUserId){
        userName = inputUserName;
        userID = inputUserId;

    }

    /**
     * This is the get userName method.
     * This method returns a string which is  username.
     * @return - This method returns a string, which is the username.
     */
    public String getUserName(){
        return userName;
    }

    /**
     * This is the get User ID Method.
     * This method gets the user ID.
     * @return - This returns the user ID, which is a integer.
     */
    public Integer getUserId(){
        return userID;
    }
}
