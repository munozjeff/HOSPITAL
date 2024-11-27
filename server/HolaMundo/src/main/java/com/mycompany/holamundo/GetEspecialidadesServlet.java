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
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

// Configura la ruta del servlet
@WebServlet("/get_especialidades")
public class GetEspecialidadesServlet extends HttpServlet {

    // Manejo de CORS para solicitudes preflight (OPTIONS)
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    // Método GET para manejar la lógica de obtención de datos
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Leer el parámetro `id` de la solicitud
        String idParam = request.getParameter("id");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Conexión a la base de datos
            conn = DatabaseConfig.connectDatabase();

            if (idParam != null) {
                // Si se pasa el parámetro `id`, obtener un único registro
                String sql = "SELECT * FROM especialidades WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(idParam));
                rs = stmt.executeQuery();

                if (rs.next()) {
                    JSONObject result = new JSONObject();
                    result.put("id", rs.getInt("id"));
                    result.put("nombre", rs.getString("nombre"));
                    // Agrega aquí otras columnas si es necesario

                    // Respuesta con un solo registro
                    sendResponse(true, "Registro encontrado", result, response);
                } else {
                    sendResponse(false, "Registro no encontrado", null, response);
                }
            } else {
                // Si no se pasa `id`, obtener todos los registros
                String sql = "SELECT * FROM especialidades";
                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();

                JSONArray results = new JSONArray();
                while (rs.next()) {
                    JSONObject record = new JSONObject();
                    record.put("id", rs.getInt("id"));
                    record.put("nombre", rs.getString("nombre"));
                    // Agrega aquí otras columnas si es necesario
                    results.put(record);
                }

                // Respuesta con todos los registros
                sendResponse(true, "Registros obtenidos correctamente", results, response);
            }
        } catch (SQLException e) {
            sendResponse(false, "Error al obtener los datos: " + e.getMessage(), null, response);
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método auxiliar para enviar respuestas JSON
    private void sendResponse(boolean success, String message, Object data, HttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        if (data != null) {
            jsonResponse.put("data", data);
        }
        response.getWriter().write(jsonResponse.toString());
    }
}

