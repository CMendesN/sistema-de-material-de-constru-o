package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.Comprador;
import com.materialsystem.util.ConsoleInputUtils;

public class CompradorView {
    

    public Comprador solicitarDadosNovoComprador() {
        System.out.print("Nome: ");
        String nome= ConsoleInputUtils.lerString();

        System.out.print("CPF: ");
        String cpf= ConsoleInputUtils.lerString();

        System.out.print("Contato: ");
        String contato= ConsoleInputUtils.lerString();

        System.out.print("Endereço: ");
        String endereco= ConsoleInputUtils.lerString();

        return new Comprador(0, nome, cpf, contato, endereco);
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
