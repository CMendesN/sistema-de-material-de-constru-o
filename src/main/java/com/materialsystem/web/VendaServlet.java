package com.materialsystem.web;

import com.materialsystem.dao.VendaDAO;
import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.dao.VendedorDAO;
import com.materialsystem.dao.CompradorDAO;

import com.materialsystem.entity.Venda;
import com.materialsystem.entity.ItemVenda;
import com.materialsystem.entity.Produto;
import com.materialsystem.entity.Vendedor;
import com.materialsystem.entity.Comprador;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaServlet extends HttpServlet {

    private final VendaDAO vendaDAO = new VendaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final VendedorDAO vendedorDAO = new VendedorDAO();
    private final CompradorDAO compradorDAO = new CompradorDAO();

    private final boolean enableStyleCSS = true;
    private final String versionWebApp = "1.0";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        render(req, resp, "Pronto.", null, null, null, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String action = p(req, "action");
        String status = "Pronto.";
        List<Venda> vendas = null;
        List<ItemVenda> itensDaVenda = null;
        Integer vendaSelecionada = null;
        boolean mostrarRelatorio = false;

        try {
            switch (action) {
                case "register": {
                    // Lê cabeçalho (vendedor/comprador)
                    Integer idVendedor = parseIntOrNull(p(req,"NVendaVendedorId"));
                    Integer idComprador = parseIntOrNull(p(req,"NVendaCompradorId"));

                    // Lê arrays de itens
                    String[] prodIds = req.getParameterValues("itemProdutoId");
                    String[] qtds    = req.getParameterValues("itemQuantidade");
                    String[] precos  = req.getParameterValues("itemPreco");

                    List<ItemVenda> itens = new ArrayList<>();
                    double total = 0.0;

                    if (prodIds != null) {
                        for (int i = 0; i < prodIds.length; i++) {
                            int idProduto   = parseIntOrZero(safeIndex(prodIds, i));
                            int quantidade  = parseIntOrZero(safeIndex(qtds, i));
                            double preco    = parseDoubleOrZero(safeIndex(precos, i));
                            if (idProduto > 0 && quantidade > 0 && preco >= 0.0) {
                                ItemVenda it = new ItemVenda();
                                it.setIdProduto(idProduto);
                                it.setQuantidade(quantidade);
                                it.setPrecoUnitarioVenda(preco);
                                itens.add(it);
                                total += quantidade * preco;
                            }
                        }
                    }

                    if (itens.isEmpty()) throw new IllegalArgumentException("Inclua pelo menos 1 item válido.");

                    Venda venda = new Venda(0, LocalDateTime.now(), idVendedor, idComprador, total);
                    boolean ok = vendaDAO.registrarVendaComItens(venda, itens);
                    status = ok ? "Venda registrada com sucesso." : "Falha ao registrar a venda.";
                    break;
                }
                case "report": {
                    mostrarRelatorio = true;
                    int filtroComprador = parseIntOrZero(p(req,"NFilterCompradorId"));
                    if (filtroComprador > 0) {
                        vendas = vendaDAO.buscarPorComprador(filtroComprador);
                        status = "Relatório filtrado pelo comprador ID " + filtroComprador + ".";
                    } else {
                        vendas = vendaDAO.buscarTodas();
                        status = "Relatório de todas as vendas.";
                    }
                    break;
                }
                case "viewItems": {
                    vendaSelecionada = parseIntOrZero(p(req,"idVenda"));
                    if (vendaSelecionada <= 0) {
                        status = "Informe um ID de venda válido.";
                    } else {
                        itensDaVenda = vendaDAO.buscarItensPorVenda(vendaSelecionada);
                        mostrarRelatorio = true;
                        vendas = vendaDAO.buscarTodas(); // mantém a grade de vendas
                        status = "Itens da venda " + vendaSelecionada + " listados abaixo.";
                    }
                    break;
                }
                case "remove": {
                    int idVenda = parseIntOrZero(p(req,"idVenda"));
                    if (idVenda <= 0) {
                        status = "Informe um ID de venda válido para excluir.";
                    } else {
                        boolean ok = vendaDAO.deletarVenda(idVenda);
                        status = ok ? "Venda " + idVenda + " excluída." : "Não foi possível excluir a venda " + idVenda + ".";
                    }
                    mostrarRelatorio = true;
                    vendas = vendaDAO.buscarTodas();
                    break;
                }
                case "help":
                    status = "Ajuda: preencha vendedor/comprador (opcional), adicione itens (produto, qtd, preço) e clique Registrar. Em Relatório, é possível filtrar por comprador, ver itens e excluir a venda.";
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

        render(req, resp, status, vendas, itensDaVenda, vendaSelecionada, mostrarRelatorio);
    }

    /* =================== RENDER =================== */
    private void render(HttpServletRequest req, HttpServletResponse resp,
                        String status,
                        List<Venda> vendas,
                        List<ItemVenda> itensDaVenda,
                        Integer vendaSelecionada,
                        boolean mostrarRelatorio) throws IOException {

        String ctx = req.getContextPath();
        List<Produto> produtos = produtoDAO.buscarTodos();
        List<Vendedor> vendedores = vendedorDAO.buscarTodos();
        List<Comprador> compradores = compradorDAO.buscarTodos();

        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Vendas</title>");
        if (enableStyleCSS) out.println("<link rel='stylesheet' href='" + ctx + "/styleLogin.css'>");
        out.println("<style> table td,table th{vertical-align:middle} .item-row td{padding:3px} .item-row select,input{min-width:120px} </style>");
        out.println("</head><body>");

        out.println("<form method='post' target='_self'>");
        out.println("<h3>Vendas - versão " + versionWebApp + "</h3><br>");

        /* ===== Cabeçalho (vendedor/comprador) ===== */
        out.println("<div class='block'><label>Vendedor:</label>");
        out.println("<select name='NVendaVendedorId' style='width:300px;'>");
        out.println("<option value=''>-- opcional --</option>");
        for (Vendedor v : vendedores) {
            out.println("<option value='" + v.getIdVendedor() + "'>" + safe(v.getNome()) + "</option>");
        }
        out.println("</select></div>");

        out.println("<div class='block'><label>Comprador:</label>");
        out.println("<select name='NVendaCompradorId' style='width:300px;'>");
        out.println("<option value=''>-- opcional --</option>");
        for (Comprador c : compradores) {
            out.println("<option value='" + c.getIdComprador() + "'>" + safe(c.getNome()) + "</option>");
        }
        out.println("</select></div>");

        /* ===== Tabela de Itens (dinâmica) ===== */
        out.println("<div class='block'><label>Itens:</label></div>");
        out.println("<table id='tbItens' border='1' cellpadding='4'>");
        out.println("<thead><tr><th>Produto</th><th>Qtd</th><th>Preço Unit.</th><th>Ações</th></tr></thead>");
        out.println("<tbody id='itensBody'></tbody>");
        out.println("</table>");
        out.println("<div class='block'>");
        out.println("<button type='button' onclick='addItem()'>Adicionar Item</button>");
        out.println("</div>");

        /* ===== Status ===== */
        out.println("<div class='block'><label>Status:</label>");
        out.println("<input value='" + safe(status) + "' type='text' readonly style='width:600px;'/></div>");

        /* ===== Botões ===== */
        out.println("<div class='block'>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='register' style='width:140px;'>Registrar Venda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='report' style='width:110px;' formnovalidate>Relatório</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='help' style='width:110px;' formnovalidate>Ajuda</button>");
        out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='exit' style='width:110px;' formnovalidate>Sair</button>");
        out.println("</div>");

        /* ===== Relatório (lista de vendas) ===== */
        if (mostrarRelatorio) {
            if (vendas == null) vendas = vendaDAO.buscarTodas();

            out.println("<hr><h4>Relatório de Vendas</h4>");
            out.println("<div class='block'>");
            out.println("<form method='post' target='_self' style='display:inline;'>");
            out.println("<label>Filtrar Comprador ID:</label>");
            out.println("<input type='number' min='1' name='NFilterCompradorId' style='width:120px;'/>");
            out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='report' formnovalidate>Aplicar</button>");
            out.println("</form>");
            out.println("</div>");

            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>ID</th><th>Data</th><th>ID Vendedor</th><th>ID Comprador</th><th>Valor Total</th><th>Ações</th></tr>");
            for (Venda v : vendas) {
                out.println("<tr>");
                out.println("<td>"+ v.getIdVenda() +"</td>");
                out.println("<td>"+ safe(v.getDataVenda()) +"</td>");
                out.println("<td>"+ safe(v.getIdVendedor()) +"</td>");
                out.println("<td>"+ safe(v.getIdComprador()) +"</td>");
                out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", v.getValorTotal()) +"</td>");
                out.println("<td>");
                out.println("<form method='post' target='_self' style='display:inline;'>");
                out.println("<input type='hidden' name='idVenda' value='"+ v.getIdVenda() +"'/>");
                out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='viewItems'>Ver Itens</button>");
                out.println("</form>");
                out.println("&nbsp;");
                out.println("<form method='post' target='_self' style='display:inline;' onsubmit=\"return confirm('Excluir venda "+v.getIdVenda()+"?')\">");
                out.println("<input type='hidden' name='idVenda' value='"+ v.getIdVenda() +"'/>");
                out.println("<button type='submit' formaction='" + ctx + "/venda' name='action' value='remove'>Excluir</button>");
                out.println("</form>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</table>");

            /* Detalhe de itens de uma venda selecionada */
            if (itensDaVenda != null && vendaSelecionada != null) {
                out.println("<h5>Itens da Venda " + vendaSelecionada + "</h5>");
                out.println("<table border='1' cellpadding='5'>");
                out.println("<tr><th>ID Item</th><th>ID Produto</th><th>Quantidade</th><th>Preço Unit.</th></tr>");
                for (ItemVenda it : itensDaVenda) {
                    out.println("<tr>");
                    out.println("<td>"+ it.getIdItemVenda() +"</td>");
                    out.println("<td>"+ it.getIdProduto() +"</td>");
                    out.println("<td>"+ it.getQuantidade() +"</td>");
                    out.println("<td>"+ String.format(java.util.Locale.US, "%.2f", it.getPrecoUnitarioVenda()) +"</td>");
                    out.println("</tr>");
                }
                out.println("</table>");
            }
        }

        /* ===== Template/JS para adicionar itens ===== */
        out.println("<template id='tplItem'>");
        out.println("<tr class='item-row'>");
        out.println("<td><select name='itemProdutoId' class='prodSel'>");
        out.println("<option value=''>-- selecione --</option>");
        for (Produto pr : produtos) {
            // usa data-preco para sugerir preço padrão
            out.println("<option value='"+ pr.getIdProduto() +"' data-preco='"+ pr.getPrecoUnitario() +"'>"
                    + safe(pr.getNome()) + "</option>");
        }
        out.println("</select></td>");
        out.println("<td><input type='number' name='itemQuantidade' min='1' step='1' value='1' /></td>");
        out.println("<td><input type='number' name='itemPreco' min='0' step='0.01' value='0.00' /></td>");
        out.println("<td><button type='button' onclick='rmItem(this)'>Remover</button></td>");
        out.println("</tr>");
        out.println("</template>");

        out.println("<script>");
        out.println("function addItem(){");
        out.println("  const tpl=document.getElementById('tplItem');");
        out.println("  const row=tpl.content.firstElementChild.cloneNode(true);");
        out.println("  // ao trocar produto, setar preço padrão (se existir)");
        out.println("  row.querySelector('.prodSel').addEventListener('change', function(){");
        out.println("    const opt=this.selectedOptions[0]; if(!opt) return;");
        out.println("    const preco=opt.getAttribute('data-preco'); if(preco){");
        out.println("      const input= row.querySelector(\"input[name='itemPreco']\");");
        out.println("      if(input && (!input.value || parseFloat(input.value)==0)) input.value=preco;");
        out.println("    }");
        out.println("  });");
        out.println("  document.getElementById('itensBody').appendChild(row);");
        out.println("}");
        out.println("function rmItem(btn){ const tr=btn.closest('tr'); if(tr) tr.remove(); }");
        out.println("// adiciona uma linha inicial");
        out.println("if(document.getElementById('itensBody').children.length===0) addItem();");
        out.println("</script>");

        out.println("</form></body></html>");
    }

    /* =================== helpers =================== */
    private static String p(HttpServletRequest req, String name) {
        String v = req.getParameter(name); return v==null? "" : v.trim();
    }
    private static String safe(Object v){
        return v==null? "" : String.valueOf(v).replace("\"","&quot;");
    }
    private static int parseIntOrZero(String s){ try { return Integer.parseInt(s); } catch(Exception e){ return 0; } }
    private static Integer parseIntOrNull(String s){
        if(s==null || s.isBlank()) return null;
        try { return Integer.valueOf(s); } catch(Exception e){ return null; }
    }
    private static double parseDoubleOrZero(String s){
        if(s==null || s.isBlank()) return 0.0;
        s = s.replace(",", ".");
        try { return Double.parseDouble(s); } catch(Exception e){ return 0.0; }
    }
    private static String safeIndex(String[] arr, int i){ return (arr!=null && i>=0 && i<arr.length) ? arr[i] : ""; }
}
