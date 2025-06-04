package com.materialsystem.dao;

import com.materialsystem.entity.ItemVenda;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemVendaDAO {

    public void inserir(ItemVenda itemVenda) {
        String sql = "INSERT INTO ItemVenda (id_venda, id_produto, quantidade, preco_unitario_venda) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itemVenda.getIdVenda());
            stmt.setInt(2, itemVenda.getIdProduto());
            stmt.setInt(3, itemVenda.getQuantidade());
            stmt.setDouble(4, itemVenda.getPrecoUnitarioVenda());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemVenda> buscarPorVenda(int idVenda) {
        List<ItemVenda> itens = new ArrayList<>();
        String sql = "SELECT * FROM ItemVenda WHERE id_venda = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenda);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ItemVenda item = new ItemVenda();
                item.setIdItemVenda(rs.getInt("id_item_venda"));
                item.setIdVenda(rs.getInt("id_venda"));
                item.setIdProduto(rs.getInt("id_produto"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitarioVenda(rs.getDouble("preco_unitario_venda"));
                itens.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itens;
    }

    public void remover(int idItemVenda) {
        String sql = "DELETE FROM ItemVenda WHERE id_item_venda = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idItemVenda);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
