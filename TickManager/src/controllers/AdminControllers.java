package controllers;

import dao.TrabajadorDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Trabajador;
import resources.Database;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.io.FileOutputStream;

public class AdminControllers {

    @FXML
    private DatePicker datePickerInicio;
    
    @FXML
    private DatePicker datePickerFin;
    
    @FXML
    private TableView<Trabajador> tablaTrabajadores;
    @FXML
    private TableColumn<Trabajador, Integer> id;
    @FXML
    private TableColumn<Trabajador, Integer> codigo_personal;
    @FXML
    private TableColumn<Trabajador, String> nombre;

    private ObservableList<Trabajador> trabajadoresList = FXCollections.observableArrayList();
    
    AlertController alerta = new AlertController();

    public void cargarTrabajadores() {
        trabajadoresList.clear(); // Limpiar la lista antes de llenarla
        try {
            Connection conn = Database.conectar(); // Asegurar conexión a la BD
            String query = "SELECT * FROM trabajadores";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                trabajadoresList.add(new Trabajador(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo_personal"), null
                ));
            }
            tablaTrabajadores.setItems(trabajadoresList); // Cargar los datos en la tabla

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        codigo_personal.setCellValueFactory(new PropertyValueFactory<>("codigo_personal"));

        cargarTrabajadores(); // Llamamos a la función al iniciar la pantalla
    }
    
    @FXML
    private void mostrarAgregarTrabajador() {
        try {
            // Cargar la vista de agregar trabajador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AgregarTrabajador.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana anterior hasta cerrar esta
            stage.setTitle("Agregar Trabajador");
            stage.showAndWait(); // Esperar hasta que se cierre esta ventana
        } catch (IOException e) {
            e.printStackTrace();
            alerta.mostrarAlerta("Error", "No se pudo abrir la ventana de Agregar Trabajador.");
        }
    }
    
    @FXML
    private void mostrarEliminarTrabajador() {
        try {
            // Cargar la vista de eliminar trabajador
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EliminarTrabajador.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana anterior hasta cerrar esta
            stage.setTitle("Eliminar Trabajador");
            stage.showAndWait(); // Esperar hasta que se cierre esta ventana
        } catch (IOException e) {
            e.printStackTrace();
            alerta.mostrarAlerta("Error", "No se pudo abrir la ventana de Eliminar Trabajador.");
        }
    }
    
    @FXML
    private void mostrarRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Register.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana anterior hasta cerrar esta
            stage.setTitle("Agregar Aministrador");
            stage.showAndWait(); // Esperar hasta que se cierre esta ventana
        } catch (IOException e) {
        	alerta.mostrarAlerta("Error", "No se pudo cargar la ventana de registro.");
            e.printStackTrace();
        }
    }

    // Método para exportar informes en formato CSV
    @FXML
    private void exportarInforme() {
        LocalDate inicio = datePickerInicio.getValue();
        LocalDate fin = datePickerFin.getValue();

        if (inicio == null || fin == null) {
        	alerta.mostrarAlerta("Información", "Debe seleccionar un rango de fechas válido.");
            return;
        }

        List<String[]> registros = TrabajadorDAO.obtenerTickados();

        if (registros.isEmpty()) {
        	alerta.mostrarAlerta("Información", "No hay datos para exportar en el rango seleccionado.");
            return;
        }

        // Elegir ubicación del archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Informe");
        fileChooser.getExtensionFilters().addAll(
        		new FileChooser.ExtensionFilter("Archivo CSV", "*.csv"),
        		new FileChooser.ExtensionFilter("Archivo PDF", "*.pdf")
        		);
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            String filePath = file.getAbsolutePath();

            if (filePath.endsWith(".csv")) {
                guardarCSV(file, registros);
                alerta.mostrarAlerta("Éxito", "Informe exportado en CSV correctamente.");
            } else if (filePath.endsWith(".pdf")) {
                exportarInformePDF(filePath, registros);
                alerta.mostrarAlerta("Éxito", "Informe exportado en PDF correctamente.");
            } else {
            	alerta.mostrarAlerta("Error", "Formato no válido. Debe ser CSV o PDF.");
            }
        }
    }

    // Guardar datos en CSV usando OpenCSV
    private void guardarCSV(File file, List<String[]> registros) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file))) {
            // Encabezados
            writer.writeNext(new String[]{"Nombre", "Hora Entrada", "Hora Salida", "Horas Trabajadas"});

            // Escribir registros
            writer.writeAll(registros);

            alerta.mostrarAlerta("Éxito", "Informe exportado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
            alerta.mostrarAlerta("Error", "No se pudo guardar el archivo.");
        }
    }
    
    public void exportarInformePDF(String filePath, List<String[]> datos) {

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Agregar título
            document.add(new Paragraph("Informe de Tickados").setBold().setFontSize(16));

            // Crear tabla
            Table table = new Table(new float[]{3, 4, 4, 4}); // Columnas con tamaños

            // Agregar encabezados
            table.addCell(new Cell().add(new Paragraph("Nombre")));
            table.addCell(new Cell().add(new Paragraph("Fecha de Entrada")));
            table.addCell(new Cell().add(new Paragraph("Fecha de Salida")));
            table.addCell(new Cell().add(new Paragraph("Horas Trabajadas")));

            // Agregar datos
            for (String[] fila : datos) {
                for (String celda : fila) {
                    table.addCell(new Cell().add(new Paragraph(celda)));
                }
            }

            document.add(table);
            document.close();

            System.out.println("PDF generado correctamente en: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

