package com.materialsystem.web;

import com.materialsystem.dao.EstoqueDAO;
import com.materialsystem.entity.Estoque;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class EstoqueServlet extends HttpServlet {

    private final EstoqueDAO dao = new EstoqueDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new Estoque(), "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = param(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Estoque e = new Estoque();
        e.setIdEstoque(parseIntOrZero(param(req, "NEstoqueId")));
        e.setLocalizacao(param(req, "NEstoqueLocalizacao"));
        e.setCapacidade(parseDoubleOrZero(param(req, "NEstoqueCapacidade")));

        try {
            switch (action) {
                case "insert":
                    validate(e);
                    dao.inserir(e);
                    status = "Estoque inserido com sucesso.";
                    if (e.getIdEstoque() > 0) { activeUpdate = true; activeRemove = true; }
                    break;

                case "find":
                    if (e.getIdEstoque() <= 0) status = "Informe o ID do estoque.";
                    else {
                        Estoque achado = dao.buscarPorId(e.getIdEstoque());
                        if (achado != null) {
                            e = achado;
                            status = "Registro localizado.";
                            activeUpdate = activeRemove = true;
                        } else status = "Não encontrado.";
                    }
                    break;

                case "update":
                    if (e.getIdEstoque() <= 0) status = "Informe o ID do estoque.";
                    else {
                        validate(e);
                        dao.atualizar(e);
                        status = "Atualizado com sucesso.";
                        activeUpdate = activeRemove = true;
                    }
                    break;

                case "remove":
                    if (e.getIdEstoque() <= 0) status = "Informe o ID do estoque.";
                    else {
                        dao.deletar(e.getIdEstoque());
                        status = "Excluído com sucesso.";
                        e = new Estoque(); // limpa form
                    }
                    break;

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true; break;

                case "help":
                    status = "Ajuda: cadastre locais de estoque com localização e capacidade. Use Inserir/Localizar/Atualizar/Excluir/Relatório.";
                    break;

                case "exit":
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;

                default:
                    status = "Ação não reconhecida.";
            }
        } catch (IllegalArgumentException ex) {
            status = "Validação: " + ex.getMessage();
        } catch (Exception ex) {
            status = "Erro: " + ex.getMessage();
        }

        mainWindow(req, resp, e, status, activeUpdate, activeRemove, showReport);
    }

    // ===== Helpers =====
    private static String param(HttpServletRequest r, String n) {
        String v = r.getParameter(n); return v == null ? "" : v.trim();
    }
    private static int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private static double parseDoubleOrZero(String s) {
        if (s == null || s.isBlank()) return 0.0;
        s = s.replace(",", ".");
        try { return Double.parseDouble(s); } catch (Exception e) { return 0.0; }
    }
    private static String safe(Object v) {
        if (v == null) return "";
        return String.valueOf(v).replace("\"","&quot;");
    }
    private static void validate(Estoque e) {
        if (e.getLocalizacao() == null || e.getLocalizacao().isBlank())
            throw new IllegalArgumentException("Localização é obrigatória.");
        if (e.getCapacidade() < 0.0)
            throw new IllegalArgumentException("Capacidade deve ser >= 0.");
    }

    // ===== Render HTML =====
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Estoque e, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Estoque</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Estoque - versão " + versionWebApp + "</h3><br>");

        // ID
        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(e.getIdEstoque()) + "' type='number' name='NEstoqueId' style='width:120px;' /></div>");

        // Localização
        out.println("<div class='block'><label>Localização:</label>");
        out.println("<input value='" + safe(e.getLocalizacao()) + "' type='text' name='NEstoqueLocalizacao' style='width:420px;' required /></div>");

        // Capacidade
        out.println("<div class='block'><label>Capacidade:</label>");
        out.println("<input value='" + safe(e.getCapacidade()) + "' type='number' step='0.01' min='0' name='NEstoqueCapacidade' style='width:160px;' required /></div>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/estoque' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

     // Relatório
        if (showReport) {
            List<Estoque> lista = dao.buscarTodos();
            out.println("<hr><h4>Lista de Estoques</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Localização</th><th>Capacidade</th></tr>");
            for (Estoque x : lista) {
                out.println("<tr>");
                out.println("<td>" + x.getIdEstoque() + "</td>");
                out.println("<td>" + safe(x.getLocalizacao()) + "</td>");
                out.println("<td>" + String.format(java.util.Locale.US, "%.2f", x.getCapacidade()) + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
