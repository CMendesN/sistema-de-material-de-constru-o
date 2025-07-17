package com.materialsystem.viewwindow;

import com.materialsystem.controller.ProdutoEstoqueController;
import com.materialsystem.entity.ProdutoEstoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoEstoqueViewSwing extends JFrame {

    private final ProdutoEstoqueController controller = new ProdutoEstoqueController();
    private JTable tabela;
    private DefaultTableModel modelo;

    public ProdutoEstoqueViewSwing() {
        setTitle("Associação Produto ↔ Estoque");
        setSize(700, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID Produto", "ID Estoque", "Quantidade"}, 0);
        tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnInserir = new JButton("Inserir Associação");
        JButton btnBuscar = new JButton("Buscar por Produto");
        JButton btnRecarregar = new JButton("Limpar Tabela");

        botoes.add(btnInserir);
        botoes.add(btnBuscar);
        botoes.add(btnRecarregar);
        add(botoes, BorderLayout.SOUTH);

        btnInserir.addActionListener(e -> inserir());
        btnBuscar.addActionListener(e -> buscarPorProduto());
        btnRecarregar.addActionListener(e -> modelo.setRowCount(0));
    }

    private void inserir() {
        JTextField idProduto = new JTextField();
        JTextField idEstoque = new JTextField();
        JTextField quantidade = new JTextField();

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("ID Produto:")); painel.add(idProduto);
        painel.add(new JLabel("ID Estoque:")); painel.add(idEstoque);
        painel.add(new JLabel("Quantidade:")); painel.add(quantidade);

        int res = JOptionPane.showConfirmDialog(this, painel, "Nova Associação", JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            try {
                ProdutoEstoque pe = new ProdutoEstoque(
                        Integer.parseInt(idProduto.getText()),
                        Integer.parseInt(idEstoque.getText()),
                        Integer.parseInt(quantidade.getText())
                );

                if (controller.inserir(pe)) {
                    JOptionPane.showMessageDialog(this, "Associação inserida com sucesso!");
                    modelo.addRow(new Object[]{pe.getIdProduto(), pe.getIdEstoque(), pe.getQuantidade()});
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: associação já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Campos numéricos inválidos.");
            }
        }
    }

    private void buscarPorProduto() {
        String input = JOptionPane.showInputDialog(this, "Informe o ID do Produto:");
        if (input == null || input.isBlank()) return;

        try {
            int idProduto = Integer.parseInt(input);
            List<ProdutoEstoque> lista = controller.buscarPorProduto(idProduto);
            modelo.setRowCount(0);
            for (ProdutoEstoque pe : lista) {
                modelo.addRow(new Object[]{
                        pe.getIdProduto(),
                        pe.getIdEstoque(),
                        pe.getQuantidade()
                });
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }
}
