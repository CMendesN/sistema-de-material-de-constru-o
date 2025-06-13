package com.materialsystem.dao;

import com.materialsystem.entity.Produto;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) {
        String sql = "INSERT INTO Produto (nome, descricao, preco_unitario, quantidade_em_estoque, id_fabricante, categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoUnitario());
            stmt.setInt(4, produto.getQuantidadeEmEstoque());
            stmt.setInt(5, produto.getIdFabricante());
            stmt.setString(6, produto.getCategoria());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> buscarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM Produto";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setQuantidadeEmEstoque(rs.getInt("quantidade_em_estoque"));
                produto.setIdFabricante(rs.getInt("id_fabricante"));
                produto.setCategoria(rs.getString("categoria"));
                produtos.add(produto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }
    public Produto buscarPorId(int id) {
        Produto produto = null;
        String sql = "SELECT * FROM Produto WHERE id_produto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                produto = new Produto();
                produto.setIdProduto(rs.getInt("id_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoUnitario(rs.getDouble("preco_unitario"));
                produto.setQuantidadeEmEstoque(rs.getInt("quantidade_em_estoque"));
                produto.setIdFabricante(rs.getInt("id_fabricante"));
                produto.setCategoria(rs.getString("categoria"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE Produto SET nome = ?, descricao = ?, preco_unitario = ?, quantidade_em_estoque = ?, id_fabricante = ?, categoria = ? WHERE id_produto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoUnitario());
            stmt.setInt(4, produto.getQuantidadeEmEstoque());
            stmt.setInt(5, produto.getIdFabricante());
            stmt.setString(6, produto.getCategoria());
            stmt.setInt(7, produto.getIdProduto());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Produto WHERE id_produto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
