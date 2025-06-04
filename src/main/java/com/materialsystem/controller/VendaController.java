package com.materialsystem.controller;

import com.materialsystem.dao.VendaDAO;
import com.materialsystem.entity.Venda;
import com.materialsystem.view.VendaView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class VendaController {

    private VendaView view;
    private VendaDAO dao;

    public VendaController() {
        this.view = new VendaView();
        this.dao = new VendaDAO();
    }

    public void gerenciarVendas() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Venda ---");
            System.out.println("1 - Inserir Venda");
            System.out.println("2 - Listar Vendas");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    Venda venda = view.solicitarDadosNovaVenda();
                    dao.inserir(venda);
                    view.exibirMensagem("Venda registrada com sucesso!");
                    break;
                case 2:
                    List<Venda> lista = dao.buscarTodas();
                    view.exibirListaVendas(lista);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
