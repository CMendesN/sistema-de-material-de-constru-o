package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.ItemVendaDAO;
import com.materialsystem.entity.ItemVenda;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.ItemVendaView;

public class ItemVendaController {

    private final ItemVendaView view = new ItemVendaView();
    private final ItemVendaDAO dao = new ItemVendaDAO();

    public void gerenciarItensVenda() {
        while (true) {
            System.out.println("\n--- Menu Itens da Venda ---");
            System.out.println("1 - Inserir Item em Venda");
            System.out.println("2 - Listar Itens de uma Venda");
            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");

            switch (opcao) {
                case 1:
                    ItemVenda item = view.solicitarDadosNovoItemVenda();
                    dao.inserir(item);
                    view.exibirMensagem("Item inserido com sucesso!");
                    break;
                case 2:
                    int idVenda = ConsoleInputUtils.lerInt("Informe o ID da Venda: ");
                    List<ItemVenda> itens = dao.buscarPorVenda(idVenda);
                    view.exibirListaItensVenda(itens);
                    break;
                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
