package com.materialsystem.view;

import com.materialsystem.entity.ItemVenda;
import com.materialsystem.entity.Venda;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.util.VendaComItens;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaView {

    public VendaComItens solicitarVendaComItens() {
        int idVendedor = ConsoleInputUtils.lerInt("ID do Vendedor: ");
        int idComprador = ConsoleInputUtils.lerInt("ID do Comprador: ");
        List<ItemVenda> itens = new ArrayList<>();
        double valorTotal = 0;

        while (true) {
            int idProduto = ConsoleInputUtils.lerInt("ID do Produto: ");
            int quantidade = ConsoleInputUtils.lerInt("Quantidade: ");
            double precoUnitario = ConsoleInputUtils.lerDouble("Preço Unitário: ");

            itens.add(new ItemVenda(0, 0, idProduto, quantidade, precoUnitario));
            valorTotal += quantidade * precoUnitario;
            System.out.println("Adicionar mais itens? (s/n): ");
            String continuar = ConsoleInputUtils.lerString();
            if (!continuar.equalsIgnoreCase("s")) break;
        }

        Venda venda = new Venda(0, LocalDateTime.now(),
            idVendedor == 0 ? null : idVendedor,
            idComprador == 0 ? null : idComprador,
            valorTotal);

        return new VendaComItens(venda, itens);
    }

    public void exibirListaVendas(List<Venda> lista) {
        for (Venda v : lista) System.out.println(v);
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}