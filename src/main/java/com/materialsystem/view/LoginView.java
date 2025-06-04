package com.materialsystem.view;

import java.util.Scanner;

public class LoginView {
    private Scanner scanner = new Scanner(System.in);

    public String solicitarUsername() {
        System.out.print("Usuário: ");
        return scanner.nextLine();
    }

    public String solicitarSenha() {
        System.out.print("Senha: ");
        return scanner.nextLine();
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
