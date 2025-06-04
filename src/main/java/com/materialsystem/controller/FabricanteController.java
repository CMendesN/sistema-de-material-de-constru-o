package com.materialsystem.controller;

import com.materialsystem.dao.FabricanteDAO;
import com.materialsystem.entity.Fabricante;
import com.materialsystem.view.FabricanteView;

import java.util.List;
import java.util.Scanner;

public class FabricanteController {

    private FabricanteView view;
    private FabricanteDAO dao;

    public FabricanteController() {
        this.view = new FabricanteView();
        this.dao = new FabricanteDAO();
    }

    public void gerenciarFabricantes() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Fabricante ---");
            System.out.println("1 - Inserir Fabricante");
            System.out.println("2 - Listar Fabricantes");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1:
                    Fabricante fabricante = view.solicitarDadosNovoFabricante();
                    dao.inserir(fabricante);
                    view.exibirMensagem("Fabricante inserido com sucesso!");
                    break;
                case 2:
                    List<Fabricante> lista = dao.buscarTodos();
                    view.exibirListaFabricantes(lista);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
