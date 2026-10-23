package ni.edu.uam.demologin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection implements IConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/"; // Falta el nombre de su base de datos
    private static final String usuario = ""; //Su usario
    private static final String password = ""; //Su contraseña

    @Override
    public Connection getConnection()  {
        try {
            return DriverManager.getConnection(
                    URL
                    , usuario
                    , password);
        }
        catch (SQLException ex) {
            return null;
        }
    }
}
