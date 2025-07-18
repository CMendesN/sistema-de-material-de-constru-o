package com.materialsystem.viewwindow;

import com.materialsystem.controller.ProdutoEstoqueController;
import com.materialsystem.dao.EstoqueDAO;
import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.dao.ProdutoEstoqueDAO;
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
        JButton btnRemoverAssociacao = new JButton("Remover Associação");
        JButton btnRecarregar = new JButton("Limpar Tabela");

        botoes.add(btnInserir);
        botoes.add(btnBuscar);
        botoes.add(btnRemoverAssociacao);
        botoes.add(btnRecarregar);
        add(botoes, BorderLayout.SOUTH);

        btnInserir.addActionListener(e -> inserir());
        btnBuscar.addActionListener(e -> buscarPorProduto());
        btnRecarregar.addActionListener(e -> modelo.setRowCount(0));
        btnRemoverAssociacao.addActionListener(e -> removerAssociacao());
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
                int idProd = Integer.parseInt(idProduto.getText());
                int idEst = Integer.parseInt(idEstoque.getText());
                int qtd = Integer.parseInt(quantidade.getText());

                ProdutoDAO produtoDAO = new ProdutoDAO();
                EstoqueDAO estoqueDAO = new EstoqueDAO();

                if (produtoDAO.buscarPorId(idProd) == null) {
                    JOptionPane.showMessageDialog(this, "Erro: Produto com ID " + idProd + " não existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (estoqueDAO.buscarPorId(idEst) == null) {
                    JOptionPane.showMessageDialog(this, "Erro: Estoque com ID " + idEst + " não existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ProdutoEstoque pe = new ProdutoEstoque(idProd, idEst, qtd);

                if (controller.inserir(pe)) {
                    JOptionPane.showMessageDialog(this, "Associação inserida com sucesso!");
                    modelo.addRow(new Object[]{pe.getIdProduto(), pe.getIdEstoque(), pe.getQuantidade()});
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: associação já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Campos numéricos inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
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
    private void removerAssociacao() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            int idProduto = (int) modelo.getValueAt(linha, 0);
            int idEstoque = (int) modelo.getValueAt(linha, 1);

            int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente remover a associação Produto-Estoque selecionada?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                new ProdutoEstoqueDAO().remover(idProduto, idEstoque);
                JOptionPane.showMessageDialog(this, "Associação removida com sucesso.");
                atualizarTabela(); // Recarrega a tabela para refletir a remoção
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma linha da tabela para remover.");
        }
    }
    private void atualizarTabela() {
        modelo.setRowCount(0);
        List<ProdutoEstoque> lista = new ProdutoEstoqueDAO().buscarTodos();
        for (ProdutoEstoque pe : lista) {
            modelo.addRow(new Object[]{
                pe.getIdProduto(), pe.getIdEstoque(), pe.getQuantidade()
            });
        }
    }


}
