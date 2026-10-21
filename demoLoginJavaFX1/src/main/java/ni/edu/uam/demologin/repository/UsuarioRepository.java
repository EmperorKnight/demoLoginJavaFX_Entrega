package ni.edu.uam.demologin.repository;

import ni.edu.uam.demologin.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    List<Usuario> findAll() throws Exception;

    Optional<Usuario> findById(String id) throws Exception;

    void save(Usuario usuario) throws Exception;

    Optional<Usuario> findByLogin(String usuario, String password) throws Exception;

    void update(Usuario usuario) throws Exception;

    void delete(Usuario usuario) throws Exception;
}
