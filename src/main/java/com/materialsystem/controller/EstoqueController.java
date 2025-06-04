package com.materialsystem.controller;

import com.materialsystem.dao.EstoqueDAO;
import com.materialsystem.entity.Estoque;
import com.materialsystem.view.EstoqueView;

import java.util.List;
import java.util.Scanner;

public class EstoqueController {

    private EstoqueView view;
    private EstoqueDAO dao;

    public EstoqueController() {
        this.view = new EstoqueView();
        this.dao = new EstoqueDAO();
    }

    public void gerenciarEstoques() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Estoque ---");
            System.out.println("1 - Inserir Estoque");
            System.out.println("2 - Listar Estoques");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

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
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
