package com.materialsystem.controller.menu;

import com.materialsystem.controller.CompradorController;
import com.materialsystem.controller.UsuarioController;
import com.materialsystem.controller.VendaController;
import com.materialsystem.viewwindow.CompradorViewSwing;
import com.materialsystem.viewwindow.UsuarioViewSwing;
import com.materialsystem.viewwindow.VendaViewSwing;

import javax.swing.*;
import java.awt.*;

public class MenuVendedorSwing implements MenuSwing {
   

	@Override
    public JPanel construirPainel() {
        JPanel painel = new JPanel(new GridLayout(0, 1, 10, 10));
        painel.add(criarBotao("Cadastrar Comprador", e -> new UsuarioViewSwing(true, false).setVisible(true)));

        painel.add(criarBotao("Gerenciar Compradores", e -> new CompradorViewSwing().setVisible(true)));
        painel.add(criarBotao("Gerenciar Vendas", e -> new VendaViewSwing().setVisible(true)));
        return painel;
    }

    private JButton criarBotao(String texto, java.awt.event.ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.addActionListener(listener);
        return botao;
    }
}
