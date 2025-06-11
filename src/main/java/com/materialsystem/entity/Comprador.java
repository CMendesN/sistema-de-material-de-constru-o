package com.materialsystem.entity;

public class Comprador {
    private int idComprador;
    private String nome;
    private String cpf;
    private String contato;
    private String endereco;

    public Comprador() {}

    public Comprador(int idComprador, String nome, String cpf, String contato, String endereco) {
        this.idComprador = idComprador;
        this.nome = nome;
        this.cpf = cpf;
        this.contato = contato;
        this.endereco = endereco;        
    }

    public int getIdComprador() { return idComprador; }
    public void setIdComprador(int idComprador) { this.idComprador = idComprador; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

}
