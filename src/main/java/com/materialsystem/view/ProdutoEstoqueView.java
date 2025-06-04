package com.materialsystem.view;

import com.materialsystem.entity.ProdutoEstoque;

import java.util.List;
import java.util.Scanner;

public class ProdutoEstoqueView {
    private Scanner scanner = new Scanner(System.in);

    public ProdutoEstoque solicitarDadosNovoProdutoEstoque() {
        System.out.print("ID Produto: ");
        int idProduto = scanner.nextInt();

        System.out.print("ID Estoque: ");
        int idEstoque = scanner.nextInt();

        System.out.print("Quantidade: ");
        int quantidade = scanner.nextInt();

        scanner.nextLine(); // limpar buffer

        return new ProdutoEstoque(idProduto, idEstoque, quantidade);
    }

    public void exibirListaAssociacoes(List<ProdutoEstoque> lista) {
        for (ProdutoEstoque pe : lista) {
            System.out.println(pe);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
