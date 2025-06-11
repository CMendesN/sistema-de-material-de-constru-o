package com.materialsystem.controller;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.view.UsuarioView;

public class UsuarioController {

    private final UsuarioView view = new UsuarioView();
    private final UsuarioDAO dao = new UsuarioDAO();

    // Fluxo normal para o menu
    public void cadastrarUsuario() {
        Usuario usuario = view.solicitarCadastroUsuario();

        if (dao.buscarPorUsername(usuario.getUsername()) != null) {
            view.exibirMensagem("Erro: Username já existente. Cadastro cancelado.");
            return;
        }

        dao.inserir(usuario);
        view.exibirMensagem("Usuário cadastrado com sucesso!");
    }

    // Fluxo especial da primeira execução
    public void cadastrarUsuarioInicial() {
        Usuario usuario = view.solicitarCadastroInicial();

        if (dao.buscarPorUsername(usuario.getUsername()) != null) {
            view.exibirMensagem("Erro: Username já existente. Cadastro cancelado.");
            return;
        }

        dao.inserir(usuario);
        view.exibirMensagem("Usuário administrador (Gerente) cadastrado com sucesso!");
    }
    // Fluxo para vendedores: cria apenas usuários compradores
    public void cadastrarUsuarioComoComprador() {
        Usuario usuario = view.solicitarCadastroDeComprador();


        if (dao.buscarPorUsername(usuario.getUsername()) != null) {
            view.exibirMensagem("Erro: Username já existente. Cadastro cancelado.");
            return;
        }

        dao.inserir(usuario);
        view.exibirMensagem("Usuário comprador cadastrado com sucesso!");
    }
}
