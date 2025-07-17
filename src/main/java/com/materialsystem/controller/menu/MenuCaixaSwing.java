package com.materialsystem.controller.menu;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.materialsystem.entity.Usuario;
import com.materialsystem.viewwindow.CaixaViewSwing;

public class MenuCaixaSwing implements MenuSwing {
    private final Usuario usuario;

    public MenuCaixaSwing(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public JPanel construirPainel() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));
        painel.add(criarBotao("Registrar Venda", e -> new CaixaViewSwing(usuario.getIdUsuario()).setVisible(true)));

        return painel;
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.addActionListener(listener);
        return botao;
    }
}
