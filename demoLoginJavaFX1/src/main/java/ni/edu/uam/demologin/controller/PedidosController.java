package ni.edu.uam.demologin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import ni.edu.uam.demologin.service.PedidosService;

public class PedidosController {

    @FXML private Button btnIniciar;
    @FXML private Button btnCancelar;
    @FXML private ProgressBar barraProgreso;
    @FXML private Label lblContador;
    @FXML private Label lblEstado;

    private final PedidosService servicio =
            new PedidosService();

    private boolean cerrando = false;

    @FXML
    public void initialize() {

        lblEstado.setText("Listo");
        lblContador.setText("0 / 30");
        barraProgreso.setProgress(0);

        btnIniciar.setDisable(false);
        btnCancelar.setDisable(true);
    }

    @FXML
    private void iniciarPedidos() {

        if (servicio.estaActivo() || cerrando) {
            return;
        }

        boolean iniciado = servicio.iniciar(
                (cantidad, estado) -> {

                    if (cerrando) {
                        return;
                    }

                    lblContador.setText(
                            cantidad + " / 30"
                    );

                    barraProgreso.setProgress(
                            cantidad / 30.0
                    );

                    lblEstado.setText(estado);

                    if (!estado.equals("En ejecución")) {

                        btnIniciar.setDisable(false);
                        btnCancelar.setDisable(true);
                    }
                }
        );

        if (iniciado) {

            btnIniciar.setDisable(true);
            btnCancelar.setDisable(false);

            lblEstado.setText("En ejecución");
            lblContador.setText("0 / 30");
            barraProgreso.setProgress(0);

        } else {

            lblEstado.setText("Error");
            btnIniciar.setDisable(false);
            btnCancelar.setDisable(true);
        }
    }

    @FXML
    private void cancelarPedidos() {

        if (servicio.estaActivo()) {

            lblEstado.setText("Cancelando");

            btnIniciar.setDisable(true);
            btnCancelar.setDisable(true);

            servicio.cancelar();
        }
    }

    public void configurarCierre(Stage stage) {

        stage.setOnCloseRequest(event -> {

            cerrando = true;
            servicio.cerrar();
        });
    }
}
