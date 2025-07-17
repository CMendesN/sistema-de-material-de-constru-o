package com.materialsystem.viewwindow;

import com.materialsystem.controller.UsuarioController;
import com.materialsystem.entity.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UsuarioViewSwing extends JFrame {

    private final UsuarioController controller = new UsuarioController();
    private final boolean somenteComprador;
    private final boolean cadastroInicial;

    public UsuarioViewSwing(boolean somenteComprador, boolean cadastroInicial) {
        this.somenteComprador = somenteComprador;
        this.cadastroInicial = cadastroInicial;

        setTitle("Cadastro de Usuário");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 10, 5));

        JLabel lblNome = new JLabel("Nome completo:");
        JTextField txtNome = new JTextField();

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();

        JLabel lblSenha = new JLabel("Senha:");
        JPasswordField txtSenha = new JPasswordField();

        JLabel lblPapel = new JLabel("Papel:");
        String[] papeis = {"Gerente", "Vendedor", "Comprador", "Caixa"};
        JComboBox<String> cbPapel = new JComboBox<>(papeis);

        if (somenteComprador) {
            cbPapel.setSelectedItem("Comprador");
            cbPapel.setEnabled(false);
        }
        if (cadastroInicial) {
            cbPapel.setSelectedItem("Gerente");
            cbPapel.setEnabled(false);
        }

        JButton btnCadastrar = new JButton("Cadastrar");

        btnCadastrar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String username = txtUsername.getText().trim();
            String senha = new String(txtSenha.getPassword()).trim();
            String papel = (String) cbPapel.getSelectedItem();

            if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
                return;
            }

            if (!controller.usernameDisponivel(username)) {
                JOptionPane.showMessageDialog(this, "Username já existe.");
                return;
            }

            List<String> errosSenha = controller.validarSenha(senha);
            if (!errosSenha.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Senha fraca:\n" + String.join("\n", errosSenha),
                        "Validação de Senha", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario usuario = new Usuario(0, nome, username, senha, papel);
            boolean sucesso = controller.cadastrarUsuario(usuario);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar.");
            }
        });

        add(lblNome); add(txtNome);
        add(lblUsername); add(txtUsername);
        add(lblSenha); add(txtSenha);
        add(lblPapel); add(cbPapel);
        add(btnCadastrar);
    }
}
