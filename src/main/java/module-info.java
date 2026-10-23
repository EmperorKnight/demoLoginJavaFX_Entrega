
module com.example.ejercicio3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.ejercicio3;
    opens com.example.ejercicio3 to javafx.fxml;
}
