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
            System.out.println("3 - Buscar Produto por ID");
            System.out.println("4 - Atualizar Produto");
            System.out.println("5 - Deletar Produto");

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
                case 3:
                    int idBusca = ConsoleInputUtils.lerInt("ID do Produto: ");
                    Produto encontrado = produtoDAO.buscarPorId(idBusca);
                    if (encontrado != null) {
                        produtoView.exibirMensagem(encontrado.toString());
                    } else {
                        produtoView.exibirMensagem("Produto não encontrado.");
                    }
                    break;

                case 4:
                    int idAtualizar = ConsoleInputUtils.lerInt("ID do Produto para atualizar: ");
                    Produto produtoAtual = produtoDAO.buscarPorId(idAtualizar);
                    if (produtoAtual != null) {
                        Produto atualizado = produtoView.solicitarDadosAtualizacao(produtoAtual);
                        produtoDAO.atualizar(atualizado);
                        produtoView.exibirMensagem("Produto atualizado com sucesso.");
                    } else {
                        produtoView.exibirMensagem("Produto não encontrado.");
                    }
                    break;

                case 5:
                    int idDeletar = ConsoleInputUtils.lerInt("ID do Produto para deletar: ");
                    Produto existe = produtoDAO.buscarPorId(idDeletar);
                    if (existe != null) {
                        produtoDAO.deletar(idDeletar);
                        produtoView.exibirMensagem("Produto deletado com sucesso.");
                    } else {
                        produtoView.exibirMensagem("Produto não encontrado.");
                    }
                    break;

                case 0:
                    return;
                default:
                    produtoView.exibirMensagem("Opção inválida.");
            }
        }
    }
}
