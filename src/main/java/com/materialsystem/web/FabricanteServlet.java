package com.materialsystem.web;

import com.materialsystem.dao.FabricanteDAO;
import com.materialsystem.entity.Fabricante;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FabricanteServlet extends HttpServlet {

    private final FabricanteDAO dao = new FabricanteDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        // Tela inicial vazia
        Fabricante fab = new Fabricante();
        mainWindow(request, response, fab, "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = param(request, "action");
        String status = "Pronto.";
        boolean activeUpdate = false;
        boolean activeRemove = false;
        boolean showReport = false;

        // Monta objeto a partir do form
        Fabricante f = new Fabricante();
        f.setIdFabricante(parseIntOrZero(param(request, "NFabricanteId")));
        f.setNomeFabricante(param(request, "NFabricanteNome"));
        f.setContato(param(request, "NFabricanteContato"));
        f.setEndereco(param(request, "NFabricanteEndereco"));

        try {
            switch (action) {
                case "insert":
                    validateRequired(f.getNomeFabricante(), "Nome do Fabricante é obrigatório.");
                    dao.inserir(f); // se você aplicar "gerated keys", o id volta preenchido
                    status = "Fabricante inserido com sucesso.";
                    // após inserir, libera update/remove se id agora > 0
                    if (f.getIdFabricante() > 0) { activeUpdate = true; activeRemove = true; }
                    break;

                case "find":
                    if (f.getIdFabricante() <= 0) {
                        status = "Informe o ID para localizar.";
                    } else {
                        Fabricante achado = dao.buscarPorId(f.getIdFabricante());
                        if (achado != null) {
                            f = achado;
                            status = "Registro localizado.";
                            activeUpdate = true;
                            activeRemove = true;
                        } else {
                            status = "Não encontrado.";
                        }
                    }
                    break;

                case "update":
                    if (f.getIdFabricante() <= 0) {
                        status = "Informe o ID para atualizar.";
                    } else {
                        validateRequired(f.getNomeFabricante(), "Nome do Fabricante é obrigatório.");
                        dao.atualizar(f);
                        status = "Atualizado com sucesso.";
                        activeUpdate = true;
                        activeRemove = true;
                    }
                    break;

                case "remove":
                    if (f.getIdFabricante() <= 0) {
                        status = "Informe o ID para excluir.";
                    } else {
                        dao.deletar(f.getIdFabricante());
                        status = "Excluído com sucesso.";
                        f = new Fabricante(); // limpa campos
                    }
                    break;

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true;
                    break;

                case "help":
                    status = "Ajuda: preencha os campos e use Inserir/Localizar/Atualizar/Excluir/Relatório.";
                    break;

                case "exit":
                    response.sendRedirect(request.getContextPath() + "/home");
                    return;

                default:
                    status = "Ação não reconhecida.";
            }

        } catch (IllegalArgumentException e) {
            status = "Erro de validação: " + e.getMessage();
        } catch (Exception e) {
            status = "Erro: " + e.getMessage();
        }

        mainWindow(request, response, f, status, activeUpdate, activeRemove, showReport);
    }

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    private static int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private static void validateRequired(String v, String msg) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(msg);
    }

    // ====== Render HTML (estilo do seu exemplo) ======
    private void mainWindow(HttpServletRequest request, HttpServletResponse response,
                            Fabricante fabricante, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = request.getContextPath(); // evita hardcode
        PrintWriter printer = response.getWriter();

        printer.println("<!DOCTYPE html>");
        printer.println("<html>");
        printer.println("<head>");
        printer.println("<meta charset=\"UTF-8\">");
        printer.println("<title>Web - Fabricante</title>");
        if (enableStyleCSS) printer.println("<link rel=\"stylesheet\" href=\"" + ctx + "/styleLogin.css\">");
        printer.println("</head>");
        printer.println("<body>");
        printer.println("<form method=\"post\" target=\"_self\">");
        printer.println("<h3>Fabricante - versão " + this.versionWebApp + "</h3>");
        printer.println("<br>");

        // ID
        printer.println("<div class=\"block\">");
        printer.println("<label>ID:</label>");
        printer.println("<input value=\"" + safe(fabricante.getIdFabricante())
                + "\" type=\"number\" min=\"0\" id=\"idFabId\" name=\"NFabricanteId\" placeholder=\"ID\" style=\"width: 120px;\" />");
        printer.println("</div>");

        // Nome
        printer.println("<div class=\"block\">");
        printer.println("<label>Nome do Fabricante:</label>");
        printer.println("<input value=\"" + safe(fabricante.getNomeFabricante())
                + "\" type=\"text\" id=\"idFabNome\" name=\"NFabricanteNome\" placeholder=\"Nome\" required style=\"width: 420px;\" />");
        printer.println("</div>");

        // Contato
        printer.println("<div class=\"block\">");
        printer.println("<label>Contato:</label>");
        printer.println("<input value=\"" + safe(fabricante.getContato())
                + "\" type=\"text\" id=\"idFabContato\" name=\"NFabricanteContato\" placeholder=\"Contato\" style=\"width: 420px;\" />");
        printer.println("</div>");

        // Endereço
        printer.println("<div class=\"block\">");
        printer.println("<label>Endereço:</label>");
        printer.println("<input value=\"" + safe(fabricante.getEndereco())
                + "\" type=\"text\" id=\"idFabEndereco\" name=\"NFabricanteEndereco\" placeholder=\"Endereço\" style=\"width: 600px;\" />");
        printer.println("</div>");

        // Status
        printer.println("<div class=\"block\">");
        printer.println("<label>Status:</label>");
        printer.println("<input value=\"" + safe(status)
                + "\" type=\"text\" readonly id=\"idBookStatus\" name=\"NBookStatus\" style=\"width: 600px;\" readonly/>");
        printer.println("</div>");

        // Botões
        printer.println("<div class=\"block\">");
        printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"insert\" style=\"width:110px;\">Inserir</button>");
        printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"find\" style=\"width:110px;\" formnovalidate>Localizar</button>");

        if (activeUpdate) {
            printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"update\" style=\"width:110px;\">Atualizar</button>");
        } else {
            printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"update\" style=\"width:110px;\" disabled>Atualizar</button>");
        }

        if (activeRemove) {
            printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"remove\" style=\"width:110px;\">Excluir</button>");
        } else {
            printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"remove\" style=\"width:110px;\" disabled>Excluir</button>");
        }

        printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"report\" style=\"width:110px;\" formnovalidate>Relatório</button>");
        printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"help\" style=\"width:110px;\" formnovalidate>Ajuda</button>");
        printer.println("<button type=\"submit\" formaction=\"" + ctx + "/fabricante\" name=\"action\" value=\"exit\" style=\"width:110px;\" formnovalidate>Sair</button>");
        printer.println("</div>");

        // Relatório (tabela) quando solicitado
        if (showReport) {
            List<Fabricante> lista = dao.buscarTodos();
            printer.println("<hr>");
            printer.println("<h4>Lista de Fabricantes</h4>");
            printer.println("<table border=\"1\" cellpadding=\"5\">");
            printer.println("<tr><th>ID</th><th>Nome</th><th>Contato</th><th>Endereço</th></tr>");
            for (Fabricante x : lista) {
                printer.println("<tr>");
                printer.println("<td>" + safe(x.getIdFabricante()) + "</td>");
                printer.println("<td>" + safe(x.getNomeFabricante()) + "</td>");
                printer.println("<td>" + safe(x.getContato()) + "</td>");
                printer.println("<td>" + safe(x.getEndereco()) + "</td>");
                printer.println("</tr>");
            }
            printer.println("</table>");
        }

        printer.println("</form>");
        printer.println("</body>");
        printer.println("</html>");
    }

    private static String safe(Object v) {
        if (v == null) return "";
        String s = String.valueOf(v);
        return s.replace("\"", "&quot;");
    }
}
