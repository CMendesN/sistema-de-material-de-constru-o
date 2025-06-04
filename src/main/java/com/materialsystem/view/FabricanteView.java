package com.materialsystem.view;

import com.materialsystem.entity.Fabricante;

import java.util.List;
import java.util.Scanner;

public class FabricanteView {
    private Scanner scanner = new Scanner(System.in);

    public Fabricante solicitarDadosNovoFabricante() {
        System.out.print("Nome do fabricante: ");
        String nome = scanner.nextLine();

        System.out.print("Contato: ");
        String contato = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        return new Fabricante(0, nome, contato, endereco);
    }

    public void exibirListaFabricantes(List<Fabricante> lista) {
        for (Fabricante f : lista) {
            System.out.println(f);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
