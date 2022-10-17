package Model;


import java.sql.Timestamp;

/**
 * This is the Appointment class.
 * This handles all the functionality of the  appointment class.
 */
public class Appointment {
    private Integer appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private Timestamp createDate;
    private String createBy;
    private Timestamp lastUpdateDateTime;
    private String lastUpdateBy;
    private Integer customerId;
    private Integer userId;
    private Integer contactId;
    private String contactName;

    /**
     * This is the appointment constructor.
     * This constructor is called when appoointment is created.
     * @param inputAppointmentId appointment id.
     * @param inputTitle title.
     * @param inputDescription description.
     * @param inputLocation location.
     * @param inputType type.
     * @param inputStartDateTime start date time.
     * @param inputEndDateTime end date time.
     * @param inputCreateDate created date.
     * @param inputCreateBy created by.
     * @param inputLastUpdateDateTime update date time.
     * @param inputLastUpdateBy updated by.
     * @param inputCustomerId customer id.
     * @param inputUserId user id.
     * @param inputContactId contact id.
     * @param inputContactName contact name.
     */
    public Appointment(Integer inputAppointmentId,String inputTitle, String inputDescription, String inputLocation, String inputType, Timestamp inputStartDateTime,
    Timestamp inputEndDateTime,Timestamp inputCreateDate, String inputCreateBy, Timestamp inputLastUpdateDateTime, String inputLastUpdateBy, Integer inputCustomerId, Integer inputUserId, Integer inputContactId, String inputContactName ){

        appointmentId = inputAppointmentId;
        title = inputTitle;
        description =  inputDescription;
        location = inputLocation;
        type = inputType;
        startDateTime = inputStartDateTime;
        endDateTime = inputEndDateTime;
        createDate = inputCreateDate;
        createBy = inputCreateBy;
        lastUpdateDateTime = inputLastUpdateDateTime;
        lastUpdateBy = inputLastUpdateBy;
        customerId = inputCustomerId;
        userId = inputUserId;
        contactId = inputContactId;
        contactName = inputContactName;
    }

    /**
     * This is the get appointments ID.
     * This returns the appointment id
     * @return this returns and Integer.
     */
    public Integer getAppointmentId() {
        return appointmentId;
    }

    /**
     * This is the get title method.
     * This returns the title.
     * @return this returns a string.
     */
    public String getTitle(){
        return title;
    }

    /**
     * This is the get description method.
     * This returns an description.
     * @return this returns a string.
     */
    public String getDescription(){
        return description;
    }

    /**
     * This is the get location method.
     * This returns the location
     * @return This returns string.
     */
    public String getLocation() {
        return location;
    }

    /**
     * This is get type method.
     * This method gets the type.
     * @return - returns a string.
     */
    public String getType(){
        return type;
    }

    /**
     * This get start date time method.
     * This method returns start date time.
     * @return - return Time Stamp.
     */
    public Timestamp getStartDateTime(){
        return startDateTime;
    }

    /**
     * This is the get end date time method.
     * This method gets the end date time
     * @return return time stamp.
     */
    public Timestamp getEndDateTime(){
        return endDateTime;
    }

    /**
     * This is the get Created date method.
     * This method gets the created date.
     * @return - returns a timestamp.
     */
    public Timestamp getCreateDate(){
        return createDate;
    }

    /**
     * This is the get created by method.
     * This method get who created teh appointment.
     * @return returns a string.
     */
    public String getCreateBy(){
        return createBy;
    }

    /**
     * This is the get last update date time.
     * This methods get last update date time.
     * @return returns  a timestamp.
     */
    public Timestamp getLastUpdateDateTime(){
        return lastUpdateDateTime;
    }

    /**
     * This is the get last update by method.
     * This returns the last update by.
     * @return - string.
     */
    public String getLastUpdateBy(){
        return lastUpdateBy;
    }

    /**
     * This is the get customer id method.
     * This gets the customer id.
     * @return returns and Integer.
     */
    public Integer getCustomerId(){
        return customerId;
    }

    /**
     * This is the get uer ID method.
     * This method gets user id.
     * @return Return an integer.
     */
    public Integer getUserId(){
        return userId;
    }

    /**
     * this is get Contact ID method.
     * This gets the contact id.
     * @return - Returns an Integer.
     */
    public Integer getContactId(){
        return contactId;
    }

    /**
     * This is get contact name method.
     * This method gets contact name.
     * @return - returns string.
     */
    public String getContactName(){
        return contactName;
    }








}
