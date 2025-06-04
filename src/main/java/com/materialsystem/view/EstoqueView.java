package com.materialsystem.view;

import com.materialsystem.entity.Estoque;

import java.util.List;
import java.util.Scanner;

public class EstoqueView {
    private Scanner scanner = new Scanner(System.in);

    public Estoque solicitarDadosNovoEstoque() {
        System.out.print("Localização do estoque: ");
        String localizacao = scanner.nextLine();

        System.out.print("Capacidade: ");
        double capacidade = scanner.nextDouble();
        scanner.nextLine(); // limpar buffer

        return new Estoque(0, localizacao, capacidade);
    }

    public void exibirListaEstoques(List<Estoque> lista) {
        for (Estoque e : lista) {
            System.out.println(e);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
