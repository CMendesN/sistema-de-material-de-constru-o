package com.materialsystem.viewwindow;

import com.materialsystem.controller.EstoqueController;
import com.materialsystem.entity.Estoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EstoqueViewSwing extends JFrame {

    private final EstoqueController controller = new EstoqueController();
    private JTable tabela;
    private DefaultTableModel modelo;

    public EstoqueViewSwing() {
        setTitle("Gerenciamento de Estoques");
        setSize(700, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Localização", "Capacidade"}, 0);
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
        List<Estoque> lista = controller.listarTodos();
        for (Estoque e : lista) {
            modelo.addRow(new Object[]{
                e.getIdEstoque(),
                e.getLocalizacao(),
                e.getCapacidade()
            });
        }
    }

    private void inserir() {
        Estoque novo = abrirDialogo(null);
        if (novo != null) {
            controller.inserir(novo);
            carregar();
        }
    }

    private void atualizar() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um estoque para atualizar.");
            return;
        }

        int id = (int) modelo.getValueAt(row, 0);
        Estoque atual = controller.buscarPorId(id);
        if (atual == null) return;

        Estoque editado = abrirDialogo(atual);
        if (editado != null) {
            controller.atualizar(editado);
            carregar();
        }
    }

    private void deletar() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um estoque para deletar.");
            return;
        }

        int id = (int) modelo.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o estoque ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletar(id);
            carregar();
        }
    }

    private Estoque abrirDialogo(Estoque estoqueAtual) {
        JTextField local = new JTextField(estoqueAtual != null ? estoqueAtual.getLocalizacao() : "");
        JTextField capacidade = new JTextField(estoqueAtual != null ? String.valueOf(estoqueAtual.getCapacidade()) : "");

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Localização:")); painel.add(local);
        painel.add(new JLabel("Capacidade:")); painel.add(capacidade);

        int result = JOptionPane.showConfirmDialog(this, painel,
                estoqueAtual == null ? "Novo Estoque" : "Atualizar Estoque",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                return new Estoque(
                        estoqueAtual != null ? estoqueAtual.getIdEstoque() : 0,
                        local.getText(),
                        Double.parseDouble(capacidade.getText())
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacidade inválida.");
            }
        }
        return null;
    }
}
