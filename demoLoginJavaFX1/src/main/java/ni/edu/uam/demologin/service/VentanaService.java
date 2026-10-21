package ni.edu.uam.demologin.service;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Window;

public class VentanaService {
    public void cerrarVentana(ActionEvent actionEvent) {
        Node node = (Node)actionEvent.getSource();
        Window window = (Window)node.getScene().getWindow();
        window.hide();
    }
}
