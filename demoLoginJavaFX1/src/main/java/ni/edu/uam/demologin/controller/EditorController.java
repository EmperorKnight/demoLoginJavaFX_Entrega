package ni.edu.uam.demologin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import ni.edu.uam.demologin.model.Usuario;
import ni.edu.uam.demologin.service.ArchivoService;
import ni.edu.uam.demologin.service.NavegationService;
import ni.edu.uam.demologin.service.UsuarioService;

import java.io.File;

public class EditorController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmaPassword;
    @FXML private ImageView imageFoto;

    private String rutaFoto;
    private Usuario usuarioLocal;

    private final ArchivoService archivoService = new ArchivoService();
    private final NavegationService navegationService = new NavegationService();
    private final UsuarioService service;

    public EditorController(){
        this.service = new UsuarioService();
    }

    @FXML
    private void seleccionarFoto() {
        File archivo = seleccionarArchivo();
        if (archivo == null) {
            UtilController.mostrarMensaje(Alert.AlertType.WARNING, "No hay Archivo", "Debe de seleccionar una fotografia");
            return;
        }
        try {
            rutaFoto = archivoService.guardarArchivo(archivo);
            mostrarImagen(rutaFoto);
        }
        catch (Exception e) {
            UtilController.mostrarException(e);
        }
    }

    private void mostrarImagen(String rutaFoto) {
        Image image = new Image(new File(rutaFoto).toURI().toString());
        imageFoto.setImage(image);
    }

    private File seleccionarArchivo() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo");
        fc.getExtensionFilters()
                .add(new FileChooser
                        .ExtensionFilter("Imagenes"
                        , "*.jpg"
                        , "*.png", "*.jpeg"));
        return fc.showOpenDialog(
                imageFoto
                        .getScene()
                        .getWindow());
    }

    public void prepararEditor(Usuario usuario){
        txtNombre.appendText(usuario.getNombre());
        txtApellido.appendText(usuario.getApellido());

        if(!usuario.getCorreo().isEmpty()){
            txtCorreo.appendText(usuario.getCorreo());
        }

        String ruta = usuario.getRutafoto();
        if(ruta == null){
            imageFoto.setImage(null);
        }
        else{
            rutaFoto=ruta;
            mostrarImagen(ruta);
        }

        txtUsuario.appendText(usuario.getUsuario());
        txtPassword.appendText(usuario.getPassword());
        txtConfirmaPassword.appendText(usuario.getPassword());

        usuarioLocal = usuario;
    }

    @FXML
    private void actualizar() throws Exception{
        if (!validarFormulario()) {
            return;
        }

        usuarioLocal.setNombre(txtNombre.getText());
        usuarioLocal.setApellido(txtApellido.getText());
        usuarioLocal.setCorreo(txtCorreo.getText());
        usuarioLocal.setUsuario(txtUsuario.getText());
        usuarioLocal.setPassword(txtPassword.getText());
        usuarioLocal.setRutafoto(rutaFoto);

        service.update(usuarioLocal);
        UtilController.mostrarMensaje(Alert.AlertType.CONFIRMATION, "Confirmacion", "Usuario actualizado");
    }

    private boolean validarFormulario() {
        if (txtNombre.getText().isBlank()
                || txtApellido.getText().isBlank()
                || txtUsuario.getText().isBlank()
                || txtPassword.getText().isBlank()
                || txtConfirmaPassword.getText().isBlank()
        ) {
            UtilController.mostrarMensaje(Alert.AlertType.WARNING,
                    "Validacion",
                    "Todos los campos son obligatorios");
            return false;
        }
        if (!txtPassword.getText().equals(txtConfirmaPassword.getText())) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Contraseña",
                    "Contraseña no coincide"
            );
            return false;
        }

        return true;
    }

    @FXML
    private void regresar() {
        try {
            navegationService.cambiarVista("usuario-modal.fxml",txtUsuario);
        }
        catch (Exception e){
            UtilController.mostrarException(e);
        }
    }
}
