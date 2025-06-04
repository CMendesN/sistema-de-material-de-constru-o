package com.materialsystem.dao;

import com.materialsystem.entity.Venda;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    public void inserir(Venda venda) {
        String sql = "INSERT INTO Venda (data_venda, id_vendedor, id_comprador, valor_total) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(venda.getDataVenda()));

            if (venda.getIdVendedor() != null)
                stmt.setInt(2, venda.getIdVendedor());
            else
                stmt.setNull(2, Types.INTEGER);

            if (venda.getIdComprador() != null)
                stmt.setInt(3, venda.getIdComprador());
            else
                stmt.setNull(3, Types.INTEGER);

            stmt.setDouble(4, venda.getValorTotal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
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

    public Venda buscarPorId(int id) {
        Venda venda = null;
        String sql = "SELECT * FROM Venda WHERE id_venda = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                venda = new Venda();
                venda.setIdVenda(rs.getInt("id_venda"));
                venda.setDataVenda(rs.getTimestamp("data_venda").toLocalDateTime());
                int idVendedor = rs.getInt("id_vendedor");
                venda.setIdVendedor(rs.wasNull() ? null : idVendedor);
                int idComprador = rs.getInt("id_comprador");
                venda.setIdComprador(rs.wasNull() ? null : idComprador);
                venda.setValorTotal(rs.getDouble("valor_total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venda;
    }
}
