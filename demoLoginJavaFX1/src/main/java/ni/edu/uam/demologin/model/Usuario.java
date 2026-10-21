package ni.edu.uam.demologin.model;

public class Usuario extends BaseEntity{
    private String nombre;
    private String apellido;
    private String correo;
    private String password;
    private String usuario;
    private String rutafoto;

    public Usuario(String nombre, String apellido, String email, String password, String usuario, String rutafoto) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = email;
        this.password = password;
        this.usuario = usuario;
        this.rutafoto = rutafoto;
    }

    public Usuario() {
        super();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRutafoto() {
        return rutafoto;
    }

    public void setRutafoto(String rutafoto) {
        this.rutafoto = rutafoto;
    }
}
