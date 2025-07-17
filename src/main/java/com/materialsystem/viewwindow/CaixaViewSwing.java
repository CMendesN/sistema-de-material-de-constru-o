package com.materialsystem.viewwindow;

import com.materialsystem.dao.CompradorDAO;
import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.dao.VendaDAO;
import com.materialsystem.entity.*;
import com.materialsystem.util.VendaComItens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CaixaViewSwing extends JFrame {

    private final int idVendedor;  // ID do caixa que está logado
    private final JComboBox<Comprador> cbComprador;
    private final JComboBox<Produto> cbProduto;
    private final JTextField txtQtd;
    private final JTextField txtPreco;
    private final DefaultTableModel modelo;
    private final List<ItemVenda> itens = new ArrayList<>();

    public CaixaViewSwing(int idVendedor) {
        this.idVendedor = idVendedor;

        setTitle("Caixa - Registrar Venda");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel superior
        JPanel painelTopo = new JPanel(new GridLayout(2, 2, 10, 5));
        cbComprador = new JComboBox<>(new Vector<>(new CompradorDAO().buscarTodos()));
        painelTopo.add(new JLabel("Selecionar Comprador:"));
        painelTopo.add(cbComprador);

        JPanel painelProduto = new JPanel(new GridLayout(1, 4, 10, 5));
        cbProduto = new JComboBox<>(new Vector<>(new ProdutoDAO().buscarTodos()));
        txtQtd = new JTextField();
        txtPreco = new JTextField();

        painelProduto.add(cbProduto);
        painelProduto.add(new JLabel("Qtd:")); painelProduto.add(txtQtd);
        painelProduto.add(new JLabel("Preço Unit:")); painelProduto.add(txtPreco);

        add(painelTopo, BorderLayout.NORTH);
        add(painelProduto, BorderLayout.BEFORE_FIRST_LINE);

        // Tabela de itens
        modelo = new DefaultTableModel(new Object[]{"Produto", "Qtd", "Preço"}, 0);
        JTable tabela = new JTable(modelo);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel();
        JButton btnAdd = new JButton("Adicionar Item");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnFinalizar = new JButton("Finalizar Venda");

        btnAdd.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row != -1) {
                modelo.removeRow(row);
                itens.remove(row);
            }
        });

        btnFinalizar.addActionListener(e -> finalizarVenda());

        botoes.add(btnAdd);
        botoes.add(btnRemover);
        botoes.add(btnFinalizar);
        add(botoes, BorderLayout.SOUTH);
    }

    private void adicionarItem() {
        try {
            Produto produto = (Produto) cbProduto.getSelectedItem();
            int qtd = Integer.parseInt(txtQtd.getText());
            double preco = Double.parseDouble(txtPreco.getText());

            modelo.addRow(new Object[]{produto.getNome(), qtd, preco});
            itens.add(new ItemVenda(0, 0, produto.getIdProduto(), qtd, preco));

            txtQtd.setText("");
            txtPreco.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro nos dados: verifique quantidade e preço.");
        }
    }

    private void finalizarVenda() {
        if (itens.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum item adicionado.");
            return;
        }

        Comprador comprador = (Comprador) cbComprador.getSelectedItem();
        double total = itens.stream().mapToDouble(i -> i.getPrecoUnitarioVenda() * i.getQuantidade()).sum();

        Venda venda = new Venda(0, LocalDateTime.now(), idVendedor, comprador.getIdComprador(), total);
        VendaComItens pacote = new VendaComItens(venda, itens);

        boolean sucesso = new VendaDAO().registrarVendaComItens(pacote.venda(), pacote.itens());

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            modelo.setRowCount(0);
            itens.clear();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao registrar a venda.");
        }
    }
}
