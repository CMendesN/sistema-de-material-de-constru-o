package com.materialsystem.dao;

import com.materialsystem.entity.ProdutoEstoque;
import com.materialsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoEstoqueDAO {

    public void inserir(ProdutoEstoque produtoEstoque) {
        String sql = "INSERT INTO ProdutoEstoque (id_produto, id_estoque, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produtoEstoque.getIdProduto());
            stmt.setInt(2, produtoEstoque.getIdEstoque());
            stmt.setInt(3, produtoEstoque.getQuantidade());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProdutoEstoque> buscarPorProduto(int idProduto) {
        List<ProdutoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM ProdutoEstoque WHERE id_produto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProduto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProdutoEstoque pe = new ProdutoEstoque();
                pe.setIdProduto(rs.getInt("id_produto"));
                pe.setIdEstoque(rs.getInt("id_estoque"));
                pe.setQuantidade(rs.getInt("quantidade"));
                lista.add(pe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void atualizar(ProdutoEstoque produtoEstoque) {
        String sql = "UPDATE ProdutoEstoque SET quantidade = ? WHERE id_produto = ? AND id_estoque = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, produtoEstoque.getQuantidade());
            stmt.setInt(2, produtoEstoque.getIdProduto());
            stmt.setInt(3, produtoEstoque.getIdEstoque());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	    public void remover(int idProduto, int idEstoque) {
	        String sql = "DELETE FROM ProdutoEstoque WHERE id_produto = ? AND id_estoque = ?";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, idProduto);
	            stmt.setInt(2, idEstoque);
	            stmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
    }
    public boolean existeAssociacao(int idProduto, int idEstoque) {
	    String sql = "SELECT COUNT(*) FROM ProdutoEstoque WHERE id_produto = ? AND id_estoque = ?";
	
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	
	        stmt.setInt(1, idProduto);
	        stmt.setInt(2, idEstoque);
	        ResultSet rs = stmt.executeQuery();
	
	        if (rs.next()) {
	            return rs.getInt(1) > 0;
	        }
	
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
    }
    public List<ProdutoEstoque> buscarTodos() {
        List<ProdutoEstoque> lista = new ArrayList<>();
        String sql = "SELECT id_produto, id_estoque, quantidade FROM ProdutoEstoque";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ProdutoEstoque pe = new ProdutoEstoque();
                pe.setIdProduto(rs.getInt("id_produto"));
                pe.setIdEstoque(rs.getInt("id_estoque"));
                pe.setQuantidade(rs.getInt("quantidade"));
                lista.add(pe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
