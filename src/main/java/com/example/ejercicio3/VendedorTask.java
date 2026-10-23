
package com.example.ejercicio3;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class VendedorTask implements Runnable {

    private final String nombre;
    private final VentaService servicio;
    private final AtomicBoolean cancelar;
    private final Consumer<Integer> actualizarProgreso;
    private final Consumer<VentaService.EstadoCaja> actualizarEstado;
    private final Consumer<String> actualizarMensaje;

    public VendedorTask(
            String nombre,
            VentaService servicio,
            AtomicBoolean cancelar,
            Consumer<Integer> actualizarProgreso,
            Consumer<VentaService.EstadoCaja> actualizarEstado,
            Consumer<String> actualizarMensaje) {

        this.nombre = nombre;
        this.servicio = servicio;
        this.cancelar = cancelar;
        this.actualizarProgreso = actualizarProgreso;
        this.actualizarEstado = actualizarEstado;
        this.actualizarMensaje = actualizarMensaje;
    }

    @Override
    public void run() {
        int completadas = 0;

        try {
            for (int i = 1; i <= 5; i++) {
                if (cancelar.get()
                        || Thread.currentThread().isInterrupted()) {
                    break;
                }

                Thread.sleep(400);

                if (cancelar.get()
                        || Thread.currentThread().isInterrupted()) {
                    break;
                }

                servicio.confirmarVenta(
                        nombre,
                        i,
                        actualizarEstado
                );

                completadas++;
                actualizarProgreso.accept(completadas);
            }

            actualizarMensaje.accept(
                    nombre + ": terminó con " + completadas + " ventas."
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            actualizarMensaje.accept(nombre + ": interrumpido.");

        } catch (Exception e) {
            cancelar.set(true);
            actualizarMensaje.accept(
                    nombre + ": error al registrar una venta: "
                            + e.getMessage()
            );
        }
    }
}
