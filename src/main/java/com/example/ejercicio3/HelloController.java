
package com.example.ejercicio3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloController {

    @FXML private Button btnIniciar;
    @FXML private Button btnCancelar;

    @FXML private Label lblEstado;
    @FXML private Label lblVentas;
    @FXML private Label lblRecaudacion;

    @FXML private Label lblVendedor1;
    @FXML private Label lblVendedor2;
    @FXML private Label lblVendedor3;

    @FXML private ProgressBar progreso1;
    @FXML private ProgressBar progreso2;
    @FXML private ProgressBar progreso3;

    private ExecutorService ejecutor;
    private AtomicBoolean cancelar;
    private boolean ejecutando = false;
    private boolean cerrando = false;

    @FXML
    private void initialize() {
        btnCancelar.setDisable(true);
        restablecerInterfaz();
    }

    @FXML
    private void iniciarSimulacion() {
        if (ejecutando || cerrando) {
            return;
        }

        ejecutando = true;
        cancelar = new AtomicBoolean(false);
        ejecutor = Executors.newFixedThreadPool(3);

        restablecerInterfaz();
        btnIniciar.setDisable(true);
        btnCancelar.setDisable(false);
        lblEstado.setText("Simulación en ejecución...");

        VentaService servicio = new VentaService();

        CompletableFuture<?>[] tareas = new CompletableFuture<?>[3];

        tareas[0] = ejecutarVendedor(
                "Vendedor 1", servicio, lblVendedor1, progreso1);

        tareas[1] = ejecutarVendedor(
                "Vendedor 2", servicio, lblVendedor2, progreso2);

        tareas[2] = ejecutarVendedor(
                "Vendedor 3", servicio, lblVendedor3, progreso3);

        CompletableFuture.allOf(tareas)
                .whenComplete((resultado, error) -> {
                    ejecutor.shutdown();

                    Platform.runLater(() -> {
                        ejecutando = false;
                        btnCancelar.setDisable(true);

                        if (cerrando) {
                            Platform.exit();
                            return;
                        }

                        btnIniciar.setDisable(false);

                        if (error != null) {
                            lblEstado.setText(
                                    "La simulación terminó con un error."
                            );
                        } else if (cancelar.get()) {
                            lblEstado.setText(
                                    "Cancelada. Todos los vendedores terminaron."
                            );
                        } else {
                            lblEstado.setText(
                                    "Finalizada: 15 ventas y C$1,500.00."
                            );
                        }
                    });
                });
    }

    private CompletableFuture<Void> ejecutarVendedor(
            String nombre,
            VentaService servicio,
            Label etiqueta,
            ProgressBar barra) {

        return CompletableFuture.runAsync(
                new VendedorTask(
                        nombre,
                        servicio,
                        cancelar,
                        progreso -> Platform.runLater(() -> {
                            etiqueta.setText(
                                    nombre + ": " + progreso + "/5 ventas"
                            );
                            barra.setProgress(progreso / 5.0);
                        }),
                        estado -> Platform.runLater(() -> {
                            lblVentas.setText(
                                    "Ventas confirmadas: " + estado.ventas()
                            );
                            lblRecaudacion.setText(
                                    "Recaudación: C$"
                                            + estado.recaudacion()
                            );
                        }),
                        mensaje -> Platform.runLater(
                                () -> etiqueta.setText(mensaje)
                        )
                ),
                ejecutor
        );
    }

    @FXML
    private void cancelarSimulacion() {
        if (!ejecutando || cancelar == null) {
            return;
        }

        cancelar.set(true);
        btnCancelar.setDisable(true);
        lblEstado.setText(
                "Cancelando: esperando la terminación de los vendedores..."
        );
    }

    private void restablecerInterfaz() {
        lblVentas.setText("Ventas confirmadas: 0");
        lblRecaudacion.setText("Recaudación: C$0.00");

        lblVendedor1.setText("Vendedor 1: 0/5 ventas");
        lblVendedor2.setText("Vendedor 2: 0/5 ventas");
        lblVendedor3.setText("Vendedor 3: 0/5 ventas");

        progreso1.setProgress(0);
        progreso2.setProgress(0);
        progreso3.setProgress(0);
    }

    public void cerrarAplicacion() {
        if (ejecutando) {
            cerrando = true;
            cancelar.set(true);
            btnIniciar.setDisable(true);
            btnCancelar.setDisable(true);
            lblEstado.setText("Cerrando: esperando a los vendedores...");
        } else {
            cerrando = true;
            Platform.exit();
        }
    }
}
