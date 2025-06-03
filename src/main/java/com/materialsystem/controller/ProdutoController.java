package com.materialsystem.controller;

import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.entity.Produto;
import com.materialsystem.view.ProdutoView;

import java.util.List;

public class ProdutoController {

    private ProdutoView produtoView;
    private ProdutoDAO produtoDAO;

    public ProdutoController() {
        this.produtoView = new ProdutoView();
        this.produtoDAO = new ProdutoDAO();
    }

    public void gerenciarProdutos() {
        while (true) {
            System.out.println("\n--- Menu Produto ---");
            System.out.println("1 - Inserir Produto");
            System.out.println("2 - Listar Produtos");
            System.out.println("0 - Sair");

            int opcao = new java.util.Scanner(System.in).nextInt();

            switch (opcao) {
                case 1:
                    Produto produto = produtoView.solicitarDadosNovoProduto();
                    produtoDAO.inserir(produto);
                    produtoView.exibirMensagem("Produto inserido com sucesso!");
                    break;
                case 2:
                    List<Produto> produtos = produtoDAO.buscarTodos();
                    produtoView.exibirListaProdutos(produtos);
                    break;
                case 0:
                    return;
                default:
                    produtoView.exibirMensagem("Opção inválida.");
            }
        }
    }
}
