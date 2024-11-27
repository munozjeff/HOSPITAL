package com.mycompany.holamundo;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jeff
 */
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtén la subruta después de /api/
        String path = request.getPathInfo(); // Obtiene la subruta

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (path == null) {
            // Ruta base /api/
            response.getWriter().write("{\"message\":\"Ruta base del API\"}");
        } else if (path.equals("/verificarCookies")) {
            // Subruta /api/verificarCookies
            response.getWriter().write("{\"message\":\"Verificando cookies\"}");
        } else {
            // Ruta no encontrada
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Ruta no encontrada\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo(); // Obtiene la subruta

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (path != null && path.equals("/guardarDatos")) {
            String data = request.getReader().lines()
                                   .reduce("", (accumulator, actual) -> accumulator + actual);
            response.getWriter().write("{\"receivedData\":\"" + data + "\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"Ruta no encontrada para POST\"}");
        }
    }
}

