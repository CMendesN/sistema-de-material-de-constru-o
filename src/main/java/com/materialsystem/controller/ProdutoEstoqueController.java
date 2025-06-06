package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.ProdutoEstoqueDAO;
import com.materialsystem.entity.ProdutoEstoque;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.ProdutoEstoqueView;

public class ProdutoEstoqueController {

    private final ProdutoEstoqueView view = new ProdutoEstoqueView();
    private final ProdutoEstoqueDAO dao = new ProdutoEstoqueDAO();

    public void gerenciarProdutoEstoque() {
        while (true) {
            System.out.println("\n--- Menu Produto-Estoque ---");
            System.out.println("1 - Inserir Associação");
            System.out.println("2 - Listar por Produto");
            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");

            switch (opcao) {
                case 1:
                    ProdutoEstoque pe = view.solicitarDadosNovoProdutoEstoque();

                    if (dao.existeAssociacao(pe.getIdProduto(), pe.getIdEstoque())) {
                        view.exibirMensagem("Erro: já existe associação deste produto com este estoque.");
                    } else {
                        dao.inserir(pe);
                        view.exibirMensagem("Associação inserida com sucesso!");
                    }
                    break;

                case 2:
                    int idProduto = ConsoleInputUtils.lerInt("Informe o ID do Produto: ");
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
