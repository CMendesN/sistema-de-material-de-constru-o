package com.materialsystem.web;

import com.materialsystem.entity.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.*;

public class HomeServlet extends HttpServlet {
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        HttpSession s = req.getSession(false);
        Usuario u = (s != null) ? (Usuario) s.getAttribute("usuarioLogado") : null;

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Home</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("<style>.grid{display:grid;grid-template-columns:repeat(3, minmax(160px,1fr));gap:12px;max-width:720px;margin:auto}</style>");
        out.println("</head><body>");

        out.println("<h3 style='text-align:center;'>Bem-vindo, " + safe(u != null ? u.getNome() : "") +
                " [" + safe(u != null ? u.getPapel() : "") + "] - v" + versionWebApp + "</h3><br>");

        out.println("<div class='grid'>");
        btn(out, ctx, "/fabricante", "Fabricantes");	
        btn(out, ctx, "/produto", "Produtos");
        btn(out, ctx, "/venda", "Vendas");
        btn(out, ctx, "/estoque", "Estoques");
        btn(out, ctx, "/produtoestoque", "Produto x Estoque");
        btn(out, ctx, "/vendedor", "Vendedores");
        btn(out, ctx, "/comprador", "Compradores");
        btn(out, ctx, "/usuario", "Usuários");
        btn(out, ctx, "/logout", "Sair");
        out.println("</div>");

        out.println("</body></html>");
    }

    private static void btn(PrintWriter out, String ctx, String path, String label){
        out.println("<form method='get' action='" + ctx + path + "'><button type='submit' style='width:180px;height:44px;'>" + label + "</button></form>");
    }
    private static String safe(Object v) {
        return v == null ? "" : String.valueOf(v).replace("\"","&quot;");
    }
}
