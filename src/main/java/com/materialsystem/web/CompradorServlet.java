package com.materialsystem.web;

import com.materialsystem.dao.CompradorDAO;
import com.materialsystem.entity.Comprador;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CompradorServlet extends HttpServlet {

    private final CompradorDAO dao = new CompradorDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new Comprador(), "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = param(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Comprador c = new Comprador();
        c.setIdComprador(parseIntOrZero(param(req, "NCompradorId")));
        c.setNome(param(req, "NCompradorNome"));
        c.setCpf(param(req, "NCompradorCpf"));
        c.setContato(param(req, "NCompradorContato"));
        c.setEndereco(param(req, "NCompradorEndereco"));

        try {
            switch (action) {
                case "insert":
                    validate(c);
                    dao.inserir(c);
                    status = "Comprador inserido com sucesso.";
                    if (c.getIdComprador() > 0) { activeUpdate = true; activeRemove = true; }
                    break;

                case "find":
                    if (c.getIdComprador() <= 0) status = "Informe o ID do comprador.";
                    else {
                        Comprador achado = dao.buscarPorId(c.getIdComprador());
                        if (achado != null) {
                            c = achado;
                            status = "Registro localizado.";
                            activeUpdate = activeRemove = true;
                        } else status = "Não encontrado.";
                    }
                    break;

                case "update":
                    if (c.getIdComprador() <= 0) status = "Informe o ID do comprador.";
                    else {
                        validate(c);
                        dao.atualizar(c);
                        status = "Atualizado com sucesso.";
                        activeUpdate = activeRemove = true;
                    }
                    break;

                case "remove":
                    if (c.getIdComprador() <= 0) status = "Informe o ID do comprador.";
                    else {
                        dao.deletar(c.getIdComprador());
                        status = "Excluído com sucesso.";
                        c = new Comprador(); // limpa form
                    }
                    break;

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true; break;

                case "help":
                    status = "Ajuda: preencha Nome e CPF (obrigatórios). Use Inserir/Localizar/Atualizar/Excluir/Relatório."; break;

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

        mainWindow(req, resp, c, status, activeUpdate, activeRemove, showReport);
    }

    /* ===== Helpers ===== */
    private static String param(HttpServletRequest r, String n) {
        String v = r.getParameter(n); return v == null ? "" : v.trim();
    }
    private static int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private static String safe(Object v) {
        if (v == null) return "";
        return String.valueOf(v).replace("\"","&quot;");
    }
    private static void validate(Comprador c) {
        if (c.getNome() == null || c.getNome().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (c.getCpf() == null || c.getCpf().isBlank()) throw new IllegalArgumentException("CPF é obrigatório.");
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Comprador c, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Comprador</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Comprador - versão " + versionWebApp + "</h3><br>");

        // ID
        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(c.getIdComprador()) + "' type='number' name='NCompradorId' style='width:120px;' /></div>");

        // Nome
        out.println("<div class='block'><label>Nome:</label>");
        out.println("<input value='" + safe(c.getNome()) + "' type='text' name='NCompradorNome' style='width:420px;' required /></div>");

        // CPF
        out.println("<div class='block'><label>CPF:</label>");
        out.println("<input value='" + safe(c.getCpf()) + "' type='text' name='NCompradorCpf' placeholder='___.___.___-__' style='width:200px;' required /></div>");

        // Contato
        out.println("<div class='block'><label>Contato:</label>");
        out.println("<input value='" + safe(c.getContato()) + "' type='text' name='NCompradorContato' style='width:420px;' /></div>");

        // Endereço
        out.println("<div class='block'><label>Endereço:</label>");
        out.println("<input value='" + safe(c.getEndereco()) + "' type='text' name='NCompradorEndereco' style='width:600px;' /></div>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/comprador' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        // Relatório
        if (showReport) {
            List<Comprador> lista = dao.buscarTodos();
            out.println("<hr><h4>Lista de Compradores</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Nome</th><th>CPF</th><th>Contato</th><th>Endereço</th></tr>");
            for (Comprador x : lista) {
                out.println("<tr>");
                out.println("<td>"+ x.getIdComprador() +"</td>");
                out.println("<td>"+ safe(x.getNome()) +"</td>");
                out.println("<td>"+ safe(x.getCpf()) +"</td>");
                out.println("<td>"+ safe(x.getContato()) +"</td>");
                out.println("<td>"+ safe(x.getEndereco()) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
																								