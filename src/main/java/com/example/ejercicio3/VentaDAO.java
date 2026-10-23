
package com.example.ejercicio3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VentaDAO {

    public void guardar(Venta venta) throws SQLException {
        String sql = """
                INSERT INTO ventas
                (simulacion_id, vendedor, numero_venta, monto)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionDB.obtenerConexion()) {
            conexion.setAutoCommit(false);

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setObject(1, venta.getSimulacionId());
                ps.setString(2, venta.getVendedor());
                ps.setInt(3, venta.getNumeroVenta());
                ps.setBigDecimal(4, venta.getMonto());

                ps.executeUpdate();
                conexion.commit();

            } catch (SQLException e) {
                try {
                    conexion.rollback();
                } catch (SQLException errorRollback) {
                    e.addSuppressed(errorRollback);
                }
                throw e;
            }
        }
    }
}
