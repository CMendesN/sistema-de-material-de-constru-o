package com.materialsystem.controller.menu;

import com.materialsystem.controller.CompradorController;
import com.materialsystem.controller.UsuarioController;
import com.materialsystem.controller.VendaController;

public class MenuVendedor implements Menu {
    public void exibirMenu() {
        System.out.println("1 - Cadastrar Comprador");
        System.out.println("2 - Gerenciar Compradores");
        System.out.println("3 - Gerenciar Vendas");        
        System.out.println("0 - Sair");
    }

    public void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> new UsuarioController().cadastrarUsuarioComoComprador();
            case 2 -> new CompradorController().gerenciarCompradores();
            case 3 -> new VendaController().gerenciarVendas();            
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }
    }
}
