package com.materialsystem.viewwindow;
import javax.swing.*;

import com.materialsystem.controller.LoginController;
import com.materialsystem.entity.Usuario;

import java.awt.*;
import java.awt.event.*;

public class LoginScreen extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtSenha;

    public LoginScreen() {
        setTitle("Login - MS Construção");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new GridLayout(3, 2, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        painelPrincipal.add(new JLabel("Usuário:"));
        txtUsuario = new JTextField();
        painelPrincipal.add(txtUsuario);

        painelPrincipal.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelPrincipal.add(txtSenha);

        JButton btnAcessar = new JButton("Acessar");
        btnAcessar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });

        painelPrincipal.add(new JLabel()); // espaço vazio
        painelPrincipal.add(btnAcessar);

        getContentPane().add(painelPrincipal, BorderLayout.CENTER);
    }

    private void autenticar() {
    	String usuarioInput = txtUsuario.getText();
        String senhaInput = new String(txtSenha.getPassword());

        LoginController controller = new LoginController();
        Usuario usuario = controller.autenticarUsuario(usuarioInput, senhaInput);

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo, " + usuario.getNome());
            dispose();
            new MenuPrincipal(usuario).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
