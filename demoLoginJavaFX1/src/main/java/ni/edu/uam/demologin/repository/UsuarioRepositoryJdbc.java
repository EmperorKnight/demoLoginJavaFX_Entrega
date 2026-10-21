package ni.edu.uam.demologin.repository;

import ni.edu.uam.demologin.config.IConnection;
import ni.edu.uam.demologin.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsuarioRepositoryJdbc implements UsuarioRepository{

    private final IConnection connection;
    public UsuarioRepositoryJdbc(IConnection connection) {
        this.connection = connection;
    }

    // Funcion de busqueda en la base de datos para todos
    @Override
    public List<Usuario> findAll() throws Exception{
        String sql = "SELECT id" +
                ", \"primerNombre\"" +
                ", \"primerApellido\"" +
                ", \"rutaFoto\" " +
                ", usuario" +
                ", correo" +
                ", password " +
                " FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection con = connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("primerNombre"),
                        rs.getString("primerApellido"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("usuario"),
                        rs.getString("rutaFoto")
                );
                usuario.setId(rs.getObject("id", UUID.class));
                usuarios.add(usuario);
            }
            return usuarios;
        }
    }

    // Funcion de busqueda en la base de datos por id
    @Override
    public Optional<Usuario> findById(String id) throws Exception{
        String sql = "SELECT id" +
                ", \"primerNombre\"" +
                ", \"primerApellido\"" +
                ", \"rutaFoto\"" +
                ", usuario" +
                ", correo" +
                ", password FROM" +
                " usuario WHERE" +
                " id = ?";
        try(Connection con= connection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            rs.getString("primerNombre"),
                            rs.getString("primerApellido"),
                            rs.getString("correo"),
                            rs.getString("password"),
                            rs.getString("usuario"),
                            rs.getString("rutaFoto")
                    );
                    return Optional.of(usuario);
                }
            }
        }
        return Optional.empty();
    }

    // Funcion de insercion de datos en la base de datos
    @Override
    public void save(Usuario usuario) throws Exception {
        String sql = "INSERT INTO usuario " +
                "(id, \"" +
                "primerNombre\", \"" +
                "primerApellido\", " +
                "usuario, " +
                "password, " +
                "correo, \"" +
                "rutaFoto\")" +
                "VALUES(?,?,?,?,?,?,?)";
        try(Connection con= connection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setObject(1, UUID.randomUUID());
            ps.setString(2,usuario.getNombre());
            ps.setString(3,usuario.getApellido());
            ps.setString(4,usuario.getUsuario());
            ps.setString(5,usuario.getPassword());
            ps.setString(6,usuario.getCorreo());
            ps.setString(7,usuario.getRutafoto());
            ps.executeUpdate();
        }
    }

    // Funcion de busqueda en la base de datos para el login
    @Override
    public Optional<Usuario> findByLogin(String usuario, String password) throws Exception {
        final String sql = " SELECT id,usuario,correo,password FROM public.usuario WHERE usuario = ? AND password = ? ";
        try(Connection con= connection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setString(1,usuario);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setUsuario(rs.getString("usuario"));
                u.setPassword(rs.getString("password"));
                u.setCorreo(rs.getString("correo"));
                u.setId(rs.getObject("id",UUID.class));
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    // Funcion de alteracion de datos en la base de datos
    @Override
    public void update(Usuario usuario) throws Exception {
        String sql = "UPDATE public.usuario \n" +
                "SET \"primerNombre\" = ?, \"primerApellido\" = ?, usuario = ?, correo = ?, password = ?, \"rutaFoto\" = ?\n" +
                "WHERE id = ?";
        try (Connection con = connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setObject(1, usuario.getNombre());
            ps.setObject(2, usuario.getApellido());
            ps.setObject(3, usuario.getUsuario());
            ps.setObject(4, usuario.getCorreo());
            ps.setObject(5, usuario.getPassword());
            ps.setObject(6, usuario.getRutafoto());
            ps.setObject(7, usuario.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Usuario usuario) throws Exception{
        String sql = "DELETE FROM public.usuario \n" +
                "WHERE id = ?";
        try (Connection con = connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql))
        {
            ps.setObject(1, usuario.getId());
            ps.executeUpdate();
        }
    }

}
