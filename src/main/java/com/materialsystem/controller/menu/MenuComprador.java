package com.materialsystem.controller.menu;

import com.materialsystem.controller.ProdutoController;
import com.materialsystem.controller.VendaController;

public class MenuComprador implements Menu{
    public void exibirMenu() {
        System.out.println("1 - Consultar Produtos");
        System.out.println("2 - Consultar Vendas");
        System.out.println("99 - Trocar de usuário");     
        System.out.println("0 - Sair");
    }

    public void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> new ProdutoController().gerenciarProdutos();
            case 2 -> new VendaController().gerenciarVendas(); 
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }
    }
}
             