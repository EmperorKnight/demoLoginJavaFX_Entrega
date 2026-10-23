
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

    @FXML
    private TextField txtParametro;

    @FXML
    private Label tableview;

    @FXML
    private TableView<Usuario> Usuarios;

    @FXML
    private TableColumn<Usuario, UUID> colID;

    @FXML
    private TableColumn<Usuario, String> colNombre;

    @FXML
    private TableColumn<Usuario, String> colApellido;

    @FXML
    private TableColumn<Usuario, String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colPassword;

    @FXML
    private TableColumn<Usuario, String> colUsuario;

    @FXML
    private TableColumn<Usuario, String> colRutaFoto;

    private final UsuarioService service;
    private final NavegationService navegationService =
            new NavegationService();

    public UsuarioModalController() {
        this.service = new UsuarioService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colID.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));

        colApellido.setCellValueFactory(
                new PropertyValueFactory<>("apellido"));

        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));

        colPassword.setCellValueFactory(
                new PropertyValueFactory<>("password"));

        colUsuario.setCellValueFactory(
                new PropertyValueFactory<>("usuario"));

        colRutaFoto.setCellValueFactory(
                new PropertyValueFactory<>("rutafoto"));

        try {
            Usuarios.setItems(getUsuarios());
        } catch (Exception e) {
            mostrarError(e);
        }

        txtParametro.textProperty().addListener(
                (observable, anterior, nuevo) -> {

                    if (nuevo.trim().isEmpty()) {
                        try {
                            Usuarios.setItems(getUsuarios());
                        } catch (Exception e) {
                            mostrarError(e);
                        }
                    }
                }
        );
    }

    private ObservableList<Usuario> getUsuarios()
            throws Exception {

        return FXCollections.observableArrayList(
                service.findAll()
        );
    }

    @FXML
    private void abrirEditor() {

        Usuario seleccionado =
                Usuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Advertencia",
                    "Seleccione un usuario de la tabla."
            );

            return;
        }

        try {

            txtParametro.clear();

            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("editarUsuario.fxml")
            );

            Parent root = loader.load();

            EditorController controller =
                    loader.getController();

            controller.prepararEditor(seleccionado);

            Scene scene = Usuarios.getScene();
            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.sizeToScene();
            stage.centerOnScreen();

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    @FXML
    private void abrirRegistro() {

        try {

            txtParametro.clear();

            navegationService.cambiarVista(
                    "registro.fxml",
                    tableview
            );

        } catch (IOException e) {
            mostrarError(e);
        }
    }

    @FXML
    private void eliminar() {

        Usuario seleccionado =
                Usuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {

            UtilController.mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Borrar Registro",
                    "Debe seleccionar un usuario."
            );

            return;
        }

        Alert alerta = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        alerta.setTitle("Borrar Registro");
        alerta.setHeaderText(null);
        alerta.setContentText(
                "¿Desea borrar este registro?"
        );

        Optional<ButtonType> resultado =
                alerta.showAndWait();

        if (resultado.isPresent()
                && resultado.get() == ButtonType.OK) {

            try {

                service.delete(seleccionado);

                Usuarios.setItems(getUsuarios());
                txtParametro.clear();

                UtilController.mostrarMensaje(
                        Alert.AlertType.INFORMATION,
                        "Eliminación",
                        "Usuario eliminado correctamente."
                );

            } catch (Exception e) {
                mostrarError(e);
            }
        }
    }

    @FXML
    private void buscar() {

        String parametro =
                txtParametro.getText().trim().toLowerCase();

        try {

            ObservableList<Usuario> todos =
                    getUsuarios();

            if (parametro.isEmpty()) {
                Usuarios.setItems(todos);
                return;
            }

            ObservableList<Usuario> resultados =
                    FXCollections.observableArrayList();

            for (Usuario usuario : todos) {

                for (TableColumn<Usuario, ?> columna :
                        Usuarios.getColumns()) {

                    Object valor =
                            columna.getCellData(usuario);

                    if (valor != null &&
                            valor.toString()
                                    .toLowerCase()
                                    .contains(parametro)) {

                        resultados.add(usuario);
                        break;
                    }
                }
            }

            Usuarios.setItems(resultados);

        } catch (Exception e) {
            mostrarError(e);
        }
    }

    @FXML
    private void regresar() {

        try {

            txtParametro.clear();

            navegationService.cambiarVista(
                    "login.fxml",
                    tableview
            );

        } catch (IOException e) {
            mostrarError(e);
        }
    }

    @FXML
    private void abrirPedidos() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("pedidos.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();

            stage.setTitle("Procesamiento de Pedidos");
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            PedidosController controller =
                    loader.getController();

            controller.configurarCierre(stage);

            stage.show();

        } catch (IOException e) {
            mostrarError(e);
        }
    }

    private void mostrarError(Exception e) {

        e.printStackTrace();

        UtilController.mostrarMensaje(
                Alert.AlertType.ERROR,
                "Error",
                e.getMessage() == null
                        ? "Ocurrió un error inesperado."
                        : e.getMessage()
        );
    }
}
