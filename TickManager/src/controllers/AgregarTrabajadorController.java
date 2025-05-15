package controllers;

import dao.TrabajadorDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarTrabajadorController {

    @FXML private TextField campoNombre;
    @FXML private TextField campoCodigo;
    
    AlertController alerta = new AlertController();

    @FXML
    private void agregarTrabajador() {
        String nombre = campoNombre.getText();
        String codigo = campoCodigo.getText();

        if (!nombre.isEmpty() && !codigo.isEmpty()) {
            TrabajadorDAO.agregarTrabajador(nombre, codigo);
            alerta.mostrarAlerta("Éxito", "Trabajador agregado correctamente.");
            cerrarVentana();
        } else {
        	alerta.mostrarAlerta("Error", "Todos los campos son obligatorios.");
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
