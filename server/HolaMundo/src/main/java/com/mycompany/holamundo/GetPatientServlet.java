/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.holamundo;

/**
 *
 * @author jeff
 */

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import org.json.JSONObject;
import org.json.JSONArray;
import com.mycompany.holamundo.DatabaseConfig; // Clase para manejar la conexión a la base de datos
import javax.servlet.annotation.WebServlet;

@WebServlet("/get_perfil_paciente")
public class GetPatientServlet extends HttpServlet {
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String idParam = request.getParameter("id");
        if (idParam == null || !idParam.matches("\\d+")) {
            sendResponse(false, "Invalid or missing ID parameter.", null, response);
            return;
        }

        int id = Integer.parseInt(idParam);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "SELECT " +
                     "CONCAT(u.nombre, ' ', u.apellido) AS NombreCompleto, " +
                     "u.email AS Email, " +
                     "p.fecha_nacimiento AS FechaNacimiento, " +
                     "p.direccion AS Direccion, " +
                     "p.telefono AS Telefono, " +
                     "p.genero AS Genero, " +
                     "COUNT(DISTINCT c.id) AS TotalCitas, " +
                     "COUNT(DISTINCT d.id) AS TotalDiagnosticos " +
                     "FROM pacientes p " +
                     "JOIN usuarios u ON p.usuario_id = u.id " +
                     "LEFT JOIN citas c ON p.id = c.paciente_id " +
                     "LEFT JOIN diagnosticos d ON d.cita_id = c.id " +
                     "WHERE p.usuario_id = ? " +
                     "GROUP BY p.id";

        try {
            conn = DatabaseConfig.connectDatabase();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                JSONObject result = new JSONObject();
                result.put("NombreCompleto", rs.getString("NombreCompleto"));
                result.put("Email", rs.getString("Email"));
                result.put("FechaNacimiento", rs.getString("FechaNacimiento"));
                result.put("Direccion", rs.getString("Direccion"));
                result.put("Telefono", rs.getString("Telefono"));
                result.put("Genero", rs.getString("Genero"));
                result.put("TotalCitas", rs.getInt("TotalCitas"));
                result.put("TotalDiagnosticos", rs.getInt("TotalDiagnosticos"));

                sendResponse(true, "Data retrieved successfully.", result, response);
            } else {
                sendResponse(false, "No data found for the specified ID.", null, response);
            }
        } catch (SQLException e) {
            sendResponse(false, "Query execution failed: " + e.getMessage(), null, response);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                // Manejo de errores al cerrar recursos
            }
        }
    }

    private void sendResponse(boolean success, String message, JSONObject data, HttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        if (data != null) jsonResponse.put("data", data);
        response.getWriter().write(jsonResponse.toString());
    }
}
