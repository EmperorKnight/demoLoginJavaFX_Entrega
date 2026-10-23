
package com.example.ejercicio3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionDB {

    private static final String URL =
            "jdbc:postgresql://localhost:5432/CajaVentasDB";

    private static final String USUARIO = "postgres";

    // Reemplaza TU_PASSWORD por tu contraseña real de PostgreSQL.
    private static final String PASSWORD = "1234";

    private ConexionDB() {
        // Evita crear instancias de esta clase.
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USUARIO,
                PASSWORD
        );
    }
}
