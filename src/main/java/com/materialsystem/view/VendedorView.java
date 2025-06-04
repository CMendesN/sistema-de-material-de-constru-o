package com.materialsystem.view;

import com.materialsystem.entity.Vendedor;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class VendedorView {
    private Scanner scanner = new Scanner(System.in);

    public Vendedor solicitarDadosNovoVendedor() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Contato: ");
        String contato = scanner.nextLine();

        System.out.print("Salário: ");
        double salario = scanner.nextDouble();

        System.out.print("Data de contratação (AAAA-MM-DD): ");
        String dataStr = scanner.next();
        LocalDate dataContratacao = LocalDate.parse(dataStr);

        System.out.print("ID do usuário (ou 0 se não houver): ");
        int idUsuario = scanner.nextInt();
        scanner.nextLine();

        Integer idUsuarioFinal = (idUsuario == 0) ? null : idUsuario;

        return new Vendedor(0, nome, cpf, contato, salario, dataContratacao, idUsuarioFinal);
    }

    public void exibirListaVendedores(List<Vendedor> lista) {
        for (Vendedor v : lista) {
            System.out.println(v);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
