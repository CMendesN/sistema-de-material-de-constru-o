package com.materialsystem.viewwindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("MS Construção - Bem-vindo");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Painel central para imagem e texto
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        // Adiciona a imagem (ajuste o caminho se necessário)
        ImageIcon logo = new ImageIcon("/sistema-de-material-de-constru-o/image/logo159.png"); // substitua pelo caminho correto
        JLabel logoLabel = new JLabel(new ImageIcon("/home/carlos/eclipse-workspace/sistema-de-material-de-constru-o/image/logo159.png"));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(logoLabel, BorderLayout.CENTER);

        // Texto abaixo do logo
        JLabel title = new JLabel("MS Construção - Sistema de Materiais de Construção");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        centerPanel.add(title, BorderLayout.SOUTH);

        getContentPane().add(centerPanel, BorderLayout.CENTER);

        // Botão entrar
        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // fecha a tela atual
                new LoginScreen().setVisible(true); // abre a tela de login
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnEntrar);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen().setVisible(true));
    }
}