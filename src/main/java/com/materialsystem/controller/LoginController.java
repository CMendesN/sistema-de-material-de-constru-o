package com.materialsystem.controller;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.PasswordUtils;
import com.materialsystem.view.LoginView;

public class LoginController {

    private final LoginView view = new LoginView();
    private final UsuarioDAO dao = new UsuarioDAO();

    public Usuario autenticarUsuario() {
        while (true) {
            view.exibirMensagem("\n=== Login ===");
            String username = view.solicitarUsername();
            String senha = view.solicitarSenha();

            Usuario usuario = dao.buscarPorUsername(username);

            if (usuario != null && PasswordUtils.verificarSenha(senha, usuario.getSenha())) {
                view.exibirMensagem("Login realizado com sucesso! Bem-vindo, " + usuario.getNome());
                return usuario;
            } else {
                view.exibirMensagem("Usuário ou senha inválidos.");
            }
        }
    }
}
