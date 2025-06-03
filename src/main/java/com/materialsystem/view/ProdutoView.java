package com.materialsystem.view;

import com.materialsystem.entity.Produto;

import java.util.List;
import java.util.Scanner;

public class ProdutoView {

    private Scanner scanner = new Scanner(System.in);

    public Produto solicitarDadosNovoProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço Unitário: ");
        double precoUnitario = scanner.nextDouble();

        System.out.print("Quantidade em Estoque: ");
        int quantidade = scanner.nextInt();

        System.out.print("ID Fabricante: ");
        int idFabricante = scanner.nextInt();
        scanner.nextLine(); // limpeza do buffer

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine();

        return new Produto(0, nome, descricao, precoUnitario, quantidade, idFabricante, categoria);
    }

    public void exibirListaProdutos(List<Produto> produtos) {
        for (Produto p : produtos) {
            System.out.println(p);
        }
    }

    public void exibirMensagem(String msg) {
        System.out.println(msg);
    }
}
