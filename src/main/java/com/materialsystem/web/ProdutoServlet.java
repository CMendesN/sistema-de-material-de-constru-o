package com.materialsystem.web;

import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.entity.Produto;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.Roles;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO dao = new ProdutoDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new Produto(), "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        Usuario u = (Usuario) req.getSession().getAttribute("usuarioLogado");
        String papel = u.getPapel();
        boolean canModify = Roles.is(papel, Roles.GERENTE); // só gerente pode inserir/atualizar/excluir

        String action = p(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Produto p = new Produto();
        p.setIdProduto(parseIntOrZero(p(req, "NProdId")));
        p.setNome(p(req, "NProdNome"));
        p.setDescricao(p(req, "NProdDescricao"));
        p.setPrecoUnitario(parseDoubleOrZero(p(req, "NProdPreco")));
        p.setQuantidadeEmEstoque(parseIntOrZero(p(req, "NProdQtd")));
        p.setIdFabricante(parseIntOrZero(p(req, "NProdFabricanteId")));
        p.setCategoria(p(req, "NProdCategoria"));

        try {
            switch (action) {
                case "insert":
                    if (!canModify) { status = "Sem permissão para inserir."; break; }
                    validate(p);
                    dao.inserir(p);
                    status = "Produto inserido.";
                    break;

                case "find":
                    if (p.getIdProduto() <= 0) status = "Informe o ID do produto.";
                    else {
                        Produto achado = dao.buscarPorId(p.getIdProduto());
                        if (achado != null) {
                            p = achado;
                            status = "Registro localizado.";
                            activeUpdate = activeRemove = canModify;
                        } else status = "Não encontrado.";
                    }
                    break;

                case "update":
                    if (!canModify) { status = "Sem permissão para atualizar."; break; }
                    if (p.getIdProduto() <= 0) status = "Informe o ID do produto.";
                    else {
                        validate(p);
                        dao.atualizar(p);
                        status = "Atualizado com sucesso.";
                        activeUpdate = activeRemove = true;
                    }
                    break;

                case "remove":
                    if (!canModify) { status = "Sem permissão para excluir."; break; }
                    if (p.getIdProduto() <= 0) status = "Informe o ID do produto.";
                    else {
                        dao.deletar(p.getIdProduto());
                        status = "Excluído com sucesso.";
                        p = new Produto();
                    }
                    break;

                case "report":
                    showReport = true;
                    status = "Relatório abaixo.";
                    break;

                case "help":
                    status = Roles.is(papel, Roles.GERENTE)
                            ? "Ajuda: Gerente pode inserir/atualizar/excluir; demais papéis só consultam."
                            : "Ajuda: Você pode consultar produtos e gerar relatório; edição é restrita ao Gerente.";
                    break;

                case "exit":
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;

                default:
                    status = "Ação não reconhecida.";
            }
        } catch (IllegalArgumentException e) {
            status = "Validação: " + e.getMessage();
        } catch (Exception e) {
            status = "Erro: " + e.getMessage();
        }

        mainWindow(req, resp, p, status, canModify && activeUpdate, canModify && activeRemove, showReport);
    }

    /* ===== Helpers ===== */
    private static String p(HttpServletRequest r, String n) { String v = r.getParameter(n); return v==null? "" : v.trim(); }
    private static int parseIntOrZero(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private static double parseDoubleOrZero(String s) {
        if (s==null || s.isBlank()) return 0.0;
        s = s.replace(',','.');
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }
    private static String safe(Object v) { return v==null? "" : String.valueOf(v).replace("\"","&quot;"); }
    private static void validate(Produto p) {
        if (p.getNome()==null || p.getNome().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (p.getPrecoUnitario() < 0) throw new IllegalArgumentException("Preço inválido.");
        if (p.getQuantidadeEmEstoque() < 0) throw new IllegalArgumentException("Quantidade inválida.");
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Produto p, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        Usuario u = (Usuario) req.getSession().getAttribute("usuarioLogado");
        String papel = u.getPapel();
        boolean canModify = Roles.is(papel, Roles.GERENTE);

        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Produtos</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Produtos - v" + versionWebApp + " | Logado: " + safe(u.getNome()) + " (" + safe(papel) + ")</h3><br>");

        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(p.getIdProduto()) + "' type='number' name='NProdId' style='width:120px;' /></div>");

        out.println("<div class='block'><label>Nome:</label>");
        out.println("<input value='" + safe(p.getNome()) + "' type='text' name='NProdNome' style='width:420px;' required /></div>");

        out.println("<div class='block'><label>Descrição:</label>");
        out.println("<input value='" + safe(p.getDescricao()) + "' type='text' name='NProdDescricao' style='width:600px;' /></div>");

        out.println("<div class='block'><label>Preço (R$):</label>");
        out.println("<input value='" + safe(p.getPrecoUnitario()) + "' type='number' step='0.01' min='0' name='NProdPreco' style='width:160px;' required /></div>");

        out.println("<div class='block'><label>Qtd Estoque:</label>");
        out.println("<input value='" + safe(p.getQuantidadeEmEstoque()) + "' type='number' step='1' min='0' name='NProdQtd' style='width:160px;' required /></div>");

        out.println("<div class='block'><label>ID Fabricante:</label>");
        out.println("<input value='" + safe(p.getIdFabricante()) + "' type='number' step='1' min='0' name='NProdFabricanteId' style='width:160px;' /></div>");

        out.println("<div class='block'><label>Categoria:</label>");
        out.println("<input value='" + safe(p.getCategoria()) + "' type='text' name='NProdCategoria' style='width:260px;' /></div>");

        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='insert' style='width:110px;' " + (canModify? "":"disabled") + ">Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        if (showReport) {
            List<Produto> lista = dao.buscarTodos();
            out.println("<hr><h4>Lista de Produtos</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Nome</th><th>Descrição</th><th>Preço</th><th>Qtd</th><th>Fabricante</th><th>Categoria</th></tr>");
            for (Produto x : lista) {
                out.println("<tr>");
                out.println("<td>"+ x.getIdProduto() +"</td>");
                out.println("<td>"+ safe(x.getNome()) +"</td>");
                out.println("<td>"+ safe(x.getDescricao()) +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", x.getPrecoUnitario()) +"</td>");
                out.println("<td>"+ x.getQuantidadeEmEstoque() +"</td>");
                out.println("<td>"+ x.getIdFabricante() +"</td>");
                out.println("<td>"+ safe(x.getCategoria()) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
