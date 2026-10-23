
package com.example.ejercicio3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {

    private HelloController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root, 550, 520);

        stage.setTitle("Caja de Ventas Concurrente");
        stage.setScene(scene);

        stage.addEventHandler(
                WindowEvent.WINDOW_CLOSE_REQUEST,
                event -> {
                    if (controller != null) {
                        event.consume();
                        controller.cerrarAplicacion();
                    }
                }
        );

        stage.show();
    }

    @Override
    public void stop() {
        // El controlador gestiona el cierre de los vendedores.
    }
}
