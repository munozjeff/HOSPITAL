/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mycompany.holamundo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author colla
 */
@WebServlet("/citas")
public class CitasServlet extends HttpServlet {
    
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
        String pacienteId = request.getParameter("paciente_id");
        String medicoId = request.getParameter("medico_id");
        String estado = request.getParameter("estado");
        String fechaHora = request.getParameter("fecha_hora");

        try (Connection conn = DatabaseConfig.connectDatabase()) {
            String sql = "SELECT * FROM citas WHERE 1=1";
            StringBuilder whereClause = new StringBuilder();
            if (id != null) whereClause.append(" AND id = ?");
            if (pacienteId != null) whereClause.append(" AND paciente_id = ?");
            if (medicoId != null) whereClause.append(" AND medico_id = ?");
            if (estado != null) whereClause.append(" AND estado = ?");
            if (fechaHora != null) whereClause.append(" AND fecha_hora = ?");
            sql += whereClause;

            PreparedStatement stmt = conn.prepareStatement(sql);
            int index = 1;
            if (id != null) stmt.setString(index++, id);
            if (pacienteId != null) stmt.setString(index++, pacienteId);
            if (medicoId != null) stmt.setString(index++, medicoId);
            if (estado != null) stmt.setString(index++, estado);
            if (fechaHora != null) stmt.setString(index++, fechaHora);

            ResultSet rs = stmt.executeQuery();
            JSONArray result = new JSONArray();
            while (rs.next()) {
                JSONObject cita = new JSONObject();
                cita.put("id", rs.getInt("id"));
                cita.put("paciente_id", rs.getInt("paciente_id"));
                cita.put("medico_id", rs.getInt("medico_id"));
                cita.put("fecha_hora", rs.getString("fecha_hora"));
                cita.put("motivo", rs.getString("motivo"));
                cita.put("estado", rs.getString("estado"));
                result.put(cita);
            }
            sendResponse(true, "Citas encontradas", result, response);
        } catch (SQLException e) {
            sendResponse(false, "Error al obtener las citas: " + e.getMessage(), null, response);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleRequestWithBody("INSERT INTO citas (paciente_id, medico_id, fecha_hora, motivo, estado) VALUES (?, ?, ?, ?, ?)", request, response);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null) {
            sendResponse(false, "ID de la cita no proporcionado", null, response);
            return;
        }
        handleRequestWithBody("UPDATE citas SET paciente_id = ?, medico_id = ?, fecha_hora = ?, motivo = ?, estado = ? WHERE id = ?", request, response, id);
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String id = request.getParameter("id");
        if (id == null) {
            sendResponse(false, "ID de la cita no proporcionado", null, response);
            return;
        }

        try (Connection conn = DatabaseConfig.connectDatabase()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM citas WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(id));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse(true, "Cita eliminada exitosamente", null, response);
            } else {
                sendResponse(false, "No se encontró la cita a eliminar", null, response);
            }
        } catch (SQLException e) {
            sendResponse(false, "Error al eliminar la cita: " + e.getMessage(), null, response);
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
            stmt.setInt(1, data.optInt("paciente_id"));
            stmt.setInt(2, data.optInt("medico_id"));
            stmt.setString(3, data.optString("fecha_hora"));
            stmt.setString(4, data.optString("motivo"));
            stmt.setString(5, data.optString("estado", "pendiente"));
            if (extraParams.length > 0) {
                stmt.setInt(6, Integer.parseInt(extraParams[0]));
            }
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                sendResponse(true, "Operación exitosa", null, response);
            } else {
                sendResponse(false, "No se realizaron cambios", null, response);
            }
        } catch (SQLException e) {
            sendResponse(false, "Error durante la operación: " + e.getMessage(), null, response);
        }
    }

    private void sendResponse(boolean success, String message, Object data, HttpServletResponse response) throws IOException {
        JSONObject json = new JSONObject();
        json.put("success", success);
        json.put("message", message);
        if (data != null) {
            json.put("data", data);
        }
        response.getWriter().write(json.toString());
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CitasServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CitasServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
