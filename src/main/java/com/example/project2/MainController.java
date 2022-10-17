package com.example.project2;

import Model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
interface DisplayMod {
    void displayError();


}

public class MainController implements Initializable {


    public Button AppointmentUpdateButton;
    public Button appointmentAddButton;
    public Button AppointmentDeleteButton;
    public Button customerDeleteButton;
    public Button customerUpdateButton;
    public Button customerAddButton;
    public Button appointmentTypesReportButton;
    public Button consultantScheduleReportButton;
    public Button viewAppointmentButton;
    public Button logOffButton;
    public TableColumn phoneNumberColumn;
    public TableColumn postalCodeColumn;
    public TableColumn addressColumn;
    public TableColumn divisionColumn;
    public TableColumn countryColumn;
    public TableColumn customerNameColumn;
    public TableColumn customerIdColumn;

    public TableColumn endDateTimeColumn;
    public TableColumn startDateTimeColumn;
    public TableColumn typeColumn;
    public TableColumn contactColumn;
    public TableColumn locationColumn;
    public TableColumn descriptionColumn;
    public TableColumn titleColumn;
    public TableColumn appointmentIdColumn;
    public TableView appointmentTable;
    public TableView customerTable;
    public TableColumn appCustomerIdColumn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment,String>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Timestamp>("endDateTime"));
        appCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        ObservableList<Appointment> allAppointments = null;

        try {
            allAppointments = AppointmentDB.getAllAppointments();

        } catch (SQLException e) {
            System.out.println("The init exception:" + e);
            throw new RuntimeException(e);
        }
        appointmentTable.setItems(allAppointments);

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
        ObservableList<Customer> allCustomers = null;
        try {
            allCustomers = CustomerDB.getAllCustomers();
            customerTable.setItems(CustomerDB.getAllCustomers());
        } catch (SQLException e) {
            System.out.println("This is the customer Init exception" + e);
        }
        customerTable.setItems(allCustomers);
    }
    public void changeScreens(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(switchPath)));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

        public void onViewAppointmentButtonPress(ActionEvent event ) throws IOException {
            changeScreens(event, "AppointmentView.fxml");
        }
        public void onViewCustomersButtonPress(ActionEvent event) throws IOException {
            changeScreens(event, "CustomerView.fxml");
        }
        public void onReportsButtonPress(ActionEvent event) throws IOException {
            changeScreens(event, "ReportsPage.fxml");
        }

        public void onLogOffButtonPress(){
            LogIn.logOff();
            System.exit(0);
        }

        public void onAppointmentAddButtonPress(ActionEvent event) throws IOException {
            changeScreens(event, "AddAppointmentPage.fxml");
        }
        public void onAppointmentUpdateButtonPress(ActionEvent event) throws IOException, SQLException {
            Appointment selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

            DisplayMod obj;
            if (selectedAppointment == null) {

                obj = () -> {

                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No selected Appointments, can not open window");
                    alert.showAndWait();

                };
                obj.displayError();

            }
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
        public void onAppointmentDeleteButtonPress() throws SQLException {


                Appointment selectedAppt = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

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
                            appointmentTable.setItems(AppointmentDB.getAllAppointments());
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

        public void onCustomerAddButtonPress(ActionEvent event) throws IOException {
            changeScreens(event, "AddCustomer.fxml");
        }
        public void onCustomerUpdateButtonPress(ActionEvent event) throws IOException, SQLException {
            Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();
            DisplayMod obj;
            if (selectedCustomer == null) {

                obj = () -> {

                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No selected Appointments, can not open window");
                    alert.showAndWait();

                };
                obj.displayError();
            } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ModifyCustomer.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            // get the controller and load our selected appointment into it
            ModifyCustomerController controller = loader.getController();
            controller.initData(selectedCustomer);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);}
        }
        public void onCustomerDeleteButtonPress() throws SQLException {

            Customer selectedCustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();

            // check that user selected an appointment in the table
            if (selectedCustomer == null) {
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, "No selected Customer", clickOkay);
                alert.showAndWait();
                return;
            }
            else {
                // show alert and ensure user wants to delete
                ButtonType clickYes = ButtonType.YES;
                ButtonType clickNo = ButtonType.NO;
                Alert deleteAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete Customer: "
                        + selectedCustomer.getId() + " and all related appointments?", clickYes, clickNo);
                Optional<ButtonType> result = deleteAlert.showAndWait();

                // if user confirms, delete appointment + related appointments due to FK contraints
                if (result.get() == ButtonType.YES) {
                    Boolean customerApptSuccess = AppointmentDB.deleteCustomersAppointments(selectedCustomer.getId());

                    Boolean customerSuccess = CustomerDB.deleteCustomer(selectedCustomer.getId());


                    // if successful notify, if not show user error.
                    if (customerSuccess && customerApptSuccess) {
                        ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                        Alert deletedCustomer = new Alert(Alert.AlertType.CONFIRMATION,
                                "Customer + related appointments deleted", clickOkay);
                        deletedCustomer.showAndWait();

                    }
                    else {

                        ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                        Alert deleteAppt = new Alert(Alert.AlertType.WARNING,
                                "Failed to delete Customer or related appointments ", clickOkay);
                        deleteAppt.showAndWait();

                    }

                    // Re-load appointments on screen
                    try {
                        customerTable.setItems(CustomerDB.getAllCustomers());
                    }
                    catch (SQLException error){
                        error.printStackTrace();
                    }

                }
                else {
                    return;
                }
            }
        }




    }






