package com.materialsystem.dao;

import com.materialsystem.entity.Vendedor;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    public void inserir(Vendedor vendedor) {
        String sql = "INSERT INTO Vendedor (nome, cpf, contato, salario, data_contratacao, id_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vendedor.getNome());
            stmt.setString(2, vendedor.getCpf());
            stmt.setString(3, vendedor.getContato());
            stmt.setDouble(4, vendedor.getSalario());
            stmt.setDate(5, Date.valueOf(vendedor.getDataContratacao()));
            
            if (vendedor.getIdUsuario() != null)
                stmt.setInt(6, vendedor.getIdUsuario());
            else
                stmt.setNull(6, Types.INTEGER);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Vendedor> buscarTodos() {
        List<Vendedor> vendedores = new ArrayList<>();
        String sql = "SELECT * FROM Vendedor";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vendedor v = new Vendedor();
                v.setIdVendedor(rs.getInt("id_vendedor"));
                v.setNome(rs.getString("nome"));
                v.setCpf(rs.getString("cpf"));
                v.setContato(rs.getString("contato"));
                v.setSalario(rs.getDouble("salario"));
                v.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
                int idUsuario = rs.getInt("id_usuario");
                v.setIdUsuario(rs.wasNull() ? null : idUsuario);
                vendedores.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendedores;
    }

    public Vendedor buscarPorId(int id) {
        Vendedor vendedor = null;
        String sql = "SELECT * FROM Vendedor WHERE id_vendedor = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                vendedor = new Vendedor();
                vendedor.setIdVendedor(rs.getInt("id_vendedor"));
                vendedor.setNome(rs.getString("nome"));
                vendedor.setCpf(rs.getString("cpf"));
                vendedor.setContato(rs.getString("contato"));
                vendedor.setSalario(rs.getDouble("salario"));
                vendedor.setDataContratacao(rs.getDate("data_contratacao").toLocalDate());
                int idUsuario = rs.getInt("id_usuario");
                vendedor.setIdUsuario(rs.wasNull() ? null : idUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vendedor;
    }
}
