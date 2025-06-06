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
}
