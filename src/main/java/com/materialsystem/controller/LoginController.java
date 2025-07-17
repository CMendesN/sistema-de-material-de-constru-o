package com.materialsystem.controller;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.PasswordUtils;


public class LoginController {

    
    private final UsuarioDAO dao = new UsuarioDAO();

    public Usuario autenticarUsuario(String username, String senha) {
        Usuario usuario = dao.buscarPorUsername(username);

        if (usuario != null && PasswordUtils.verificarSenha(senha, usuario.getSenha())) {
            return usuario;
        }
        return null;
    }
}
