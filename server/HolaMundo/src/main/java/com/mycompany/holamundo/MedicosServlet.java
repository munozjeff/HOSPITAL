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

@WebServlet("/medicos")
public class MedicosServlet extends HttpServlet {

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String id = request.getParameter("id");
        String usuarioId = request.getParameter("usuario_id");
        String especialidadId = request.getParameter("especialidad_id");
        String fechaContratacion = request.getParameter("fecha_contratacion");

        try (Connection conn = DatabaseConfig.connectDatabase()) {
            String sql = "SELECT m.*, CONCAT(u.nombre, ' ', u.apellido) AS nombre_usuario " +
                         "FROM medicos m " +
                         "JOIN usuarios u ON m.usuario_id = u.id " +
                         "WHERE 1 = 1";

            StringBuilder whereClause = new StringBuilder();
            if (id != null) whereClause.append(" AND m.id = ? ");
            if (usuarioId != null) whereClause.append(" AND m.usuario_id = ? ");
            if (especialidadId != null) whereClause.append(" AND m.especialidad_id = ? ");
            if (fechaContratacion != null) whereClause.append(" AND m.fecha_contratacion = ? ");
            sql += whereClause;

            PreparedStatement stmt = conn.prepareStatement(sql);
            int index = 1;
            if (id != null) stmt.setString(index++, id);
            if (usuarioId != null) stmt.setString(index++, usuarioId);
            if (especialidadId != null) stmt.setString(index++, especialidadId);
            if (fechaContratacion != null) stmt.setString(index++, fechaContratacion);

            ResultSet rs = stmt.executeQuery();
            JSONArray result = new JSONArray();
            while (rs.next()) {
                JSONObject medico = new JSONObject();
                medico.put("id", rs.getInt("id"));
                medico.put("usuario_id", rs.getInt("usuario_id"));
                medico.put("telefono", rs.getString("telefono"));
                medico.put("fecha_contratacion", rs.getString("fecha_contratacion"));
                medico.put("especialidad_id", rs.getInt("especialidad_id"));
                medico.put("nombre_usuario", rs.getString("nombre_usuario"));
                result.put(medico);
            }
            sendResponse(true, "Médicos encontrados", result, response);
        } catch (SQLException e) {
            sendResponse(false, "Error al obtener los datos: " + e.getMessage(), null, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequestWithBody("INSERT INTO medicos (usuario_id, telefono, fecha_contratacion, especialidad_id) VALUES (?, ?, ?, ?)", request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null) {
            sendResponse(false, "ID del médico no proporcionado", null, response);
            return;
        }
        handleRequestWithBody("UPDATE medicos SET usuario_id = ?, telefono = ?, fecha_contratacion = ?, especialidad_id = ? WHERE id = ?", request, response, id);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String id = request.getParameter("id");
        if (id == null) {
            sendResponse(false, "ID del médico no proporcionado", null, response);
            return;
        }

        try (Connection conn = DatabaseConfig.connectDatabase()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM medicos WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(true, "Médico eliminado exitosamente", null, response);
            } else {
                sendResponse(false, "No se encontró el médico a eliminar", null, response);
            }
        } catch (SQLException e) {
            sendResponse(false, "Error al eliminar el médico: " + e.getMessage(), null, response);
        }
    }

    private void handleRequestWithBody(String sql, HttpServletRequest request, HttpServletResponse response, String... extraParams) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        try (Connection conn = DatabaseConfig.connectDatabase()) {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject data = new JSONObject(sb.toString());

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, data.optInt("usuario_id"));
            stmt.setString(2, data.optString("telefono"));
            stmt.setString(3, data.optString("fecha_contratacion"));
            stmt.setInt(4, data.optInt("especialidad_id"));
            if (extraParams.length > 0) {
                stmt.setInt(5, Integer.parseInt(extraParams[0]));
            }
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                sendResponse(true, "Operación exitosa", null, response);
            } else {
                sendResponse(false, "No se realizaron cambios", null, response);
            }
        } catch (SQLException e) {
            sendResponse(false, "Error en la operación: " + e.getMessage(), null, response);
        }
    }

    private void sendResponse(boolean success, String message, Object data, HttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        if (data != null) jsonResponse.put("data", data);
        response.getWriter().write(jsonResponse.toString());
    }
}

