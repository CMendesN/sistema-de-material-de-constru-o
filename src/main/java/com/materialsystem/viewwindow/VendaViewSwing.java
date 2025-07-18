package com.materialsystem.viewwindow;

import com.materialsystem.controller.VendaController;
import com.materialsystem.dao.CompradorDAO;
import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.dao.VendedorDAO;
import com.materialsystem.entity.*;
import com.materialsystem.util.VendaComItens;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VendaViewSwing extends JFrame {

    private final VendaController controller = new VendaController();

    private JComboBox<Comprador> cbComprador;
    private JComboBox<Vendedor> cbVendedor;
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade;
    private JTextField txtPrecoUnitario;
    private DefaultTableModel modeloItens;
    private JTable tabelaItens;
    private List<ItemVenda> itensVenda = new ArrayList<>();

    private Usuario usuarioLogado; 

    // Construtor padrão: usado por Gerente ou Vendedor
    public VendaViewSwing() {
        this(null);
    }

    // Construtor com usuário: usado por Comprador
    public VendaViewSwing(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Registro e Consulta de Vendas");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTabbedPane abas = new JTabbedPane();
        abas.add("Registrar Venda", criarPainelRegistrar());
        abas.add("Visualizar Vendas", criarPainelListar());

        add(abas);
    }

    private JPanel criarPainelRegistrar() {
        JPanel painel = new JPanel(new BorderLayout());

        // Topo
        JPanel cabecalho = new JPanel(new GridLayout(2, 4));
        cbVendedor = new JComboBox<>(new Vector<>(new VendedorDAO().buscarTodos()));
        cbComprador = new JComboBox<>(new Vector<>(new CompradorDAO().buscarTodos()));
        cbProduto = new JComboBox<>(new Vector<>(new ProdutoDAO().buscarTodos()));

        txtQuantidade = new JTextField();
        
        txtPrecoUnitario = new JTextField();
        if (usuarioLogado != null && "Comprador".equalsIgnoreCase(usuarioLogado.getPapel())) {
            txtPrecoUnitario.setEditable(false);
        } else {
            txtPrecoUnitario.setEditable(true); 
        }


        cbProduto.addActionListener(e -> {
            Produto produtoSelecionado = (Produto) cbProduto.getSelectedItem();
            if (produtoSelecionado != null) {
                txtPrecoUnitario.setText(String.valueOf(produtoSelecionado.getPrecoUnitario()));
            }
        });
        cabecalho.add(new JLabel("Vendedor:")); cabecalho.add(cbVendedor);
        cabecalho.add(new JLabel("Comprador:")); cabecalho.add(cbComprador);
        cabecalho.add(new JLabel("Produto:")); cabecalho.add(cbProduto);
        cabecalho.add(new JLabel("Qtd:")); cabecalho.add(txtQuantidade);
        cabecalho.add(new JLabel("Preço Unit:")); cabecalho.add(txtPrecoUnitario);

        painel.add(cabecalho, BorderLayout.NORTH);

        // Tabela de itens
        modeloItens = new DefaultTableModel(new Object[]{"Produto", "Quantidade", "Preço"}, 0);
        tabelaItens = new JTable(modeloItens);
        painel.add(new JScrollPane(tabelaItens), BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel();
        JButton btnAdd = new JButton("Adicionar Item");
        JButton btnRemover = new JButton("Remover Item");
        JButton btnFinalizar = new JButton("Registrar Venda");

        btnAdd.addActionListener(e -> adicionarItem());
        btnRemover.addActionListener(e -> removerItem());
        btnFinalizar.addActionListener(e -> registrarVenda());

        botoes.add(btnAdd);
        botoes.add(btnRemover);
        botoes.add(btnFinalizar);
        painel.add(botoes, BorderLayout.SOUTH);

        return painel;
    }

    private void adicionarItem() {
        try {
            Produto produto = (Produto) cbProduto.getSelectedItem();
            int qtd = Integer.parseInt(txtQuantidade.getText());
            double preco = Double.parseDouble(txtPrecoUnitario.getText());

            modeloItens.addRow(new Object[]{produto.getNome(), qtd, preco});
            itensVenda.add(new ItemVenda(0, 0, produto.getIdProduto(), qtd, preco));

            txtQuantidade.setText("");
            txtPrecoUnitario.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Dados inválidos.");
        }
    }

    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha >= 0) {
            modeloItens.removeRow(linha);
            itensVenda.remove(linha);
        }
    }

    private void registrarVenda() {
        Vendedor vendedor = (Vendedor) cbVendedor.getSelectedItem();
        Comprador comprador = (Comprador) cbComprador.getSelectedItem();

        double total = itensVenda.stream()
            .mapToDouble(i -> i.getPrecoUnitarioVenda() * i.getQuantidade())
            .sum();

        Venda venda = new Venda(0, LocalDateTime.now(),
                vendedor != null ? vendedor.getIdVendedor() : null,
                comprador != null ? comprador.getIdComprador() : null,
                total);

        boolean sucesso = controller.registrarVenda(new VendaComItens(venda, itensVenda));

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            modeloItens.setRowCount(0);
            itensVenda.clear();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao registrar a venda.");
        }
    }

    private JPanel criarPainelListar() {
        JPanel painel = new JPanel(new BorderLayout());
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Data", "Vendedor", "Comprador", "Valor"}, 0);
        JTable tabela = new JTable(modelo);

        List<Venda> vendas = (usuarioLogado != null && "Comprador".equalsIgnoreCase(usuarioLogado.getPapel()))
                ? controller.listarPorComprador(usuarioLogado.getIdUsuario())
                : controller.listarTodas();

        for (Venda v : vendas) {
            modelo.addRow(new Object[]{
                    v.getIdVenda(), v.getDataVenda(), v.getIdVendedor(), v.getIdComprador(), v.getValorTotal()
            });
        }

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnDetalhes = new JButton("Ver Itens");
        btnDetalhes.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row >= 0) {
                int id = (int) modelo.getValueAt(row, 0);
                List<ItemVenda> itens = controller.buscarItensPorVenda(id);

                StringBuilder msg = new StringBuilder("Itens da Venda " + id + ":\n");
                for (ItemVenda item : itens) {
                    msg.append("• Produto ").append(item.getIdProduto())
                            .append(", Qtd: ").append(item.getQuantidade())
                            .append(", Preço: R$ ").append(item.getPrecoUnitarioVenda()).append("\n");
                }
                JOptionPane.showMessageDialog(this, msg.toString());
            }
        });

        painel.add(btnDetalhes, BorderLayout.SOUTH);
        return painel;
    }
}
