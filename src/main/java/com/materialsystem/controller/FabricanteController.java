package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.FabricanteDAO;
import com.materialsystem.entity.Fabricante;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.FabricanteView;

public class FabricanteController {

    private FabricanteView view;
    private FabricanteDAO dao;

    public FabricanteController() {
        this.view = new FabricanteView();
        this.dao = new FabricanteDAO();
    }

    public void gerenciarFabricantes() {
        
        while (true) {
            System.out.println("\n--- Menu Fabricante ---");
            System.out.println("1 - Inserir Fabricante");
            System.out.println("2 - Listar Fabricantes");
            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
            // Limpar buffer

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
