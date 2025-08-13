package com.materialsystem.web;

import com.materialsystem.dao.ProdutoEstoqueDAO;
import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.dao.EstoqueDAO;

import com.materialsystem.entity.ProdutoEstoque;
import com.materialsystem.entity.Produto;
import com.materialsystem.entity.Estoque;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ProdutoEstoqueServlet extends HttpServlet {

    private final ProdutoEstoqueDAO peDAO = new ProdutoEstoqueDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();

    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new ProdutoEstoque(), "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = p(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        ProdutoEstoque pe = new ProdutoEstoque();
        pe.setIdProduto(parseIntOrZero(p(req, "NPEProdutoId")));
        pe.setIdEstoque(parseIntOrZero(p(req, "NPEEstoqueId")));
        pe.setQuantidade(parseIntOrZero(p(req, "NPEQuantidade")));

        try {
            switch (action) {
                case "insert":
                    validate(pe, true);
                    if (peDAO.existeAssociacao(pe.getIdProduto(), pe.getIdEstoque())) {
                        status = "Associação já existe. Use Atualizar.";
                        activeUpdate = activeRemove = true;
                    } else {
                        peDAO.inserir(pe);
                        status = "Associação produto/estoque inserida.";
                        activeUpdate = activeRemove = true;
                    }
                    break;

                case "find": {
                    validateIds(pe);

                    final int produtoId = pe.getIdProduto();
                    final int estoqueId = pe.getIdEstoque();

                    List<ProdutoEstoque> lista = peDAO.buscarPorProduto(produtoId);
                    ProdutoEstoque encontrado = lista.stream()
                            .filter(x -> x.getIdEstoque() == estoqueId)
                            .findFirst()
                            .orElse(null);

                    if (encontrado != null) {
                        pe = encontrado;
                        status = "Associação localizada.";
                        activeUpdate = activeRemove = true;
                    } else {
                        status = "Associação não encontrada.";
                    }
                    break;
                }


                case "update":
                    validate(pe, false);
                    if (peDAO.existeAssociacao(pe.getIdProduto(), pe.getIdEstoque())) {
                        peDAO.atualizar(pe);
                        status = "Quantidade atualizada.";
                        activeUpdate = activeRemove = true;
                    } else {
                        status = "Associação inexistente. Use Inserir.";
                    }
                    break;

                case "remove":
                    validateIds(pe);
                    if (peDAO.existeAssociacao(pe.getIdProduto(), pe.getIdEstoque())) {
                        peDAO.remover(pe.getIdProduto(), pe.getIdEstoque());
                        status = "Associação removida.";
                        pe = new ProdutoEstoque();
                    } else {
                        status = "Associação inexistente.";
                    }
                    break;

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true;
                    break;

                case "help":
                    status = "Ajuda: selecione Produto + Estoque, informe Quantidade. Inserir cria a associação; Atualizar altera; Excluir remove. Relatório lista todas as associações.";
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

        mainWindow(req, resp, pe, status, activeUpdate, activeRemove, showReport);
    }

    /* ===== Helpers ===== */
    private static String p(HttpServletRequest r, String n) {
        String v = r.getParameter(n); return v == null ? "" : v.trim();
    }
    private static int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private static String safe(Object v) {
        return v == null ? "" : String.valueOf(v).replace("\"","&quot;");
    }
    private static void validateIds(ProdutoEstoque pe) {
        if (pe.getIdProduto() <= 0) throw new IllegalArgumentException("Selecione um produto.");
        if (pe.getIdEstoque() <= 0) throw new IllegalArgumentException("Selecione um estoque.");
    }
    private static void validate(ProdutoEstoque pe, boolean requireQty) {
        validateIds(pe);
        if (requireQty && pe.getQuantidade() <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0.");
        if (!requireQty && pe.getQuantidade() < 0) throw new IllegalArgumentException("Quantidade deve ser >= 0.");
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            ProdutoEstoque pe, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = req.getContextPath();
        List<Produto> produtos = produtoDAO.buscarTodos();
        List<Estoque> estoques = estoqueDAO.buscarTodos();

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Produto x Estoque</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Produto x Estoque - versão " + versionWebApp + "</h3><br>");

        // Produto
        out.println("<div class='block'><label>Produto:</label>");
        out.println("<select name='NPEProdutoId' style='width:420px;' required>");
        out.println("<option value=''>-- selecione --</option>");
        for (Produto pr : produtos) {
            boolean sel = pe.getIdProduto() == pr.getIdProduto();
            out.println("<option value='" + pr.getIdProduto() + "'" + (sel ? " selected" : "") + ">"
                    + safe(pr.getNome()) + "</option>");
        }
        out.println("</select></div>");

        // Estoque
        out.println("<div class='block'><label>Estoque (local):</label>");
        out.println("<select name='NPEEstoqueId' style='width:420px;' required>");
        out.println("<option value=''>-- selecione --</option>");
        for (Estoque es : estoques) {
            boolean sel = pe.getIdEstoque() == es.getIdEstoque();
            out.println("<option value='" + es.getIdEstoque() + "'" + (sel ? " selected" : "") + ">"
                    + safe(es.getLocalizacao()) + "</option>");
        }
        out.println("</select></div>");

        // Quantidade
        out.println("<div class='block'><label>Quantidade:</label>");
        out.println("<input value='" + (pe.getQuantidade() == 0 ? "" : pe.getQuantidade()) + "' type='number' min='0' step='1' name='NPEQuantidade' style='width:160px;' />");
        out.println("</div>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' />");
        out.println("</div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/produtoestoque' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        // Relatório
        if (showReport) {
            List<ProdutoEstoque> list = peDAO.buscarTodos();
            out.println("<hr><h4>Mapa Produto x Estoque</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID Produto</th><th>Produto</th><th>ID Estoque</th><th>Localização</th><th>Quantidade</th></tr>");
            for (ProdutoEstoque x : list) {
                String prodNome = produtos.stream()
                        .filter(pr -> pr.getIdProduto() == x.getIdProduto())
                        .map(Produto::getNome).findFirst().orElse("ID " + x.getIdProduto());
                String estLoc = estoques.stream()
                        .filter(es -> es.getIdEstoque() == x.getIdEstoque())
                        .map(Estoque::getLocalizacao).findFirst().orElse("ID " + x.getIdEstoque());
                out.println("<tr>");
                out.println("<td>"+ x.getIdProduto() +"</td>");
                out.println("<td>"+ safe(prodNome) +"</td>");
                out.println("<td>"+ x.getIdEstoque() +"</td>");
                out.println("<td>"+ safe(estLoc) +"</td>");
                out.println("<td>"+ x.getQuantidade() +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
