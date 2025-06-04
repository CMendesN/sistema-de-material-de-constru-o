package com.materialsystem.view;

import com.materialsystem.entity.Comprador;

import java.util.List;
import java.util.Scanner;

public class CompradorView {
    private Scanner scanner = new Scanner(System.in);

    public Comprador solicitarDadosNovoComprador() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Contato: ");
        String contato = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        System.out.print("ID do usuário (ou 0 se não houver): ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();

        Integer idUsuarioFinal = (idUsuario == 0) ? null : idUsuario;

        return new Comprador(0, nome, cpf, contato, endereco, idUsuarioFinal);
    }

    public void exibirListaCompradores(List<Comprador> lista) {
        for (Comprador c : lista) {
            System.out.println(c);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
