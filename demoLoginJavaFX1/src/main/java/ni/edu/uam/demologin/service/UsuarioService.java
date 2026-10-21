package ni.edu.uam.demologin.service;

import ni.edu.uam.demologin.config.PostgreSQLConnection;
import ni.edu.uam.demologin.model.Usuario;
import ni.edu.uam.demologin.repository.UsuarioRepository;
import ni.edu.uam.demologin.repository.UsuarioRepositoryJdbc;

import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService()  {
        this.usuarioRepository = new UsuarioRepositoryJdbc(new PostgreSQLConnection());
    }

    public List<Usuario> findAll() throws Exception{
        return usuarioRepository.findAll();
    }

    public Usuario findById(String id) throws Exception{
        return usuarioRepository.findById(id).get();
    }

    public void save(Usuario usuario) throws Exception{
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> findByLogin(String usuario, String password) throws Exception {
        return usuarioRepository.findByLogin(usuario,password);
    }

    public void update(Usuario usuario) throws Exception{
        usuarioRepository.update(usuario);
    }

    public void delete(Usuario usuario) throws Exception{
        usuarioRepository.delete(usuario);
    }
}
