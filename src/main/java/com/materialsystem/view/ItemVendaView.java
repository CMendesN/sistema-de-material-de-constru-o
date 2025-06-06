package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.ItemVenda;
import com.materialsystem.util.ConsoleInputUtils;

public class ItemVendaView {
    

    public ItemVenda solicitarDadosNovoItemVenda() {
        System.out.print("ID da Venda: ");
        int idVenda = ConsoleInputUtils.lerInt("Escolha: ");

        System.out.print("ID do Produto: ");
        int idProduto = ConsoleInputUtils.lerInt("Escolha: ");

        System.out.print("Quantidade: ");
        int quantidade = ConsoleInputUtils.lerInt("Escolha: ");

        double precoUnitarioVenda = ConsoleInputUtils.lerDouble("Preço unitário da venda: ");

        // limpar buffer

        return new ItemVenda(0, idVenda, idProduto, quantidade, precoUnitarioVenda);
    }

    public void exibirListaItensVenda(List<ItemVenda> lista) {
        for (ItemVenda item : lista) {
            System.out.println(item);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
