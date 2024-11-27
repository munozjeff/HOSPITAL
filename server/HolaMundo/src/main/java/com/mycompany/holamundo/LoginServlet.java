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
import com.mycompany.holamundo.DatabaseConfig; // Asegúrate de importar la clase DatabaseConfig
import javax.servlet.annotation.WebServlet;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Leer el cuerpo de la solicitud
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        JSONObject data = new JSONObject(sb.toString());
        String email = data.optString("email", "");
        String password = data.optString("password", "");
        
        if (email.isEmpty() || password.isEmpty()) {
            response(false, "Por favor, proporciona el email y la contraseña.", response);
            return;
        }

        // Conectar a la base de datos usando DatabaseConfig
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConfig.connectDatabase(); // Usamos el método de DatabaseConfig para obtener la conexión
            stmt = conn.prepareStatement("SELECT id, password, rol FROM usuarios WHERE email = ?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String rol = rs.getString("rol");
                int userId = rs.getInt("id");

                // Verificar la contraseña
                if (password.equals(storedPassword)) {
                    int primaryId = 0;
                    if ("paciente".equals(rol)) {
                        stmt = conn.prepareStatement("SELECT id FROM pacientes WHERE usuario_id = ?");
                        stmt.setInt(1, userId);
                        rs = stmt.executeQuery();
                        if (rs.next()) primaryId = rs.getInt("id");
                    } else if ("medico".equals(rol)) {
                        stmt = conn.prepareStatement("SELECT id FROM medicos WHERE usuario_id = ?");
                        stmt.setInt(1, userId);
                        rs = stmt.executeQuery();
                        if (rs.next()) primaryId = rs.getInt("id");
                    }

                    if (primaryId > 0) {
                        // Guardar los datos en la sesión
                        HttpSession session = request.getSession();
                        session.setAttribute("user_id", userId);
                        session.setAttribute("rol", rol);
                        session.setAttribute("primary_id", primaryId);

                        // Establecer las cookies

// Establecer las cookies
Cookie emailCookie = new Cookie("email", "ddd");
Cookie rolCookie = new Cookie("rol", "eded");
Cookie idCookie = new Cookie("id", "deee");

// Establecer la duración de las cookies (1 hora)
emailCookie.setMaxAge(3600);  // 1 hora
rolCookie.setMaxAge(3600);  // 1 hora
idCookie.setMaxAge(3600);  // 1 hora

// Evitar Secure en HTTP
emailCookie.setSecure(false);
rolCookie.setSecure(false);
idCookie.setSecure(false);

// Establecer dominio y ruta
emailCookie.setDomain("localhost");
rolCookie.setDomain("localhost");
idCookie.setDomain("localhost");

emailCookie.setPath("/");
rolCookie.setPath("/");
idCookie.setPath("/");

// Hacer que las cookies estén disponibles en todo el sitio
response.addCookie(emailCookie);
response.addCookie(rolCookie);
response.addCookie(idCookie);



                        // Responder con los datos de sesión
                        JSONObject jsonResponse = new JSONObject();
                        jsonResponse.put("success", true);
                        jsonResponse.put("message", "Inicio de sesión exitoso");
                        JSONObject sessionData = new JSONObject();
                        sessionData.put("rol", rol);
                        sessionData.put("id", primaryId);
                        sessionData.put("email", email);
                        jsonResponse.put("data", sessionData);

                        response.getWriter().write(jsonResponse.toString());
                    } else {
                        response(false, "No se encontró el registro del usuario en la tabla correspondiente.", response);
                    }
                } else {
                    response(false, "Credenciales incorrectas.", response);
                }
            } else {
                response(false, "Credenciales incorrectas.", response);
            }
        } catch (SQLException e) {
            response(false, "Error en la consulta: " + e.getMessage(), response);
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

    private void response(boolean success, String message, HttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);
        jsonResponse.put("message", message);
        response.getWriter().write(jsonResponse.toString());
    }
}

