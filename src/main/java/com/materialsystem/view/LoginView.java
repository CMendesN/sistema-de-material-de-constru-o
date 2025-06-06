package com.materialsystem.view;

import com.materialsystem.util.ConsoleInputUtils;

public class LoginView {
    

    public String solicitarUsername() {
        System.out.print("Usuário: ");
        return ConsoleInputUtils.lerString();
    }

    public String solicitarSenha() {
        System.out.print("Senha: ");
        return ConsoleInputUtils.lerString();
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
