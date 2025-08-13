package com.materialsystem.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.*;

public class WelcomeServlet extends HttpServlet {
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Bem-vindo</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("<style> .center{display:flex;flex-direction:column;align-items:center;gap:16px;margin-top:50px} img{max-width:256px;height:auto} </style>");
        out.println("</head><body>");

        out.println("<div class='center'>");
        out.println("<img src='" + ctx + "/image/logo512.png' alt='logo'/>");
        out.println("<h2>MS Construção - Sistema de Materiais</h2>");
        out.println("<form method='get' action='" + ctx + "/login'><button type='submit' style='width:120px;'>Entrar</button></form>");
        out.println("</div>");

        out.println("</body></html>");
    }
}
