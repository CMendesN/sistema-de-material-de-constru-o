package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.entity.Produto;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.ProdutoView;

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

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
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
