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
    public Produto solicitarDadosAtualizacao(Produto produtoAtual) {
        System.out.println("Atualizando produto ID: " + produtoAtual.getIdProduto());

        System.out.print("Nome [" + produtoAtual.getNome() + "]: ");
        String nome = ConsoleInputUtils.lerString();
        if (nome.isBlank()) nome = produtoAtual.getNome();

        System.out.print("Descrição [" + produtoAtual.getDescricao() + "]: ");
        String descricao = ConsoleInputUtils.lerString();
        if (descricao.isBlank()) descricao = produtoAtual.getDescricao();

        System.out.print("Preço Unitário [" + produtoAtual.getPrecoUnitario() + "]: ");
        String precoStr = ConsoleInputUtils.lerString();
        double preco;
        try {
            preco = precoStr.isBlank() ? produtoAtual.getPrecoUnitario() : Double.parseDouble(precoStr);
        } catch (NumberFormatException e) {
            preco = produtoAtual.getPrecoUnitario();
        }

        System.out.print("Quantidade em Estoque [" + produtoAtual.getQuantidadeEmEstoque() + "]: ");
        String qtdStr = ConsoleInputUtils.lerString();
        int quantidade;
        try {
            quantidade = qtdStr.isBlank() ? produtoAtual.getQuantidadeEmEstoque() : Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            quantidade = produtoAtual.getQuantidadeEmEstoque();
        }

        System.out.print("ID Fabricante [" + produtoAtual.getIdFabricante() + "]: ");
        String idFabStr = ConsoleInputUtils.lerString();
        int idFabricante;
        try {
            idFabricante = idFabStr.isBlank() ? produtoAtual.getIdFabricante() : Integer.parseInt(idFabStr);
        } catch (NumberFormatException e) {
            idFabricante = produtoAtual.getIdFabricante();
        }

        System.out.print("Categoria [" + produtoAtual.getCategoria() + "]: ");
        String categoria = ConsoleInputUtils.lerString();
        if (categoria.isBlank()) categoria = produtoAtual.getCategoria();

        return new Produto(produtoAtual.getIdProduto(), nome, descricao, preco, quantidade, idFabricante, categoria);
    }

}
