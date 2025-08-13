package com.materialsystem.web;

import com.materialsystem.controller.LoginController;
import com.materialsystem.entity.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.*;

public class LoginServlet extends HttpServlet {
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";
    private final LoginController controller = new LoginController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderForm(req, resp, "", "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = p(req, "username");
        String senha    = p(req, "password");
        String ctx = req.getContextPath();

        Usuario usuario = controller.autenticarUsuario(username, senha);
        if (usuario != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("usuarioLogado", usuario);

            String next = p(req, "next");
            if (!next.isEmpty() && next.startsWith(ctx)) {
                resp.sendRedirect(next);
            } else {
                resp.sendRedirect(ctx + "/home");
            }
            return;
        }

        renderForm(req, resp, username, "Usuário ou senha inválidos.");
    }

    private void renderForm(HttpServletRequest req, HttpServletResponse resp, String usernamePrefill, String status) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Login</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self' action='" + ctx + "/login'>");
        out.println("<h3>Login - versão " + versionWebApp + "</h3><br>");

        String next = p(req, "next");
        if (!next.isEmpty()) out.println("<input type='hidden' name='next' value='" + safe(next) + "'/>");

        out.println("<div class='block'><label>Usuário:</label>");
        out.println("<input type='text' name='username' value='" + safe(usernamePrefill) + "' style='width:260px;' required/></div>");

        out.println("<div class='block'><label>Senha:</label>");
        out.println("<input type='password' name='password' style='width:260px;' required/></div>");

        out.println("<div class='block'>");
        out.println("<button type='submit' style='width:120px;'>Acessar</button>");
        out.println("<a href='" + ctx + "/welcome' style='margin-left:12px;'>Voltar</a>");
        out.println("</div>");

        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:420px;'/></div>");

        out.println("</form></body></html>");
    }

    private static String p(HttpServletRequest req, String name) {
        String v = req.getParameter(name); return v == null ? "" : v.trim();
    }
    private static String safe(Object v) {
        return v == null ? "" : String.valueOf(v).replace("\"","&quot;");
    }
}
