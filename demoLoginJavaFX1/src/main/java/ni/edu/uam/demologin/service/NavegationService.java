package ni.edu.uam.demologin.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.edu.uam.demologin.Main;

import java.io.IOException;

public class NavegationService {

    public void cambiarVista(String fxml,Node node) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        Parent root = loader.load();
        Scene scene = node.getScene();
        scene.setRoot(root);

        Stage stage = (Stage) scene.getWindow();
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}
