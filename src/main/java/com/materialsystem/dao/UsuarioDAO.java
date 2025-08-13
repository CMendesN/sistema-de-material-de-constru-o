package com.materialsystem.dao;

import com.materialsystem.entity.Usuario;
import com.materialsystem.util.DatabaseConnection;
import com.materialsystem.util.PasswordUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario buscarPorUsername(String username) {
        Usuario usuario = null;
        String sql = "SELECT id_usuario, nome, username, senha, papel FROM Usuario WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = map(rs, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public Usuario buscarPorId(int id) {
        Usuario usuario = null;
        String sql = "SELECT id_usuario, nome, username, senha, papel FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = map(rs, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public List<Usuario> buscarTodos() {
        List<Usuario> list = new ArrayList<>();
        String sql = "SELECT id_usuario, nome, username, papel FROM Usuario ORDER BY nome"; // não traz senha no relatório

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNome(rs.getString("nome"));
                u.setUsername(rs.getString("username"));
                u.setPapel(rs.getString("papel"));
                list.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, username, senha, papel) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getUsername());
            // assume que já veio com hash (PasswordUtils.gerarHashSenha)
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getPapel());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Usuario u) {
        // Atualiza tudo, assumindo que a senha já veio hasheada ou preservada
        String sql = "UPDATE Usuario SET nome=?, username=?, senha=?, papel=? WHERE id_usuario=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getUsername());
            stmt.setString(3, u.getSenha()); // hash atual ou novo hash
            stmt.setString(4, u.getPapel());
            stmt.setInt(5, u.getIdUsuario());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ==== util ==== */
    private static Usuario map(ResultSet rs, boolean comSenha) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNome(rs.getString("nome"));
        u.setUsername(rs.getString("username"));
        if (comSenha) u.setSenha(rs.getString("senha"));
        u.setPapel(rs.getString("papel"));
        return u;
    }
}
