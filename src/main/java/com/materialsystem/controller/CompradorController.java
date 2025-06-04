package com.materialsystem.controller;

import com.materialsystem.dao.CompradorDAO;
import com.materialsystem.entity.Comprador;
import com.materialsystem.view.CompradorView;

import java.util.List;
import java.util.Scanner;

public class CompradorController {

    private CompradorView view;
    private CompradorDAO dao;

    public CompradorController() {
        this.view = new CompradorView();
        this.dao = new CompradorDAO();
    }

    public void gerenciarCompradores() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Comprador ---");
            System.out.println("1 - Inserir Comprador");
            System.out.println("2 - Listar Compradores");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    Comprador comprador = view.solicitarDadosNovoComprador();
                    dao.inserir(comprador);
                    view.exibirMensagem("Comprador inserido com sucesso!");
                    break;
                case 2:
                    List<Comprador> lista = dao.buscarTodos();
                    view.exibirListaCompradores(lista);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
