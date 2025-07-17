package com.materialsystem.viewwindow;

import com.materialsystem.controller.ProdutoController;
import com.materialsystem.entity.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoViewSwing extends JFrame {

    private ProdutoController controller = new ProdutoController();
    private JTable tabela;
    private DefaultTableModel modelo;

    public ProdutoViewSwing() {
        setTitle("Gerenciamento de Produtos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Modelo da tabela
        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Preço", "Qtd", "Fabricante", "Categoria"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel();
        JButton btnInserir = new JButton("Inserir");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnDeletar = new JButton("Deletar");
        JButton btnRecarregar = new JButton("Recarregar");

        painelBotoes.add(btnInserir);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnRecarregar);
        add(painelBotoes, BorderLayout.SOUTH);

        // Ações dos botões
        btnInserir.addActionListener(e -> inserirProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnDeletar.addActionListener(e -> deletarProduto());
        btnRecarregar.addActionListener(e -> carregarProdutos());

        carregarProdutos();
    }

    private void carregarProdutos() {
        modelo.setRowCount(0);
        List<Produto> produtos = controller.listarProdutos();
        for (Produto p : produtos) {
            modelo.addRow(new Object[]{
                p.getIdProduto(),
                p.getNome(),
                p.getDescricao(),
                p.getPrecoUnitario(),
                p.getQuantidadeEmEstoque(),
                p.getIdFabricante(),
                p.getCategoria()
            });
        }
    }

    private void inserirProduto() {
        Produto produto = abrirDialogoProduto(null);
        if (produto != null) {
            controller.inserirProduto(produto);
            carregarProdutos();
        }
    }

    private void atualizarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para atualizar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        Produto produtoAtual = controller.buscarPorId(id);
        Produto atualizado = abrirDialogoProduto(produtoAtual);

        if (atualizado != null) {
            controller.atualizarProduto(atualizado);
            carregarProdutos();
        }
    }

    private void deletarProduto() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para deletar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        int opcao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o produto ID " + id + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (opcao == JOptionPane.YES_OPTION) {
            controller.deletarProduto(id);
            carregarProdutos();
        }
    }

    private Produto abrirDialogoProduto(Produto produtoAtual) {
        JTextField nomeField = new JTextField(produtoAtual != null ? produtoAtual.getNome() : "");
        JTextField descField = new JTextField(produtoAtual != null ? produtoAtual.getDescricao() : "");
        JTextField precoField = new JTextField(produtoAtual != null ? String.valueOf(produtoAtual.getPrecoUnitario()) : "");
        JTextField qtdField = new JTextField(produtoAtual != null ? String.valueOf(produtoAtual.getQuantidadeEmEstoque()) : "");
        JTextField fabField = new JTextField(produtoAtual != null ? String.valueOf(produtoAtual.getIdFabricante()) : "");
        JTextField catField = new JTextField(produtoAtual != null ? produtoAtual.getCategoria() : "");

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:")); painel.add(nomeField);
        painel.add(new JLabel("Descrição:")); painel.add(descField);
        painel.add(new JLabel("Preço Unitário:")); painel.add(precoField);
        painel.add(new JLabel("Quantidade em Estoque:")); painel.add(qtdField);
        painel.add(new JLabel("ID Fabricante:")); painel.add(fabField);
        painel.add(new JLabel("Categoria:")); painel.add(catField);

        int resultado = JOptionPane.showConfirmDialog(this, painel,
                produtoAtual == null ? "Novo Produto" : "Atualizar Produto", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                return new Produto(
                        produtoAtual != null ? produtoAtual.getIdProduto() : 0,
                        nomeField.getText(),
                        descField.getText(),
                        Double.parseDouble(precoField.getText()),
                        Integer.parseInt(qtdField.getText()),
                        Integer.parseInt(fabField.getText()),
                        catField.getText()
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valores inválidos nos campos numéricos.");
            }
        }
        return null;
    }
}

