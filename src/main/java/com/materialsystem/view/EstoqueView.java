package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.Estoque;
import com.materialsystem.util.ConsoleInputUtils;

public class EstoqueView {
    

    public Estoque solicitarDadosNovoEstoque() {
        System.out.print("Localização do estoque: ");
        String localizacao= ConsoleInputUtils.lerString();

        double capacidade = ConsoleInputUtils.lerDouble("Capacidade: ");
        

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
    public Estoque solicitarDadosAtualizacao(Estoque estoqueAtual) {
        System.out.println("Atualizando estoque ID: " + estoqueAtual.getIdEstoque());

        System.out.print("Localização [" + estoqueAtual.getLocalizacao() + "]: ");
        String localInput = ConsoleInputUtils.lerString();
        String localizacao = localInput.isBlank() ? estoqueAtual.getLocalizacao() : localInput;

        System.out.print("Capacidade [" + estoqueAtual.getCapacidade() + "]: ");
        String capInput = ConsoleInputUtils.lerString();
        double capacidade;
        try {
            capacidade = capInput.isBlank() ? estoqueAtual.getCapacidade() : Double.parseDouble(capInput);
        } catch (NumberFormatException e) {
            capacidade = estoqueAtual.getCapacidade();
        }

        return new Estoque(estoqueAtual.getIdEstoque(), localizacao, capacidade);
    }

}
