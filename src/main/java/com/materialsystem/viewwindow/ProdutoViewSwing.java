package com.materialsystem.viewwindow;

import com.materialsystem.controller.ProdutoController;
import com.materialsystem.dao.EstoqueDAO;
import com.materialsystem.dao.ProdutoEstoqueDAO;
import com.materialsystem.entity.Estoque;
import com.materialsystem.entity.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoViewSwing extends JFrame {

    private final ProdutoController controller = new ProdutoController();
    private final JTable tabela;
    private final DefaultTableModel modelo;
    private final String papel;

    private final JButton btnInserir = new JButton("Inserir");
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JButton btnDeletar = new JButton("Deletar");
    private final JButton btnRecarregar = new JButton("Recarregar");
    private final JButton btnRemoverAssociacao = new JButton("Remover Associação Estoque");

    public ProdutoViewSwing(String papel) {
        this.papel = papel;

        setTitle("Gerenciamento de Produtos");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Preço", "Qtd", "Fabricante", "Categoria"}, 0);
        tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnInserir);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnDeletar);
        painelBotoes.add(btnRemoverAssociacao);
        painelBotoes.add(btnRecarregar);
        add(painelBotoes, BorderLayout.SOUTH);

        btnInserir.addActionListener(e -> inserirProduto());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnDeletar.addActionListener(e -> deletarProduto());
        btnRecarregar.addActionListener(e -> carregarProdutos());
        btnRemoverAssociacao.addActionListener(e -> removerAssociacaoProdutoEstoque());

        carregarProdutos();
        aplicarPermissoes();
    }

    private void aplicarPermissoes() {
        if ("Comprador".equalsIgnoreCase(papel)) {
            btnInserir.setEnabled(false);
            btnAtualizar.setEnabled(false);
            btnDeletar.setEnabled(false);
            btnRemoverAssociacao.setEnabled(false);
        }
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

    private void removerAssociacaoProdutoEstoque() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int idProduto = (int) modelo.getValueAt(linhaSelecionada, 0);

            List<Estoque> estoques = new EstoqueDAO().buscarTodos();
            Estoque estoqueSelecionado = (Estoque) JOptionPane.showInputDialog(
                this,
                "Selecione o estoque:",
                "Remover Associação",
                JOptionPane.PLAIN_MESSAGE,
                null,
                estoques.toArray(),
                estoques.get(0)
            );

            if (estoqueSelecionado != null) {
                int idEstoque = estoqueSelecionado.getIdEstoque();
                new ProdutoEstoqueDAO().remover(idProduto, idEstoque);
                JOptionPane.showMessageDialog(this, "Associação removida com sucesso.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.");
        }
    }
}
