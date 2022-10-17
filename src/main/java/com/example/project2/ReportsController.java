package com.example.project2;

import Model.AppointmentDB;
import Model.ContactDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/***
 * This is the Reports Controllers.
 * This controller handles the functionality for the Report View.
 */
public class ReportsController implements Initializable {


    public Button clearButton;
    @FXML
    Button AppointmentsByReportButton;
    @FXML
    Button contactScheduleReportButton;
    @FXML
    Button minutesPerContactButton;
    @FXML
    TextArea reportTextField;
    @FXML
    Button closeButton;

    /***
     * This is the Change Screen Method.
     * This method changes to the desired screen.
     * @param event - This is the event that is triggered the method.
     * @param switchPath - This is the controllers Path that is needed.
     * @throws IOException - This is the exception that can happen when the method is ran.
     */
    public void changeScreen(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    /**
     * This is the Close window Button.
     * This method changes the screen to the MainView.
     * @param event - This is the event that is used in the method.
     * @throws IOException - This is the exception that the method causes.
     */
    public void onCloseButtonPress(ActionEvent event) throws IOException {
        changeScreen(event, "MainView.fxml");
    }

    /**
     * This is the show Appoints by Report Button.
     * This button shows the reports once the button is pressed.
     * @param event This is the event that is triggered in the menthod.
     * @throws SQLException This is the SQL exception that could happen when method is called.
     */
    public void onAppointmentsByReportButtonPress(ActionEvent event) throws SQLException {
        ObservableList<String> reportStrings = AppointmentDB.reportTotalsByTypeAndMonth();
        for( String str : reportStrings){
            reportTextField.appendText(str);
        }
    }

    /**
     * This is the show minute per contact button.
     * This method shows the minutes per contact once the button is pressed.
     * @param event - This handles the action that triggers the method.
     * @throws SQLException - This is the SQL exception that could happen when the method is called.
     */
    public void onMinutePerContactButtonPress(ActionEvent event) throws SQLException {
        ObservableList<String> contacts = ContactDB.getAllContactName();

        for( String contact: contacts){
            String contactId = ContactDB.findContactId(contact).toString();
            reportTextField.appendText("Contact Name" + contact + "ID" + contactId + "\n");
            reportTextField.appendText(" Total minutes scheduled: " + ContactDB.getMinuteScheduled(contactId)+ "\n");
        }
    }

    /**
     * This is the show Contact Schedule Button.
     * This method shows the contact Schedule once teh button is pressed.
     * @throws SQLException - This is the SQL Exception that could happen when the method is called.
     */
    public void onContactScheduleButtonPress() throws SQLException {
        ObservableList<String> contacts = ContactDB.getAllContactName();

        for(String contact : contacts) {
            String contactId = ContactDB.findContactId(contact).toString();
            reportTextField.appendText("Contact Name:" + contact + "ID" + contactId + "\n");

            ObservableList<String> appts = ContactDB.getContactAppointments(contactId);
            if(appts.isEmpty()) {
                reportTextField.appendText(" No appointment for contacts \n");
            }

            for(String appt: appts) {
                reportTextField.appendText(appt);
            }
        }
    }

    /**
     * This is the clear field Button.
     * This method clears the reports text field area.
     * @param event - This the action event that triggers the method.
     */
    public void onClearButtonPress(ActionEvent event) {

        reportTextField.clear();
    }


    /**
     * This is the initialize method.
     * This method initializes the Controller.
     * @param location - This is the location.
     * @param resources This is the Resources.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){


}

}
