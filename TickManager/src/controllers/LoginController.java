package controllers;

import java.io.IOException;

import dao.AdminDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField campoCorreo;

    @FXML
    private PasswordField campoContrasena;

    private boolean loginExitoso = false;
    private Stage loginStage;
    
    AlertController alerta = new AlertController();
    
    public void setLoginStage(Stage stage) {
        this.loginStage = stage;
    }
 
    @FXML
    private void iniciarSesion() {
        String correo = campoCorreo.getText();
        String password = campoContrasena.getText();

        try {
            if (!AdminDAO.existeCorreo(correo)) {
            	alerta.mostrarAlerta("Error", "Usuario no registrado.");
                return;
            }

            if (AdminDAO.verificarCredenciales(correo, password)) {
                loginExitoso = true;
                loginStage.close(); // Cierra la ventana de login si es exitoso
            } else {
            	alerta.mostrarAlerta("Error", "Correo o contraseña incorrectos.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Opcional: para depuración
            alerta.mostrarAlerta("Error", "Correo o contraseña incorrectos.");
        }
    }

    @FXML
    private void cancelar() {
        loginStage.close(); // Cierra la ventana si el usuario cancela
    }

    public boolean isLoginExitoso() {
        return loginExitoso;
    }
}

