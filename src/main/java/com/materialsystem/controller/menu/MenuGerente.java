package com.materialsystem.controller.menu;

import com.materialsystem.controller.CompradorController;
import com.materialsystem.controller.EstoqueController;
import com.materialsystem.controller.FabricanteController;
import com.materialsystem.controller.ProdutoController;
import com.materialsystem.controller.ProdutoEstoqueController;
import com.materialsystem.controller.UsuarioController;
import com.materialsystem.controller.VendaController;
import com.materialsystem.controller.VendedorController;

public class MenuGerente implements Menu {
    public void exibirMenu() {
        System.out.println("1 - Gerenciar Produtos");
        System.out.println("2 - Gerenciar Fabricantes");
        System.out.println("3 - Gerenciar Estoques");
        System.out.println("4 - Gerenciar Produto-Estoque");
        System.out.println("5 - Gerenciar Vendedores");
        System.out.println("6 - Gerenciar Compradores");
        System.out.println("7 - Gerenciar Vendas");
        System.out.println("8 - Cadastrar Novo Usuário");
        System.out.println("99 - Trocar de usuário");     
        System.out.println("0 - Sair");
    }

    public void executarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> new ProdutoController().gerenciarProdutos();
            case 2 -> new FabricanteController().gerenciarFabricantes();
            case 3 -> new EstoqueController().gerenciarEstoques();
            case 4 -> new ProdutoEstoqueController().gerenciarProdutoEstoque();
            case 5 -> new VendedorController().gerenciarVendedores();
            case 6 -> new CompradorController().gerenciarCompradores();
            case 7 -> new VendaController().gerenciarVendas();
            case 8 -> new UsuarioController().cadastrarUsuario();
            case 0 -> System.exit(0);
            default -> System.out.println("Opção inválida.");
        }
    }
}