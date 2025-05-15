package application;

import database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/Main.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Tickado de Trabajadores");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	if (Database.conectar() != null) {
            System.out.println("✅ Conexión exitosa a MySQL");
        } else {
            System.out.println("❌ Error en la conexión a MySQL");
        }
        launch(args);
    }
}
