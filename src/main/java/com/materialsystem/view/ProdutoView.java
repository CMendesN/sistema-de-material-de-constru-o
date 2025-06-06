package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.Produto;
import com.materialsystem.util.ConsoleInputUtils;

public class ProdutoView {

    

    public Produto solicitarDadosNovoProduto() {
        System.out.print("Nome: ");
        String nome= ConsoleInputUtils.lerString();

        System.out.print("Descrição: ");
        String descricao= ConsoleInputUtils.lerString();

        
        double precoUnitario = ConsoleInputUtils.lerDouble("Preço Unitário: ");

        System.out.print("Quantidade em Estoque: ");
        int quantidade = ConsoleInputUtils.lerInt("Escolha: ");

        System.out.print("ID Fabricante: ");
        int idFabricante = ConsoleInputUtils.lerInt("Escolha: ");
        // limpeza do buffer

        System.out.print("Categoria: ");
        String categoria= ConsoleInputUtils.lerString();

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
