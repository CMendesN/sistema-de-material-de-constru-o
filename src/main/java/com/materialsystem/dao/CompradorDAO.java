package com.materialsystem.dao;

import com.materialsystem.entity.Comprador;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompradorDAO {

    public void inserir(Comprador comprador) {
        String sql = "INSERT INTO Comprador (nome, cpf, contato, endereco) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, comprador.getNome());
            stmt.setString(2, comprador.getCpf());
            stmt.setString(3, comprador.getContato());
            stmt.setString(4, comprador.getEndereco());
                        
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Comprador> buscarTodos() {
        List<Comprador> compradores = new ArrayList<>();
        String sql = "SELECT * FROM Comprador";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Comprador c = new Comprador();
                c.setIdComprador(rs.getInt("id_comprador"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setContato(rs.getString("contato"));
                c.setEndereco(rs.getString("endereco"));
                compradores.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compradores;
    }

    public Comprador buscarPorId(int id) {
        Comprador comprador = null;
        String sql = "SELECT * FROM Comprador WHERE id_comprador = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                comprador = new Comprador();
                comprador.setIdComprador(rs.getInt("id_comprador"));
                comprador.setNome(rs.getString("nome"));
                comprador.setCpf(rs.getString("cpf"));
                comprador.setContato(rs.getString("contato"));
                comprador.setEndereco(rs.getString("endereco"));                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comprador;
    }
}
