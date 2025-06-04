package com.materialsystem.controller;

import com.materialsystem.dao.ItemVendaDAO;
import com.materialsystem.entity.ItemVenda;
import com.materialsystem.view.ItemVendaView;

import java.util.List;
import java.util.Scanner;

public class ItemVendaController {

    private ItemVendaView view;
    private ItemVendaDAO dao;

    public ItemVendaController() {
        this.view = new ItemVendaView();
        this.dao = new ItemVendaDAO();
    }

    public void gerenciarItensVenda() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Menu Itens da Venda ---");
            System.out.println("1 - Inserir Item em Venda");
            System.out.println("2 - Listar Itens de uma Venda");
            System.out.println("0 - Voltar");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // limpar buffer

            switch (opcao) {
                case 1:
                    ItemVenda item = view.solicitarDadosNovoItemVenda();
                    dao.inserir(item);
                    view.exibirMensagem("Item inserido com sucesso!");
                    break;
                case 2:
                    System.out.print("Informe o ID da Venda: ");
                    int idVenda = scanner.nextInt();
                    List<ItemVenda> lista = dao.buscarPorVenda(idVenda);
                    view.exibirListaItensVenda(lista);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
