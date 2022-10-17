package com.example.project2;
import java.io.IOException;
import Model.Appointment;
import Model.LogIn;
import Model.AppointmentDB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;


interface DisplayLog {
    void DisplayConfirmation();


}

public class LoginPageController implements Initializable {
    public PasswordField passwordTextField;
    public Label titleLabel;
    public TextField userNameTextField;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    public Button logInButton;
    public Button clearButton;
    public Button exitButton;
    @FXML
    private Label zoneLabel;
    private Label logonFailedButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            Locale userLocale = Locale.getDefault();
            System.out.println(userLocale);
            zoneLabel.setText(ZoneId.systemDefault().toString());
            resources = ResourceBundle.getBundle("lang", userLocale);
            titleLabel.setText(resources.getString("titleLabel"));
            userNameLabel.setText(resources.getString("userNameLabel"));
            passwordLabel.setText(resources.getString("passwordLabel"));
            logInButton.setText(resources.getString("loginButton"));
            clearButton.setText(resources.getString("clearButton"));
            exitButton.setText(resources.getString("exitButton"));

        }
        catch(MissingResourceException e){
         e.printStackTrace();

        }

    }
    public void changeScreens(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(switchPath)));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    public void onLogInButtonPress(ActionEvent event) throws SQLException, IOException {
        String userName = userNameTextField.getText();
        String password = passwordTextField.getText();

        // attempt Login

        Boolean logIn = LogIn.logInAttempt(userName, password);
        DAO.Logger.auditLogin(userName, logIn);
    try {
        if (logIn) {

            //Get Appointment in 15 minutes and display notification if there is.

            ObservableList<Appointment> upcomingAppointments = AppointmentDB.getAppointmentsIn15Mins();

            if (!upcomingAppointments.isEmpty()) {
                for (Appointment upcoming : upcomingAppointments) {

                    String message = "upcoming appointmentID:" + upcoming.getAppointmentId() + "Start: "
                            + upcoming.getStartDateTime().toString();
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, message, clickOkay);
                    invalidInput.showAndWait();
                }

            }

            // If no appointments in 15 Minute, displays notification that no upcoming appointments.
            else {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.CONFIRMATION, " No upcoming Appointments", clickOkay);
                    invalidInput.showAndWait();
                //Interface

                DisplayLog obj;

                obj = () -> {
                    ButtonType clicknew = new ButtonType("Continue", ButtonBar.ButtonData.OK_DONE);
                    Alert newLogIn = new Alert(Alert.AlertType.CONFIRMATION, " Congratulations  You Have been logged on", clicknew);

                    newLogIn.showAndWait();
                };
                obj.DisplayConfirmation();

            }

                // Lambda Function



            changeScreens(event, "MainView.fxml");

        } else {
            Locale userLocale = Locale.getDefault();
            ResourceBundle resources = ResourceBundle.getBundle("lang", userLocale);
            ButtonType clickOkay = new ButtonType(resources.getString("okayButton"), ButtonBar.ButtonData.OK_DONE);
            Alert failedLogon = new Alert(Alert.AlertType.WARNING, resources.getString("LogonFailedButton"), clickOkay);
            failedLogon.showAndWait();
        }
    }
    catch(MissingResourceException e){
        e.printStackTrace();
    }
    }

    public void onClearButtonPress(ActionEvent event){
    userNameTextField.clear();
    passwordTextField.clear();
    }
    public void onExitButtonPress(){
        LogIn.logOff();
        System.exit(0);
    }


}