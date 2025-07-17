package com.materialsystem.controller.menu;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.materialsystem.controller.CompradorController;
import com.materialsystem.controller.EstoqueController;
import com.materialsystem.controller.FabricanteController;
import com.materialsystem.controller.ProdutoController;
import com.materialsystem.controller.ProdutoEstoqueController;
import com.materialsystem.controller.UsuarioController;
import com.materialsystem.controller.VendaController;
import com.materialsystem.controller.VendedorController;
import com.materialsystem.viewwindow.CompradorViewSwing;
import com.materialsystem.viewwindow.EstoqueViewSwing;
import com.materialsystem.viewwindow.FabricanteViewSwing;
import com.materialsystem.viewwindow.ProdutoEstoqueViewSwing;
import com.materialsystem.viewwindow.ProdutoViewSwing;
import com.materialsystem.viewwindow.UsuarioViewSwing;
import com.materialsystem.viewwindow.VendaViewSwing;
import com.materialsystem.viewwindow.VendedorViewSwing;

public class MenuGerenteSwing implements MenuSwing {

    @Override
    public JPanel construirPainel() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));

        painel.add(criarBotao("Gerenciar Produtos", e -> new ProdutoViewSwing().setVisible(true)));

        painel.add(criarBotao("Gerenciar Fabricantes", e -> new FabricanteViewSwing().setVisible(true)));

        painel.add(criarBotao("Gerenciar Estoques", e -> new EstoqueViewSwing().setVisible(true)));

        painel.add(criarBotao("Gerenciar Produto-Estoque", e -> new ProdutoEstoqueViewSwing().setVisible(true)));

        painel.add(criarBotao("Gerenciar Vendedores", e -> new VendedorViewSwing().setVisible(true)));

        painel.add(criarBotao("Gerenciar Compradores", e -> new CompradorViewSwing().setVisible(true)));

        painel.add(criarBotao("Gerenciar Vendas", e -> new VendaViewSwing().setVisible(true)));

        painel.add(criarBotao("Cadastrar Novo Usuário", e -> new UsuarioViewSwing(false, false).setVisible(true)));


        return painel;
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.addActionListener(listener);
        return botao;
    }
}
