package controllers;

import java.io.IOException;

import dao.TickadoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import models.Trabajador;
import javafx.stage.Modality;


public class MainController {
	
	@FXML
    private MenuItem menuAdmin;
	
	AlertController alerta = new AlertController();
	
	// Método para cargar la vista principal (Main.fxml)
    public void mostrarVistaMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
            Stage stage = (Stage) menuAdmin.getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
        	alerta.mostrarAlerta("Error", "No se pudo cargar la vista principal.");
        }
    }

    // Método para mostrar la ventana de login antes de ir a Admin.fxml
    public void mostrarLogin() {
    	 try {
    	        // Cargar la ventana de login
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
    	        Stage loginStage = new Stage();
    	        loginStage.setScene(new Scene(loader.load()));
    	        loginStage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana anterior
    	        loginStage.setTitle("Iniciar Sesión");
    	        
    	     // Obtener el controlador de Login y asignar la instancia de loginStage
                LoginController loginController = loader.getController();
                loginController.setLoginStage(loginStage);  // Se configura antes de showAndWait()

    	        loginStage.showAndWait(); // Espera a que el usuario se loguee

    	        // Verificar si el login fue exitoso
    	        if (loginController.isLoginExitoso()) {
    	            try {
    	                // Cargar la ventana de administración
    	                FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/views/Admin.fxml"));
    	                Stage adminStage = new Stage();
    	                adminStage.setScene(new Scene(adminLoader.load()));
    	                adminStage.setTitle("Menú Administración");
    	                adminStage.show();
    	            } catch (IOException e) {
    	                e.printStackTrace(); // 🛑 Imprime el error en la consola para ver detalles
    	                alerta.mostrarAlerta("Error", "No se pudo cargar Admin.fxml. Verifica que la ruta y el controlador sean correctos.");
    	            }
    	        }

    	    } catch (IOException e) {
    	        e.printStackTrace(); // 🛑 Imprime el error en la consola para ver detalles
    	        alerta.mostrarAlerta("Error", "No se pudo cargar Login.fxml. Verifica la ruta y estructura del archivo.");
    	    }
} 
        
    
    @FXML
    private TableView<Trabajador> tablaTrabajadores;
    
    @FXML
    private TableColumn<Trabajador, String> columnaNombre;
    
    @FXML
    private TableColumn<Trabajador, String> columnaHoraEntrada;
    
    @FXML
    private TextField campoCodigo;

    @FXML
    private Button btnRegistrar;

    private ObservableList<Trabajador> listaTrabajadores = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurar la columna para mostrar los nombres
        columnaNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        
        // Agregar la hora de entrada
        columnaHoraEntrada.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getHoraEntrada())); 
        
        // Enlazar la lista con la tabla
        tablaTrabajadores.setItems(listaTrabajadores);

        // Cargar los trabajadores en turno al iniciar la aplicación
        actualizarLista();
    }

    @FXML
    private void registrarEntradaSalida() {
        String codigo = campoCodigo.getText().trim();

        if (!codigo.isEmpty()) {
            boolean salidaRegistrada = TickadoDAO.registrarSalida(codigo);
            
            if (!salidaRegistrada) {
                TickadoDAO.registrarEntrada(codigo);
            }

            campoCodigo.clear();
            actualizarLista();
        } else {
        	alerta.mostrarAlerta("Error", "Por favor, introduce un código antes de continuar.");
        }
    }

    private void actualizarLista() {
        listaTrabajadores.clear();
        listaTrabajadores.addAll(TickadoDAO.obtenerTrabajadoresEnTurno());
    }
    
    public void mostrarAyuda() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Ayuda");
        alerta.setHeaderText("Información de contacto");
        alerta.setContentText("Para soporte técnico, contacte a:\n\n📧 soporteTickManager@cesur.com\n📞 +34 690 386 887");

        alerta.showAndWait();
    }
}

