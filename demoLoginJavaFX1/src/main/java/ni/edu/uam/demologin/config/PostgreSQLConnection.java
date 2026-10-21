package ni.edu.uam.demologin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection implements IConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/ejemplo";
    private static final String usuario = "postgres";
    private static final String password = "bdserver1234";

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
