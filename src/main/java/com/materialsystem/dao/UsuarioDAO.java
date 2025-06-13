package com.materialsystem.dao;

import com.materialsystem.entity.Usuario;
import com.materialsystem.util.DatabaseConnection;
import com.materialsystem.util.PasswordUtils;

import java.sql.*;

public class UsuarioDAO {

    public Usuario buscarPorUsername(String username) {
        Usuario usuario = null;
        String sql = "SELECT * FROM Usuario WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setUsername(rs.getString("username"));
                usuario.setSenha(rs.getString("senha"));  
                usuario.setPapel(rs.getString("papel"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, username, senha, papel) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getUsername());
            
            // Sempre gera o hash antes de salvar
            String hashSenha = PasswordUtils.gerarHashSenha(usuario.getSenha());
            stmt.setString(3, hashSenha);
            
            stmt.setString(4, usuario.getPapel());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
