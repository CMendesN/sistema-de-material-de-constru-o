package com.materialsystem.controller.menu;

import com.materialsystem.controller.CaixaController;

public class MenuCaixa implements Menu {
    public void exibirMenu() {
        System.out.println("1 - Gerenciar Vendas");        
        System.out.println("0 - Sair");
    }

    public void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> new CaixaController().gerenciarVendas();            
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }
    }
}
