package com.materialsystem.dao;

import com.materialsystem.entity.Fabricante;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricanteDAO {

    public void inserir(Fabricante fabricante) {
        String sql = "INSERT INTO Fabricante (nome_fabricante, contato, endereco) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fabricante.getNomeFabricante());
            stmt.setString(2, fabricante.getContato());
            stmt.setString(3, fabricante.getEndereco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Fabricante> buscarTodos() {
        List<Fabricante> fabricantes = new ArrayList<>();
        String sql = "SELECT * FROM Fabricante";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Fabricante f = new Fabricante();
                f.setIdFabricante(rs.getInt("id_fabricante"));
                f.setNomeFabricante(rs.getString("nome_fabricante"));
                f.setContato(rs.getString("contato"));
                f.setEndereco(rs.getString("endereco"));
                fabricantes.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fabricantes;
    }

    public Fabricante buscarPorId(int id) {
        Fabricante fabricante = null;
        String sql = "SELECT * FROM Fabricante WHERE id_fabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                fabricante = new Fabricante();
                fabricante.setIdFabricante(rs.getInt("id_fabricante"));
                fabricante.setNomeFabricante(rs.getString("nome_fabricante"));
                fabricante.setContato(rs.getString("contato"));
                fabricante.setEndereco(rs.getString("endereco"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fabricante;
    }
    public void atualizar(Fabricante fabricante) {
        String sql = "UPDATE Fabricante SET nome_fabricante = ?, contato = ?, endereco = ? WHERE id_fabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fabricante.getNomeFabricante());
            stmt.setString(2, fabricante.getContato());
            stmt.setString(3, fabricante.getEndereco());
            stmt.setInt(4, fabricante.getIdFabricante());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Fabricante WHERE id_fabricante = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
