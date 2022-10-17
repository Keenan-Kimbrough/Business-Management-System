package com.example.project2;

import DAO.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        JDBC.openConnection();
        System.out.print("java version" + System.getProperty("java.version"));
        System.out.print(" java fx" + System.getProperty("javafx.runtime.version"));
    }

    public static void main(String[] args) {
        launch();
    }
}