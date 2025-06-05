package com.materialsystem.controller;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.view.UsuarioView;

public class UsuarioController {

    private final UsuarioView view = new UsuarioView();
    private final UsuarioDAO dao = new UsuarioDAO();

    public void cadastrarUsuario() {
        Usuario usuario = view.solicitarCadastroUsuario();

        // Verifica se username já existe
        if (dao.buscarPorUsername(usuario.getUsername()) != null) {
            view.exibirMensagem("Erro: Username já existente. Cadastro cancelado.");
            return;
        }

        dao.inserir(usuario);
        view.exibirMensagem("Usuário cadastrado com sucesso!");
    }
}
