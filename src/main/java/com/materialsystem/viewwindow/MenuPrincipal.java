package com.materialsystem.viewwindow;

import com.materialsystem.controller.menu.*;

import javax.swing.*;
import java.awt.*;
import com.materialsystem.entity.Usuario;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal(Usuario usuario) {
        setTitle("Menu Principal - MS Construção");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel boasVindas = new JLabel("Bem-vindo, " + usuario.getNome() + " (" + usuario.getPapel() + ")");
        boasVindas.setFont(new Font("Arial", Font.BOLD, 16));
        boasVindas.setHorizontalAlignment(SwingConstants.CENTER);
        boasVindas.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(boasVindas, BorderLayout.NORTH);

        MenuSwing menuSwing = construirMenuPorPapel(usuario);
        add(menuSwing.construirPainel(), BorderLayout.CENTER);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });

        add(btnSair, BorderLayout.SOUTH);
    }

    private MenuSwing construirMenuPorPapel(Usuario usuario) {
        return switch (usuario.getPapel().toLowerCase()) {
            case "gerente" -> new MenuGerenteSwing();
            case "vendedor" -> new MenuVendedorSwing();
            case "comprador" -> new MenuCompradorSwing(usuario);
            case "caixa" -> new MenuCaixaSwing(usuario);
            default -> () -> new JPanel();
        };
    }

}