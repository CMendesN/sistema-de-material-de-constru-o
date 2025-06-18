package com.materialsystem.controller;

import com.materialsystem.controller.menu.Menu;
import com.materialsystem.controller.menu.MenuCaixa;
import com.materialsystem.controller.menu.MenuComprador;
import com.materialsystem.controller.menu.MenuGerente;
import com.materialsystem.controller.menu.MenuVendedor;
import com.materialsystem.entity.Usuario;
import com.materialsystem.util.ConsoleInputUtils;

public class MainController {

    public void iniciarAplicacao() {
        Usuario usuario = new LoginController().autenticarUsuario();
        Menu menu = criarMenuPorPapel(usuario.getPapel());

        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("Papel: " + usuario.getPapel());
            menu.exibirMenu();
            int opcao = ConsoleInputUtils.lerInt("Escolha: ");
            menu.executarOpcao(opcao);
        }
    }

    private Menu criarMenuPorPapel(String papel) {
        return switch (papel.toLowerCase()) {
            case "gerente" -> new MenuGerente();
            case "vendedor" -> new MenuVendedor();
            case "comprador" -> new MenuComprador();
            case "caixa" -> new MenuCaixa();
            default -> throw new IllegalArgumentException("Papel inválido: " + papel);
        };
    }
}
