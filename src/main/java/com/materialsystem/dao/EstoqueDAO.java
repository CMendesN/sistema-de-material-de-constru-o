package com.materialsystem.dao;

import com.materialsystem.entity.Estoque;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {

    public void inserir(Estoque estoque) {
        String sql = "INSERT INTO Estoque (localizacao, capacidade) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, estoque.getLocalizacao());
            stmt.setDouble(2, estoque.getCapacidade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Estoque> buscarTodos() {
        List<Estoque> estoques = new ArrayList<>();
        String sql = "SELECT * FROM Estoque";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Estoque e = new Estoque();
                e.setIdEstoque(rs.getInt("id_estoque"));
                e.setLocalizacao(rs.getString("localizacao"));
                e.setCapacidade(rs.getDouble("capacidade"));
                estoques.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estoques;
    }

    public Estoque buscarPorId(int id) {
        Estoque estoque = null;
        String sql = "SELECT * FROM Estoque WHERE id_estoque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                estoque = new Estoque();
                estoque.setIdEstoque(rs.getInt("id_estoque"));
                estoque.setLocalizacao(rs.getString("localizacao"));
                estoque.setCapacidade(rs.getDouble("capacidade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estoque;
    }
}
