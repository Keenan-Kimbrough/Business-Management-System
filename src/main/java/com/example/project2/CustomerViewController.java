package com.example.project2;

import Model.AppointmentDB;
import Model.Customer;
import Model.CustomerDB;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {
    @FXML
    Button addButton;
    @FXML
    Button editButton;
    @FXML
    Button deleteButton;
    @FXML
    Button backButton;
    @FXML
    TableView<Customer> customerTable;
    @FXML
    TableColumn<Customer, Integer> customerIDColumn;
    @FXML
    TableColumn<Customer, String> customerNameColumn;
    @FXML
    TableColumn<Customer, String> countryColumn;
    @FXML
    TableColumn<Customer, String> divisionColumn;
    @FXML
    TableColumn<Customer, String> addressColumn;
    @FXML
    TableColumn<Customer, String> postalCodeColumn;
    @FXML
    TableColumn<Customer, String> phoneNumberColumn;


    public void changeScreens(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void populateCustomerTable(ObservableList<Customer> inputList) {
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("Id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));

        customerTable.setItems(inputList);

    }


    public void onAddButtonPress(ActionEvent event) throws IOException {
        changeScreens(event, "AddCustomer.fxml");

    }


    public void onEditButtonPress(ActionEvent event) throws IOException, SQLException {

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        // throw error if no selection
        if (selectedCustomer == null) {
            DisplayMod obj;
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
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
        }
    }

    public void onDeleteButtonPress(ActionEvent event) throws IOException, SQLException {

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

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
                    populateCustomerTable(CustomerDB.getAllCustomers());
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


    public void onBackButtonPress(ActionEvent event) throws IOException {

        changeScreens(event, "MainView.fxml");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            populateCustomerTable(CustomerDB.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}