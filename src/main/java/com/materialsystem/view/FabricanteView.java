package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.Fabricante;
import com.materialsystem.util.ConsoleInputUtils;

public class FabricanteView {
    

    public Fabricante solicitarDadosNovoFabricante() {
        System.out.print("Nome do fabricante: ");
        String nome= ConsoleInputUtils.lerString();

        System.out.print("Contato: ");
        String contato= ConsoleInputUtils.lerString();

        System.out.print("Endereço: ");
        String endereco= ConsoleInputUtils.lerString();

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
