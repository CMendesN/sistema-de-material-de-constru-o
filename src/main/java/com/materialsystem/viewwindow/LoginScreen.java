package com.materialsystem.viewwindow;
import javax.swing.*;
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
        String usuario = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        // Exemplo fixo. Substitua por verificação no banco via DAO futuramente.
        if (usuario.equals("admin") && senha.equals("123")) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            // Aqui você pode abrir o menu principal
            dispose();
            new MenuPrincipal(usuario).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
