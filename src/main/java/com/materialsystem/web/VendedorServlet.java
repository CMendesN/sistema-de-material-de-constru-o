package com.materialsystem.web;

import com.materialsystem.dao.VendedorDAO;
import com.materialsystem.entity.Vendedor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class VendedorServlet extends HttpServlet {

    private final VendedorDAO dao = new VendedorDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new Vendedor(), "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = param(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Vendedor v = new Vendedor();
        v.setIdVendedor(parseIntOrZero(param(req, "NVendedorId")));
        v.setNome(param(req, "NVendedorNome"));
        v.setCpf(param(req, "NVendedorCpf"));
        v.setContato(param(req, "NVendedorContato"));
        v.setSalario(parseDoubleOrZero(param(req, "NVendedorSalario")));
        v.setDataContratacao(parseDateOrNull(param(req, "NVendedorDataContratacao")));

        try {
            switch (action) {
                case "insert":
                    validate(v);
                    dao.inserir(v);
                    status = "Vendedor inserido com sucesso.";
                    if (v.getIdVendedor() > 0) { activeUpdate = true; activeRemove = true; }
                    break;

                case "find":
                    if (v.getIdVendedor() <= 0) status = "Informe o ID do vendedor.";
                    else {
                        Vendedor achado = dao.buscarPorId(v.getIdVendedor());
                        if (achado != null) {
                            v = achado;
                            status = "Registro localizado.";
                            activeUpdate = activeRemove = true;
                        } else status = "Não encontrado.";
                    }
                    break;

                case "update":
                    if (v.getIdVendedor() <= 0) status = "Informe o ID do vendedor.";
                    else {
                        validate(v);
                        dao.atualizar(v);
                        status = "Atualizado com sucesso.";
                        activeUpdate = activeRemove = true;
                    }
                    break;

                case "remove":
                    if (v.getIdVendedor() <= 0) status = "Informe o ID do vendedor.";
                    else {
                        dao.deletar(v.getIdVendedor());
                        status = "Excluído com sucesso.";
                        v = new Vendedor();
                    }
                    break;

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true;
                    break;

                case "help":
                    status = "Ajuda: preencha Nome, CPF, Salário e Data de Contratação. Use Inserir/Localizar/Atualizar/Excluir/Relatório.";
                    break;

                case "exit":
                    resp.sendRedirect(req.getContextPath() + "/index.html");
                    return;

                default:
                    status = "Ação não reconhecida.";
            }
        } catch (IllegalArgumentException e) {
            status = "Validação: " + e.getMessage();
        } catch (Exception e) {
            status = "Erro: " + e.getMessage();
        }

        mainWindow(req, resp, v, status, activeUpdate, activeRemove, showReport);
    }

    /* ===== Helpers ===== */
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
    private static LocalDate parseDateOrNull(String s) {
        try { return (s == null || s.isBlank()) ? null : LocalDate.parse(s); }
        catch (Exception e) { return null; }
    }
    private static String safe(Object v) {
        if (v == null) return "";
        return String.valueOf(v).replace("\"","&quot;");
    }
    private static void validate(Vendedor v) {
        if (v.getNome() == null || v.getNome().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (v.getCpf() == null || v.getCpf().isBlank()) throw new IllegalArgumentException("CPF é obrigatório.");
        if (v.getDataContratacao() == null) throw new IllegalArgumentException("Data de contratação é obrigatória.");
        if (v.getSalario() < 0.0) throw new IllegalArgumentException("Salário deve ser >= 0.");
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Vendedor v, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Vendedor</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Vendedor - versão " + versionWebApp + "</h3><br>");

        // ID
        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(v.getIdVendedor()) + "' type='number' name='NVendedorId' style='width:120px;' /></div>");

        // Nome
        out.println("<div class='block'><label>Nome:</label>");
        out.println("<input value='" + safe(v.getNome()) + "' type='text' name='NVendedorNome' style='width:420px;' required /></div>");

        // CPF
        out.println("<div class='block'><label>CPF:</label>");
        out.println("<input value='" + safe(v.getCpf()) + "' type='text' name='NVendedorCpf' "
                + "placeholder='___.___.___-__' style='width:200px;' required /></div>");

        // Contato
        out.println("<div class='block'><label>Contato:</label>");
        out.println("<input value='" + safe(v.getContato()) + "' type='text' name='NVendedorContato' style='width:420px;' /></div>");

        // Salário
        out.println("<div class='block'><label>Salário (R$):</label>");
        out.println("<input value='" + safe(v.getSalario()) + "' type='number' step='0.01' min='0' name='NVendedorSalario' style='width:160px;' required /></div>");

        // Data de contratação
        out.println("<div class='block'><label>Data de contratação:</label>");
        out.println("<input value='" + safe(v.getDataContratacao()) + "' type='date' name='NVendedorDataContratacao' style='width:180px;' required /></div>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/vendedor' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        // Relatório
        if (showReport) {
            List<Vendedor> lista = dao.buscarTodos();
            out.println("<hr><h4>Lista de Vendedores</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Nome</th><th>CPF</th><th>Contato</th><th>Salário</th><th>Data Contratação</th></tr>");
            for (Vendedor x : lista) {
                out.println("<tr>");
                out.println("<td>"+ x.getIdVendedor() +"</td>");
                out.println("<td>"+ safe(x.getNome()) +"</td>");
                out.println("<td>"+ safe(x.getCpf()) +"</td>");
                out.println("<td>"+ safe(x.getContato()) +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", x.getSalario()) +"</td>");
                out.println("<td>"+ safe(x.getDataContratacao()) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
