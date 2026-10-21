package ni.edu.uam.demologin.controller;

import javafx.scene.control.Alert;

public class UtilController {
    public static void mostrarMensaje(Alert.AlertType type, String title, String mensaje) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void mostrarException(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception");
        alert.setHeaderText(null);
        alert.setContentText(e.getCause().getMessage());
        alert.showAndWait();
    }
}
