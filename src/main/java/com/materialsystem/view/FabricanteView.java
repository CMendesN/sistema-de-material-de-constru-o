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
    public Fabricante solicitarDadosAtualizacao(Fabricante fabricanteExistente) {
        System.out.println("Atualizando fabricante ID: " + fabricanteExistente.getIdFabricante());

        System.out.print("Nome [" + fabricanteExistente.getNomeFabricante() + "]: ");
        String nomeInput = ConsoleInputUtils.lerString();
        String nome = nomeInput.isBlank() ? fabricanteExistente.getNomeFabricante() : nomeInput;

        System.out.print("Contato [" + fabricanteExistente.getContato() + "]: ");
        String contatoInput = ConsoleInputUtils.lerString();
        String contato = contatoInput.isBlank() ? fabricanteExistente.getContato() : contatoInput;

        System.out.print("Endereço [" + fabricanteExistente.getEndereco() + "]: ");
        String enderecoInput = ConsoleInputUtils.lerString();
        String endereco = enderecoInput.isBlank() ? fabricanteExistente.getEndereco() : enderecoInput;

        return new Fabricante(fabricanteExistente.getIdFabricante(), nome, contato, endereco);
    }

}
