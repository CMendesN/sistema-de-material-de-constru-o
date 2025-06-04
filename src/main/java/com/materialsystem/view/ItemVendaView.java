package com.materialsystem.view;

import com.materialsystem.entity.ItemVenda;

import java.util.List;
import java.util.Scanner;

public class ItemVendaView {
    private Scanner scanner = new Scanner(System.in);

    public ItemVenda solicitarDadosNovoItemVenda() {
        System.out.print("ID da Venda: ");
        int idVenda = scanner.nextInt();

        System.out.print("ID do Produto: ");
        int idProduto = scanner.nextInt();

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();

        System.out.print("Preço unitário da venda: ");
        double precoUnitarioVenda = scanner.nextDouble();

        scanner.nextLine(); // limpar buffer

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
