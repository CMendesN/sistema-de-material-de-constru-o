package com.materialsystem.viewwindow;

import com.materialsystem.controller.FabricanteController;
import com.materialsystem.entity.Fabricante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FabricanteViewSwing extends JFrame {

    private final FabricanteController controller = new FabricanteController();
    private JTable tabela;
    private DefaultTableModel modelo;

    public FabricanteViewSwing() {
        setTitle("Gerenciamento de Fabricantes");
        setSize(700, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Contato", "Endereço"}, 0);
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
        List<Fabricante> lista = controller.listarTodos();
        for (Fabricante f : lista) {
            modelo.addRow(new Object[]{
                f.getIdFabricante(),
                f.getNomeFabricante(),
                f.getContato(),
                f.getEndereco()
            });
        }
    }

    private void inserir() {
        Fabricante novo = abrirDialogo(null);
        if (novo != null) {
            controller.inserir(novo);
            carregar();
        }
    }

    private void atualizar() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fabricante para atualizar.");
            return;
        }

        int id = (int) modelo.getValueAt(row, 0);
        Fabricante atual = controller.buscarPorId(id);
        if (atual == null) return;

        Fabricante editado = abrirDialogo(atual);
        if (editado != null) {
            controller.atualizar(editado);
            carregar();
        }
    }

    private void deletar() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um fabricante para deletar.");
            return;
        }

        int id = (int) modelo.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o fabricante ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletar(id);
            carregar();
        }
    }

    private Fabricante abrirDialogo(Fabricante fabricante) {
        JTextField nome = new JTextField(fabricante != null ? fabricante.getNomeFabricante() : "");
        JTextField contato = new JTextField(fabricante != null ? fabricante.getContato() : "");
        JTextField endereco = new JTextField(fabricante != null ? fabricante.getEndereco() : "");

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:")); painel.add(nome);
        painel.add(new JLabel("Contato:")); painel.add(contato);
        painel.add(new JLabel("Endereço:")); painel.add(endereco);

        int result = JOptionPane.showConfirmDialog(this, painel,
                fabricante == null ? "Novo Fabricante" : "Atualizar Fabricante",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return new Fabricante(
                    fabricante != null ? fabricante.getIdFabricante() : 0,
                    nome.getText(),
                    contato.getText(),
                    endereco.getText()
            );
        }
        return null;
    }
}
