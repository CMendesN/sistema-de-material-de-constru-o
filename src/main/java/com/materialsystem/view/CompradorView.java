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
    public Comprador solicitarDadosAtualizacao(Comprador compradorExistente) {
        System.out.println("Atualizando comprador ID: " + compradorExistente.getIdComprador());

        System.out.print("Nome [" + compradorExistente.getNome() + "]: ");
        String nomeInput = ConsoleInputUtils.lerString();
        String nome = nomeInput.isBlank() ? compradorExistente.getNome() : nomeInput;

        System.out.print("CPF [" + compradorExistente.getCpf() + "]: ");
        String cpfInput = ConsoleInputUtils.lerString();
        String cpf = cpfInput.isBlank() ? compradorExistente.getCpf() : cpfInput;

        System.out.print("Contato [" + compradorExistente.getContato() + "]: ");
        String contatoInput = ConsoleInputUtils.lerString();
        String contato = contatoInput.isBlank() ? compradorExistente.getContato() : contatoInput;

        System.out.print("Endereço [" + compradorExistente.getEndereco() + "]: ");
        String enderecoInput = ConsoleInputUtils.lerString();
        String endereco = enderecoInput.isBlank() ? compradorExistente.getEndereco() : enderecoInput;

        return new Comprador(compradorExistente.getIdComprador(), nome, cpf, contato, endereco);
    }

}
