package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.CompradorDAO;
import com.materialsystem.entity.Comprador;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.CompradorView;

public class CompradorController {

    private CompradorView view;
    private CompradorDAO dao;

    public CompradorController() {
        this.view = new CompradorView();
        this.dao = new CompradorDAO();
    }

    public void gerenciarCompradores() {
        
        while (true) {
            System.out.println("\n--- Menu Comprador ---");
            System.out.println("1 - Inserir Comprador");
            System.out.println("2 - Listar Compradores");
            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
            // limpar buffer

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
