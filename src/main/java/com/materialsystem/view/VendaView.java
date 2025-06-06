package com.materialsystem.view;

import java.time.LocalDateTime;
import java.util.List;

import com.materialsystem.entity.Venda;
import com.materialsystem.util.ConsoleInputUtils;

public class VendaView {
    

    public Venda solicitarDadosNovaVenda() {
        System.out.print("ID do Vendedor (ou 0 se não houver): ");
        int idVendedor = ConsoleInputUtils.lerInt("Escolha: ");

        System.out.print("ID do Comprador (ou 0 se não houver): ");
        int idComprador = ConsoleInputUtils.lerInt("Escolha: ");

        
        double valorTotal = ConsoleInputUtils.lerDouble("Valor total: ");
        
        Integer idVendedorFinal = (idVendedor == 0) ? null : idVendedor;
        Integer idCompradorFinal = (idComprador == 0) ? null : idComprador;

        LocalDateTime dataVenda = LocalDateTime.now();

        return new Venda(0, dataVenda, idVendedorFinal, idCompradorFinal, valorTotal);
    }

    public void exibirListaVendas(List<Venda> lista) {
        for (Venda v : lista) {
            System.out.println(v);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
