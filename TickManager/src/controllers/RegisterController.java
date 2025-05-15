package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import dao.AdminDAO;

public class RegisterController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;
    
    AlertController alerta = new AlertController();

    @SuppressWarnings("unused")
	@FXML
    private void registrarAdministrador() {
        String correo = txtCorreo.getText().trim();
        String password = txtPassword.getText().trim();

        if (correo.isEmpty() || password.isEmpty()) {
            lblError.setText("Todos los campos son obligatorios.");
            lblError.setVisible(true);
            return;
        }

		AdminDAO adminDAO = new AdminDAO();
        boolean registrado = AdminDAO.registrarAdministrador(correo, password);

        if (registrado) {
        	alerta.mostrarAlerta("Éxito", "Administrador registrado correctamente.");
            cerrarVentana();
        } else {
        	alerta.mostrarAlerta("Error", "Error al registrar. Intenta con otro correo.");
            cerrarVentana();
        }
    }

    @FXML
    private void cerrarVentana() {
    	Platform.runLater(() -> {
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.close();
    	});
    }
}
