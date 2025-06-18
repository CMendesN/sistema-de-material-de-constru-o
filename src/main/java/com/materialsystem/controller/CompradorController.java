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
            System.out.println("3 - Buscar Comprador por ID");
            System.out.println("4 - Atualizar Comprador");
            System.out.println("5 - Deletar Comprador");

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
                case 3:
                    int idBusca = ConsoleInputUtils.lerInt("ID do Comprador: ");
                    Comprador encontrado = dao.buscarPorId(idBusca);
                    if (encontrado != null) {
                        view.exibirMensagem(encontrado.toString());
                    } else {
                        view.exibirMensagem("Comprador não encontrado.");
                    }
                    break;

                case 4:
                    int idAtualizar = ConsoleInputUtils.lerInt("ID do Comprador para atualizar: ");
                    Comprador compradorAtual = dao.buscarPorId(idAtualizar);
                    if (compradorAtual != null) {
                        Comprador atualizado = view.solicitarDadosAtualizacao(compradorAtual);
                        dao.atualizar(atualizado);
                        view.exibirMensagem("Comprador atualizado com sucesso.");
                    } else {
                        view.exibirMensagem("Comprador não encontrado.");
                    }
                    break;

                case 5:
                    int idDeletar = ConsoleInputUtils.lerInt("ID do Comprador para deletar: ");
                    Comprador existe = dao.buscarPorId(idDeletar);
                    if (existe != null) {
                        dao.deletar(idDeletar);
                        view.exibirMensagem("Comprador deletado com sucesso.");
                    } else {
                        view.exibirMensagem("Comprador não encontrado.");
                    }
                    break;

                case 0:
                    return;
                default:
                    view.exibirMensagem("Opção inválida.");
            }
        }
    }
}
