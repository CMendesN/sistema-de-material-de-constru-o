package com.materialsystem.entity;

public class Usuario {
    private int idUsuario;
    private String nome;
    private String username;
    private String senha;
    private String papel;

    public Usuario() {}

    public Usuario(int idUsuario, String nome, String username, String senha, String papel) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.papel = papel;
    }

    // Getters e setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getPapel() { return papel; }
    public void setPapel(String papel) { this.papel = papel; }
}
