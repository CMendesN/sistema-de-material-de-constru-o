package com.materialsystem.web;

import com.materialsystem.entity.Usuario;
import com.materialsystem.entity.Venda;
import com.materialsystem.entity.ItemVenda;
import com.materialsystem.dao.VendaDAO;
import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.entity.Produto;
import com.materialsystem.util.Roles;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class VendaServlet extends HttpServlet {

    private final VendaDAO vendaDAO = new VendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	resp.setContentType("text/html; charset=UTF-8");
        mainWindow(req, resp, "Pronto.", false, false, false,
                   null, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        Usuario user = (Usuario) req.getSession().getAttribute("usuarioLogado");
        String papel = user.getPapel();
        boolean isGerente  = Roles.is(papel, Roles.GERENTE);
        boolean isVendedor = Roles.is(papel, Roles.VENDEDOR);
        boolean isCaixa    = Roles.is(papel, Roles.CAIXA);
        boolean isComprador= Roles.is(papel, Roles.COMPRADOR);

        // Permissões por ação
        boolean canInsert = isGerente || isVendedor || isCaixa;           // Comprador não insere
        boolean canRemove = isGerente || isVendedor;                       // Caixa/Comprador não removem
        boolean canReportAll = isGerente || isVendedor || isCaixa;         // Comprador vê só dele

        String action = p(req, "action");
        String status = "Pronto.";
        boolean showReport = false;
        boolean showItems  = false;
        Integer vendaIdToShow = null;
        List<Venda> vendas = Collections.emptyList();
        List<ItemVenda> itens = Collections.emptyList();

        try {
            switch (action) {
                case "insert": {
                    if (!canInsert) { status = "Sem permissão para registrar vendas."; break; }

                    // Campos básicos
                    Integer idVendedor = parseIntOrNull(p(req,"NVendaVendedorId"));
                    Integer idComprador = parseIntOrNull(p(req,"NVendaCompradorId"));

                    // Comprador logado: força o id_comprador = id do usuário (assumindo mapeamento 1:1)
                    if (isComprador) idComprador = user.getIdUsuario();

                    // Itens (arrays)
                    String[] sProd = req.getParameterValues("NItemProdutoId");
                    String[] sQtd  = req.getParameterValues("NItemQuantidade");
                    String[] sPr   = req.getParameterValues("NItemPrecoUnit");

                    if (sProd == null || sQtd == null || sPr == null) {
                        status = "Informe ao menos um item."; break;
                    }

                    List<ItemVenda> list = new ArrayList<>();
                    double total = 0.0;
                    for (int i=0; i<sProd.length; i++) {
                        Integer pid = parseIntOrNull(sProd[i]);
                        Integer q   = parseIntOrNull(sQtd[i]);
                        Double  pr  = parseDoubleOrNull(sPr[i]);
                        if (pid == null || q == null || q <= 0 || pr == null || pr < 0) continue;
                        ItemVenda it = new ItemVenda(0, 0, pid, q, pr);
                        list.add(it);
                        total += q * pr;
                    }
                    if (list.isEmpty()) { status = "Itens inválidos."; break; }

                    Venda v = new Venda(0, LocalDateTime.now(), idVendedor, idComprador, total);
                    boolean ok = vendaDAO.registrarVendaComItens(v, list);
                    status = ok ? "Venda registrada com sucesso." : "Erro ao registrar a venda.";
                    break;
                }
                case "report": {
                    showReport = true;
                    if (isComprador) {
                        // Comprador: só as próprias
                        vendas = vendaDAO.buscarPorComprador(user.getIdUsuario());
                        status = "Listando suas compras.";
                    } else if (canReportAll) {
                        vendas = vendaDAO.buscarTodas();
                        status = "Listando todas as vendas.";
                    } else {
                        status = "Sem permissão para listar vendas.";
                        showReport = false;
                    }
                    break;
                }
                case "items": {
                    Integer idParam = parseIntOrNull(p(req,"NVendaId"));
                    if (idParam == null) { status = "Informe o ID da venda."; break; }
                    vendaIdToShow = idParam;

                    if (isComprador) {
                        List<Venda> minhas = vendaDAO.buscarPorComprador(user.getIdUsuario());
                        final int idCheck = vendaIdToShow;              // efetivamente final
                        boolean ok = minhas.stream().anyMatch(vd -> vd.getIdVenda() == idCheck);
                        if (!ok) { status = "Sem permissão para ver itens desta venda."; break; }
                    }

                    itens = vendaDAO.buscarItensPorVenda(vendaIdToShow);
                    showItems = true;
                    status = "Itens da venda " + vendaIdToShow + ":";
                    break;
                }

                case "remove": {
                    if (!canRemove) { status = "Sem permissão para excluir venda."; break; }
                    Integer id = parseIntOrNull(p(req,"NVendaId"));
                    if (id == null) { status = "Informe o ID da venda."; break; }
                    boolean ok = vendaDAO.deletarVenda(id);
                    status = ok ? "Venda excluída." : "Não foi possível excluir.";
                    break;
                }
                case "help": {
                    status = "Ajuda: Gerente/Vendedor/Caixa registram e listam; Gerente/Vendedor removem; Comprador lista apenas as próprias.";
                    break;
                }
                case "exit": {
                    resp.sendRedirect(req.getContextPath() + "/home");
                    return;
                }
                default:
                    status = "Ação não reconhecida.";
            }
        } catch (Exception e) {
            status = "Erro: " + e.getMessage();
        }

        mainWindow(req, resp, status, showReport, showItems, canRemove, vendaIdToShow, vendas.isEmpty()? null : vendas, itens);
    }

    /* ===== Render ===== */
    private void mainWindow(HttpServletRequest req, HttpServletResponse resp,
                            String status,
                            boolean showReport, boolean showItems, boolean canRemove,
                            Integer vendaIdToShow,
                            List<Venda> vendas, List<ItemVenda> itens) throws IOException {

        Usuario user = (Usuario) req.getSession().getAttribute("usuarioLogado");
        String papel = user.getPapel();
        boolean isGerente  = Roles.is(papel, Roles.GERENTE);
        boolean isVendedor = Roles.is(papel, Roles.VENDEDOR);
        boolean isCaixa    = Roles.is(papel, Roles.CAIXA);
        boolean isComprador= Roles.is(papel, Roles.COMPRADOR);

        boolean canInsert = isGerente || isVendedor || isCaixa;

        String ctx = req.getContextPath();
        PrintWriter out = resp.getWriter();

        // Produtos para preencher selects dos itens
        List<Produto> produtos = produtoDAO.buscarTodos();
        Map<Integer,String> prodNome = produtos.stream().collect(Collectors.toMap(
                Produto::getIdProduto, Produto::getNome, (a,b)->a, LinkedHashMap::new));

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Vendas</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("<style>table{border-collapse:collapse} td,th{padding:6px;border:1px solid #aaa} .items td{border:0}</style>");
        out.println("</head><body><form method='post' target='_self'>");
        out.println("<h3>Vendas - v" + versionWebApp + " | Logado: " + safe(user.getNome()) + " (" + safe(papel) + ")</h3><br>");

        // Campos básicos
        out.println("<div class='block'><label>ID Venda:</label>");
        out.println("<input type='number' name='NVendaId' style='width:120px;' value='" + (vendaIdToShow==null?"":vendaIdToShow) + "' /></div>");

        out.println("<div class='block'><label>ID Vendedor:</label>");
        out.println("<input type='number' name='NVendaVendedorId' style='width:120px;' " + (isComprador? "disabled" : "") + " /></div>");

        out.println("<div class='block'><label>ID Comprador:</label>");
        out.println("<input type='number' name='NVendaCompradorId' style='width:120px;' " +
                (isComprador? ("value='"+ user.getIdUsuario() +"' readonly") : "") + " /></div>");

        // Itens
        out.println("<hr><h4>Itens da Venda</h4>");
        out.println("<table class='items'><tbody id='tbodyItems'>");
        out.println(rowItem(produtos)); // primeira linha
        out.println("</tbody></table>");
        out.println("<button type='button' onclick='addRow()'>Adicionar Item</button>");

        // Status
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;' /></div>");

        // Botões
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='insert' style='width:130px;' "+ (canInsert? "":"disabled") +">Registrar Venda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='report' style='width:130px;' formnovalidate>Listar Vendas</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='items' style='width:130px;' formnovalidate>Ver Itens</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='remove' style='width:130px;' " + (canRemove? "" : "disabled") + ">Excluir Venda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        // Relatório de vendas
        if (showReport && vendas != null) {
            out.println("<hr><h4>Lista de Vendas</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Data</th><th>Vendedor</th><th>Comprador</th><th>Total (R$)</th></tr>");
            for (Venda v : vendas) {
                out.println("<tr>");
                out.println("<td>"+ v.getIdVenda() +"</td>");
                out.println("<td>"+ safe(v.getDataVenda()) +"</td>");
                out.println("<td>"+ safe(v.getIdVendedor()) +"</td>");
                out.println("<td>"+ safe(v.getIdComprador()) +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", v.getValorTotal()) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        // Itens de uma venda
        if (showItems && itens != null) {
            out.println("<hr><h4>Itens da Venda " + vendaIdToShow + "</h4>");
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>Produto</th><th>Qtd</th><th>Preço Unit</th><th>Subtotal</th></tr>");
            for (ItemVenda it : itens) {
                String nome = prodNome.getOrDefault(it.getIdProduto(), "ID " + it.getIdProduto());
                double sub = it.getQuantidade() * it.getPrecoUnitarioVenda();
                out.println("<tr>");
                out.println("<td>"+ safe(nome) +"</td>");
                out.println("<td>"+ it.getQuantidade() +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", it.getPrecoUnitarioVenda()) +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", sub) +"</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }

        // JS para adicionar linhas de itens
        out.println("<script>");
        out.println("function addRow(){");
        out.println("  var tb = document.getElementById('tbodyItems');");
        out.println("  var tr = document.createElement('tr');");
        out.println("  tr.innerHTML = `" + jsEscape(rowItem(produtos)) + "`;");
        out.println("  tb.appendChild(tr);");
        out.println("}");
        out.println("</script>");

        out.println("</form></body></html>");
    }

    /* ===== Helpers ===== */
    private static String p(HttpServletRequest r, String n){ String v = r.getParameter(n); return v==null? "" : v.trim(); }
    private static Integer parseIntOrNull(String s){ try{ if(s==null||s.isBlank())return null; return Integer.parseInt(s);}catch(Exception e){return null;} }
    private static Double parseDoubleOrNull(String s){ try{ if(s==null||s.isBlank())return null; s=s.replace(',','.'); return Double.parseDouble(s);}catch(Exception e){return null;} }
    private static String safe(Object v){ return v==null? "" : String.valueOf(v).replace("\"","&quot;"); }

    private String rowItem(List<Produto> produtos){
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td><select name='NItemProdutoId' required>");
        sb.append("<option value=''>-- produto --</option>");
        for (Produto p : produtos) {
            sb.append("<option value='").append(p.getIdProduto()).append("'>")
              .append(safe(p.getNome())).append("</option>");
        }
        sb.append("</select></td>");
        sb.append("<td><input type='number' name='NItemQuantidade' min='1' step='1' style='width:100px;' required/></td>");
        sb.append("<td><input type='number' name='NItemPrecoUnit' min='0' step='0.01' style='width:120px;' required/></td>");
        sb.append("</tr>");
        return sb.toString();
    }

    private static String jsEscape(String html){
        return html.replace("`","\\`").replace("\\","\\\\");
    }
}
