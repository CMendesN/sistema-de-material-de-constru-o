package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.EstoqueDAO;
import com.materialsystem.entity.Estoque;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.EstoqueView;

public class EstoqueController {

    private EstoqueView view;
    private EstoqueDAO dao;

    public EstoqueController() {
        this.view = new EstoqueView();
        this.dao = new EstoqueDAO();
    }

    public void gerenciarEstoques() {
        
        while (true) {
            System.out.println("\n--- Menu Estoque ---");
            System.out.println("1 - Inserir Estoque");
            System.out.println("2 - Listar Estoques");
            System.out.println("3 - Buscar Estoque por ID");
            System.out.println("4 - Atualizar Estoque");
            System.out.println("5 - Deletar Estoque");

            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");

            switch (opcao) {
                case 1:
                    Estoque estoque = view.solicitarDadosNovoEstoque();
                    dao.inserir(estoque);
                    view.exibirMensagem("Estoque inserido com sucesso!");
                    break;
                case 2:
                    List<Estoque> lista = dao.buscarTodos();
                    view.exibirListaEstoques(lista);
                    break;
                case 3:
                    int idBusca = ConsoleInputUtils.lerInt("ID do Estoque: ");
                    Estoque encontrado = dao.buscarPorId(idBusca);
                    if (encontrado != null) {
                        view.exibirMensagem(encontrado.toString());
                    } else {
                        view.exibirMensagem("Estoque não encontrado.");
                    }
                    break;

                case 4:
                    int idAtualizar = ConsoleInputUtils.lerInt("ID do Estoque para atualizar: ");
                    Estoque estoqueAtual = dao.buscarPorId(idAtualizar);
                    if (estoqueAtual != null) {
                        Estoque atualizado = view.solicitarDadosAtualizacao(estoqueAtual);
                        dao.atualizar(atualizado);
                        view.exibirMensagem("Estoque atualizado com sucesso.");
                    } else {
                        view.exibirMensagem("Estoque não encontrado.");
                    }
                    break;

                case 5:
                    int idDeletar = ConsoleInputUtils.lerInt("ID do Estoque para deletar: ");
                    Estoque existe = dao.buscarPorId(idDeletar);
                    if (existe != null) {
                        dao.deletar(idDeletar);
                        view.exibirMensagem("Estoque deletado com sucesso.");
                    } else {
                        view.exibirMensagem("Estoque não encontrado.");
                    }
                    break;

                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
