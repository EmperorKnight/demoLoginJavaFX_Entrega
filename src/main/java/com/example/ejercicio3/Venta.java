
package com.example.ejercicio3;

import java.math.BigDecimal;
import java.util.UUID;

public class Venta {

    private final UUID simulacionId;
    private final String vendedor;
    private final int numeroVenta;
    private final BigDecimal monto;

    public Venta(UUID simulacionId, String vendedor,
                 int numeroVenta, BigDecimal monto) {
        this.simulacionId = simulacionId;
        this.vendedor = vendedor;
        this.numeroVenta = numeroVenta;
        this.monto = monto;
    }

    public UUID getSimulacionId() {
        return simulacionId;
    }

    public String getVendedor() {
        return vendedor;
    }

    public int getNumeroVenta() {
        return numeroVenta;
    }

    public BigDecimal getMonto() {
        return monto;
    }
}
