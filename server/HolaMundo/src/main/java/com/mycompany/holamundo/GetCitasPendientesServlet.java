/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.holamundo;

/**
 *
 * @author jeff
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "GetCitasPendientesServlet", urlPatterns = {"/get_citas_futuras"})
public class GetCitasPendientesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String id = request.getParameter("id");

        // Validación del parámetro 'id'
        if (id == null || id.isEmpty()) {
            try (PrintWriter out = response.getWriter()) {
                out.println("{\"success\":false, \"message\":\"El parámetro 'id' es requerido\"}");
                return;
            }
        }

        String sql = "SELECT "
                   + "c.id AS id, "
                   + "c.fecha_hora AS FechaHoraCita, "
                   + "e.nombre AS Motivo, "
                   + "CONCAT(u.nombre, ' ', u.apellido) AS NombreMedico, "
                   + "esp.nombre AS Especialidad "
                   + "FROM citas c "
                   + "JOIN pacientes p ON c.paciente_id = p.id "
                   + "JOIN medicos m ON c.medico_id = m.id "
                   + "JOIN usuarios u ON m.usuario_id = u.id "
                   + "JOIN especialidades esp ON m.especialidad_id = esp.id "
                   + "LEFT JOIN especialidades e ON c.motivo = e.id "
                   + "WHERE p.usuario_id = ? "
                   + "AND c.estado = 'pendiente' "
                   + "AND c.fecha_hora > NOW() "
                   + "ORDER BY c.fecha_hora ASC";

        try (Connection conn = DatabaseConfig.connectDatabase();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\"success\":true, \"data\":[");

            while (rs.next()) {
                if (jsonBuilder.charAt(jsonBuilder.length() - 1) != '[') {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("{")
                           .append("\"id\":").append(rs.getInt("id")).append(",")
                           .append("\"FechaHoraCita\":\"").append(rs.getString("FechaHoraCita")).append("\",")
                           .append("\"Motivo\":\"").append(rs.getString("Motivo") != null ? rs.getString("Motivo") : "Sin especificar").append("\",")
                           .append("\"NombreMedico\":\"").append(rs.getString("NombreMedico")).append("\",")
                           .append("\"Especialidad\":\"").append(rs.getString("Especialidad")).append("\"")
                           .append("}");
            }
            jsonBuilder.append("]}");

            try (PrintWriter out = response.getWriter()) {
                out.println(jsonBuilder.toString());
            }
        } catch (Exception e) {
            try (PrintWriter out = response.getWriter()) {
                out.println("{\"success\":false, \"message\":\"Error al ejecutar la consulta: " + e.getMessage() + "\"}");
            }
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuración de CORS para preflight requests
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}

