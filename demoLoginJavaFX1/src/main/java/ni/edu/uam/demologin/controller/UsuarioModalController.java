package ni.edu.uam.demologin.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ni.edu.uam.demologin.Main;
import ni.edu.uam.demologin.model.Usuario;
import ni.edu.uam.demologin.service.NavegationService;
import ni.edu.uam.demologin.service.UsuarioService;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class UsuarioModalController implements Initializable {
    @FXML private TextField txtParametro;
    @FXML private Label tableview;
    @FXML private TableView<Usuario> Usuarios;
    @FXML private TableColumn<Usuario, UUID> colID;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colPassword;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colRutaFoto;

    private final UsuarioService service;
    private final NavegationService navegationService = new NavegationService();

    public UsuarioModalController(){this.service = new UsuarioService();}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRutaFoto.setCellValueFactory(new PropertyValueFactory<>("rutafoto"));

        try {
            Usuarios.setItems(getUsuarios());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        txtParametro.textProperty().addListener((observable, valorAnterior, valorNuevo) -> {
            if (valorNuevo.trim().isEmpty()) {
                try {
                    Usuarios.setItems(getUsuarios());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private ObservableList<Usuario> getUsuarios() throws Exception {
        return FXCollections.observableArrayList(service.findAll());
    }

    @FXML
    private void abrirEditor() {
        Usuario usuarioSeleccionado = Usuarios.getSelectionModel().getSelectedItem();

        if (usuarioSeleccionado == null) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "Por favor, seleccione un usuario de la tabla.");
            return;
        }

        try {
            txtParametro.clear();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("editarUsuario.fxml"));
            Parent root = loader.load();

            EditorController controller = loader.getController();
            controller.prepararEditor(usuarioSeleccionado);

            Scene scene = Usuarios.getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            UtilController.mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Validacion",
                    "Error: "+e.getMessage()
            );
        }
    }

    @FXML
    private void abrirRegistro(){
        try{
            txtParametro.clear();
            navegationService.cambiarVista("registro.fxml", tableview);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void eliminar() throws Exception{
        Usuario usuarioSeleccionado = Usuarios.getSelectionModel().getSelectedItem();

        if(usuarioSeleccionado == null){
            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Borrar Registro",
                    "Debe seleccionar una fila para borar usuarios");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Borrar Registro");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea borrar este registro?");

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            service.delete(usuarioSeleccionado);
            Usuarios.setItems(getUsuarios());
            txtParametro.clear();
            UtilController.mostrarMensaje(
                    Alert.AlertType.CONFIRMATION,
                    "Eliminacion con exito",
                    "Usuario eliminado con exito"
            );
        }
        else {
            UtilController.mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Cancelado",
                    "Eliminacion cancelada"
            );
        }
    }

    @FXML
    private void buscar() {
        String parametro = txtParametro.getText().trim().toLowerCase();

        if (parametro.isEmpty()) {
            try {
                Usuarios.setItems(getUsuarios());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }

        ObservableList<Usuario> resultados = FXCollections.observableArrayList();

        for (Usuario usuario : Usuarios.getItems()) {
            for (TableColumn<Usuario, ?> columna : Usuarios.getColumns()) {
                Object valor = columna.getCellData(usuario);

                if (valor != null &&
                        valor.toString().toLowerCase().contains(parametro)) {
                    resultados.add(usuario);
                    break;
                }
            }
        }

        Usuarios.setItems(resultados);
    }

    @FXML
    private void regresar(){
        try{
            txtParametro.clear();
            navegationService.cambiarVista("login.fxml", tableview);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}