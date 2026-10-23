
package com.example.ejercicio3;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Consumer;

public class VentaService {

    private final UUID simulacionId = UUID.randomUUID();
    private final VentaDAO ventaDAO = new VentaDAO();

    private int totalVentas = 0;
    private BigDecimal recaudacion = BigDecimal.ZERO;

    public synchronized void confirmarVenta(
            String vendedor,
            int numeroVenta,
            Consumer<EstadoCaja> actualizarEstado) throws Exception {

        Venta venta = new Venta(
                simulacionId,
                vendedor,
                numeroVenta,
                new BigDecimal("100.00")
        );

        // Primero se guarda la venta completa en PostgreSQL.
        ventaDAO.guardar(venta);

        // Solo después de guardarla se actualizan los totales.
        totalVentas++;
        recaudacion = recaudacion.add(venta.getMonto());

        EstadoCaja estado = new EstadoCaja(totalVentas, recaudacion);
        actualizarEstado.accept(estado);
    }

    public synchronized EstadoCaja obtenerEstado() {
        return new EstadoCaja(totalVentas, recaudacion);
    }

    public record EstadoCaja(int ventas, BigDecimal recaudacion) {
    }
}
