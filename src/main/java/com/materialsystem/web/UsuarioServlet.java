package com.materialsystem.web;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UsuarioServlet extends HttpServlet {

    private final UsuarioDAO dao = new UsuarioDAO();
    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    // papéis permitidos (ajuste conforme seu sistema)
    private static final String[] PAPEIS = new String[] { "Gerente", "Vendedor", "Comprador" };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, new Usuario(), "Pronto.", false, false, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = param(req, "action");
        String status = "Pronto.";
        boolean activeUpdate = false, activeRemove = false, showReport = false;

        Usuario u = new Usuario();
        u.setIdUsuario(parseIntOrZero(param(req, "NUsuarioId")));
        u.setNome(param(req, "NUsuarioNome"));
        u.setUsername(param(req, "NUsuarioUsername"));
        u.setSenha(param(req, "NUsuarioSenha")); // senha “pura” apenas na requisição
        u.setPapel(param(req, "NUsuarioPapel"));

        try {
            switch (action) {
                case "insert": {
                    validate(u, true); // exige senha
                    // checa username único
                    if (dao.buscarPorUsername(u.getUsername()) != null) {
                        status = "Username já existe. Escolha outro.";
                        break;
                    }
                    // hash da senha
                    String hash = PasswordUtils.gerarHashSenha(u.getSenha());
                    u.setSenha(hash);
                    dao.inserir(u);
                    status = "Usuário inserido com sucesso.";
                    // como o inserir atual não retorna ID, habilitamos update/remove apenas se localizar depois
                    break;
                }

                case "find": {
                    // busca por ID OU por username (se ID não informado)
                    if (u.getIdUsuario() > 0) {
                        Usuario achado = dao.buscarPorId(u.getIdUsuario());
                        if (achado != null) {
                            u = achado;
                            status = "Registro localizado por ID.";
                            activeUpdate = activeRemove = true;
                        } else status = "Não encontrado (ID).";
                    } else if (!u.getUsername().isBlank()) {
                        Usuario achado = dao.buscarPorUsername(u.getUsername());
                        if (achado != null) {
                            u = achado;
                            status = "Registro localizado por username.";
                            activeUpdate = activeRemove = true;
                        } else status = "Não encontrado (username).";
                    } else {
                        status = "Informe ID ou username para localizar.";
                    }
                    break;
                }

                case "update": {
                    if (u.getIdUsuario() <= 0) {
                        status = "Informe o ID do usuário para atualizar (localize antes).";
                        break;
                    }
                    // busca atual
                    Usuario atual = dao.buscarPorId(u.getIdUsuario());
                    if (atual == null) { status = "Usuário não encontrado."; break; }

                    // se username mudou e já existe em outro, bloqueia
                    if (!u.getUsername().isBlank() && !u.getUsername().equalsIgnoreCase(atual.getUsername())) {
                        Usuario outro = dao.buscarPorUsername(u.getUsername());
                        if (outro != null && outro.getIdUsuario() != atual.getIdUsuario()) {
                            status = "Username já utilizado por outro usuário.";
                            break;
                        }
                    }

                    // se senha foi informada, validar força + re-hash; se vazia, mantém a atual
                    boolean alterarSenha = u.getSenha() != null && !u.getSenha().isBlank();
                    if (alterarSenha) {
                        validate(u, true); // exige senha forte
                        String novoHash = PasswordUtils.gerarHashSenha(u.getSenha());
                        u.setSenha(novoHash);
                    } else {
                        validate(u, false); // valida campos sem exigir senha
                        u.setSenha(atual.getSenha()); // mantém hash atual
                    }

                    // se “nome” ou “papel”/“username” vierem vazios, mantém os atuais
                    if (u.getNome() == null || u.getNome().isBlank()) u.setNome(atual.getNome());
                    if (u.getUsername() == null || u.getUsername().isBlank()) u.setUsername(atual.getUsername());
                    if (u.getPapel() == null || u.getPapel().isBlank()) u.setPapel(atual.getPapel());

                    dao.atualizar(u);
                    status = "Atualizado com sucesso.";
                    activeUpdate = activeRemove = true;
                    break;
                }

                case "remove": {
                    if (u.getIdUsuario() <= 0) {
                        status = "Informe o ID do usuário para excluir.";
                    } else {
                        dao.deletar(u.getIdUsuario());
                        status = "Usuário excluído.";
                        u = new Usuario(); // limpa form
                    }
                    break;
                }

                case "report":
                    status = "Relatório gerado abaixo.";
                    showReport = true; break;

                case "help":
                    status = "Ajuda: informe Nome, Username, Papel e Senha (forte) para inserir. Localize por ID ou Username. Ao atualizar, a senha é opcional (deixe em branco para manter)."; break;

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

        mainWindow(req, resp, u, status, activeUpdate, activeRemove, showReport);
    }

    /* ===== Helpers ===== */
    private static String param(HttpServletRequest r, String n) {
        String v = r.getParameter(n); return v == null ? "" : v.trim();
    }
    private static int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private static String safe(Object v) {
        return v == null ? "" : String.valueOf(v).replace("\"","&quot;");
    }

    private void validate(Usuario u, boolean exigirSenha) {
        if (u.getNome() == null || u.getNome().isBlank())
            throw new IllegalArgumentException("Nome é obrigatório.");
        if (u.getUsername() == null || u.getUsername().isBlank())
            throw new IllegalArgumentException("Username é obrigatório.");
        if (u.getPapel() == null || u.getPapel().isBlank())
            throw new IllegalArgumentException("Papel é obrigatório.");
        if (exigirSenha) {
            if (u.getSenha() == null || u.getSenha().isBlank())
                throw new IllegalArgumentException("Senha é obrigatória.");
            List<String> erros = PasswordUtils.validarForcaSenha(u.getSenha());
            if (!erros.isEmpty())
                throw new IllegalArgumentException(String.join(" | ", erros));
        }
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            Usuario u, String status,
                            boolean activeUpdate, boolean activeRemove, boolean showReport)
            throws IOException {

        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Usuários</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Usuários - versão " + versionWebApp + "</h3><br>");

        // ID
        out.println("<div class='block'><label>ID:</label>");
        out.println("<input value='" + safe(u.getIdUsuario()) + "' type='number' name='NUsuarioId' style='width:120px;' /></div>");

        // Nome
        out.println("<div class='block'><label>Nome:</label>");
        out.println("<input value='" + safe(u.getNome()) + "' type='text' name='NUsuarioNome' style='width:420px;' required /></div>");

        // Username
        out.println("<div class='block'><label>Username:</label>");
        out.println("<input value='" + safe(u.getUsername()) + "' type='text' name='NUsuarioUsername' style='width:260px;' required /></div>");

        // Senha
        out.println("<div class='block'><label>Senha:</label>");
        out.println("<input value='' type='password' name='NUsuarioSenha' style='width:260px;' " + (u.getIdUsuario() > 0 ? "" : "required") + " />");
        out.println("<span style='font-size:12px;margin-left:8px;'>"
                + (u.getIdUsuario() > 0 ? "(em branco = manter atual)" : "mín 8 c/ maiúscula, minúscula, dígito e símbolo")
                + "</span></div>");

        // Papel
        out.println("<div class='block'><label>Papel:</label>");
        out.println("<select name='NUsuarioPapel' style='width:220px;' required>");
        out.println("<option value=''>-- selecione --</option>");
        for (String papel : PAPEIS) {
            boolean sel = papel.equalsIgnoreCase(u.getPapel());
            out.println("<option value='" + papel + "'" + (sel ? " selected" : "") + ">" + papel + "</option>");
        }
        out.println("</select></div>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='insert' style='width:110px;'>Inserir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='find' style='width:110px;' formnovalidate>Localizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='update' style='width:110px;' " + (activeUpdate? "" : "disabled") + ">Atualizar</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='remove' style='width:110px;' " + (activeRemove? "" : "disabled") + ">Excluir</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/usuario' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        // Relatório
        if (showReport) {
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
