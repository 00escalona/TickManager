package controllers;

import dao.TrabajadorDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EliminarTrabajadorController {

    @FXML private TextField campoNombre;
    
    AlertController alerta = new AlertController();

    @FXML
    private void eliminarTrabajador() {
        String nombre = campoNombre.getText();

        if (!nombre.isEmpty()) {
            TrabajadorDAO.eliminarTrabajador(nombre);
            alerta.mostrarAlerta("Éxito", "Trabajador eliminado.");
            cerrarVentana();
        } else {
        	alerta.mostrarAlerta("Error", "Debe ingresar un nombre.");
        }
    }
    
    @FXML
    private void cerrarVentana() {
    	Platform.runLater(() -> {
            Stage stage = (Stage) campoNombre.getScene().getWindow();
            stage.close();
    	});
    }
}
