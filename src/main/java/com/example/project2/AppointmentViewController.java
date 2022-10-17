package com.example.project2;

import DAO.JDBC;
import Model.Appointment;
import Model.AppointmentDB;
import Model.LogIn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;


public class AppointmentViewController implements Initializable {
    public Button consultantReportsButton;
    public Button appointmentReportsButton;
    @FXML
    Button newAppointmentButton;
    @FXML
    Button editAppointmentButton;
    @FXML
    Button deleteButton;
    @FXML
    Button customersViewButton;
   
    @FXML
    Button logOutButton;
    @FXML
    Button nextTimeButton;
    @FXML
    Button previousTimeButton;
    @FXML
    RadioButton monthFilterButton;
    @FXML
    RadioButton weekFilterButton;
    @FXML
    RadioButton noFilterButton;
    @FXML
    TableView<Appointment> appointmentTable;
    @FXML
    TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    TableColumn<Appointment, String> titleColumn;
    @FXML
    TableColumn<Appointment, String> descriptionColumn;
    @FXML
    TableColumn<Appointment, String> locationColumn;
    @FXML
    TableColumn<Appointment, String> contactColumn;
    @FXML
    TableColumn<Appointment, String> typeColumn;
    @FXML
    TableColumn<Appointment, ZonedDateTime> startDateTimeColumn;
    @FXML
    TableColumn<Appointment, ZonedDateTime> endDateTimeColumn;
    @FXML
    TableColumn<Appointment, Integer> customerIdColumn;
    @FXML
    ToggleGroup filterToggle;
    @FXML
    Label selectedTimeLabel;

    // Markers for date filtering.
    ZonedDateTime startRangeMarker;
    ZonedDateTime endRangeMarker;


    public void changeScreens(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void onPressCloseButton(ActionEvent event) throws IOException {
    changeScreens(event, "MainView.fxml");}
    public void initialToggleGroup() {

        filterToggle = new ToggleGroup();

        noFilterButton.setToggleGroup(filterToggle);
        weekFilterButton.setToggleGroup(filterToggle);
        monthFilterButton.setToggleGroup(filterToggle);

    }


    public void onNoFilterButtonPress(ActionEvent event) {
        // only one selection at a time!
        monthFilterButton.setSelected(false);
        weekFilterButton.setSelected(false);

        ObservableList<Appointment> allAppts;
        try {
            allAppts = AppointmentDB.getAllAppointments();
        }
        catch (SQLException error){
            // Sometimes the connection to DB breaks here.(not sure why) If it does, re-connnect and try again.
            error.printStackTrace();
            JDBC.openConnection();
           //removed database connection here

            try {
                allAppts = AppointmentDB.getAllAppointments();
            } catch (SQLException anotherError) {
                anotherError.printStackTrace();
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "DB connection failed. please restart", clickOkay);
                invalidInput.showAndWait();
                return;
            }

        }
        populateAppointmentsTable(allAppts);
        selectedTimeLabel.setText("All Appointments");
        startRangeMarker = null;


    }


