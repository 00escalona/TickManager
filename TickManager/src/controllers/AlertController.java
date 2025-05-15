package controllers;

import javafx.scene.control.Alert;

public class AlertController {
	
	// Método para mostrar alertas en pantalla
    public void mostrarAlerta(String titulo, String mensaje) {
    	if(titulo == "Error")
    	{
    		Alert alerta = new Alert(Alert.AlertType.ERROR);
    		alerta.setTitle(titulo);
            alerta.setHeaderText(null);
            alerta.setContentText(mensaje);
            alerta.showAndWait();
            return;
    	}
    	
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}