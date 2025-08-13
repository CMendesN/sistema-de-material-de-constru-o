package com.materialsystem.web;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.PasswordUtils;
import com.materialsystem.util.Roles;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UsuarioServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";
    private static final String[] PAPEIS = new String[] { Roles.GERENTE, Roles.VENDEDOR, Roles.COMPRADOR, Roles.CAIXA };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario atual = (Usuario) req.getSession().getAttribute("usuarioLogado");
        if (!Roles.any(atual.getPapel(), Roles.GERENTE, Roles.VENDEDOR)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
            return;
        }
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new Usuario(), "Pronto.", false, false, false, atual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Usuario atual = (Usuario) req.getSession().getAttribute("usuarioLogado");
        if (!Roles.any(atual.getPapel(), Roles.GERENTE, Roles.VENDEDOR)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado.");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = p(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Usuario u = new Usuario();
        u.setIdUsuario(parseIntOrZero(p(req, "NUsuarioId")));
        u.setNome(p(req, "NUsuarioNome"));
        u.setUsername(p(req, "NUsuarioUsername"));
        u.setSenha(p(req, "NUsuarioSenha"));
        u.setPapel(p(req, "NUsuarioPapel"));

        try {
            if (Roles.is(atual.getPapel(), Roles.VENDEDOR)) {
                // Vendedor só pode INSERIR COMPRADOR. Demais ações bloqueadas.
                switch (action) {
                    case "insert": {
                        if (!Roles.is(u.getPapel(), Roles.COMPRADOR)) {
                            status = "Vendedor só pode criar usuário do papel 'Comprador'.";
                            break;
                        }
                        validate(u, true);
                        if (dao.buscarPorUsername(u.getUsername()) != null) {
                            status = "Username já existe.";
                            break;
                        }
                        u.setSenha(PasswordUtils.gerarHashSenha(u.getSenha()));
                        dao.inserir(u);
                        status = "Comprador criado com sucesso.";
                        break;
                    }
                    case "help":
                        status = "Vendedor: apenas pode cadastrar usuário com papel 'Comprador'.";
                        break;
                    case "exit":
                        resp.sendRedirect(req.getContextPath() + "/home");
                        return;
                    default:
                        status = "Permissão negada para esta ação.";
                }
            } else { // GERENTE
                switch (action) {
                    case "insert": {
                        validate(u, true);
                        if (dao.buscarPorUsername(u.getUsername()) != null) {
                            status = "Username já existe. Escolha outro."; break;
                        }
                        u.setSenha(PasswordUtils.gerarHashSenha(u.getSenha()));
                        dao.inserir(u);
                        status = "Usuário inserido com sucesso.";
                        break;
                    }
                    case "find": {
                        if (u.getIdUsuario() > 0) {
                            Usuario achado = dao.buscarPorId(u.getIdUsuario());
                            if (achado != null) {
                                u = achado; status = "Registro localizado por ID.";
                                activeUpdate = activeRemove = true;
                            } else status = "Não encontrado (ID).";
                        } else if (!u.getUsername().isBlank()) {
                            Usuario achado = dao.buscarPorUsername(u.getUsername());
                            if (achado != null) {
                                u = achado; status = "Registro localizado por username.";
                                activeUpdate = activeRemove = true;
                            } else status = "Não encontrado (username).";
                        } else status = "Informe ID ou username para localizar.";
                        break;
                    }
                    case "update": {
                        if (u.getIdUsuario() <= 0) { status = "Informe o ID do usuário."; break; }
                        Usuario atualDb = dao.buscarPorId(u.getIdUsuario());
                        if (atualDb == null) { status = "Usuário não encontrado."; break; }

                        if (!u.getUsername().isBlank() && !u.getUsername().equalsIgnoreCase(atualDb.getUsername())) {
                            Usuario outro = dao.buscarPorUsername(u.getUsername());
                            if (outro != null && outro.getIdUsuario() != u.getIdUsuario()) {
                                status = "Username já utilizado por outro usuário."; break;
                            }
                        }

                        boolean alterarSenha = u.getSenha() != null && !u.getSenha().isBlank();
                        if (alterarSenha) {
                            validate(u, true);
                            u.setSenha(PasswordUtils.gerarHashSenha(u.getSenha()));
                        } else {
                            validate(u, false);
                            u.setSenha(atualDb.getSenha());
                        }

                        if (u.getNome() == null || u.getNome().isBlank()) u.setNome(atualDb.getNome());
                        if (u.getUsername() == null || u.getUsername().isBlank()) u.setUsername(atualDb.getUsername());
                        if (u.getPapel() == null || u.getPapel().isBlank()) u.setPapel(atualDb.getPapel());

                        dao.atualizar(u);
                        status = "Atualizado com sucesso.";
                        activeUpdate = activeRemove = true;
                        break;
                    }
                    case "remove": {
                        if (u.getIdUsuario() <= 0) status = "Informe o ID do usuário.";
                        else { dao.deletar(u.getIdUsuario()); status = "Usuário excluído."; u = new Usuario(); }
                        break;
                    }
                    case "report":
                        status = "Relatório abaixo."; showReport = true; break;
                    case "help":
                        status = "Ajuda: Gerente possui CRUD completo."; break;
                    case "exit":
                        resp.sendRedirect(req.getContextPath() + "/home"); return;
                    default:
                        status = "Ação não reconhecida.";
                }
            }
        } catch (IllegalArgumentException e) {
            status = "Validação: " + e.getMessage();
        } catch (Exception e) {
            status = "Erro: " + e.getMessage();
        }

        mainWindow(req, resp, u, status, activeUpdate, activeRemove, showReport, atual);
    }

    /* ===== Helpers ===== */
    private static String p(HttpServletRequest r, String n) { String v = r.getParameter(n); return v == null ? "" : v.trim(); }
    private static int parseIntOrZero(String s) { try { return Integer.parseInt(s); } catch (Exception e) { return 0; } }
    private static String safe(Object v) { return v == null ? "" : String.valueOf(v).replace("\"","&quot;"); }

    private void validate(Usuario u, boolean exigirSenha) {
        if (u.getNome() == null || u.getNome().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        if (u.getUsername() == null || u.getUsername().isBlank()) throw new IllegalArgumentException("Username é obrigatório.");
        if (u.getPapel() == null || u.getPapel().isBlank()) throw new IllegalArgumentException("Papel é obrigatório.");
        if (exigirSenha) {
            if (u.getSenha() == null || u.getSenha().isBlank()) throw new IllegalArgumentException("Senha é obrigatória.");
            List<String> erros = com.materialsystem.util.PasswordUtils.validarForcaSenha(u.getSenha());
            if (!erros.isEmpty()) throw new IllegalArgumentException(String.join(" | ", erros));
        }
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Usuario u, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport,
                            Usuario atual) throws IOException {

        String ctx = req.getContextPath();
        String papelAtual = atual.getPapel();
        boolean vendedor = Roles.is(papelAtual, Roles.VENDEDOR);
        boolean gerente  = Roles.is(papelAtual, Roles.GERENTE);

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Usuários</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Usuários - v" + versionWebApp + " | Logado: " + safe(atual.getNome()) + " (" + safe(papelAtual) + ")</h3><br>");

        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(u.getIdUsuario()) + "' type='number' name='NUsuarioId' style='width:120px;' " + (vendedor ? "disabled" : "") + " /></div>");

        out.println("<div class='block'><label>Nome:</label>");
        out.println("<input value='" + safe(u.getNome()) + "' type='text' name='NUsuarioNome' style='width:420px;' required /></div>");

        out.println("<div class='block'><label>Username:</label>");
        out.println("<input value='" + safe(u.getUsername()) + "' type='text' name='NUsuarioUsername' style='width:260px;' required /></div>");

        out.println("<div class='block'><label>Senha:</label>");
        out.println("<input value='' type='password' name='NUsuarioSenha' style='width:260px;' " + (u.getIdUsuario() > 0 ? "" : "required") + " />");
        out.println("<span style='font-size:12px;margin-left:8px;'>" + (u.getIdUsuario() > 0 ? "(em branco = manter)" : "mín 8 c/ maiúscula, minúscula, dígito e símbolo") + "</span></div>");

        out.println("<div class='block'><label>Papel:</label>");
        out.println("<select name='NUsuarioPapel' style='width:220px;' required " + (vendedor ? "" : "") + ">");
        out.println("<option value=''>-- selecione --</option>");
        for (String papel : PAPEIS) {
            if (vendedor && !Roles.is(papel, Roles.COMPRADOR)) continue; // vendedor só pode selecionar 'Comprador'
            boolean sel = papel.equalsIgnoreCase(u.getPapel());
            out.println("<option value='" + papel + "'" + (sel ? " selected" : "") + ">" + papel + "</option>");
        }
        out.println("</select></div>");

        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='find' style='width:110px;' formnovalidate " + (gerente ? "" : "disabled") + ">Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='update' style='width:110px;' " + (gerente && activeUpdate ? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='remove' style='width:110px;' " + (gerente && activeRemove ? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='report' style='width:110px;' formnovalidate " + (gerente ? "" : "disabled") + ">Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        if (gerente && showReport) {
            List<Usuario> lista = dao.buscarTodos();
            out.println("<hr><h4>Lista de Usuários</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Nome</th><th>Username</th><th>Papel</th></tr>");
            for (Usuario x : lista) {
                out.println("<tr>");
                out.println("<td>"+ x.getIdUsuario() +"</td>");
                out.println("<td>"+ safe(x.getNome()) +"</td>");
                out.println("<td>"+ safe(x.getUsername()) +"</td>");
                out.println("<td>"+ safe(x.getPapel()) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        out.println("</form></body></html>");
    }
}
