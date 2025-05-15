package dao;

import database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAO {

    // Obtener tickados para exportación
    public static List<String[]> obtenerTickados() {
        List<String[]> tickados = new ArrayList<>();
        String sql = "SELECT nombre, hora_entrada, hora_salida, horas_trabajadas " +
                     "FROM tickado_db.registros_tickado";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String horaEntrada = rs.getString("hora_entrada");
                String horaSalida = rs.getString("hora_salida");
                String horasTrabajadas = rs.getString("horas_trabajadas");

                tickados.add(new String[]{nombre, horaEntrada, horaSalida, horasTrabajadas});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickados;
    }

    // Agregar un nuevo trabajador
    public static void agregarTrabajador(String nombre, String codigo) {
        String sql = "INSERT INTO trabajadores (nombre, codigo_personal) VALUES (?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, codigo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Eliminar un trabajador por nombre
    public static void eliminarTrabajador(String nombre) {
        String sql = "DELETE FROM trabajadores WHERE nombre = ?";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
