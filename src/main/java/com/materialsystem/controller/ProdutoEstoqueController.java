package com.materialsystem.controller;

import com.materialsystem.dao.ProdutoEstoqueDAO;
import com.materialsystem.entity.ProdutoEstoque;
import com.materialsystem.view.ProdutoEstoqueView;

import java.util.List;
import java.util.Scanner;

public class ProdutoEstoqueController {

    private ProdutoEstoqueView view;
    private ProdutoEstoqueDAO dao;

    public ProdutoEstoqueController() {
        this.view = new ProdutoEstoqueView();
        this.dao = new ProdutoEstoqueDAO();
    }

    public void gerenciarProdutoEstoque() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Produto-Estoque ---");
            System.out.println("1 - Inserir Associação");
            System.out.println("2 - Listar por Produto");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    ProdutoEstoque pe = view.solicitarDadosNovoProdutoEstoque();
                    dao.inserir(pe);
                    view.exibirMensagem("Associação inserida com sucesso!");
                    break;
                case 2:
                    System.out.print("Informe o ID do Produto: ");
                    int idProduto = scanner.nextInt();
                    List<ProdutoEstoque> lista = dao.buscarPorProduto(idProduto);
                    view.exibirListaAssociacoes(lista);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
