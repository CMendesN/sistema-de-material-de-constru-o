package com.materialsystem.web;

import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.dao.FabricanteDAO;
import com.materialsystem.entity.Produto;
import com.materialsystem.entity.Fabricante;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProdutoServlet extends HttpServlet {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final FabricanteDAO fabricanteDAO = new FabricanteDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        Produto p = new Produto();
        mainWindow(req, resp, p, "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = param(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Produto p = new Produto();
        p.setIdProduto(parseIntOrZero(param(req, "NProdutoId")));
        p.setNome(param(req, "NProdutoNome"));
        p.setDescricao(param(req, "NProdutoDescricao"));
        p.setPrecoUnitario(parseDoubleOrZero(param(req, "NProdutoPreco")));
        p.setQuantidadeEmEstoque(parseIntOrZero(param(req, "NProdutoQuantidade")));
        p.setIdFabricante(parseIntOrZero(param(req, "NProdutoFabricanteId")));
        p.setCategoria(param(req, "NProdutoCategoria"));

        try {
            switch (action) {
                case "insert":
                    validateProduto(p);
                    produtoDAO.inserir(p);
                    status = "Produto inserido com sucesso.";
                    if (p.getIdProduto() > 0) { activeUpdate = true; activeRemove = true; }
                    break;

                case "find":
                    if (p.getIdProduto() <= 0) status = "Informe o ID do produto.";
                    else {
                        Produto achado = produtoDAO.buscarPorId(p.getIdProduto());
                        if (achado != null) {
                            p = achado;
                            status = "Registro localizado.";
                            activeUpdate = activeRemove = true;
                        } else status = "Não encontrado.";
                    }
                    break;

                case "update":
                    if (p.getIdProduto() <= 0) status = "Informe o ID do produto.";
                    else {
                        validateProduto(p);
                        produtoDAO.atualizar(p);
                        status = "Atualizado com sucesso.";
                        activeUpdate = activeRemove = true;
                    }
                    break;

                case "remove":
                    if (p.getIdProduto() <= 0) status = "Informe o ID do produto.";
                    else {
                        produtoDAO.deletar(p.getIdProduto());
                        status = "Excluído com sucesso.";
                        p = new Produto(); // limpa form
                    }
                    break;

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true; break;

                case "help":
                    status = "Ajuda: preencha os campos e use Inserir/Localizar/Atualizar/Excluir/Relatório."; break;

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

        mainWindow(req, resp, p, status, activeUpdate, activeRemove, showReport);
    }

    // ==== Helpers ====
    private static String param(HttpServletRequest r, String n) {
        String v = r.getParameter(n); return v == null ? "" : v.trim();
    }
    private static int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private static double parseDoubleOrZero(String s) {
        if (s == null || s.isBlank()) return 0.0;
        s = s.replace(",", "."); // aceita vírgula
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }
    private static String safe(Object v) {
        if (v == null) return "";
        return String.valueOf(v).replace("\"","&quot;");
    }
    private void validateProduto(Produto p) {
        if (p.getNome() == null || p.getNome().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (p.getPrecoUnitario() < 0.0) throw new IllegalArgumentException("Preço deve ser >= 0.");
        if (p.getQuantidadeEmEstoque() < 0) throw new IllegalArgumentException("Quantidade deve ser >= 0.");
        if (p.getIdFabricante() <= 0) throw new IllegalArgumentException("Selecione um fabricante.");
        if (p.getCategoria() == null || p.getCategoria().isBlank()) throw new IllegalArgumentException("Categoria é obrigatória.");
    }

    // ==== Render HTML ====
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Produto p, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = req.getContextPath();
        List<Fabricante> fabricantes = fabricanteDAO.buscarTodos();

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Produto</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Produto - versão " + versionWebApp + "</h3><br>");

        // ID
        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(p.getIdProduto()) + "' type='number' name='NProdutoId' style='width:120px;'/></div>");

        // Nome
        out.println("<div class='block'><label>Nome:</label>");
        out.println("<input value='" + safe(p.getNome()) + "' type='text' name='NProdutoNome' style='width:420px;' required/></div>");

        // Descrição
        out.println("<div class='block'><label>Descrição:</label>");
        out.println("<input value='" + safe(p.getDescricao()) + "' type='text' name='NProdutoDescricao' style='width:600px;'/></div>");

        // Preço
        out.println("<div class='block'><label>Preço (R$):</label>");
        out.println("<input value='" + safe(p.getPrecoUnitario()) + "' type='number' step='0.01' min='0' name='NProdutoPreco' style='width:160px;' required/></div>");

        // Quantidade
        out.println("<div class='block'><label>Quantidade em estoque:</label>");
        out.println("<input value='" + safe(p.getQuantidadeEmEstoque()) + "' type='number' step='1' min='0' name='NProdutoQuantidade' style='width:120px;' required/></div>");

        // Categoria
        out.println("<div class='block'><label>Categoria:</label>");
        out.println("<input value='" + safe(p.getCategoria()) + "' type='text' name='NProdutoCategoria' style='width:300px;' required/></div>");

        // Fabricante (select)
        out.println("<div class='block'><label>Fabricante:</label>");
        out.println("<select name='NProdutoFabricanteId' style='width:420px;' required>");
        out.println("<option value=''>-- selecione --</option>");
        for (Fabricante f : fabricantes) {
            boolean sel = (p.getIdFabricante() == f.getIdFabricante());
            out.println("<option value='" + f.getIdFabricante() + "'" + (sel ? " selected" : "") + ">" +
                        safe(f.getNomeFabricante()) + "</option>");
        }
        out.println("</select></div>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;'/></div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produto' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        // Relatório (tabela)
        if (showReport) {
            List<Produto> lista = produtoDAO.buscarTodos();
            out.println("<hr><h4>Lista de Produtos</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Nome</th><th>Descrição</th><th>Preço</th><th>Qtd</th><th>Categoria</th><th>Fabricante</th></tr>");
            for (Produto x : lista) {
                String fabName = fabricantes.stream()
                        .filter(f -> f.getIdFabricante() == x.getIdFabricante())
                        .map(Fabricante::getNomeFabricante)
                        .findFirst().orElse("ID " + x.getIdFabricante());
                out.println("<tr>");
                out.println("<td>"+ x.getIdProduto() +"</td>");
                out.println("<td>"+ safe(x.getNome()) +"</td>");
                out.println("<td>"+ safe(x.getDescricao()) +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", x.getPrecoUnitario()) +"</td>");
                out.println("<td>"+ x.getQuantidadeEmEstoque() +"</td>");
                out.println("<td>"+ safe(x.getCategoria()) +"</td>");
                out.println("<td>"+ safe(fabName) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
