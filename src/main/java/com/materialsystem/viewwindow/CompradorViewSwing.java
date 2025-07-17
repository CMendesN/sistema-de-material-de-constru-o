package com.materialsystem.viewwindow;

import com.materialsystem.controller.CompradorController;
import com.materialsystem.entity.Comprador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CompradorViewSwing extends JFrame {

    private final CompradorController controller = new CompradorController();
    private JTable tabela;
    private DefaultTableModel modelo;

    public CompradorViewSwing() {
        setTitle("Gerenciamento de Compradores");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Contato", "Endereço"}, 0);
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnInserir = new JButton("Inserir");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnRecarregar = new JButton("Recarregar");

        botoes.add(btnInserir);
        botoes.add(btnAtualizar);
        botoes.add(btnDeletar);
        botoes.add(btnRecarregar);
        add(botoes, BorderLayout.SOUTH);

        btnInserir.addActionListener(e -> inserir());
        btnAtualizar.addActionListener(e -> atualizar());
        btnDeletar.addActionListener(e -> deletar());
        btnRecarregar.addActionListener(e -> carregar());

        carregar();
    }

    private void carregar() {
        modelo.setRowCount(0);
        List<Comprador> lista = controller.listarTodos();
        for (Comprador c : lista) {
            modelo.addRow(new Object[]{
                c.getIdComprador(),
                c.getNome(),
                c.getCpf(),
                c.getContato(),
                c.getEndereco()
            });
        }
    }

    private void inserir() {
        Comprador novo = abrirDialogo(null);
        if (novo != null) {
            controller.inserir(novo);
            carregar();
        }
    }

    private void atualizar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um comprador para atualizar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        Comprador atual = controller.buscarPorId(id);
        if (atual == null) return;

        Comprador editado = abrirDialogo(atual);
        if (editado != null) {
            controller.atualizar(editado);
            carregar();
        }
    }

    private void deletar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um comprador para deletar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o comprador ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletar(id);
            carregar();
        }
    }

    private Comprador abrirDialogo(Comprador comprador) {
        JTextField nome = new JTextField(comprador != null ? comprador.getNome() : "");
        JTextField cpf = new JTextField(comprador != null ? comprador.getCpf() : "");
        JTextField contato = new JTextField(comprador != null ? comprador.getContato() : "");
        JTextField endereco = new JTextField(comprador != null ? comprador.getEndereco() : "");

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:")); painel.add(nome);
        painel.add(new JLabel("CPF:")); painel.add(cpf);
        painel.add(new JLabel("Contato:")); painel.add(contato);
        painel.add(new JLabel("Endereço:")); painel.add(endereco);

        int resultado = JOptionPane.showConfirmDialog(this, painel,
                comprador == null ? "Novo Comprador" : "Atualizar Comprador", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            return new Comprador(
                    comprador != null ? comprador.getIdComprador() : 0,
                    nome.getText(),
                    cpf.getText(),
                    contato.getText(),
                    endereco.getText()
            );
        }
        return null;
    }
}
