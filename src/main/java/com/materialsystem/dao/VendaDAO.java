package com.materialsystem.dao;

import com.materialsystem.entity.ItemVenda;
import com.materialsystem.entity.Venda;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public boolean registrarVendaComItens(Venda venda, List<ItemVenda> itens) {
        String sqlVenda = "INSERT INTO Venda (data_venda, id_vendedor, id_comprador, valor_total) VALUES (?, ?, ?, ?) RETURNING id_venda";
        String sqlItem = "INSERT INTO ItemVenda (id_venda, id_produto, quantidade, preco_unitario_venda) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            int idVenda;
            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda)) {
                stmtVenda.setTimestamp(1, Timestamp.valueOf(venda.getDataVenda()));
                if (venda.getIdVendedor() != null) stmtVenda.setInt(2, venda.getIdVendedor());
                else stmtVenda.setNull(2, Types.INTEGER);
                if (venda.getIdComprador() != null) stmtVenda.setInt(3, venda.getIdComprador());
                else stmtVenda.setNull(3, Types.INTEGER);
                stmtVenda.setDouble(4, venda.getValorTotal());

                ResultSet rs = stmtVenda.executeQuery();
                if (!rs.next()) { conn.rollback(); return false; }
                idVenda = rs.getInt(1);
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                for (ItemVenda item : itens) {
                    stmtItem.setInt(1, idVenda);
                    stmtItem.setInt(2, item.getIdProduto());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitarioVenda());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Venda> buscarTodas() {
        List<Venda> vendas = new ArrayList<>();
        String sql = "SELECT * FROM Venda";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Venda v = new Venda();
                v.setIdVenda(rs.getInt("id_venda"));
                v.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                int idVendedor = rs.getInt("id_vendedor");
                v.setIdVendedor(rs.wasNull() ? null : idVendedor);
                int idComprador = rs.getInt("id_comprador");
                v.setIdComprador(rs.wasNull() ? null : idComprador);
                v.setValorTotal(rs.getDouble("valor_total"));
                vendas.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendas;
    }
    public List<ItemVenda> buscarItensPorVenda(int idVenda) {
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
    public boolean deletarVenda(int idVenda) {
        String deleteItens = "DELETE FROM ItemVenda WHERE id_venda = ?";
        String deleteVenda = "DELETE FROM Venda WHERE id_venda = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteItens);
                PreparedStatement stmt2 = conn.prepareStatement(deleteVenda)) {

                stmt1.setInt(1, idVenda);
                stmt1.executeUpdate();

                stmt2.setInt(1, idVenda);
                stmt2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}