module com.example.project2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;



    opens com.example.project2 to javafx.fxml, javafx.base;

    exports Model;

    exports com.example.project2;
    opens Model to javafx.fxml;
}