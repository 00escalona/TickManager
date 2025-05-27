package dao;

import java.sql.*;

import resources.Database;

public class AdminDAO {

    //Verifica si las credenciales del administrador son correctas.
    public static boolean verificarCredenciales(String correo, String contraseña) {
        String sql = "SELECT * FROM administradores WHERE correo = ? AND contraseña = ?";
        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, correo);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next(); // Si hay resultados, las credenciales son correctas.
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Registra un nuevo administrador en la base de datos.
    public static boolean registrarAdministrador(String correo, String contraseña) {
        String sql = "INSERT INTO administradores (correo, contraseña) VALUES (?, ?)";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, correo);
            stmt.setString(2, contraseña);
            int filasAfectadas = stmt.executeUpdate();

            return filasAfectadas > 0; // Si al menos una fila fue afectada, el registro fue exitoso.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Confirma si el correo está registrado en la base de datos.
    public static boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM administradores WHERE correo = ?";
        
        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        	
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
