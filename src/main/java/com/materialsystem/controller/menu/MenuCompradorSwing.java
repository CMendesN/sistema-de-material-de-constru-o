package com.materialsystem.controller.menu;

import com.materialsystem.entity.Usuario;
import com.materialsystem.viewwindow.ProdutoViewSwing;
import com.materialsystem.viewwindow.VendaViewSwing;

import javax.swing.*;
import java.awt.*;
public class MenuCompradorSwing implements MenuSwing {
    private final Usuario usuario;

    public MenuCompradorSwing(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public JPanel construirPainel() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));

        painel.add(criarBotao("Consultar Produtos", e -> new ProdutoViewSwing().setVisible(true)));
        painel.add(criarBotao("Consultar Minhas Compras", e -> new VendaViewSwing(usuario).setVisible(true)));

        return painel;
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.addActionListener(listener);
        return botao;
    }
}
