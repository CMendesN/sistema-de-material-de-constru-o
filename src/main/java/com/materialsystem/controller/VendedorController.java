package com.materialsystem.controller;

import java.util.List;

import com.materialsystem.dao.VendedorDAO;
import com.materialsystem.entity.Vendedor;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.view.VendedorView;

public class VendedorController {

    private VendedorView view;
    private VendedorDAO dao;

    public VendedorController() {
        this.view = new VendedorView();
        this.dao = new VendedorDAO();
    }

    public void gerenciarVendedores() {
        
        while (true) {
            System.out.println("\n--- Menu Vendedor ---");
            System.out.println("1 - Inserir Vendedor");
            System.out.println("2 - Listar Vendedores");
            System.out.println("3 - Buscar Vendedor por ID");
            System.out.println("4 - Atualizar Vendedor");
            System.out.println("5 - Deletar Vendedor");

            System.out.println("0 - Voltar");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
            

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
                case 3:
                    int idBusca = ConsoleInputUtils.lerInt("ID do Vendedor: ");
                    Vendedor encontrado = dao.buscarPorId(idBusca);
                    if (encontrado != null) {
                        view.exibirMensagem(encontrado.toString());
                    } else {
                        view.exibirMensagem("Vendedor não encontrado.");
                    }
                    break;

                case 4:
                    int idAtualizar = ConsoleInputUtils.lerInt("ID do Vendedor para atualizar: ");
                    Vendedor vendedorAtual = dao.buscarPorId(idAtualizar);
                    if (vendedorAtual != null) {
                        Vendedor atualizado = view.solicitarDadosAtualizacao(vendedorAtual);
                        dao.atualizar(atualizado);
                        view.exibirMensagem("Vendedor atualizado com sucesso.");
                    } else {
                        view.exibirMensagem("Vendedor não encontrado.");
                    }
                    break;

                case 5:
                    int idDeletar = ConsoleInputUtils.lerInt("ID do Vendedor para deletar: ");
                    Vendedor existe = dao.buscarPorId(idDeletar);
                    if (existe != null) {
                        dao.deletar(idDeletar);
                        view.exibirMensagem("Vendedor deletado com sucesso.");
                    } else {
                        view.exibirMensagem("Vendedor não encontrado.");
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
