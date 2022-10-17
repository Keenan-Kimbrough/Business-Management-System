package com.example.project2;

import Model.Customer;
import Model.CustomerDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {

    public Button closeButton;
    public TextField phoneNumberTextBox;
    public TextField customerNameTextBox;
    public ComboBox userComboBox;
    public TextField titleTextBox;
    public TextField descriptionTextBox;
    public TextField locationTextBox;
    public TextField typeTextField;
    public DatePicker apptDatePicker;
    public TextField startTimeTextBox;
    public TextField endTimeTextBox;
    public ComboBox contactComboBox;
    public ComboBox customerComboBox;
    public Label timeZoneLabel;
    @FXML
    TextField customerIDTextBox;
    @FXML
    ComboBox<String> countryComboBox;
    @FXML
    ComboBox<String> divisionComboBox;
    @FXML
    TextField nameTextBox;
    @FXML
    TextField addressTextBox;
    @FXML
    TextField postalCodeTextBox;
    @FXML
    TextField phoneTextBox;
    @FXML
    Button saveButton;
    @FXML
    Button clearButton;



    public void initData(Customer selectedCustomer) throws SQLException {

        countryComboBox.setItems(CustomerDB.getAllCountries());
        countryComboBox.getSelectionModel().select(selectedCustomer.getCountry());
        divisionComboBox.setItems(CustomerDB.getFilteredDivision(selectedCustomer.getCountry()));
        divisionComboBox.getSelectionModel().select(selectedCustomer.getDivision());

        customerIDTextBox.setText(selectedCustomer.getId().toString());
        nameTextBox.setText(selectedCustomer.getName());
        addressTextBox.setText(selectedCustomer.getAddress());
        postalCodeTextBox.setText(selectedCustomer.getPostalCode().toString());
        phoneTextBox.setText(selectedCustomer.getPhoneNumber().toString());


    }


    public void changeScreens(ActionEvent event, String switchPath) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(switchPath));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void onSaveButtonPress(ActionEvent event) throws IOException, SQLException {
        // INPUT VALIDATION - check for nulls
        String country = countryComboBox.getValue();
        String division = divisionComboBox.getValue();
        String name = nameTextBox.getText();
        String address = addressTextBox.getText();
        String postalCode = postalCodeTextBox.getText();
        String phone = phoneTextBox.getText();
        Integer customerID = Integer.parseInt(customerIDTextBox.getText());

        if (country.isBlank() || division.isBlank() || name.isBlank() || address.isBlank() || postalCode.isBlank() ||
                phone.isBlank()) {

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert emptyVal = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are completed.",
                    clickOkay);
            emptyVal.showAndWait();
            return;

        }

        // CustomerDB updateCustomer
        Boolean success = CustomerDB.updateCustomer(division, name, address, postalCode, phone, customerID);

        if (success) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment updated successfully!", clickOkay);
            alert.showAndWait();
            changeScreens(event, "MainView.fxml");
        }
        else {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "failed to Update appointment", clickOkay);
            invalidInput.showAndWait();
        }
    }


    public void onClearButtonPress(ActionEvent event) {
        countryComboBox.getSelectionModel().clearSelection();
        divisionComboBox.getSelectionModel().clearSelection();
        nameTextBox.clear();
        addressTextBox.clear();
        postalCodeTextBox.clear();
        phoneTextBox.clear();
    }







    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // Listener for combo box change
        countryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                divisionComboBox.getItems().clear();
                divisionComboBox.setDisable(true);

            }
            else {
                divisionComboBox.setDisable(false);
                try {
                    divisionComboBox.setItems(CustomerDB.getFilteredDivision(countryComboBox.getValue()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

    }

    public void onCloseButtonPress(ActionEvent event) throws IOException {
        changeScreens(event, "MainView.fxml");
    }



}