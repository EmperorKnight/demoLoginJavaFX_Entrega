package ni.edu.uam.demologin.service;

import javafx.application.Platform;

import java.util.function.BiConsumer;

public class PedidosService {

    private static final int TOTAL_PEDIDOS = 30;
    private static final int TIEMPO_PEDIDO = 200;

    private final Object bloqueo = new Object();

    private Thread trabajador;
    private boolean activo = false;
    private boolean cancelacionSolicitada = false;
    private boolean cerrado = false;

    private int completados = 0;
    private long numeroEjecucion = 0;

    public boolean iniciar(BiConsumer<Integer, String> actualizar) {

        final long ejecucionActual;
        final Thread nuevoTrabajador;

        synchronized (bloqueo) {

            if (activo || cerrado) {
                System.out.println("No se puede iniciar: "
                        + "hay otro procesamiento activo o la ventana está cerrada.");
                return false;
            }

            activo = true;
            cancelacionSolicitada = false;
            completados = 0;

            ejecucionActual = ++numeroEjecucion;

            nuevoTrabajador = new Thread(
                    () -> procesar(actualizar, ejecucionActual),
                    "ProcesadorPedidos-" + ejecucionActual
            );

            nuevoTrabajador.setDaemon(true);
            trabajador = nuevoTrabajador;
        }

        try {
            nuevoTrabajador.start();

            Thread supervisor = new Thread(() -> {
                try {
                    // Espera fuera del hilo de JavaFX.
                    nuevoTrabajador.join();

                    final int totalFinal;
                    final String estadoFinal;

                    synchronized (bloqueo) {

                        // El trabajador ya terminó realmente.
                        activo = false;
                        trabajador = null;

                        totalFinal = completados;

                        estadoFinal = cancelacionSolicitada
                                ? "Cancelado"
                                : totalFinal == TOTAL_PEDIDOS
                                ? "Completado"
                                : "Error";
                    }

                    System.out.println("HILO TERMINADO REALMENTE: "
                            + nuevoTrabajador.getName());

                    Platform.runLater(() -> {
                        synchronized (bloqueo) {

                            if (cerrado
                                    || ejecucionActual != numeroEjecucion) {
                                return;
                            }
                        }

                        actualizar.accept(totalFinal, estadoFinal);
                    });

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "SupervisorPedidos-" + ejecucionActual);

            supervisor.setDaemon(true);
            supervisor.start();

            return true;

        } catch (RuntimeException e) {

            synchronized (bloqueo) {
                activo = false;
                trabajador = null;
            }

            System.out.println("ERROR AL INICIAR: " + e.getMessage());
            return false;
        }
    }

    private void procesar(
            BiConsumer<Integer, String> actualizar,
            long ejecucionActual) {

        System.out.println("HILO INICIADO: "
                + Thread.currentThread().getName());

        try {

            for (int i = 0; i < TOTAL_PEDIDOS; i++) {

                synchronized (bloqueo) {
                    if (cancelacionSolicitada || cerrado) {
                        break;
                    }
                }

                // Simula el procesamiento de un pedido.
                Thread.sleep(TIEMPO_PEDIDO);

                final int cantidad;

                synchronized (bloqueo) {

                    if (cancelacionSolicitada || cerrado) {
                        break;
                    }

                    completados++;
                    cantidad = completados;
                }

                System.out.println("Pedido completado: "
                        + cantidad + " / " + TOTAL_PEDIDOS);

                Platform.runLater(() -> {

                    synchronized (bloqueo) {

                        if (cerrado
                                || ejecucionActual != numeroEjecucion) {
                            return;
                        }
                    }

                    actualizar.accept(cantidad, "En ejecución");
                });
            }

        } catch (InterruptedException e) {

            System.out.println("CANCELACIÓN DETECTADA: "
                    + Thread.currentThread().getName());

            Thread.currentThread().interrupt();

        } catch (Exception e) {

            System.out.println("ERROR EN EL PROCESAMIENTO: "
                    + e.getMessage());

            e.printStackTrace();

        } finally {

            System.out.println("PROCESAMIENTO FINALIZANDO: "
                    + Thread.currentThread().getName());
        }
    }

    public void cancelar() {

        synchronized (bloqueo) {

            if (!activo) {
                return;
            }

            cancelacionSolicitada = true;

            System.out.println("CANCELACIÓN SOLICITADA");

            if (trabajador != null) {
                trabajador.interrupt();
            }
        }
    }

    public boolean estaActivo() {

        synchronized (bloqueo) {
            return activo;
        }
    }

    public void cerrar() {

        synchronized (bloqueo) {

            cerrado = true;
            cancelacionSolicitada = true;

            System.out.println("VENTANA CERRADA: cancelando procesamiento.");

            if (trabajador != null) {
                trabajador.interrupt();
            }
        }
    }
}
