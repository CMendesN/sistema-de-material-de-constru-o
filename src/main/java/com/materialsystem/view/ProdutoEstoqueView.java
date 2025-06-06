package com.materialsystem.view;

import java.util.List;

import com.materialsystem.entity.ProdutoEstoque;
import com.materialsystem.util.ConsoleInputUtils;

public class ProdutoEstoqueView {
    

    public ProdutoEstoque solicitarDadosNovoProdutoEstoque() {
        System.out.print("ID Produto: ");
        int idProduto = ConsoleInputUtils.lerInt("Escolha: ");

        System.out.print("ID Estoque: ");
        int idEstoque = ConsoleInputUtils.lerInt("Escolha: ");

        System.out.print("Quantidade: ");
        int quantidade = ConsoleInputUtils.lerInt("Escolha: ");

        // limpar buffer

        return new ProdutoEstoque(idProduto, idEstoque, quantidade);
    }

    public void exibirListaAssociacoes(List<ProdutoEstoque> lista) {
        for (ProdutoEstoque pe : lista) {
            System.out.println(pe);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
