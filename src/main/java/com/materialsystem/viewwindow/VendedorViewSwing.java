package com.materialsystem.viewwindow;

import com.materialsystem.controller.VendedorController;
import com.materialsystem.entity.Vendedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class VendedorViewSwing extends JFrame {

    private final VendedorController controller = new VendedorController();
    private JTable tabela;
    private DefaultTableModel modelo;

    public VendedorViewSwing() {
        setTitle("Gerenciamento de Vendedores");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "Contato", "Salário", "Data Contratação"}, 0);
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
        List<Vendedor> lista = controller.listarTodos();
        for (Vendedor v : lista) {
            modelo.addRow(new Object[]{
                v.getIdVendedor(),
                v.getNome(),
                v.getCpf(),
                v.getContato(),
                v.getSalario(),
                v.getDataContratacao().toString()
            });
        }
    }

    private void inserir() {
        Vendedor novo = abrirDialogo(null);
        if (novo != null) {
            controller.inserir(novo);
            carregar();
        }
    }

    private void atualizar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um vendedor para atualizar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        Vendedor atual = controller.buscarPorId(id);
        if (atual == null) return;

        Vendedor editado = abrirDialogo(atual);
        if (editado != null) {
            controller.atualizar(editado);
            carregar();
        }
    }

    private void deletar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um vendedor para deletar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o vendedor ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletar(id);
            carregar();
        }
    }

    private Vendedor abrirDialogo(Vendedor vendedor) {
        JTextField nome = new JTextField(vendedor != null ? vendedor.getNome() : "");
        JTextField cpf = new JTextField(vendedor != null ? vendedor.getCpf() : "");
        JTextField contato = new JTextField(vendedor != null ? vendedor.getContato() : "");
        JTextField salario = new JTextField(vendedor != null ? String.valueOf(vendedor.getSalario()) : "");
        JTextField data = new JTextField(vendedor != null ? vendedor.getDataContratacao().toString() : "");

        JPanel painel = new JPanel(new GridLayout(0, 1));
        painel.add(new JLabel("Nome:")); painel.add(nome);
        painel.add(new JLabel("CPF:")); painel.add(cpf);
        painel.add(new JLabel("Contato:")); painel.add(contato);
        painel.add(new JLabel("Salário:")); painel.add(salario);
        painel.add(new JLabel("Data de Contratação (AAAA-MM-DD):")); painel.add(data);

        int resultado = JOptionPane.showConfirmDialog(this, painel,
                vendedor == null ? "Novo Vendedor" : "Atualizar Vendedor", JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                return new Vendedor(
                        vendedor != null ? vendedor.getIdVendedor() : 0,
                        nome.getText(),
                        cpf.getText(),
                        contato.getText(),
                        Double.parseDouble(salario.getText()),
                        LocalDate.parse(data.getText())
                );
            } catch (NumberFormatException | DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Dados inválidos.");
            }
        }
        return null;
    }
}
