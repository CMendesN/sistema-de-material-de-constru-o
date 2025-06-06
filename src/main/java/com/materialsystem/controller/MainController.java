package com.materialsystem.controller;

import com.materialsystem.entity.Usuario;
import com.materialsystem.util.ConsoleInputUtils;

public class MainController {

    private final LoginController loginController = new LoginController();
    private final UsuarioController usuarioController = new UsuarioController();

    public void iniciarAplicacao() {
        Usuario usuario = loginController.autenticarUsuario();

        
        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("Papel: " + usuario.getPapel());
            System.out.println("1 - Gerenciar Produtos");
            System.out.println("2 - Gerenciar Fabricantes");
            System.out.println("3 - Gerenciar Estoques");
            System.out.println("4 - Gerenciar Produto-Estoque");
            System.out.println("5 - Gerenciar Vendedores");
            System.out.println("6 - Gerenciar Compradores");
            System.out.println("7 - Gerenciar Vendas");
            System.out.println("8 - Gerenciar Itens de Venda");
            System.out.println("9 - Cadastrar Novo Usuário");  // <-- NOVO
            System.out.println("0 - Sair");

            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
            
            switch (opcao) {
                case 1 -> new ProdutoController().gerenciarProdutos();
                case 2 -> new FabricanteController().gerenciarFabricantes();
                case 3 -> new EstoqueController().gerenciarEstoques();
                case 4 -> new ProdutoEstoqueController().gerenciarProdutoEstoque();
                case 5 -> new VendedorController().gerenciarVendedores();
                case 6 -> new CompradorController().gerenciarCompradores();
                case 7 -> new VendaController().gerenciarVendas();
                case 8 -> new ItemVendaController().gerenciarItensVenda();
                case 9 -> usuarioController.cadastrarUsuario();  // <-- NOVA OPÇÃO
                case 0 -> {
                    System.out.println("Encerrando...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
