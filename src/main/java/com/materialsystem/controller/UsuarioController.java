package com.materialsystem.controller;

import com.materialsystem.dao.UsuarioDAO;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.PasswordUtils;

import java.util.List;

public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    public boolean usernameDisponivel(String username) {
        return dao.buscarPorUsername(username) == null;
    }

    public List<String> validarSenha(String senha) {
        return PasswordUtils.validarForcaSenha(senha);
    }

    public boolean cadastrarUsuario(Usuario usuario) {
        if (!usernameDisponivel(usuario.getUsername())) {
            return false;
        }
        dao.inserir(usuario);
        return true;
    }
}
