package com.materialsystem.view;

import com.materialsystem.entity.Venda;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class VendaView {
    private Scanner scanner = new Scanner(System.in);

    public Venda solicitarDadosNovaVenda() {
        System.out.print("ID do Vendedor (ou 0 se não houver): ");
        int idVendedor = scanner.nextInt();

        System.out.print("ID do Comprador (ou 0 se não houver): ");
        int idComprador = scanner.nextInt();

        System.out.print("Valor total: ");
        double valorTotal = scanner.nextDouble();
        scanner.nextLine();

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
