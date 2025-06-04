package com.materialsystem.controller;

import com.materialsystem.dao.VendedorDAO;
import com.materialsystem.entity.Vendedor;
import com.materialsystem.view.VendedorView;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class VendedorController {

    private VendedorView view;
    private VendedorDAO dao;

    public VendedorController() {
        this.view = new VendedorView();
        this.dao = new VendedorDAO();
    }

    public void gerenciarVendedores() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Vendedor ---");
            System.out.println("1 - Inserir Vendedor");
            System.out.println("2 - Listar Vendedores");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1:
                    Vendedor vendedor = view.solicitarDadosNovoVendedor();
                    dao.inserir(vendedor);
                    view.exibirMensagem("Vendedor inserido com sucesso!");
                    break;
                case 2:
                    List<Vendedor> lista = dao.buscarTodos();
                    view.exibirListaVendedores(lista);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
