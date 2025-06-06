package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.VendaDAO;
import com.materialsystem.entity.Venda;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.VendaView;

public class VendaController {

    private VendaView view;
    private VendaDAO dao;

    public VendaController() {
        this.view = new VendaView();
        this.dao = new VendaDAO();
    }

    public void gerenciarVendas() {
        
        while (true) {
            System.out.println("\n--- Menu Venda ---");
            System.out.println("1 - Inserir Venda");
            System.out.println("2 - Listar Vendas");
            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
            // limpar buffer

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
