package dao;

import models.Trabajador;
import resources.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TickadoDAO {
    
    // Registrar entrada de un trabajador
    public static boolean registrarEntrada(String codigo) {
        String sql = "INSERT INTO registros_tickado (codigo_personal, nombre, hora_entrada, fecha) " +
                     "SELECT codigo_personal, nombre, NOW(), CURDATE() FROM trabajadores WHERE codigo_personal = ? " +
                     "AND NOT EXISTS (SELECT 1 FROM registros_tickado WHERE codigo_personal = ? AND hora_salida IS NULL)";
        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, codigo);
            return stmt.executeUpdate() > 0; // Devuelve true si se registró correctamente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Registrar salida de un trabajador
    public static boolean registrarSalida(String codigo) {
        String sql = "UPDATE registros_tickado SET hora_salida = NOW(), " +
                     "horas_trabajadas = TIMESTAMPDIFF(MINUTE, hora_entrada, NOW()) / 60.0 " +
                     "WHERE codigo_personal = ? AND hora_salida IS NULL";
        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            return stmt.executeUpdate() > 0; // Devuelve true si se actualizó correctamente
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener trabajadores actualmente en turno
    public static List<Trabajador> obtenerTrabajadoresEnTurno() {
        List<Trabajador> lista = new ArrayList<>();
        String sql = "SELECT t.id, t.nombre, t.codigo_personal, r.hora_entrada FROM trabajadores t " +
                     "JOIN registros_tickado r ON t.codigo_personal = r.codigo_personal " +
                     "WHERE r.hora_salida IS NULL";
        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Trabajador(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo_personal"),
                        rs.getString("hora_entrada")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
