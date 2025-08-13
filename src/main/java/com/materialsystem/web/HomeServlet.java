package com.materialsystem.web;

import com.materialsystem.entity.Usuario;
import com.materialsystem.util.Roles;
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

        Usuario u = (Usuario) req.getSession().getAttribute("usuarioLogado");
        String papel = u.getPapel();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Home</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("<style>.col{display:flex;flex-direction:column;gap:10px;max-width:380px;margin:auto}</style>");
        out.println("</head><body>");

        out.println("<h3 style='text-align:center;'>Bem-vindo, " + safe(u.getNome()) +
                " (" + safe(papel) + ") - v" + versionWebApp + "</h3><br>");

        out.println("<div class='col'>");

        if (Roles.is(papel, Roles.CAIXA)) {
            btn(out, ctx, "/venda", "Registrar Venda");
            btn(out, ctx, "/produto", "Consultar Produtos");
        } else if (Roles.is(papel, Roles.COMPRADOR)) {
            btn(out, ctx, "/produto", "Consultar Produtos");
            btn(out, ctx, "/venda",   "Consultar Minhas Compras");
        } else if (Roles.is(papel, Roles.VENDEDOR)) {
            btn(out, ctx, "/comprador", "Gerenciar Compradores");
            btn(out, ctx, "/venda",     "Gerenciar Vendas");
            btn(out, ctx, "/usuario",   "Cadastrar Comprador (Usuários)");
            btn(out, ctx, "/produto",   "Consultar Produtos");
        } else if (Roles.is(papel, Roles.GERENTE)) {
            btn(out, ctx, "/produto",        "Gerenciar Produtos");
            btn(out, ctx, "/fabricante",     "Gerenciar Fabricantes");
            btn(out, ctx, "/estoque",        "Gerenciar Estoques");
            btn(out, ctx, "/produtoestoque", "Gerenciar Produto-Estoque");
            btn(out, ctx, "/vendedor",       "Gerenciar Vendedores");
            btn(out, ctx, "/comprador",      "Gerenciar Compradores");
            btn(out, ctx, "/venda",          "Gerenciar Vendas");
            btn(out, ctx, "/usuario",        "Cadastrar Novo Usuário");
        }

        btn(out, ctx, "/logout", "Sair");
        out.println("</div></body></html>");
    }

    private static void btn(PrintWriter out, String ctx, String path, String label){
        out.println("<form method='get' action='" + ctx + path + "'><button type='submit' style='width:280px;height:44px;'>" + label + "</button></form>");
    }
    private static String safe(Object v) {
        return v == null ? "" : String.valueOf(v).replace("\"","&quot;");
    }
}
