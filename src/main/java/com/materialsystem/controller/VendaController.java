package com.materialsystem.controller;

import com.materialsystem.dao.VendaDAO;
import com.materialsystem.entity.Venda;
import com.materialsystem.util.VendaComItens;
import com.materialsystem.view.VendaView;

import java.util.List;

public class VendaController {
    private final VendaView view = new VendaView();
    private final VendaDAO dao = new VendaDAO();

    public void gerenciarVendas() {
        while (true) {
            System.out.println("\n--- Menu Venda ---");
            System.out.println("1 - Registrar Venda Completa");
            System.out.println("2 - Listar Vendas");
            System.out.println("0 - Voltar");

            int opcao = com.materialsystem.util.ConsoleInputUtils.lerInt("Escolha: ");
            switch (opcao) {
                case 1 -> realizarVendaCompleta();
                case 2 -> listarVendas();
                case 0 -> { return; }
                default -> view.exibirMensagem("Opção inválida.");
            }
        }
    }

    public void realizarVendaCompleta() {
        VendaComItens entrada = view.solicitarVendaComItens();

        if (entrada == null || entrada.venda() == null || entrada.itens().isEmpty()) {
            view.exibirMensagem("Venda cancelada. Nenhum item informado.");
            return;
        }

        boolean sucesso = dao.registrarVendaComItens(entrada.venda(), entrada.itens());
        view.exibirMensagem(sucesso ? "Venda registrada com sucesso!" : "Erro ao registrar a venda.");
    }

    public void listarVendas() {
        List<Venda> lista = dao.buscarTodas();
        view.exibirListaVendas(lista);
    }
}