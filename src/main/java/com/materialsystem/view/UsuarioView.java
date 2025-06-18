package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.Usuario;
import com.materialsystem.util.ConsoleInputUtils;
import com.materialsystem.util.PasswordUtils;

public class UsuarioView {
    

    // Fluxo normal (com papel sendo escolhido)
    public Usuario solicitarCadastroUsuario() {
        String nome = solicitarNome();
        String username = solicitarUsername();
        String papel = solicitarPapel();
        String senha = solicitarSenhaComValidacao();

        return new Usuario(0, nome, username, senha, papel);
    }

    // NOVO: Fluxo de cadastro inicial (sem perguntar papel)
    public Usuario solicitarCadastroInicial() {
        System.out.println("=== Cadastro Inicial de Administrador (Gerente) ===");

        String nome = solicitarNome();
        String username = solicitarUsername();
        String senha = solicitarSenhaComValidacao();

        return new Usuario(0, nome, username, senha, "Gerente");
    }
    public Usuario solicitarCadastroDeComprador() {       
        String nome = solicitarNome();
        String username = solicitarUsername();
        String senha = solicitarSenhaComValidacao();

        return new Usuario(0, nome, username, senha, "Comprador");
    }

    private String solicitarNome() {
        System.out.print("Nome completo: ");
        return ConsoleInputUtils.lerString();
    }

    private String solicitarUsername() {
        System.out.print("Username (único): ");
        return ConsoleInputUtils.lerString();
    }

    private String solicitarPapel() {
        while (true) {
            System.out.print("Papel (Gerente, Vendedor, Comprador, Caixa): ");
            String papel = ConsoleInputUtils.lerString().trim();

            if (papel.equalsIgnoreCase("Gerente") || papel.equalsIgnoreCase("Vendedor")
                    || papel.equalsIgnoreCase("Comprador") || papel.equalsIgnoreCase("Caixa")) {
                return papel;
            } else {
                System.out.println("Papel inválido. Tente novamente.");
            }
        }
    }

    private String solicitarSenhaComValidacao() {
        String senha;

        while (true) {
            System.out.print("Senha: ");
            senha= ConsoleInputUtils.lerString();

            List<String> erros = PasswordUtils.validarForcaSenha(senha);

            if (erros.isEmpty()) {
                return senha;
            }

            System.out.println("A senha não atende aos requisitos:");
            erros.forEach(System.out::println);

            System.out.print("Deseja tentar novamente? (S/N): ");
            String opcao= ConsoleInputUtils.lerString();
            if (!opcao.equalsIgnoreCase("S")) {
                System.out.println("Cadastro de usuário cancelado.");
                System.exit(0);
            }
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
