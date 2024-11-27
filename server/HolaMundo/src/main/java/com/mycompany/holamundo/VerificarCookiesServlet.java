/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.holamundo;

/**
 *
 * @author jeff
 */
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// Define la URL del servlet
@WebServlet("/verificarCookies")
public class VerificarCookiesServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Maneja las solicitudes preflight (OPTIONS)
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuración de CORS
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        
        // Obtener cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String email = null, role = null, id = null;
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "email":
                        email = cookie.getValue();
                        break;
                    case "rol":
                        role = cookie.getValue();
                        break;
                    case "id":
                        id = cookie.getValue();
                        break;
                }
            }
            
            // Verifica si las cookies necesarias están presentes
            if (email != null && role != null && id != null) {
                // Respuesta en formato JSON
                sendResponse(response, true, "Autenticado", Map.of("email", email, "role", role, "id", id));
                return;
            }
        }

        // Respuesta en caso de que no haya autenticación
        sendResponse(response, false, "No autenticado", null);
    }

    private void sendResponse(HttpServletResponse response, boolean success, String message, Map<String, String> data) throws IOException {
        // Construye el objeto JSON
        StringBuilder jsonResponse = new StringBuilder();
        jsonResponse.append("{");
        jsonResponse.append("\"success\":").append(success).append(",");
        jsonResponse.append("\"message\":\"").append(message).append("\",");
        jsonResponse.append("\"data\":");
        if (data != null) {
            jsonResponse.append("{");
            for (Map.Entry<String, String> entry : data.entrySet()) {
                jsonResponse.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
            }
            jsonResponse.setLength(jsonResponse.length() - 1); // Elimina la última coma
            jsonResponse.append("}");
        } else {
            jsonResponse.append("null");
        }
        jsonResponse.append("}");

        // Escribe la respuesta
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse.toString());
        writer.flush();
    }
}