    public void onWeekFilterButtonPress(ActionEvent event) throws SQLException {
        // Only one selection at a time!
        monthFilterButton.setSelected(false);
        noFilterButton.setSelected(false);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        startRangeMarker = ZonedDateTime.now(LogIn.getUserTimeZone());
        endRangeMarker = startRangeMarker.plusWeeks(1);

        // Convert to UTC
        ZonedDateTime startRange = startRangeMarker.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endRange = endRangeMarker.withZoneSameInstant(ZoneOffset.UTC);

        // query DB for time frame
        filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);
        // populate
        populateAppointmentsTable(filteredAppts);
        // update label
        selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                LogIn.getUserTimeZone());
        // update filterRangeMarker to next week.




    }


    public void onMonthFilterButtonPress(ActionEvent event) throws SQLException {
        weekFilterButton.setSelected(false);
        noFilterButton.setSelected(false);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();
        startRangeMarker = ZonedDateTime.now(LogIn.getUserTimeZone());
        endRangeMarker = startRangeMarker.plusMonths(1);

        // Convert to UTC
        ZonedDateTime startRange = startRangeMarker.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endRange = endRangeMarker.withZoneSameInstant(ZoneOffset.UTC);

        // query DB for time frame
        filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);
        // populate
        populateAppointmentsTable(filteredAppts);
        // update label
        selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                LogIn.getUserTimeZone());



    }


    public void onNextButtonPress(ActionEvent event) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();

        if (filterToggle.getSelectedToggle() == weekFilterButton) {

            ZonedDateTime startRange = startRangeMarker.plusWeeks(1);
            ZonedDateTime endRange = endRangeMarker.plusWeeks(1);

            // update markers
            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to UTC for the DB
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            populateAppointmentsTable(filteredAppts);

            // update label
            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogIn.getUserTimeZone());

        }
        if (filterToggle.getSelectedToggle() == monthFilterButton) {

            ZonedDateTime startRange = startRangeMarker.plusMonths(1);
            ZonedDateTime endRange = endRangeMarker.plusMonths(1);

            // update markers
            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to UTC for the DB
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            populateAppointmentsTable(filteredAppts);

            // update label
            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogIn.getUserTimeZone());

        }

    }


    public void onPreviousButtonPress(ActionEvent event) throws SQLException {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ObservableList<Appointment> filteredAppts = FXCollections.observableArrayList();

        if (filterToggle.getSelectedToggle() == weekFilterButton) {

            ZonedDateTime startRange = startRangeMarker.minusWeeks(1);
            ZonedDateTime endRange = endRangeMarker.minusWeeks(1);

            // update markers
            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to UTC for the DB
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            appointmentTable.setItems(filteredAppts);

            // update label
            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogIn.getUserTimeZone());

        }
        if (filterToggle.getSelectedToggle() == monthFilterButton) {

            ZonedDateTime startRange = startRangeMarker.minusMonths(1);
            ZonedDateTime endRange = endRangeMarker.minusMonths(1);

            // update markers
            startRangeMarker = startRange;
            endRangeMarker = endRange;

            // convert to UTC for the DB
            startRange = startRange.withZoneSameInstant(ZoneOffset.UTC);
            endRange = endRange.withZoneSameInstant(ZoneOffset.UTC);

            filteredAppts = AppointmentDB.getDateFilteredAppointments(startRange, endRange);

            appointmentTable.setItems(filteredAppts);

            // update label
            selectedTimeLabel.setText(startRangeMarker.format(formatter) + " - " + endRangeMarker.format(formatter) + " " +
                    LogIn.getUserTimeZone());
        }

    }



    public void onDeleteButtonPress(ActionEvent event) throws IOException, SQLException {

        Appointment selectedAppt = appointmentTable.getSelectionModel().getSelectedItem();

        // check that user selected an appointment in the table
        if (selectedAppt == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "No selected Appointment", clickOkay);
            invalidInput.showAndWait();
            return;
        }
        else {
            // show alert and ensure user wants to delete
            ButtonType clickYes = ButtonType.YES;
            ButtonType clickNo = ButtonType.NO;
            Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete Appointment: "
                    + selectedAppt.getAppointmentId() + " ?", clickYes, clickNo);
            Optional<ButtonType> result = deleteAlert.showAndWait();

            // if user confirms, delete appointment
            if (result.get() == ButtonType.YES) {
                Boolean success = AppointmentDB.deleteAppointment(selectedAppt.getAppointmentId());

                // if successful notify, if not show user error.
                if (success) {
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deletedAppt = new Alert(Alert.AlertType.CONFIRMATION, "Appointment deleted", clickOkay);
                    deletedAppt.showAndWait();

                }
                else {
                    //TODO - log error if it occurs
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Failed to delete Appointment", clickOkay);
                    deleteAppt.showAndWait();

                }

                // Re-load appointments on screen
                try {
                    populateAppointmentsTable(AppointmentDB.getAllAppointments());
                }
                catch (SQLException error){
                    //TODO - log error
                    error.printStackTrace();
                }

            }
            else {
                return;
            }
        }
    }


    public void onAddButtonPress(ActionEvent event) throws IOException {
        changeScreens(event, "AddAppointmentPage.fxml");

    }


    public void onLogOutButtonPress(ActionEvent event) throws IOException {
        ButtonType clickYes = ButtonType.YES;
        ButtonType clickNo = ButtonType.NO;
        Alert logOff = new Alert(Alert.AlertType.WARNING, "Are you sure you want to Log Off?", clickYes, clickNo);

        Optional<ButtonType> result = logOff.showAndWait();

        if (result.get() == ButtonType.YES) {
            LogIn.logOff();
            changeScreens(event, "LoginPage.fxml");
        }
        else {
            return;
        }


    }


    public void onModifyButtonPress(ActionEvent event) throws IOException, SQLException {

        Appointment selectedAppointment =  appointmentTable.getSelectionModel().getSelectedItem();

        DisplayMod obj;
        if (selectedAppointment == null) {

            obj = () -> {

                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.ERROR, "No selected Appointments, can not open window");
                alert.showAndWait();

            };
            obj.displayError();}
        else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyAppointment.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            // get the controller and load our selected appointment into it
            ModifyAppointmentController controller = loader.getController();
            controller.onInitData(selectedAppointment);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
        }

    }





    public void onReportsButtonPress(ActionEvent event) throws IOException {
        changeScreens(event, "ReportsPage.fxml");

    }


    public void populateAppointmentsTable(ObservableList<Appointment> inputList) {
        // Takes an observable list of appointments and populates them on screen.

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory("contactName"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory("customerId"));
        appointmentTable.setItems(inputList);

    }


    public void checkCanceled(ObservableList<Appointment> inputList) {

        inputList.forEach((appt) -> {
            if (appt.getType().equalsIgnoreCase("canceled")) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Appointment " + appt.getAppointmentId() +
                        " is canceled.", clickOkay);
                invalidInput.showAndWait();
            }
        });

    }


    @Override
    public void initialize(URL location, ResourceBundle resources)   {





        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory("contactName"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory("endDateTime"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory("customerId"));
        try {
            appointmentTable.setItems(AppointmentDB.getAllAppointments());
        } catch (SQLException e) {
            System.out.println("The init exception:" + e);
            throw new RuntimeException(e);
        }


        noFilterButton.setSelected(true);
        initialToggleGroup();

        System.out.println("Initialize query ran successful");

        try {
            checkCanceled(AppointmentDB.getAllAppointments());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    }

